package taintedmagic.common.items.equipment;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;

public class ItemLumosRing extends Item implements IBauble
{
    public ItemLumosRing ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setTextureName("taintedmagic:ItemLumosRing");
        this.setMaxDamage(-1);
        this.setMaxStackSize(1);
        this.setUnlocalizedName("ItemLumosRing");
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
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
        return BaubleType.RING;
    }

    @Override
    public void onEquipped (ItemStack stack, EntityLivingBase entity)
    {
    }

    @Override
    public void onUnequipped (ItemStack stack, EntityLivingBase entity)
    {
    }

    @Override
    public void onWornTick (ItemStack stack, EntityLivingBase entity)
    {
    }
}
