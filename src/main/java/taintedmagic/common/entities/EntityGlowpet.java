package taintedmagic.common.entities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.common.Thaumcraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityGlowpet extends EntityThrowable
{
	public int maxAge = 0;
	Material[] mats = { Material.plants, Material.air, Material.leaves, Material.portal, Material.vine, Material.web, Material.portal };

	public EntityGlowpet (World w)
	{
		super(w);
	}

	public EntityGlowpet (World w, EntityLivingBase p, int maxAgeMul)
	{
		super(w, p);
		this.maxAge = 200 * maxAgeMul;
		Vec3 v = p.getLookVec();
		setLocationAndAngles(p.posX + v.xCoord / 2.0D, p.posY + p.getEyeHeight() + v.yCoord / 2.0D, p.posZ + v.zCoord / 2.0D, p.rotationYaw, p.rotationPitch);
		float f = 0.5F;
		float ry = p.rotationYaw + (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0F;
		float rp = p.rotationPitch + (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0F;
		this.motionX = (-MathHelper.sin(ry / 180.0F * (float) Math.PI) * MathHelper.cos(rp / 180.0F * (float) Math.PI) * f);
		this.motionZ = (MathHelper.cos(ry / 180.0F * (float) Math.PI) * MathHelper.cos(rp / 180.0F * (float) Math.PI) * f);
		this.motionY = (-MathHelper.sin(rp / 180.0F * (float) Math.PI) * f);
	}

	@Override
	public boolean shouldRenderInPass (int pass)
	{
		return pass == 1;
	}

	@Override
	protected float getGravityVelocity ()
	{
		return 0.0F;
	}

	@Override
	protected void onImpact (MovingObjectPosition mop)
	{
		if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			Block b = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);

			int l = mats.length;
			List<Material> whitelisted = new ArrayList<Material>(l);
			for (int i = 0; i < l; i++)
				whitelisted.add(mats[i]);

			if (!whitelisted.contains(b.getMaterial()))
			{
				this.motionZ *= -0.5D;
				this.motionX *= -0.5D;
				this.motionY *= -0.5D;
			}
		}
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void handleHealthUpdate (byte b)
	{
		if (b == 16) Thaumcraft.proxy.burst(worldObj, this.posX, this.posY, this.posZ, 0.3F);
		else super.handleHealthUpdate(b);
	}

	@Override
	public void onUpdate ()
	{
		double motion = Math.abs(this.motionX) + Math.abs(this.motionY) + Math.abs(this.motionZ);
		if (motion > 0.0D && this.worldObj.isRemote) Thaumcraft.proxy.sparkle((float) this.posX + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, (float) this.posY + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, (float) this.posZ + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, 0.5F, 6, 0.0F);

		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			int x = MathHelper.floor_double(this.posX);
			int y = MathHelper.floor_double(this.posY - this.getYOffset());
			int z = MathHelper.floor_double(this.posZ);

			for (int i = -1; i < 2; i++)
			{
				for (int j = -1; j < 2; j++)
				{
					for (int k = -1; k < 2; k++)
					{
						if (this.worldObj.getBlock(x + i, y + k, z + j) == Blocks.air)
						{
							this.worldObj.setBlock(x + i, y + k, z + j, BlockRegistry.BlockLumos, 0, 3);
							break;
						}
					}
				}
			}

			if (this.getThrower() == null || this.getThrower().isDead || this.getThrower().getDistanceSqToEntity(this) > 1250.0D || this.ticksExisted > this.maxAge || this.isWet())
			{
				this.worldObj.setEntityState(this, (byte) 16);
				setDead();
			}
		}

		if (this.ticksExisted % 10 == 0 && this.getThrower() != null && !this.getThrower().isDead && this.getThrower().getDistanceSqToEntity(this) > 10.0D)
		{
			double d = getDistanceToEntity(this.getThrower());
			double dx = this.getThrower().posX - this.posX;
			double dy = this.getThrower().boundingBox.minY + this.getThrower().height * 0.6D - this.posY;
			double dz = this.getThrower().posZ - this.posZ;
			dx /= d;
			dy /= d;
			dz /= d;

			this.motionX = dx;
			this.motionY = dy;
			this.motionZ = dz;

			this.worldObj.playSoundAtEntity(this, "taintedmagic:shard", 0.1F, 2.0F * this.worldObj.rand.nextFloat());
		}
		this.motionX *= 0.85D;
		this.motionY *= 0.85D;
		this.motionZ *= 0.85D;
	}
}
