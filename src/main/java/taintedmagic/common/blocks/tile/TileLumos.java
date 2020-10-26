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

public class TileLumos extends TileEntity
{
    int ticksExisted;

    public TileLumos ()
    {
    }

    public boolean canUpdate ()
    {
        return true;
    }

    @Override
    public void updateEntity ()
    {
        ticksExisted++;

        Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

        if (block instanceof BlockLumos)
        {
            EntityPlayer player = worldObj.getClosestPlayer(xCoord, yCoord, zCoord, 4);

            if (meta == 0 && worldObj.rand.nextInt(15) == 0)
            {
                if (worldObj.isRemote) spawnParticles();
            }
            if ( (meta == 1 || meta == 2) && player == null)
            {
                worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
            else if (meta == 1 && player != null)
            {
                if (player.getHeldItem() == null
                        || (player.getHeldItem() != null && ! (player.getHeldItem().getItem() instanceof ItemWandCasting)))
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                else if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting)
                {
                    ItemStack stack = player.getHeldItem();
                    ItemWandCasting wand = (ItemWandCasting) stack.getItem();

                    if (wand.getFocus(stack) == null
                            || (wand.getFocus(stack) != null && wand.getFocus(stack) != ItemRegistry.ItemFocusLumos))
                        worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                }
            }
            if (meta == 2 && player != null)
            {
                IInventory baub = BaublesApi.getBaubles(player);
                if (baub.getStackInSlot(1) == null && baub.getStackInSlot(2) == null)
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                else if ( (baub.getStackInSlot(1) != null && baub.getStackInSlot(2) == null)
                        && ! (baub.getStackInSlot(1).getItem() instanceof ItemLumosRing))
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                else if ( (baub.getStackInSlot(1) == null && baub.getStackInSlot(2) != null)
                        && ! (baub.getStackInSlot(2).getItem() instanceof ItemLumosRing))
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                else if ( (baub.getStackInSlot(1) != null && baub.getStackInSlot(2) != null)
                        && (! (baub.getStackInSlot(1).getItem() instanceof ItemLumosRing)
                                && ! (baub.getStackInSlot(2).getItem() instanceof ItemLumosRing)))
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
            }
        }
    }

    @SideOnly (Side.CLIENT)
    void spawnParticles ()
    {
        FXSparkle fx = new FXSparkle(worldObj, (double) xCoord + 0.5D + (worldObj.rand.nextGaussian() * 0.1D),
                (double) yCoord + 0.5D + (worldObj.rand.nextGaussian() * 0.1D),
                (double) zCoord + 0.5D + (worldObj.rand.nextGaussian() * 0.1D), 1.75F, 6, 3 + worldObj.rand.nextInt(2));
        fx.slowdown = true;
        fx.setGravity(-0.5F);
        ParticleEngine.instance.addEffect(worldObj, fx);
    }
}