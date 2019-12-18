package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRepairable;

public class ItemHollowDagger extends ItemSword implements IRepairable
{
	public ItemHollowDagger (ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemHollowDagger");
		this.setMaxStackSize(1);
		this.setTextureName("taintedmagic:ItemHollowDagger");
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}
}
