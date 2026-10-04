package taintedmagic.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

public class BlockWarpwoodLeaves extends Block implements IShearable {

    private int[] adjacentTreeBlocks;
    private IIcon opaqueIcon;
    private IIcon transparentIcon;

    public BlockWarpwoodLeaves() {
        super(Material.leaves);
        setCreativeTab(TaintedMagic.tabTM);
        setLightOpacity(0);
        setHardness(0.1F);
        setStepSound(soundTypeGrass);
        setBlockName("BlockWarpwoodLeaves");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(
            final IBlockAccess world, final int x, final int y, final int z, final int side) {
        return !Blocks.leaves.isOpaqueCube() || super.shouldSideBeRendered(world, x, y, z, side);
    }

    @Override
    public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }

    @Override
    public IIcon getIcon(final int side, final int meta) {
        return !Blocks.leaves.isOpaqueCube() ? transparentIcon : opaqueIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister ir) {
        opaqueIcon = ir.registerIcon("taintedmagic:BlockWarpwoodLeaves_opaque");
        transparentIcon = ir.registerIcon("taintedmagic:BlockWarpwoodLeaves_transparent");
    }

    @Override
    public void breakBlock(
            final World world, final int x, final int y, final int z, final Block block, final int meta) {
        final byte dist = 1;
        final int off = dist + 1;

        if (world.checkChunksExist(x - off, y - off, z - off, x + off, y + off, z + off)) {
            for (int i = -dist; i <= dist; i++) {
                for (int j = -dist; j <= dist; j++) {
                    for (int k = -dist; k <= dist; k++) {
                        final Block b = world.getBlock(x + i, y + j, z + k);

                        if (b == Blocks.air) {
                            continue;
                        }
                        b.beginLeavesDecay(world, x + i, y + j, z + k);
                    }
                }
            }
        }
    }

    @Override
    public void updateTick(final World world, final int x, final int y, final int z, final Random random) {
        if (!world.isRemote) {
            final int meta = world.getBlockMetadata(x, y, z);

            if ((meta & 0x8) != 0 && (meta & 0x4) == 0) {
                final byte pos = 4;
                final int off = pos + 1;
                final byte dist = 32;
                final int dist2 = dist * dist;
                final int half = dist / 2;

                if (adjacentTreeBlocks == null) {
                    adjacentTreeBlocks = new int[dist * dist * dist];
                }

                if (world.checkChunksExist(x - off, y - off, z - off, x + off, y + off, z + off)) {
                    for (int i = -pos; i <= pos; i++) {
                        for (int j = -pos; j <= pos; j++) {
                            for (int k = -pos; k <= pos; k++) {
                                final Block block = world.getBlock(x + i, y + j, z + k);

                                if (block != null && block.canSustainLeaves(world, x + i, y + j, z + k)) {
                                    adjacentTreeBlocks[(i + half) * dist2 + (j + half) * dist + k + half] = 0;
                                } else if (block != null && block.isLeaves(world, x + i, y + j, z + k)) {
                                    adjacentTreeBlocks[(i + half) * dist2 + (j + half) * dist + k + half] = -2;
                                } else {
                                    adjacentTreeBlocks[(i + half) * dist2 + (j + half) * dist + k + half] = -1;
                                }
                            }
                        }
                    }
                    for (int i = 1; i <= 4; i++) {
                        for (int j = -pos; j <= pos; j++) {
                            for (int k = -pos; k <= pos; k++) {
                                for (int l = -pos; l <= pos; l++) {
                                    if (adjacentTreeBlocks[(j + half) * dist2 + (k + half) * dist + l + half]
                                            != i - 1) {
                                        continue;
                                    }
                                    if (adjacentTreeBlocks[(j + half - 1) * dist2 + (k + half) * dist + l + half]
                                            == -2) {
                                        adjacentTreeBlocks[(j + half - 1) * dist2 + (k + half) * dist + l + half] = i;
                                    }

                                    if (adjacentTreeBlocks[(j + half + 1) * dist2 + (k + half) * dist + l + half]
                                            == -2) {
                                        adjacentTreeBlocks[(j + half + 1) * dist2 + (k + half) * dist + l + half] = i;
                                    }

                                    if (adjacentTreeBlocks[(j + half) * dist2 + (k + half - 1) * dist + l + half]
                                            == -2) {
                                        adjacentTreeBlocks[(j + half) * dist2 + (k + half - 1) * dist + l + half] = i;
                                    }

                                    if (adjacentTreeBlocks[(j + half) * dist2 + (k + half + 1) * dist + l + half]
                                            == -2) {
                                        adjacentTreeBlocks[(j + half) * dist2 + (k + half + 1) * dist + l + half] = i;
                                    }

                                    if (adjacentTreeBlocks[(j + half) * dist2 + (k + half) * dist + l + half - 1]
                                            == -2) {
                                        adjacentTreeBlocks[(j + half) * dist2 + (k + half) * dist + l + half - 1] = i;
                                    }

                                    if (adjacentTreeBlocks[(j + half) * dist2 + (k + half) * dist + l + half + 1]
                                            != -2) {
                                        continue;
                                    }
                                    adjacentTreeBlocks[(j + half) * dist2 + (k + half) * dist + l + half + 1] = i;
                                }
                            }
                        }
                    }
                }

                final int adj = adjacentTreeBlocks[half * dist2 + half * dist + half];

                if (adj >= 0) {
                    world.setBlock(x, y, z, this, meta & 0xFFFFFFF7, 3);
                } else {
                    removeLeaves(world, x, y, z);
                }
            }
        }
    }

    private void removeLeaves(final World world, final int x, final int y, final int z) {
        dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        world.setBlockToAir(x, y, z);
    }

    @Override
    public void dropBlockAsItemWithChance(
            final World world, final int x, final int y, final int z, final int m, final float f, final int i) {
        if (!world.isRemote && (m & 0x8) != 0 && (m & 0x4) == 0) {
            if ((m & 0x1) == 0 && world.rand.nextInt(50) == 0) {
                dropBlockAsItem(world, x, y, z, new ItemStack(BlockRegistry.BlockWarpwoodSapling));
            }
        }
    }

    @Override
    public void harvestBlock(
            final World world, final EntityPlayer player, final int x, final int y, final int z, final int i) {
        super.harvestBlock(world, player, x, y, z, i);
    }

    @Override
    public int damageDropped(final int i) {
        return i & 0x1;
    }

    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(final int i, final Random random, final int i2) {
        return Item.getItemById(0);
    }

    @Override
    public boolean isLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public void beginLeavesDecay(final World world, final int x, final int y, final int z) {
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 0x8, 4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return 0xFFFFFF;
    }

    @Override
    public ItemStack getPickBlock(
            final MovingObjectPosition mop, final World world, final int x, final int y, final int z) {
        final int m = world.getBlockMetadata(x, y, z);
        return new ItemStack(this, 1, m & 0x1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(final int m) {
        return 0xFFFFFF;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess world, final int x, final int y, final int z) {
        return 0xFFFFFF;
    }

    @Override
    public boolean isShearable(final ItemStack stack, final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(
            final ItemStack stack, final IBlockAccess world, final int x, final int y, final int z, final int fortune) {
        final ArrayList drops = new ArrayList();
        drops.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 0x3));
        return drops;
    }

    @Override
    public int getFlammability(
            final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(
            final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return 0;
    }
}
