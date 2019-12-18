package taintedmagic.common.items.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockShadowOre extends ItemBlock
{
	public ItemBlockShadowOre (Block b)
	{
		super(b);
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}
}
