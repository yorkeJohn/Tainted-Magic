package taintedmagic.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.config.Config;

public class BlockLumos extends Block implements ITileEntityProvider {

    public BlockLumos() {
        super(Config.airyMaterial);
        setStepSound(new Block.SoundType("stone", -1, -1));
        setBlockName("BlockLumos");
        setTickRandomly(false);
        setLightLevel(1.0F);
        setBlockTextureName("thaumcraft:blank");
        setBlockBounds(0.3F, 0.3F, 0.3F, 0.7F, 0.7F, 0.7F);
    }

    @Override
    public void onBlockDestroyedByPlayer(final World world, final int x, final int y, final int z, final int meta) {
        super.onBlockDestroyedByPlayer(world, x, y, z, meta);

        world.playSound(x, y, z, "thaumcraft:ice", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F, true);

        for (int i = 0; i < 9; i++) {
            if (world.isRemote) {
                spawnBreakParticles(world, x, y, z);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    void spawnBreakParticles(final World world, final double x, final double y, final double z) {
        final FXSparkle fx = new FXSparkle(
                world,
                x + world.rand.nextFloat(),
                y + world.rand.nextFloat(),
                z + world.rand.nextFloat(),
                1.75F,
                6,
                3 + world.rand.nextInt(3));
        fx.motionX += world.rand.nextGaussian() * 0.1D;
        fx.motionY += world.rand.nextGaussian() * 0.1D;
        fx.motionZ += world.rand.nextGaussian() * 0.1D;
        fx.setGravity(0.5F);
        ParticleEngine.instance.addEffect(world, fx);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
        return null;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isSideSolid(
            final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isReplaceable(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public Item getItemDropped(final int a, final Random random, final int b) {
        return null;
    }

    @Override
    public Item getItem(final World world, final int x, final int y, final int z) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(final World world, final int m) {
        return new TileLumos();
    }
}
