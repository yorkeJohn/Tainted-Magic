package taintedmagic.common.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import thaumcraft.common.Thaumcraft;

public class EntityDiffusion extends EntityThrowable {

    public static final String TAG_DAMAGE = "dmg";

    public boolean corrosive = false;
    public float dmg = 0.0F;

    public EntityDiffusion(final World world) {
        super(world);
    }

    public EntityDiffusion(
            final World world,
            final EntityLivingBase entity,
            final float scatter,
            final float dmg,
            final boolean corrosive) {
        super(world, entity);
        this.corrosive = corrosive;
        this.dmg = dmg;
        setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), scatter);
    }

    @Override
    public boolean shouldRenderInPass(final int pass) {
        return pass == 1;
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    protected float func_70182_d() {
        return 1.0F;
    }

    @Override
    public void handleHealthUpdate(final byte b) {
        if (b == 16) {
            if (worldObj.isRemote) {
                final float fx = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F;
                final float fy = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F;
                final float fz = (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.3F;
                Thaumcraft.proxy.wispFX3(
                        worldObj,
                        posX + fx,
                        posY + fy,
                        posZ + fz,
                        posX + fx * 8.0F,
                        posY + fy * 8.0F,
                        posZ + fz * 8.0F,
                        0.3F,
                        5,
                        true,
                        0.02F);
            }
        } else {
            super.handleHealthUpdate(b);
        }
    }

    @Override
    public void onUpdate() {
        if (ticksExisted > 20) {
            setDead();
        }

        motionX *= 0.95D;
        motionY *= 0.95D;
        motionZ *= 0.95D;

        if (onGround) {
            motionX *= 0.66D;
            motionY *= 0.66D;
            motionZ *= 0.66D;
        }
        super.onUpdate();
    }

    public void writeSpawnData(final ByteBuf buf) {
        buf.writeFloat(dmg);
    }

    public void readSpawnData(final ByteBuf buf) {
        dmg = buf.readFloat();
    }

    @Override
    protected void onImpact(final MovingObjectPosition mop) {
        if (!worldObj.isRemote && getThrower() != null) {
            final List<Entity> entities =
                    worldObj.getEntitiesWithinAABBExcludingEntity(getThrower(), boundingBox.expand(1.0D, 1.0D, 1.0D));

            for (final Entity e : entities) {
                if (e instanceof EntityLivingBase) {
                    final EntityLivingBase entity = (EntityLivingBase) e;
                    if (mop.entityHit != null) {
                        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()), dmg);
                        if (corrosive) {
                            entity.addPotionEffect(new PotionEffect(Potion.wither.id, 40, 1));
                        }
                        entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 40, 1));
                    }
                }
            }
            worldObj.setEntityState(this, (byte) 16);
        }
        setDead();
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.1F;
    }

    @Override
    public void writeEntityToNBT(final NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setFloat(TAG_DAMAGE, dmg);
    }

    @Override
    public void readEntityFromNBT(final NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        dmg = tag.getInteger(TAG_DAMAGE);
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(final DamageSource dmg, final float f) {
        return false;
    }
}
