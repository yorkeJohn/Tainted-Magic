package taintedmagic.common.entities;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;

public class EntityDiffusion extends EntityThrowable
{
    public static final String TAG_DAMAGE = "dmg";

    public boolean corrosive = false;
    public float dmg = 0.0F;

    public EntityDiffusion (World world)
    {
        super(world);
    }

    public EntityDiffusion (World world, EntityLivingBase entity, float scatter, float dmg, boolean corrosive)
    {
        super(world, entity);
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

    public void handleHealthUpdate (byte b)
    {
        if (b == 16)
        {
            if (this.worldObj.isRemote)
            {
                float fx = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F;
                float fy = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F;
                float fz = (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.3F;
                Thaumcraft.proxy.wispFX3(this.worldObj, this.posX + fx, this.posY + fy, this.posZ + fz, this.posX + fx * 8.0F,
                        this.posY + fy * 8.0F, this.posZ + fz * 8.0F, 0.3F, 5, true, 0.02F);
            }
        }
        else
        {
            super.handleHealthUpdate(b);
        }
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
            List entities =
                    this.worldObj.getEntitiesWithinAABBExcludingEntity(getThrower(), this.boundingBox.expand(1.0D, 1.0D, 1.0D));

            for (int i = 0; i < entities.size(); i++)
            {
                Entity entity = (Entity) entities.get(i);
                if (entity instanceof EntityLivingBase)
                {
                    if (mop.entityHit != null)
                    {
                        ((EntityLivingBase) entity)
                                .attackEntityFrom(DamageSource.causeIndirectMagicDamage(getThrower(), entity), this.dmg);
                        if (this.corrosive)
                        {
                            try
                            {
                                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.wither.id, 160, 1));
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
                                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 1));
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
        this.worldObj.setEntityState(this, (byte) 16);
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

    public void writeEntityToNBT (NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);
        tag.setFloat(TAG_DAMAGE, this.dmg);
    }

    public void readEntityFromNBT (NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);
        this.dmg = tag.getInteger(TAG_DAMAGE);
    }

    public boolean canBeCollidedWith ()
    {
        return false;
    }

    public boolean attackEntityFrom (DamageSource dmg, float f)
    {
        return false;
    }
}
