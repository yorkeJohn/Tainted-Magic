package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.List;
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
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;

public class ItemGateKey extends Item implements IWarpingGear {

    private IIcon icon;
    private IIcon overlay;

    private static final String TAG_X = "posX";
    private static final String TAG_Y = "posY";
    private static final String TAG_Z = "posZ";
    private static final String TAG_DIM = "dimID";
    private static final String TAG_COLOUR = "colour";

    public ItemGateKey() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemGateKey");
        setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemGateKey");
        overlay = ir.registerIcon("taintedmagic:ItemGateKey_overlay");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(final int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int meta, final int pass) {
        return pass == 1 ? overlay : icon;
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int pass) {
        if (pass == 1 && stack.stackTagCompound != null && stack.stackTagCompound.getInteger(TAG_COLOUR) != 0)
            return Color.HSBtoRGB(
                    stack.stackTagCompound.getInteger(TAG_COLOUR) / 360F,
                    1.0F,
                    0.2F * (float) Math.sin(TaintedMagic.proxy.getClientPlayer().ticksExisted / 10D) + 0.8F);
        return 0xFFFFFF;
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 3;
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        if (stack.stackTagCompound != null) {
            list.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("key.bound"));

            final int dim = stack.stackTagCompound.getInteger(TAG_DIM);
            list.add(WorldProvider.getProviderForDimension(dim).getDimensionName());

            final int x = stack.stackTagCompound.getInteger(TAG_X);
            final int y = stack.stackTagCompound.getInteger(TAG_Y);
            final int z = stack.stackTagCompound.getInteger(TAG_Z);
            list.add(x + ", " + y + ", " + z);
        } else {
            list.add(EnumChatFormatting.RED + StatCollector.translateToLocal("key.unbound"));
        }
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 40;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (stack.stackTagCompound != null) {
            player.setItemInUse(stack, getMaxItemUseDuration(stack));
        }
        return stack;
    }

    @Override
    public boolean onItemUse(
            final ItemStack stack,
            final EntityPlayer player,
            final World world,
            final int x,
            final int y,
            final int z,
            final int face,
            final float hitX,
            final float hitY,
            final float hitZ) {
        if (stack.stackTagCompound == null) {
            if (!world.isRemote) {
                stack.stackTagCompound = new NBTTagCompound();
                stack.stackTagCompound.setInteger(TAG_COLOUR, world.rand.nextInt(360) + 1);
                stack.stackTagCompound.setInteger(TAG_X, x);
                stack.stackTagCompound.setInteger(TAG_Y, y + 1);
                stack.stackTagCompound.setInteger(TAG_Z, z);
                stack.stackTagCompound.setInteger(TAG_DIM, world.provider.dimensionId);
            } else {
                player.swingItem();
                for (int a = 0; a < 9; a++) {
                    sparkle(world, x + 0.5D, y, z + 0.5D);
                }
            }
            world.playSoundAtEntity(player, "thaumcraft:wand", 0.5F + (float) Math.random() * 0.5F, 1.0F);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void sparkle(final World world, final double x, final double y, final double z) {
        final FXSparkle fx = new FXSparkle(
                world,
                x + 0.33F * world.rand.nextGaussian(),
                y + 0.5D + world.rand.nextFloat(),
                z + 0.33F * world.rand.nextGaussian(),
                1.75F,
                6,
                3 + world.rand.nextInt(3));
        fx.setGravity(0.1F);
        ParticleEngine.instance.addEffect(world, fx);
    }

    @Override
    public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int i) {
        super.onUsingTick(stack, player, i);

        if (player.worldObj.isRemote) {
            sparkle(player.worldObj, player.posX, player.boundingBox.minY, player.posZ);
        }

        final float f = 1.0F + (float) Math.random() * 0.25F;
        if (player.ticksExisted % 5 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", f * 0.1F, f);
        }
    }

    @Override
    public ItemStack onEaten(final ItemStack stack, final World world, final EntityPlayer player) {
        super.onEaten(stack, world, player);
        if (stack.stackTagCompound != null) {
            final int x = stack.stackTagCompound.getInteger(TAG_X);
            final int y = stack.stackTagCompound.getInteger(TAG_Y);
            final int z = stack.stackTagCompound.getInteger(TAG_Z);
            final int dim = stack.stackTagCompound.getInteger(TAG_DIM);

            if (y > -1
                    && dim == world.provider.dimensionId
                    && world.getBlock(x, y, z) == Blocks.air
                    && world.getBlock(x, y + 1, z) == Blocks.air) {
                if (!world.isRemote) {
                    if (player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) player)
                                .playerNetServerHandler.setPlayerLocation(
                                        x + 0.5D, y, z + 0.5D, player.rotationYaw, player.rotationPitch);
                    }
                    world.playSoundAtEntity(player, "mob.endermen.portal", 5.0F, 1.0F);
                }
                player.addPotionEffect(new PotionEffect(Potion.confusion.id, 160, 0));
            } else if (dim != world.provider.dimensionId) {
                HUDHandler.displayString(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("key.invaliddim"), 300, false);
            } else {
                HUDHandler.displayString(
                        EnumChatFormatting.RED + StatCollector.translateToLocal("key.error"), 300, false);
            }
        }
        return stack;
    }
}
