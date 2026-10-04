package taintedmagic.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
 * This class is based off of EntityHomingShard.class created by <Azanor> as part of Thaumcraft 5
 */
public class EntityHomingShard extends EntityThrowable implements IEntityAdditionalSpawnData {

    private Class targetClass;
    private boolean persistent;
    private EntityLivingBase target;

    private static final Material[] MATS = {
        Material.plants, Material.air, Material.leaves, Material.portal, Material.vine, Material.web
    };

    public EntityHomingShard(final World world) {
        super(world);
    }

    public EntityHomingShard(
            final World world,
            final EntityLivingBase thrower,
            final EntityLivingBase target,
            final int strength,
            final boolean persistent) {
        super(world, thrower);
        this.target = target;
        targetClass = target.getClass();
        this.persistent = persistent;
        setStrength(strength);
        final Vec3 vec = thrower.getLookVec();
        setLocationAndAngles(
                thrower.posX + vec.xCoord / 2.0D,
                thrower.posY + thrower.getEyeHeight() + vec.yCoord / 2.0D,
                thrower.posZ + vec.zCoord / 2.0D,
                thrower.rotationYaw,
                thrower.rotationPitch);
        final float f = 0.5F;
        final float ry = thrower.rotationYaw + (rand.nextFloat() - rand.nextFloat()) * 60.0F;
        final float rp = thrower.rotationPitch + (rand.nextFloat() - rand.nextFloat()) * 60.0F;
        motionX = -MathHelper.sin(ry / 180.0F * (float) Math.PI) * MathHelper.cos(rp / 180.0F * (float) Math.PI) * f;
        motionZ = MathHelper.cos(ry / 180.0F * (float) Math.PI) * MathHelper.cos(rp / 180.0F * (float) Math.PI) * f;
        motionY = -MathHelper.sin(rp / 180.0F * (float) Math.PI) * f;
    }

    @Override
    public boolean shouldRenderInPass(final int pass) {
        return pass == 1;
    }

    @Override
    public void entityInit() {
        super.entityInit();
        dataWatcher.addObject(17, (byte) 0);
    }

    public void setStrength(final int strength) {
        dataWatcher.updateObject(17, Byte.valueOf((byte) strength));
    }

    public int getStrength() {
        return dataWatcher.getWatchableObjectByte(17);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    public void writeSpawnData(final ByteBuf buf) {
        int id = -1;
        if (target != null) {
            id = target.getEntityId();
        }
        buf.writeInt(id);
    }

    @Override
    public void readSpawnData(final ByteBuf buf) {
        final int id = buf.readInt();
        if (id >= 0) {
            target = (EntityLivingBase) worldObj.getEntityByID(id);
        }
    }

    @Override
    protected void onImpact(final MovingObjectPosition mop) {
        if (!worldObj.isRemote
                && mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY
                && (getThrower() == null || getThrower() != null && mop.entityHit != getThrower())) {
            mop.entityHit.attackEntityFrom(
                    DamageSource.causeIndirectMagicDamage(this, getThrower()), 2.0F + getStrength() * 0.5F);
            worldObj.playSoundAtEntity(
                    this, "thaumcraft:zap", 1.0F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
            worldObj.setEntityState(this, (byte) 16);
            setDead();
        }

        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final Block block = worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);

            final List<Material> whitelisted = new ArrayList<>(MATS.length);
            Collections.addAll(whitelisted, MATS);

            if (!whitelisted.contains(block.getMaterial())) {
                motionZ *= -0.8D;
                motionX *= -0.8D;
                motionY *= -0.8D;
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(final byte b) {
        if (b == 16) {
            Thaumcraft.proxy.burst(worldObj, posX, posY, posZ, 0.3F);
        } else {
            super.handleHealthUpdate(b);
        }
    }

    @Override
    public void onUpdate() {
        final double motion = Math.abs(motionX) + Math.abs(motionY) + Math.abs(motionZ);
        if (motion > 0.0D && worldObj.isRemote) {
            Thaumcraft.proxy.sparkle(
                    (float) posX + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.25F,
                    (float) posY + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.25F,
                    (float) posZ + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.25F,
                    0.5F,
                    0,
                    0.0F);
        }

        super.onUpdate();

        if (!worldObj.isRemote) {
            if (persistent && (target == null || target.isDead || target.getDistanceSqToEntity(this) > 1250.0D)) {
                final ArrayList<Entity> ents =
                        EntityUtils.getEntitiesInRange(worldObj, posX, posY, posZ, this, targetClass, 16.0D);

                for (final Entity entity : ents) {
                    if (entity instanceof EntityLivingBase
                            && !entity.isDead
                            && (getThrower() == null
                                    || entity.getEntityId() != getThrower().getEntityId())) {
                        target = (EntityLivingBase) entity;
                        break;
                    }
                }
            }
            if (target == null || target.isDead) {
                worldObj.setEntityState(this, (byte) 16);
                setDead();
            }
        }

        if (ticksExisted > 300) {
            worldObj.setEntityState(this, (byte) 16);
            setDead();
        }

        if (ticksExisted % 10 == 0 && target != null && !target.isDead) {
            final double d = getDistanceToEntity(target);
            double dx = target.posX - posX;
            double dy = target.boundingBox.minY + target.height * 0.6D - posY;
            double dz = target.posZ - posZ;
            dx /= d;
            dy /= d;
            dz /= d;

            motionX = dx;
            motionY = dy;
            motionZ = dz;

            worldObj.playSoundAtEntity(this, "taintedmagic:shard", 0.1F, 2.0F * worldObj.rand.nextFloat());
        }
        motionX *= 0.85D;
        motionY *= 0.85D;
        motionZ *= 0.85D;
    }
}
