package taintedmagic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import taintedmagic.common.TaintedMagic;

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
    public boolean isBeaconBase (IBlockAccess world, int x, int y, int z, int x2, int y2, int z2)
	{
		return true;
	}
}
