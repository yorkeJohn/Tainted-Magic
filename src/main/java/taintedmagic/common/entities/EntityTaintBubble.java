package taintedmagic.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.api.potions.PotionFluxTaint;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;

public class EntityTaintBubble extends EntityThrowable implements IEntityAdditionalSpawnData
{
	public static final String TAG_DURATION = "duration";
	public static final String TAG_DAMAGE = "damage";

	public boolean corrosive = false;
	public int duration = 20;
	public float damage = 5F;

	public EntityTaintBubble (World w)
	{
		super(w);
	}

	public EntityTaintBubble (World w, EntityLivingBase e, float scatter, boolean corrosive)
	{
		super(w, e);
		this.corrosive = corrosive;
		setThrowableHeading(this.motionX, this.motionY, this.motionZ, func_70182_d(), scatter);
	}

	protected float getGravityVelocity ()
	{
		return 0.0F;
	}

	protected float func_70182_d ()
	{
		return 1.0F;
	}

	public void onUpdate ()
	{
		if (this.ticksExisted > this.duration) setDead();

		if (this.duration <= 20)
		{
			this.motionX *= 0.95D;
			this.motionY *= 0.95D;
			this.motionZ *= 0.95D;
		}
		else
		{
			this.motionX *= 0.975D;
			this.motionY *= 0.975D;
			this.motionZ *= 0.975D;
		}

		if (this.onGround)
		{
			this.motionX *= 0.66D;
			this.motionY *= 0.66D;
			this.motionZ *= 0.66D;
		}
		super.onUpdate();
	}

	public void writeSpawnData (ByteBuf data)
	{
		data.writeByte(this.duration);
	}

	public void readSpawnData (ByteBuf data)
	{
		this.duration = data.readByte();
	}

	protected void onImpact (MovingObjectPosition mop)
	{
		if ( (!this.worldObj.isRemote) && (getThrower() != null))
		{
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(getThrower(), this.boundingBox.expand(1.0D, 1.0D, 1.0D));

			for (int i = 0; i < list.size(); i++)
			{
				Entity ent = (Entity) list.get(i);
				if (ent instanceof EntityLivingBase)
				{
					if (mop.entityHit != null)
					{
						((EntityLivingBase) ent).attackEntityFrom(new EntityDamageSourceIndirect("taint", this, getThrower()).setMagicDamage(), this.damage);
						if (this.corrosive)
						{
							try
							{
								((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.wither.id, 100, 1));
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							try
							{
								((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.poison.id, 100, 1));
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		setDead();
	}

	protected boolean canTriggerWalking ()
	{
		return false;
	}

	@SideOnly (Side.CLIENT)
	public float getShadowSize ()
	{
		return 0.1F;
	}

	public void writeEntityToNBT (NBTTagCompound cmp)
	{
		super.writeEntityToNBT(cmp);
		cmp.setFloat(TAG_DAMAGE, this.damage);
		cmp.setInteger(TAG_DURATION, this.duration);
	}

	public void readEntityFromNBT (NBTTagCompound cmp)
	{
		super.readEntityFromNBT(cmp);
		this.damage = cmp.getInteger(TAG_DAMAGE);
		this.duration = cmp.getInteger(TAG_DURATION);
	}

	public boolean canBeCollidedWith ()
	{
		return false;
	}

	public boolean attackEntityFrom (DamageSource s, float f)
	{
		return false;
	}
}
