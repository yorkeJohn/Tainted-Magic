package taintedmagic.common.items.tools;

import java.util.List;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.WandManager;

public class ItemThaumicDisassembler extends Item
{
    public static final String TAG_MODE = "mode";
    public static final String TAG_CHARGE = "charge";
    public static final int MAX_CHARGE = 50000;
    public static final int ENTROPY_USAGE_BASE = 5;
    public static final int ENTROPY_USAGE_HOE = ENTROPY_USAGE_BASE * 5;
    public static final int ENTROPY_USAGE_HIT = ENTROPY_USAGE_BASE * 10;

    public ItemThaumicDisassembler ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ItemThaumicDisassembler");
        this.setTextureName("taintedmagic:ItemThaumicDisassembler");
    }

    @Override
    public boolean showDurabilityBar (ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay (ItemStack stack)
    {
        return 1d - ((double) getEntropyCharge(stack) / (double) MAX_CHARGE);
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);

        list.add(StatCollector.translateToLocal("text.disassembler.charge") + ": " + EnumChatFormatting.DARK_GRAY
                + (getEntropyCharge(stack) / 100) + "/" + (MAX_CHARGE / 100));
        list.add(StatCollector.translateToLocal("text.disassembler.mode") + ": " + getModeName(stack));
        list.add(StatCollector.translateToLocal("text.disassembler.efficiency") + ": "
                + (getMode(stack) == 3 ? EnumChatFormatting.RED : EnumChatFormatting.GREEN) + getEfficiency(stack));
        list.add(" ");
        list.add(EnumChatFormatting.BLUE + "+20 " + StatCollector.translateToLocal("text.attackdamage"));
    }

    @Override
    public boolean canHarvestBlock (Block block, ItemStack stack)
    {
        return block != Blocks.bedrock;
    }

    @Override
    public boolean hitEntity (ItemStack stack, EntityLivingBase entity, EntityLivingBase player)
    {
        if (getEntropyCharge(stack) > 0)
        {
            entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), 20);
            consumeEntropy(stack, ENTROPY_USAGE_HIT);
        }
        else entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), 4);

        return false;
    }

    @Override
    public float getDigSpeed (ItemStack stack, Block block, int meta)
    {
        return getEntropyCharge(stack) != 0 ? getEfficiency(stack) : 1f;
    }

    @Override
    public boolean onBlockDestroyed (ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
    {
        if (block.getBlockHardness(world, x, y, z) != 0.0D)
            consumeEntropy(stack, (int) ((double) ENTROPY_USAGE_BASE * (double) getEfficiency(stack) / 8d));
        return true;
    }

    @Override
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean isFull3D ()
    {
        return true;
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        if (player.isSneaking())
        {
            if (stack.stackTagCompound == null) stack.setTagCompound(new NBTTagCompound());
            stack.stackTagCompound.setInteger(TAG_MODE, getMode(stack) < 3 ? getMode(stack) + 1 : 0);

            String str = EnumChatFormatting.GRAY + StatCollector.translateToLocal("text.disassembler.mode") + ": "
                    + getModeName(getMode(stack)) + (getMode(stack) == 3 ? EnumChatFormatting.RED : EnumChatFormatting.GREEN)
                    + " (" + getEfficiency(getMode(stack)) + ")";
            HUDHandler.displayString(str, 300, false);
        }
        return stack;
    }

    @Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
            float hitY, float hitZ)
    {
        if (!player.isSneaking() && getMode(stack) != 3)
        {
            if (!useHoe(stack, player, world, x, y, z, side) && world.getBlock(x, y, z) != Blocks.farmland) return false;

            if (getMode(stack) == 1) return true;

            int rad = getMode(stack) == 0 ? 1 : 2;
            for (int x1 = x - rad; x1 <= x + rad; x1++)
            {
                for (int z1 = z - rad; z1 <= z + rad; z1++)
                {
                    useHoe(stack, player, world, x1, y, z1, side);
                }
            }
            return true;
        }
        return false;
    }

    private boolean useHoe (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side)
    {
        if (!player.canPlayerEdit(x, y, z, side, stack)
                || (!player.capabilities.isCreativeMode && getEntropyCharge(stack) < ENTROPY_USAGE_HOE))
            return false;
        else
        {
            UseHoeEvent event = new UseHoeEvent(player, stack, world, x, y, z);
            if (MinecraftForge.EVENT_BUS.post(event)) return false;

            if (event.getResult() == Result.ALLOW)
            {
                consumeEntropy(stack, ENTROPY_USAGE_HOE);
                return true;
            }

            Block block = world.getBlock(x, y, z);
            boolean air = world.isAirBlock(x, y + 1, z);

            if (side != 0 && air && (block == Blocks.grass || block == Blocks.dirt))
            {
                Block farm = Blocks.farmland;
                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, farm.stepSound.getStepResourcePath(),
                        (farm.stepSound.getVolume() + 1.0F) / 2.0F, farm.stepSound.getPitch() * 0.8F);

                if (world.isRemote) return true;
                else
                {
                    world.setBlock(x, y, z, farm);
                    if (!player.capabilities.isCreativeMode) consumeEntropy(stack, ENTROPY_USAGE_HOE);

                    return true;
                }
            }
            else return false;
        }
    }

    public static int getEfficiency (int mode)
    {
        switch (mode)
        {
        case 0 :
            return 20;
        case 1 :
            return 8;
        case 2 :
            return 128;
        case 3 :
            return 0;
        }
        return 0;
    }

    public static int getEfficiency (ItemStack stack)
    {
        return getEfficiency(getMode(stack));
    }

    public static int getMode (ItemStack stack)
    {
        if (stack.stackTagCompound == null) return 0;
        return stack.stackTagCompound.getInteger(TAG_MODE);
    }

    public static String getModeName (int mode)
    {
        switch (mode)
        {
        case 0 :
            return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.normal");
        case 1 :
            return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.slow");
        case 2 :
            return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.fast");
        case 3 :
            return "\u00A7c" + StatCollector.translateToLocal("text.disassembler.off");
        }
        return null;
    }

    public static String getModeName (ItemStack stack)
    {
        return getModeName(getMode(stack));
    }

    private void addEntropy (ItemStack stack, int amount)
    {
        if (stack.getTagCompound() == null) stack.stackTagCompound = new NBTTagCompound();
        stack.getTagCompound().setInteger(TAG_CHARGE, getEntropyCharge(stack) + amount);
    }

    private void consumeEntropy (ItemStack stack, int amount)
    {
        if (stack.getTagCompound() == null) stack.stackTagCompound = new NBTTagCompound();
        stack.getTagCompound().setInteger(TAG_CHARGE, getEntropyCharge(stack) - amount);
    }

    private int getEntropyCharge (ItemStack stack)
    {
        if (stack.getTagCompound() == null) return 0;
        return stack.getTagCompound().getInteger(TAG_CHARGE);
    }

    @Override
    public void onUpdate (ItemStack stack, World world, Entity entity, int i, boolean b)
    {
        super.onUpdate(stack, world, entity, i, b);
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (!world.isRemote && getEntropyCharge(stack) < MAX_CHARGE && entity.ticksExisted % 20 == 0)
            {
                int amount = MAX_CHARGE - getEntropyCharge(stack);
                if (amount > 100) amount = 100;

                if (WandManager.consumeVisFromInventory(player, new AspectList().add(Aspect.ENTROPY, amount)))
                    addEntropy(stack, amount);
            }
        }
    }
}