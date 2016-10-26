package taintedmagic.common.blocks;

import java.util.List;

import taintedmagic.common.TaintedMagic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockShadowmetal extends Block
{
	public BlockShadowmetal ()
	{
		super(Material.iron);
		this.setHardness(5.0F);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setBlockTextureName("taintedmagic:BlockShadowmetal");
		this.setBlockName("BlockShadowmetal");
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public boolean isBeaconBase (IBlockAccess w, int x, int y, int z, int x2, int y2, int z2)
	{
		return true;
	}
}
