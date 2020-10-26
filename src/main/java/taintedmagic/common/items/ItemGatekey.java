package taintedmagic.common.items;

import java.awt.Color;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import taintedmagic.client.handler.ClientHandler;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;

public class ItemGateKey extends Item implements IWarpingGear
{
    IIcon icon;
    IIcon overlay;

    private static final String TAG_X = "posX";
    private static final String TAG_Y = "posY";
    private static final String TAG_Z = "posZ";
    private static final String TAG_DIM = "dimID";
    private static final String TAG_COLOUR = "colour";

    public ItemGateKey ()
    {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemGateKey");
        this.setMaxStackSize(1);
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack stack)
    {
        return TaintedMagic.rarityCreation;
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemGateKey");
        this.overlay = ir.registerIcon("taintedmagic:ItemGateKey_overlay");
    }

    @SideOnly (Side.CLIENT)
    public boolean requiresMultipleRenderPasses ()
    {
        return true;
    }

    @SideOnly (Side.CLIENT)
    public int getRenderPasses (int meta)
    {
        return 2;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass (int meta, int pass)
    {
        return (pass == 1) ? this.overlay : this.icon;
    }

    @Override
    public int getColorFromItemStack (ItemStack stack, int pass)
    {
        if (pass == 1 && stack.stackTagCompound != null)
        {
            return Color.HSBtoRGB((float) stack.stackTagCompound.getInteger(TAG_COLOUR) / 360F, 1.0F,
                    0.2F * (float) Math.sin((double) ClientHandler.ticks / 150D) + 0.8F);
        }
        return 0xFFFFFF;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 3;
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        if (stack.stackTagCompound != null)
        {
            list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("key.bound"));

            int dim = stack.stackTagCompound.getInteger(TAG_DIM);
            list.add(WorldProvider.getProviderForDimension(dim).getDimensionName());

            int x = stack.stackTagCompound.getInteger(TAG_X);
            int y = stack.stackTagCompound.getInteger(TAG_Y);
            int z = stack.stackTagCompound.getInteger(TAG_Z);
            list.add(x + ", " + y + ", " + z);
        }
        else list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("key.unbound"));
    }

    @Override
    public EnumAction getItemUseAction (ItemStack stack)
    {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration (ItemStack stack)
    {
        return 40;
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        if (stack.stackTagCompound != null) player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int face, float hitX,
            float hitY, float hitZ)
    {
        if (stack.stackTagCompound == null)
        {
            stack.stackTagCompound = new NBTTagCompound();

            stack.stackTagCompound.setInteger(TAG_COLOUR, world.rand.nextInt(360) + 1);

            stack.stackTagCompound.setInteger(TAG_X, x);
            stack.stackTagCompound.setInteger(TAG_Y, y + 1);
            stack.stackTagCompound.setInteger(TAG_Z, z);
            stack.stackTagCompound.setInteger(TAG_DIM, world.provider.dimensionId);

            world.playSoundAtEntity(player, "thaumcraft:wand", 0.5F + ((float) Math.random() * 0.5F), 1.0F);

            if (world.isRemote)
            {
                player.swingItem();
                for (int a = 0; a < 9; a++)
                    sparkle(world, x + 0.5D, y, z + 0.5D);
            }
        }
        return false;
    }

    @SideOnly (Side.CLIENT)
    public void sparkle (World world, double x, double y, double z)
    {
        FXSparkle fx = new FXSparkle(world, x + 0.33F * world.rand.nextGaussian(), y + 0.5D + world.rand.nextFloat(),
                z + 0.33F * world.rand.nextGaussian(), 1.75F, 6, 3 + world.rand.nextInt(3));
        fx.setGravity(0.1F);
        ParticleEngine.instance.addEffect(world, fx);
    }

    @Override
    public void onUsingTick (ItemStack stack, EntityPlayer player, int i)
    {
        super.onUsingTick(stack, player, i);

        if (player.worldObj.isRemote) sparkle(player.worldObj, player.posX, player.boundingBox.minY, player.posZ);

        float f = 1.0F + ((float) Math.random() * 0.25F);
        if (player.ticksExisted % 5 == 0) player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", f * 0.1F, f);
    }

    @Override
    public ItemStack onEaten (ItemStack stack, World world, EntityPlayer player)
    {
        super.onEaten(stack, world, player);
        if (stack.stackTagCompound != null)
        {
            int x = stack.stackTagCompound.getInteger(TAG_X);
            int y = stack.stackTagCompound.getInteger(TAG_Y);
            int z = stack.stackTagCompound.getInteger(TAG_Z);
            int dim = stack.stackTagCompound.getInteger(TAG_DIM);

            if (y > -1 && dim == world.provider.dimensionId && world.getBlock(x, y, z) == Blocks.air
                    && world.getBlock(x, y + 1, z) == Blocks.air)
            {
                if (!world.isRemote)
                {
                    if (player instanceof EntityPlayerMP)
                        ((EntityPlayerMP) player).playerNetServerHandler.setPlayerLocation((double) x + 0.5D, (double) y,
                                (double) z + 0.5D, player.rotationYaw, player.rotationPitch);
                    world.playSoundAtEntity(player, "mob.endermen.portal", 5.0F, 1.0F);
                }
                try
                {
                    player.addPotionEffect(new PotionEffect(Potion.confusion.id, 140, 1));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (dim != world.provider.dimensionId) HUDHandler
                    .displayString(EnumChatFormatting.RED + StatCollector.translateToLocal("key.invaliddim"), 300, false);
            else HUDHandler.displayString(EnumChatFormatting.RED + StatCollector.translateToLocal("key.error"), 300, false);
        }
        return stack;
    }
}
