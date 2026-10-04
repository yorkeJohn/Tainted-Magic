package taintedmagic.common.blocks.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import taintedmagic.common.blocks.BlockLumos;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;

public class TileLumos extends TileEntity {

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public void updateEntity() {
        if (getBlockType() instanceof BlockLumos && worldObj.rand.nextInt(15) == 0 && worldObj.isRemote) {
            spawnParticles();
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        final FXSparkle fx = new FXSparkle(
                worldObj,
                xCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D,
                yCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D,
                zCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D,
                1.75F,
                6,
                3 + worldObj.rand.nextInt(2));
        fx.slowdown = true;
        fx.setGravity(-0.5F);
        ParticleEngine.instance.addEffect(worldObj, fx);
    }
}
