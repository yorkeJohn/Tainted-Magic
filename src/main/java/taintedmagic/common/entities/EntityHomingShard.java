package taintedmagic.common.entities;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.utils.EntityUtils;

/**
 * this class is based off of EntityHomingShard.class created by <Azanor> as
 * part of Thaumcraft 5
 */
public class EntityHomingShard extends EntityThrowable implements IEntityAdditionalSpawnData
{
    Class targetClass = null;
    boolean persistant = false;

    int targetID = 0;
    EntityLivingBase target;

    Material[] mats = { Material.plants, Material.air, Material.leaves, Material.portal, Material.vine, Material.web };

    public EntityHomingShard (World world)
    {
        super(world);
    }

    public EntityHomingShard (World world, EntityLivingBase thrower, EntityLivingBase target, int strength, boolean b)
    {
        super(world, thrower);
        this.target = target;
        this.targetClass = target.getClass();
        this.persistant = b;
        setStrength(strength);
        Vec3 vec = thrower.getLookVec();
        setLocationAndAngles(thrower.posX + vec.xCoord / 2.0D, thrower.posY + thrower.getEyeHeight() + vec.yCoord / 2.0D,
                thrower.posZ + vec.zCoord / 2.0D, thrower.rotationYaw, thrower.rotationPitch);
        float f = 0.5F;
        float ry = thrower.rotationYaw + (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0F;
        float rp = thrower.rotationPitch + (this.rand.nextFloat() - this.rand.nextFloat()) * 60.0F;
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
    public void entityInit ()
    {
        super.entityInit();
        this.dataWatcher.addObject(17, (byte) 0);
    }

    public void setStrength (int strength)
    {
        this.dataWatcher.updateObject(17, Byte.valueOf((byte) strength));
    }

    public int getStrength ()
    {
        return this.dataWatcher.getWatchableObjectByte(17);
    }

    @Override
    protected float getGravityVelocity ()
    {
        return 0.0F;
    }

    @Override
    public void writeSpawnData (ByteBuf buf)
    {
        int id = -1;
        if (this.target != null) id = this.target.getEntityId();
        buf.writeInt(id);
    }

    @Override
    public void readSpawnData (ByteBuf buf)
    {
        int id = buf.readInt();
        try
        {
            if (id >= 0) this.target = ((EntityLivingBase) this.worldObj.getEntityByID(id));
        }
        catch (Exception e)
        {
        }
    }

    @Override
    protected void onImpact (MovingObjectPosition mop)
    {
        if (!this.worldObj.isRemote && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                && (getThrower() == null || (getThrower() != null && mop.entityHit != getThrower())))
        {
            mop.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()),
                    2.0F + getStrength() * 0.5F);
            this.worldObj.playSoundAtEntity(this, "thaumcraft:zap", 1.0F,
                    1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.worldObj.setEntityState(this, (byte) 16);
            setDead();
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            Block block = this.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);

            List<Material> whitelisted = new ArrayList<Material>(mats.length);
            for (int i = 0; i < mats.length; i++)
                whitelisted.add(mats[i]);

            if (!whitelisted.contains(block.getMaterial()))
            {
                this.motionZ *= -0.8D;
                this.motionX *= -0.8D;
                this.motionY *= -0.8D;
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
        if (motion > 0.0D && this.worldObj.isRemote) Thaumcraft.proxy.sparkle(
                (float) this.posX + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F,
                (float) this.posY + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F,
                (float) this.posZ + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.25F, 0.5F, 0, 0.0F);

        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.persistant
                    && (this.target == null || this.target.isDead || this.target.getDistanceSqToEntity(this) > 1250.0D))
            {
                ArrayList<Entity> ents = EntityUtils.getEntitiesInRange(this.worldObj, this.posX, this.posY, this.posZ, this,
                        this.targetClass, 16.0D);

                for (Entity entity : ents)
                {
                    if (entity instanceof EntityLivingBase && !entity.isDead
                            && (getThrower() == null || entity.getEntityId() != getThrower().getEntityId()))
                    {
                        this.target = ((EntityLivingBase) entity);
                        break;
                    }
                }
            }
            if (this.target == null || this.target.isDead)
            {
                this.worldObj.setEntityState(this, (byte) 16);
                setDead();
            }
        }

        if (this.ticksExisted > 300)
        {
            this.worldObj.setEntityState(this, (byte) 16);
            setDead();
        }

        if (this.ticksExisted % 10 == 0 && this.target != null && !this.target.isDead)
        {
            double d = getDistanceToEntity(this.target);
            double dx = this.target.posX - this.posX;
            double dy = this.target.boundingBox.minY + this.target.height * 0.6D - this.posY;
            double dz = this.target.posZ - this.posZ;
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
