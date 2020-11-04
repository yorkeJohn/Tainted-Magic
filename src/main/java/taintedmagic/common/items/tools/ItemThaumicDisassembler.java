package taintedmagic.common.items.tools;

import java.util.List;

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
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.WandManager;

public class ItemThaumicDisassembler extends Item
{
    public static final String TAG_MODE = "mode";

    public ItemThaumicDisassembler ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setMaxDamage(500);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ItemThaumicDisassembler");
        this.setTextureName("taintedmagic:ItemThaumicDisassembler");
    }

    @Override
    public boolean canHarvestBlock (Block block, ItemStack stack)
    {
        return block != Blocks.bedrock;
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);

        list.add(StatCollector.translateToLocal("text.disassembler.mode") + ": " + getModeName(stack));
        list.add(StatCollector.translateToLocal("text.disassembler.efficiency") + ": "
                + (getMode(stack) == 3 ? "\u00A7c" : "\u00A7a") + getEfficiency(stack));
        list.add(" ");
        list.add("\u00A79" + "+20 " + StatCollector.translateToLocal("text.attackdamage"));
    }

    @Override
    public boolean hitEntity (ItemStack stack, EntityLivingBase entity, EntityLivingBase player)
    {
        entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), 20);
        stack.damageItem(2, player);
        return false;
    }

    @Override
    public float getDigSpeed (ItemStack stack, Block block, int meta)
    {
        return getEfficiency(stack);
    }

    @Override
    public boolean onBlockDestroyed (ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
    {
        if (block.getBlockHardness(world, x, y, z) != 0.0D) stack.damageItem(1, entity);
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
                    + getModeName(getMode(stack)) + (getMode(stack) == 3 ? "\u00A7c" : "\u00A7a") + " ("
                    + getEfficiency(getMode(stack)) + ")";
            HUDHandler.displayString(str, 300, false);
        }
        return stack;
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

    public void onUpdate (ItemStack stack, World world, Entity entity, int i, boolean b)
    {
        super.onUpdate(stack, world, entity, i, b);
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0)
            {
                if (WandManager.consumeVisFromInventory(player, new AspectList().add(Aspect.ENTROPY, 5)))
                    stack.damageItem(-1, (EntityLivingBase) entity);
            }
        }
    }
}