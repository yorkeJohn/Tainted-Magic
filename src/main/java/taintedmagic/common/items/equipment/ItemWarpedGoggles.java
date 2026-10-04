package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.items.armor.ItemGoggles;

public class ItemWarpedGoggles extends ItemGoggles implements IWarpingGear {

    public ItemWarpedGoggles(final ArmorMaterial material, final int j, final int k) {
        super(material, j, k);
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemWarpedGoggles");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemWarpedGoggles");
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "taintedmagic:textures/models/ModelWarpedGoggles.png";
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 1;
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0))
                ? true
                : super.getIsRepairable(stack, repairItem);
    }
}
