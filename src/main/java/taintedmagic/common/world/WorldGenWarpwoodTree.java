package taintedmagic.common.world;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.common.lib.world.WorldGenCustomFlowers;

/**
 * This class is based off of WorldGenSilverwoodTrees.class created by <Azanor> as part of Thaumcraft 4.
 */
public class WorldGenWarpwoodTree extends WorldGenAbstractTree {

    private final Block trunk;
    private final Block leaves;
    private final int minTreeHeight;
    private final int randomTreeHeight;
    boolean worldgen = false;

    public WorldGenWarpwoodTree(final boolean doBlockNotify, final int minTreeHeight, final int randomTreeHeight) {
        super(doBlockNotify);
        worldgen = !doBlockNotify;
        this.minTreeHeight = minTreeHeight;
        this.randomTreeHeight = randomTreeHeight;
        trunk = BlockRegistry.BlockWarpwoodLog;
        leaves = BlockRegistry.BlockWarpwoodLeaves;
    }

    @Override
    public boolean generate(final World world, final Random random, final int x, final int y, final int z) {
        final int height = random.nextInt(randomTreeHeight) + minTreeHeight;
        boolean flag = true;
        if (y < 1 || y + height + 1 > 256) return false;
        for (int i1 = y; i1 <= y + 1 + height; ++i1) {
            byte spread = 1;
            if (i1 == y) {
                spread = 0;
            }

            if (i1 >= y + 1 + height - 2) {
                spread = 3;
            }

            for (int j1 = x - spread; j1 <= x + spread && flag; ++j1) {
                for (int k1 = z - spread; k1 <= z + spread && flag; ++k1) {
                    if (i1 >= 0 && i1 < 256) {
                        final Block block = world.getBlock(j1, i1, k1);
                        if (!block.isAir(world, j1, i1, k1)
                                && !block.isLeaves(world, j1, i1, k1)
                                && !block.isReplaceable(world, j1, i1, k1)
                                && i1 > y) {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                }
            }
        }

        if (!flag) return false;
        final Block block1 = world.getBlock(x, y - 1, z);
        final boolean isSoil =
                block1.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) Blocks.sapling);
        if (!isSoil || y >= 256 - height - 1) return false;
        block1.onPlantGrow(world, x, y - 1, z, x, y, z);
        final int start = y + height - 5;
        final int end = y + height + 3 + random.nextInt(3);

        int k2;
        for (k2 = start; k2 <= end; ++k2) {
            final int cty = MathHelper.clamp_int(k2, y + height - 3, y + height);

            for (int xx = x - 5; xx <= x + 5; ++xx) {
                for (int zz = z - 5; zz <= z + 5; ++zz) {
                    final double d3 = xx - x;
                    final double d4 = k2 - cty;
                    final double d5 = zz - z;
                    final double dist = d3 * d3 + d4 * d4 + d5 * d5;

                    if (dist < 10 + random.nextInt(8)
                            && world.getBlock(xx, k2, zz).canBeReplacedByLeaves(world, xx, k2, zz)) {
                        setBlockAndNotifyAdequately(world, xx, k2, zz, leaves, 1);
                    }
                }
            }
        }

        int chance = height;
        boolean lastblock = false;

        for (k2 = 0; k2 < height; k2++) {
            final Block block2 = world.getBlock(x, y + k2, z);

            if (!block2.isAir(world, x, y + k2, z)
                    && !block2.isLeaves(world, x, y + k2, z)
                    && !block2.isReplaceable(world, x, y + k2, z)) {
                continue;
            }

            if (k2 > 0 && !lastblock && random.nextInt(chance) == 0) {
                setBlockAndNotifyAdequately(world, x, y + k2, z, trunk, 2);
                lastblock = true;
            } else {
                setBlockAndNotifyAdequately(world, x, y + k2, z, trunk, 0);
                lastblock = false;
                chance--;
            }
            setBlockAndNotifyAdequately(world, x - 1, y + k2, z, trunk, 0);
            setBlockAndNotifyAdequately(world, x + 1, y + k2, z, trunk, 0);
            setBlockAndNotifyAdequately(world, x, y + k2, z - 1, trunk, 0);
            setBlockAndNotifyAdequately(world, x, y + k2, z + 1, trunk, 0);
        }

        setBlockAndNotifyAdequately(world, x, y + k2, z, trunk, 0);
        setBlockAndNotifyAdequately(world, x - 1, y, z - 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x + 1, y, z + 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x - 1, y, z + 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x + 1, y, z - 1, trunk, 0);

        if (random.nextInt(3) != 0) {
            setBlockAndNotifyAdequately(world, x - 1, y + 1, z - 1, trunk, 0);
        }

        if (random.nextInt(3) != 0) {
            setBlockAndNotifyAdequately(world, x + 1, y + 1, z + 1, trunk, 0);
        }

        if (random.nextInt(3) != 0) {
            setBlockAndNotifyAdequately(world, x - 1, y + 1, z + 1, trunk, 0);
        }

        if (random.nextInt(3) != 0) {
            setBlockAndNotifyAdequately(world, x + 1, y + 1, z - 1, trunk, 0);
        }

        setBlockAndNotifyAdequately(world, x - 2, y, z, trunk, 5);
        setBlockAndNotifyAdequately(world, x + 2, y, z, trunk, 5);
        setBlockAndNotifyAdequately(world, x, y, z - 2, trunk, 9);
        setBlockAndNotifyAdequately(world, x, y, z + 2, trunk, 9);
        setBlockAndNotifyAdequately(world, x - 2, y - 1, z, trunk, 0);
        setBlockAndNotifyAdequately(world, x + 2, y - 1, z, trunk, 0);
        setBlockAndNotifyAdequately(world, x, y - 1, z - 2, trunk, 0);
        setBlockAndNotifyAdequately(world, x, y - 1, z + 2, trunk, 0);
        setBlockAndNotifyAdequately(world, x - 1, y + height - 4, z - 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x + 1, y + height - 4, z + 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x - 1, y + height - 4, z + 1, trunk, 0);
        setBlockAndNotifyAdequately(world, x + 1, y + height - 4, z - 1, trunk, 0);

        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, x - 1, y + height - 5, z - 1, trunk, 0);
        }

        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, x + 1, y + height - 5, z + 1, trunk, 0);
        }

        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, x - 1, y + height - 5, z + 1, trunk, 0);
        }

        if (random.nextInt(3) == 0) {
            setBlockAndNotifyAdequately(world, x + 1, y + height - 5, z - 1, trunk, 0);
        }

        setBlockAndNotifyAdequately(world, x - 2, y + height - 4, z, trunk, 5);
        setBlockAndNotifyAdequately(world, x + 2, y + height - 4, z, trunk, 5);
        setBlockAndNotifyAdequately(world, x, y + height - 4, z - 2, trunk, 9);
        setBlockAndNotifyAdequately(world, x, y + height - 4, z + 2, trunk, 9);

        final WorldGenerator flowers = new WorldGenCustomFlowers(BlockRegistry.BlockNightshadeBush, 0);
        flowers.generate(world, random, x, y, z);

        return true;
    }
}
