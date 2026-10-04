package taintedmagic.common.entities;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;

public class EntityDarkMatter extends EntityEldritchOrb {

    private float dmg = 0.0F;
    private int enlarge;
    private boolean corrosive;

    public EntityDarkMatter(final World world) {
        super(world);
    }

    public EntityDarkMatter(
            final World world,
            final EntityLivingBase entity,
            final float dmg,
            final int enlarge,
            final boolean corrosive) {
        super(world, entity);
        this.dmg = dmg;
        this.enlarge = enlarge;
        this.corrosive = corrosive;
    }

    @Override
    public boolean shouldRenderInPass(final int pass) {
        return pass == 1;
    }

    @Override
    protected void onImpact(final MovingObjectPosition mop) {
        if (!worldObj.isRemote && getThrower() != null) {
            final double expand = 1.5D + enlarge * 0.5D;
            final List<Entity> entities = worldObj.getEntitiesWithinAABBExcludingEntity(
                    getThrower(), boundingBox.expand(expand, expand, expand));

            for (final Entity e : entities) {
                if (e instanceof EntityLivingBase) {
                    final EntityLivingBase entity = (EntityLivingBase) e;
                    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()), dmg);
                    if (corrosive) {
                        entity.addPotionEffect(new PotionEffect(Potion.wither.id, 160, 1));
                    }
                    entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 1));
                }
            }
            worldObj.playSoundAtEntity(this, "random.fizz", 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
            ticksExisted = 100;
            worldObj.setEntityState(this, (byte) 16);
        }
        setDead();
    }
}
