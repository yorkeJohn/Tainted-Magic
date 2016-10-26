package taintedmagic.common.entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;

public class EntityEldritchOrbAttack extends EntityEldritchOrb
{
	public float dmg = 10F;
	public boolean corrosive;

	public EntityEldritchOrbAttack (World w)
	{
		super(w);
	}

	public EntityEldritchOrbAttack (World w, EntityLivingBase e, boolean corrosive)
	{
		super(w, e);
		this.corrosive = corrosive;
	}

	@Override
	protected void onImpact (MovingObjectPosition mop)
	{
		if (!this.worldObj.isRemote && getThrower() != null)
		{
			if (mop.entityHit != null)
			{
				Entity ent = mop.entityHit;
				if (ent instanceof EntityLivingBase)
				{
					((EntityLivingBase) ent).attackEntityFrom(DamageSource.causeIndirectMagicDamage(getThrower(), ent), this.dmg);
					if (this.corrosive)
					{
						try
						{
							((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.wither.id, 160, 1));
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
							((EntityLivingBase) ent).addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 1));
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			this.worldObj.playSoundAtEntity(this, "random.fizz", 0.5F, 2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
			this.ticksExisted = 100;
			this.worldObj.setEntityState(this, (byte) 16);
		}
		setDead();
	}
}