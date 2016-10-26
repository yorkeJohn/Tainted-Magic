package taintedmagic.common.items.tools;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ItemShadowmetalAxe extends ItemAxe implements IRepairable
{
	public ItemShadowmetalAxe (ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemShadowmetalAxe");
		this.setUnlocalizedName("ItemShadowmetalAxe");
	}

	public boolean getIsRepairable (ItemStack s, ItemStack s2)
	{
		return (s2.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial)) && s2.getItemDamage() == 0) ? true : super.getIsRepairable(s, s2);
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}
}
