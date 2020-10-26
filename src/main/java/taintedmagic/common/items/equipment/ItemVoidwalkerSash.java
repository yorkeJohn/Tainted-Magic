package taintedmagic.common.items.equipment;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.ItemRunic;
import thaumcraft.common.Thaumcraft;

public class ItemVoidwalkerSash extends ItemRunic implements IRunicArmor, IWarpingGear, IBauble
{
    public static final String TAG_MODE = "mode";

    public ItemVoidwalkerSash ()
    {
        super(20);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemVoidwalkerSash");
        this.setMaxDamage(-1);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ItemVoidwalkerSash");

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.epic;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 2;
    }

    @Override
    public int getRunicCharge (ItemStack stack)
    {
        return 20;
    }

    @Override
    public boolean canEquip (ItemStack stack, EntityLivingBase entity)
    {
        return true;
    }

    @Override
    public boolean canUnequip (ItemStack stack, EntityLivingBase entity)
    {
        return true;
    }

    @Override
    public BaubleType getBaubleType (ItemStack stack)
    {
        return BaubleType.BELT;
    }

    @Override
    public void onEquipped (ItemStack stack, EntityLivingBase entity)
    {
        Thaumcraft.instance.runicEventHandler.isDirty = true;
    }

    @Override
    public void onUnequipped (ItemStack stack, EntityLivingBase entity)
    {
        Thaumcraft.instance.runicEventHandler.isDirty = true;
    }

    @Override
    public void onWornTick (ItemStack stack, EntityLivingBase entity)
    {
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && player.isSneaking())
        {
            if (stack.stackTagCompound == null)
            {
                stack.setTagCompound(new NBTTagCompound());
                stack.stackTagCompound.setBoolean(TAG_MODE, true);
            }
            if (stack.stackTagCompound != null)
            {
                stack.stackTagCompound.setBoolean(TAG_MODE, !stack.stackTagCompound.getBoolean(TAG_MODE));
                if (isSpeedEnabled(stack))
                    HUDHandler.displayString(EnumChatFormatting.GREEN + StatCollector.translateToLocal("text.sash.speed.on"),
                            300, false);
                else HUDHandler.displayString(EnumChatFormatting.RED + StatCollector.translateToLocal("text.sash.speed.off"),
                        300, false);
            }
        }
        return stack;
    }

    /**
     * Returns true if the speed boost feature is enabled.
     */
    public static boolean isSpeedEnabled (ItemStack stack)
    {
        if (stack.stackTagCompound == null) return true;
        else return stack.stackTagCompound.getBoolean(TAG_MODE);
    }
}
