package taintedmagic.common.blocks.tile;

import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import taintedmagic.common.blocks.BlockLumos;
import taintedmagic.common.items.equipment.ItemLumosRing;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TileLumos extends TileEntity {

    @Override
    public boolean canUpdate () {
        return true;
    }

    @Override
    public void updateEntity () {
        if (this.getBlockType() instanceof BlockLumos) {
            if (worldObj.rand.nextInt(15) == 0 && worldObj.isRemote) {
                spawnParticles();
            }
        }
    }

    @SideOnly (Side.CLIENT)
    private void spawnParticles () {
        final FXSparkle fx = new FXSparkle(worldObj, xCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D,
                yCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D, zCoord + 0.5D + worldObj.rand.nextGaussian() * 0.1D, 1.75F,
                6, 3 + worldObj.rand.nextInt(2));
        fx.slowdown = true;
        fx.setGravity(-0.5F);
        ParticleEngine.instance.addEffect(worldObj, fx);
    }
}