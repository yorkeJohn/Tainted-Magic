package taintedmagic.common.blocks;

import java.util.ArrayList;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
        this.setStepSound(soundTypeGrass);
        this.setBlockName("BlockWarpwoodLeaves");
    }

    @Override
    @SideOnly (Side.CLIENT)
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side)
    {
        return !Blocks.leaves.isOpaqueCube() || super.shouldSideBeRendered(world, x, y, z, side);
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

    public void breakBlock (World world, int x, int y, int z, Block block, int i)
    {
        byte var7 = 1;
        int var8 = var7 + 1;

        if (world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
        {
            for (int var9 = -var7; var9 <= var7; var9++)
            {
                for (int var10 = -var7; var10 <= var7; var10++)
                {
                    for (int var11 = -var7; var11 <= var7; var11++)
                    {
                        Block var12 = world.getBlock(x + var9, y + var10, z + var11);

                        if (var12 == Blocks.air) continue;
                        var12.beginLeavesDecay(world, x + var9, y + var10, z + var11);
                    }
                }
            }
        }
    }

    public void updateTick (World world, int x, int y, int z, Random random)
    {
        if (!world.isRemote)
        {
            int var6 = world.getBlockMetadata(x, y, z);

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

                if (world.checkChunksExist(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8))
                {
                    for (int var12 = -var7; var12 <= var7; var12++)
                    {
                        for (int var13 = -var7; var13 <= var7; var13++)
                        {
                            for (int var14 = -var7; var14 <= var7; var14++)
                            {
                                Block block = world.getBlock(x + var12, y + var13, z + var14);

                                if ( (block != null) && (block.canSustainLeaves(world, x + var12, y + var13, z + var14)))
                                {
                                    this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14
                                            + var11)] = 0;
                                }
                                else if ( (block != null) && (block.isLeaves(world, x + var12, y + var13, z + var14)))
                                {
                                    this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14
                                            + var11)] = -2;
                                }
                                else
                                {
                                    this.adjacentTreeBlocks[ ( (var12 + var11) * var10 + (var13 + var11) * var9 + var14
                                            + var11)] = -1;
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
                                    if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15
                                            + var11)] != var12 - 1)
                                        continue;
                                    if (this.adjacentTreeBlocks[ ( (var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15
                                            + var11)] == -2)
                                    {
                                        this.adjacentTreeBlocks[ ( (var13 + var11 - 1) * var10 + (var14 + var11) * var9 + var15
                                                + var11)] = var12;
                                    }

                                    if (this.adjacentTreeBlocks[ ( (var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15
                                            + var11)] == -2)
                                    {
                                        this.adjacentTreeBlocks[ ( (var13 + var11 + 1) * var10 + (var14 + var11) * var9 + var15
                                                + var11)] = var12;
                                    }

                                    if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15
                                            + var11)] == -2)
                                    {
                                        this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 - 1) * var9 + var15
                                                + var11)] = var12;
                                    }

                                    if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15
                                            + var11)] == -2)
                                    {
                                        this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11 + 1) * var9 + var15
                                                + var11)] = var12;
                                    }

                                    if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9
                                            + (var15 + var11 - 1))] == -2)
                                    {
                                        this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9
                                                + (var15 + var11 - 1))] = var12;
                                    }

                                    if (this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15
                                            + var11 + 1)] != -2)
                                        continue;
                                    this.adjacentTreeBlocks[ ( (var13 + var11) * var10 + (var14 + var11) * var9 + var15 + var11
                                            + 1)] = var12;
                                }
                            }
                        }
                    }
                }

                int var12 = this.adjacentTreeBlocks[ (var11 * var10 + var11 * var9 + var11)];

                if (var12 >= 0)
                {
                    world.setBlock(x, y, z, this, var6 & 0xFFFFFFF7, 3);
                }
                else
                {
                    removeLeaves(world, x, y, z);
                }
            }
        }
    }

    private void removeLeaves (World world, int x, int y, int z)
    {
        dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    public void dropBlockAsItemWithChance (World world, int x, int y, int z, int m, float f, int i)
    {
        if ( (!world.isRemote) && ( (m & 0x8) != 0) && ( (m & 0x4) == 0))
        {
            if ( ( (m & 0x1) == 0) && (world.rand.nextInt(50) == 0))
            {
                dropBlockAsItem(world, x, y, z, new ItemStack(BlockRegistry.BlockWarpwoodSapling));
            }
        }
    }

    public void harvestBlock (World world, EntityPlayer player, int x, int y, int z, int i)
    {
        super.harvestBlock(world, player, x, y, z, i);
    }

    public int damageDropped (int i)
    {
        return i & 0x1;
    }

    public int quantityDropped (Random random)
    {
        return 0;
    }

    public Item getItemDropped (int i, Random random, int i2)
    {
        return Item.getItemById(0);
    }

    @Override
    public boolean isLeaves (IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    public void beginLeavesDecay (World world, int x, int y, int z)
    {
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 0x8, 4);
    }

    @SideOnly (Side.CLIENT)
    public int getBlockColor ()
    {
        return 0xFFFFFF;
    }

    public ItemStack getPickBlock (MovingObjectPosition mop, World world, int x, int y, int z)
    {
        int m = world.getBlockMetadata(x, y, z);
        return new ItemStack(this, 1, m & 0x1);
    }

    @SideOnly (Side.CLIENT)
    @Override
    public int getRenderColor (int m)
    {
        return 0xFFFFFF;
    }

    @SideOnly (Side.CLIENT)
    public int colorMultiplier (IBlockAccess world, int x, int y, int z)
    {
        return 0xFFFFFF;
    }

    @Override
    public boolean isShearable (ItemStack stack, IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    public ArrayList<ItemStack> onSheared (ItemStack stack, IBlockAccess world, int x, int y, int z, int fortune)
    {
        ArrayList ret = new ArrayList();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 0x3));
        return ret;
    }

    public int getFlammability (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }

    public int getFireSpreadSpeed (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }
}