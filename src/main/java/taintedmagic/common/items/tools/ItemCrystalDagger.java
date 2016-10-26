package taintedmagic.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import taintedmagic.api.IBloodlust;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRepairable;

public class ItemCrystalDagger extends ItemSword implements IBloodlust, IRepairable
{
	public ItemCrystalDagger (ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemCrystalDagger");
		this.setMaxStackSize(1);
		this.setTextureName("taintedmagic:ItemCrystalDagger");
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}
}
