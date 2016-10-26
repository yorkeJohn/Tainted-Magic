package taintedmagic.common.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWarpwoodLeaves extends Block implements IShearable
{
	int[] adjacentTreeBlocks;
	private IIcon opaqueIcon;
	private IIcon transparentIcon;

	public BlockWarpwoodLeaves ()
	{
		super(Material.leaves);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setLightOpacity(0);
		this.setHardness(0.1F);
		this.setStepSound(ConfigBlocks.blockTaint.stepSound);
		this.setBlockName("BlockWarpwoodLeaves");
	}

	@Override
	@SideOnly (Side.CLIENT)
	public boolean shouldSideBeRendered (IBlockAccess w, int x, int y, int z, int s)
	{
		return !Blocks.leaves.isOpaqueCube() || super.shouldSideBeRendered(w, x, y, z, s);
	}

	@Override
	public boolean isOpaqueCube ()
	{
		return Blocks.leaves.isOpaqueCube();
	}

	@Override
	public IIcon getIcon (int side, int meta)
	{
		return !Blocks.leaves.isOpaqueCube() ? transparentIcon : opaqueIcon;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void registerBlockIcons (IIconRegister ir)
	{
		opaqueIcon = ir.registerIcon("taintedmagic:BlockWarpwoodLeaves_opaque");
		transparentIcon = ir.registerIcon("taintedmagic:BlockWarpwoodLeaves_transparent");
	}

	public void breakBlock (World w, int x, int y, int z, Block b, int i)
	{
		byte var7 = 1;
		int var8 = var7 + 1;

		if (w.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
		{
			for (int var9 = -var7; var9 <= var7; var9++)
			{
				for (int var10 = -var7; var10 <= var7; var10++)
				{
					for (int var11 = -var7; var11 <= var7; var11++)
					{
						Block var12 = w.getBlock(x + var9, y + var10, z + var11);

						if (var12 == Blocks.air) continue;
						var12.beginLeavesDecay(w, x + var9, y + var10, z + var11);
					}
				}
			}
		}
	}

	public void updateTick (World w, int x, int y, int z, Random r)
	{
		if (!w.isRemote)
		{
			int var6 = w.getBlockMetadata(x, y, z);

			if ( ( (var6 & 0x8) != 0) && ( (var6 & 0x4) == 0))
			{
				byte var7 = 4;
				int var8 = var7 + 1;
				byte var9 = 32;
				int var10 = var9 * var9;
				int var11 = var9 / 2;

				if (this.adjacentTreeBlocks == null)
				{
					this.adjacentTreeBlocks = new int[var9 * var9 * var9];
				}

				if (w.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
				{
					for (int var12 = -var7; var12 <= var7; var12++)
					{
						for (int var13 = -var7; var13 <= var7; var13++)
						{
							for (int var14 = -var7; var14 <= var7; var14++)
							{
								Block block = w.getBlock(x + var12, y + var13, z + var14);

								if ( (block != null) && (block.canSustainLeaves(w, x + var12, y + var13, z + var14)))
								{
									this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11)] = 0;
								}
								else if ( (block != null) && (block.isLeaves(w, x + var12, y + var13, z + var14)))
								{
									this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11)] = -2;
								}
								else
								{
									this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11)] = -1;
								}
							}
						}
					}
					int var15 = 0;
					for (int var12 = 1; var12 <= 4; var12++)
					{
						for (int var13 = -var7; var13 <= var7; var13++)
						{
							for (int var14 = -var7; var14 <= var7; var14++)
							{
								for (var15 = -var7; var15 <= var7; var15++)
								{
									if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11)] != var12 - 1) continue;
									if (this.adjacentTreeBlocks[ ( (var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11)] == -2)
									{
										this.adjacentTreeBlocks[ ( (var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15 + var11)] = var12;
									}

									if (this.adjacentTreeBlocks[ ( (var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11)] == -2)
									{
										this.adjacentTreeBlocks[ ( (var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15 + var11)] = var12;
									}

									if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11)] == -2)
									{
										this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15 + var11)] = var12;
									}

									if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11)] == -2)
									{
										this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15 + var11)] = var12;
									}

									if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1))] == -2)
									{
										this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + (var15 + var11 - 1))] = var12;
									}

									if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1)] != -2) continue;
									this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11 + 1)] = var12;
								}
							}
						}
					}
				}

				int var12 = this.adjacentTreeBlocks[ (var11 * var10 + var11 * var9 + var11)];

				if (var12 >= 0)
				{
					w.setBlock(x, y, z, this, var6 & 0xFFFFFFF7, 3);
				}
				else
				{
					removeLeaves(w, x, y, z);
				}
			}
		}
	}

	private void removeLeaves (World w, int x, int y, int z)
	{
		dropBlockAsItem(w, x, y, z, w.getBlockMetadata(x, y, z), 0);
		w.setBlockToAir(x, y, z);
	}

	public void dropBlockAsItemWithChance (World w, int x, int y, int z, int m, float f, int i)
	{
		if ( (!w.isRemote) && ( (m & 0x8) != 0) && ( (m & 0x4) == 0))
		{
			if ( ( (m & 0x1) == 0) && (w.rand.nextInt(50) == 0))
			{
				dropBlockAsItem(w, x, y, z, new ItemStack(BlockRegistry.BlockWarpwoodSapling));
			}
		}
	}

	public void harvestBlock (World w, EntityPlayer p, int x, int y, int z, int i)
	{
		super.harvestBlock(w, p, x, y, z, i);
	}

	public int damageDropped (int i)
	{
		return i & 0x1;
	}

	public int quantityDropped (Random r)
	{
		return 0;
	}

	public Item getItemDropped (int i, Random r, int i2)
	{
		return Item.getItemById(0);
	}

	@Override
	public boolean isLeaves (IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	public void beginLeavesDecay (World w, int x, int y, int z)
	{
		w.setBlockMetadataWithNotify(x, y, z, w.getBlockMetadata(x, y, z) | 0x8, 4);
	}

	@SideOnly (Side.CLIENT)
	public int getBlockColor ()
	{
		return 0xFFFFFF;
	}

	public ItemStack getPickBlock (MovingObjectPosition t, World w, int x, int y, int z)
	{
		int m = w.getBlockMetadata(x, y, z);
		return new ItemStack(this, 1, m & 0x1);
	}

	@SideOnly (Side.CLIENT)
	@Override
	public int getRenderColor (int m)
	{
		return 0xFFFFFF;
	}

	@SideOnly (Side.CLIENT)
	public int colorMultiplier (IBlockAccess w, int x, int y, int z)
	{
		return 0xFFFFFF;
	}

	@Override
	public boolean isShearable (ItemStack s, IBlockAccess w, int x, int y, int z)
	{
		return true;
	}

	public ArrayList<ItemStack> onSheared (ItemStack s, IBlockAccess w, int x, int y, int z, int fortune)
	{
		ArrayList ret = new ArrayList();
		ret.add(new ItemStack(this, 1, w.getBlockMetadata(x, y, z) & 0x3));
		return ret;
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