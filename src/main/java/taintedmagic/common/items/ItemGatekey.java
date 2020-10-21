package taintedmagic.common.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.client.handler.ClientHandler;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.client.lib.UtilsFX;

public class ItemGatekey extends Item implements IWarpingGear
{
    IIcon icon;
    IIcon overlay;

    private static final String TAG_X = "posX";
    private static final String TAG_Y = "posY";
    private static final String TAG_Z = "posZ";
    private static final String TAG_DIM = "dimID";
    private static final String TAG_COLOUR = "colour";

    public ItemGatekey ()
    {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemGatekey");
        this.setMaxStackSize(1);
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack s)
    {
        return TaintedMagic.rarityCreation;
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemGatekey");
        this.overlay = ir.registerIcon("taintedmagic:ItemGatekey_overlay");
    }

    @SideOnly (Side.CLIENT)
    public boolean requiresMultipleRenderPasses ()
    {
        return true;
    }

    @SideOnly (Side.CLIENT)
    public int getRenderPasses (int m)
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
    public int getColorFromItemStack (ItemStack s, int pass)
    {
        if (pass == 1 && s.stackTagCompound != null)
        {
            return Color.HSBtoRGB((float) s.stackTagCompound.getInteger(TAG_COLOUR) / 360F, 1.0F,
                    0.2F * (float) Math.sin((double) ClientHandler.ticks / 150D) + 0.8F);
        }
        return 0xFFFFFF;
    }

    @Override
    public int getWarp (ItemStack s, EntityPlayer p)
    {
        return 3;
    }

    @Override
    public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
    {
        if (s.stackTagCompound != null)
        {
            l.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gatekey.bound"));

            int dim = s.stackTagCompound.getInteger(TAG_DIM);
            l.add(WorldProvider.getProviderForDimension(dim).getDimensionName());

            int x = s.stackTagCompound.getInteger(TAG_X);
            int y = s.stackTagCompound.getInteger(TAG_Y);
            int z = s.stackTagCompound.getInteger(TAG_Z);
            l.add(x + ", " + y + ", " + z);
        }
        else l.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gatekey.inactive"));
    }

    @Override
    public EnumAction getItemUseAction (ItemStack s)
    {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration (ItemStack s)
    {
        return 40;
    }

    @Override
    public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
    {
        if (s.stackTagCompound != null) p.setItemInUse(s, getMaxItemUseDuration(s));
        return s;
    }

    @Override
    public boolean onItemUse (ItemStack s, EntityPlayer p, World w, int x, int y, int z, int face, float hitX, float hitY,
            float hitZ)
    {
        if (s.stackTagCompound == null)
        {
            s.stackTagCompound = new NBTTagCompound();

            s.stackTagCompound.setInteger(TAG_COLOUR, w.rand.nextInt(360) + 1);

            s.stackTagCompound.setInteger(TAG_X, x);
            s.stackTagCompound.setInteger(TAG_Y, y + 1);
            s.stackTagCompound.setInteger(TAG_Z, z);
            s.stackTagCompound.setInteger(TAG_DIM, w.provider.dimensionId);

            w.playSoundAtEntity(p, "thaumcraft:wand", 0.5F + ((float) Math.random() * 0.5F), 1.0F);

            if (w.isRemote)
            {
                p.swingItem();
                for (int a = 0; a < 9; a++)
                    sparkle(w, x + 0.5D, y, z + 0.5D);
            }
        }
        return false;
    }

    @SideOnly (Side.CLIENT)
    public void sparkle (World w, double x, double y, double z)
    {
        FXSparkle fx = new FXSparkle(w, x + 0.33F * w.rand.nextGaussian(), y + 0.5D + w.rand.nextFloat(),
                z + 0.33F * w.rand.nextGaussian(), 1.75F, 6, 3 + w.rand.nextInt(3));
        fx.setGravity(0.1F);
        ParticleEngine.instance.addEffect(w, fx);
    }

    @Override
    public void onUsingTick (ItemStack s, EntityPlayer p, int i)
    {
        super.onUsingTick(s, p, i);

        if (p.worldObj.isRemote) sparkle(p.worldObj, p.posX, p.boundingBox.minY, p.posZ);

        float f = 1.0F + ((float) Math.random() * 0.25F);
        if (p.ticksExisted % 5 == 0) p.worldObj.playSoundAtEntity(p, "thaumcraft:wind", f * 0.1F, f);
    }

    @Override
    public ItemStack onEaten (ItemStack s, World w, EntityPlayer p)
    {
        super.onEaten(s, w, p);
        if (s.stackTagCompound != null)
        {
            int x = s.stackTagCompound.getInteger(TAG_X);
            int y = s.stackTagCompound.getInteger(TAG_Y);
            int z = s.stackTagCompound.getInteger(TAG_Z);
            int dim = s.stackTagCompound.getInteger(TAG_DIM);

            if (y > -1 && dim == w.provider.dimensionId && w.getBlock(x, y, z) == Blocks.air
                    && w.getBlock(x, y + 1, z) == Blocks.air)
            {
                if (!w.isRemote)
                {
                    if (p instanceof EntityPlayerMP)
                        ((EntityPlayerMP) p).playerNetServerHandler.setPlayerLocation((double) x + 0.5D, (double) y,
                                (double) z + 0.5D, p.rotationYaw, p.rotationPitch);
                    w.playSoundAtEntity(p, "mob.endermen.portal", 5.0F, 1.0F);
                }
                try
                {
                    p.addPotionEffect(new PotionEffect(Potion.confusion.id, 140, 1));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (dim != w.provider.dimensionId) HUDHandler
                    .displayString(EnumChatFormatting.RED + StatCollector.translateToLocal("gatekey.invaliddim"), 300, false);
            else HUDHandler.displayString(EnumChatFormatting.RED + StatCollector.translateToLocal("gatekey.error"), 300, false);
        }
        return s;
    }
}
