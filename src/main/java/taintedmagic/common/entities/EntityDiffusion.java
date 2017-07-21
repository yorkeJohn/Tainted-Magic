package taintedmagic.common.entities;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityDiffusion extends EntityThrowable
{
	public static final String TAG_DAMAGE = "dmg";

	public boolean corrosive = false;
	public float dmg = 0.0F;

	public EntityDiffusion (World w)
	{
		super(w);
	}

	public EntityDiffusion (World w, EntityLivingBase e, float scatter, float dmg, boolean corrosive)
	{
		super(w, e);
		this.corrosive = corrosive;
		this.dmg = dmg;
		setThrowableHeading(this.motionX, this.motionY, this.motionZ, func_70182_d(), scatter);
	}

	@Override
	public boolean shouldRenderInPass (int pass)
	{
		return pass == 1;
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
		if (this.ticksExisted > 20) setDead();

		this.motionX *= 0.95D;
		this.motionY *= 0.95D;
		this.motionZ *= 0.95D;

		if (this.onGround)
		{
			this.motionX *= 0.66D;
			this.motionY *= 0.66D;
			this.motionZ *= 0.66D;
		}
		super.onUpdate();
	}

	public void writeSpawnData (ByteBuf buf)
	{
		buf.writeFloat(this.dmg);
	}

	public void readSpawnData (ByteBuf buf)
	{
		this.dmg = buf.readFloat();
	}

	protected void onImpact (MovingObjectPosition mop)
	{
		if (!this.worldObj.isRemote && getThrower() != null)
		{
			List l = this.worldObj.getEntitiesWithinAABBExcludingEntity(getThrower(), this.boundingBox.expand(1.0D, 1.0D, 1.0D));

			for (int i = 0; i < l.size(); i++)
			{
				Entity e = (Entity) l.get(i);
				if (e instanceof EntityLivingBase)
				{
					if (mop.entityHit != null)
					{
						((EntityLivingBase) e).attackEntityFrom(DamageSource.causeIndirectMagicDamage(getThrower(), e), this.dmg);
						if (this.corrosive)
						{
							try
							{
								((EntityLivingBase) e).addPotionEffect(new PotionEffect(Potion.wither.id, 160, 1));
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}
						}
						else
						{
							try
							{
								((EntityLivingBase) e).addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 1));
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
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
		cmp.setFloat(TAG_DAMAGE, this.dmg);
	}

	public void readEntityFromNBT (NBTTagCompound cmp)
	{
		super.readEntityFromNBT(cmp);
		this.dmg = cmp.getInteger(TAG_DAMAGE);
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
