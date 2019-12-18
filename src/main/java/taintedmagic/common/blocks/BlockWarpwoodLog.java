package taintedmagic.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.TaintedMagic;

public class BlockWarpwoodLog extends BlockRotatedPillar
{
	private IIcon side;
	private IIcon top;

	public BlockWarpwoodLog ()
	{
		super(Material.wood);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setHardness(2.5F);
		this.setStepSound(soundTypeWood);
		this.setBlockName("BlockWarpwoodLog");
	}

	@SideOnly (Side.CLIENT)
	protected IIcon getTopIcon (int i)
	{
		return this.top;
	}

	@SideOnly (Side.CLIENT)
	protected IIcon getSideIcon (int i)
	{
		return this.side;
	}

	@SideOnly (Side.CLIENT)
	public void registerBlockIcons (IIconRegister ir)
	{
		this.side = ir.registerIcon("taintedmagic:BlockWarpwoodLog_side");
		this.top = ir.registerIcon("taintedmagic:BlockWarpwoodLog_top");
	}

	public boolean canSustainLeaves (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	public boolean isWood (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	public int getFlammability (IBlockAccess w, int x, int y, int z, ForgeDirection face)
	{
		return 0;
	}

	public int getFireSpreadSpeed (IBlockAccess w, int x, int y, int z, ForgeDirection face)
	{
		return 0;
	}
}
