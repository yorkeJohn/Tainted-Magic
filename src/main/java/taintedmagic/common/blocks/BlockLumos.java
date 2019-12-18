package taintedmagic.common.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.blocks.tile.TileLumos;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.config.Config;

public class BlockLumos extends Block implements ITileEntityProvider
{
	public BlockLumos ()
	{
		super(Config.airyMaterial);
		this.setStepSound(new Block.SoundType("stone", -1, -1));
		this.setBlockName("BlockLumos");
		this.setTickRandomly(false);
		this.setLightLevel(1.0F);
		this.setBlockTextureName("thaumcraft:blank");
	}

	@Override
	public void onBlockDestroyedByPlayer (World w, int x, int y, int z, int meta)
	{
		super.onBlockDestroyedByPlayer(w, x, y, z, meta);

		w.playSound(x, y, z, "thaumcraft:ice", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F, true);

		for (int a = 0; a < 9; a++)
			if (w.isRemote) spawnBreakParticles(w, x, y, z);
	}

	@SideOnly (Side.CLIENT)
	void spawnBreakParticles (World w, double x, double y, double z)
	{
		FXSparkle fx = new FXSparkle(w, x + w.rand.nextFloat(), y + w.rand.nextFloat(), z + w.rand.nextFloat(), 1.75F, 6, 3 + w.rand.nextInt(3));
		fx.motionX += w.rand.nextGaussian() * 0.1D;
		fx.motionY += w.rand.nextGaussian() * 0.1D;
		fx.motionZ += w.rand.nextGaussian() * 0.1D;
		fx.setGravity(0.5F);
		ParticleEngine.instance.addEffect(w, fx);
	}

	@Override
	public void setBlockBoundsBasedOnState (IBlockAccess w, int x, int y, int z)
	{
		int meta = w.getBlockMetadata(x, y, z);
		if (meta == 0) setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
		else setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
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