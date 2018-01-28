package taintedmagic.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.blocks.tile.TileLumos;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLumos extends Block implements ITileEntityProvider
{
	int duration = 0;

	public BlockLumos ()
	{
		super(Config.airyMaterial);
		this.setBlockName("BlockLumos");
		this.setTickRandomly(false);
		this.setLightLevel(1.0F);
		this.setStepSound(new CustomStepSound("thaumcraft:crystal", 0.3F, 1.0F));
		this.setBlockTextureName("thaumcraft:blank");
	}

	public void setGlowDuration (int i)
	{
		this.duration = i;
	}

	public int getGlowDuration ()
	{
		return this.duration;
	}

	@Override
	public void setBlockBoundsBasedOnState (IBlockAccess w, int x, int y, int z)
	{
		int meta = w.getBlockMetadata(x, y, z);
		if (meta == 2) setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		else setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
	}

	@SideOnly (Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool (World w, int x, int y, int z)
	{
		return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool (World w, int x, int y, int z)
	{
		return null;
	}

	@Override
	public int getRenderType ()
	{
		return -1;
	}

	@Override
	public boolean isSideSolid (IBlockAccess w, int x, int y, int z, ForgeDirection side)
	{
		return false;
	}

	@Override
	public boolean isBlockNormalCube ()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube ()
	{
		return false;
	}

	@Override
	public boolean isReplaceable (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean canBeReplacedByLeaves (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	public boolean renderAsNormalBlock ()
	{
		return false;
	}

	@Override
	public boolean isLeaves (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	@Override
	public Item getItemDropped (int a, Random r, int b)
	{
		return null;
	}

	@Override
	public Item getItem (World w, int x, int y, int z)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity (World w, int m)
	{
		return new TileLumos();
	}
}