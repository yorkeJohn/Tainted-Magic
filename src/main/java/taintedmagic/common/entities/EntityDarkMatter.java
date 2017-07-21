package taintedmagic.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;

public class EntityDarkMatter extends EntityEldritchOrb
{
	public float dmg = 0.0F;
	public boolean corrosive;

	public EntityDarkMatter (World w)
	{
		super(w);
	}

	public EntityDarkMatter (World w, EntityLivingBase e, float dmg, boolean corrosive)
	{
		super(w, e);
		this.corrosive = corrosive;
		this.dmg = dmg;
	}

	@Override
	public boolean shouldRenderInPass (int pass)
	{
		return pass == 1;
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