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

public class ItemWarpedGoggles extends ItemGoggles implements IWarpingGear
{
	public ItemWarpedGoggles (ArmorMaterial m, int j, int k)
	{
		super(m, j, k);
		this.setMaxDamage(500);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemWarpedGoggles");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemWarpedGoggles");
	}

	public String getArmorTexture (ItemStack s, Entity e, int slot, String type)
	{
		return "taintedmagic:textures/models/ModelWarpedGoggles.png";
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.rare;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 1;
	}

	public boolean getIsRepairable (ItemStack s, ItemStack s2)
	{
		return (s2.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial)) && s2.getItemDamage() == 0) ? true : super.getIsRepairable(s, s2);
	}
}
