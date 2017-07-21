package taintedmagic.common.blocks.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import taintedmagic.common.blocks.BlockLumos;
import taintedmagic.common.entities.EntityGlowpet;
import taintedmagic.common.items.wand.foci.ItemFocusLumos;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TileEntityLumos extends TileEntity
{
	int ticksExisted;

	public TileEntityLumos ()
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

		Block b = worldObj.getBlock(xCoord, yCoord, zCoord);
		int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

		List<EntityGlowpet> ents = worldObj.getEntitiesWithinAABB(EntityGlowpet.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(2.0D, 2.0D, 2.0D));
		if (b instanceof BlockLumos)
		{
			BlockLumos src = (BlockLumos) b;
			if (meta == 0 && ents.isEmpty())
			{
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}

			else if (meta == 1 && ticksExisted > src.getGlowDuration())
			{
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
			}

			Random r = new Random();
			if ( (meta == 1 && r.nextInt(50) == 0) || (meta == 2 && r.nextInt(9) == 0))
			{
				Thaumcraft.proxy.sparkle((float) xCoord + 0.5F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, (float) yCoord + 0.5F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, (float) zCoord + 0.5F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, 0.5F, 6, -0.05F);
			}

			if ( (meta == 1 || meta == 2) && r.nextInt(512) == 0)
			{
				for (int a = 0; a < 9; a++)
				{
					FXSparkle fx = new FXSparkle(worldObj, xCoord + worldObj.rand.nextFloat(), yCoord + worldObj.rand.nextFloat(), zCoord + worldObj.rand.nextFloat(), 1.75F, worldObj.rand.nextInt(5), 3 + worldObj.rand.nextInt(3));
					fx.setGravity(0.1F);
					ParticleEngine.instance.addEffect(worldObj, fx);
					if (r.nextBoolean()) break;
				}
			}
		}
	}
}
