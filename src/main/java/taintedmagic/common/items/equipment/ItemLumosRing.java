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

public class ItemLumosRing extends Item implements IBauble {

    public ItemLumosRing() {
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemLumosRing");
        setMaxDamage(-1);
        setMaxStackSize(1);
        setUnlocalizedName("ItemLumosRing");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public boolean canEquip(final ItemStack stack, final EntityLivingBase entity) {
        return true;
    }

    @Override
    public boolean canUnequip(final ItemStack stack, final EntityLivingBase entity) {
        return true;
    }

    @Override
    public BaubleType getBaubleType(final ItemStack stack) {
        return BaubleType.RING;
    }

    @Override
    public void onEquipped(final ItemStack stack, final EntityLivingBase entity) {}

    @Override
    public void onUnequipped(final ItemStack stack, final EntityLivingBase entity) {}

    @Override
    public void onWornTick(final ItemStack stack, final EntityLivingBase entity) {}
}
