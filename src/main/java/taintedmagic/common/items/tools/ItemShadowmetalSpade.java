package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalSpade extends ItemSpade implements IRepairable
{
	public ItemShadowmetalSpade (ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemShadowmetalSpade");
		this.setUnlocalizedName("ItemShadowmetalSpade");
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
