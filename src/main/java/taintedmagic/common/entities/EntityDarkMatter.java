package taintedmagic.common.entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;

public class EntityDarkMatter extends EntityEldritchOrb
{
    private float dmg = 0.0F;
    private int enlarge;
    private boolean corrosive;

    public EntityDarkMatter (World world)
    {
        super(world);
    }

    public EntityDarkMatter (World world, EntityLivingBase entity, float dmg, int enlarge, boolean corrosive)
    {
        super(world, entity);
        this.dmg = dmg;
        this.enlarge = enlarge;
        this.corrosive = corrosive;
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
            double expand = 1.0D + (double) enlarge * 0.5D;
            System.out.println(expand);
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(getThrower(),
                    this.boundingBox.expand(expand, expand, expand));

            for (Entity entity : list)
            {
                if (entity instanceof EntityLivingBase)
                {
                    ((EntityLivingBase) entity).attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()),
                            this.dmg);
                    try
                    {
                        if (this.corrosive)
                            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.wither.id, 160, 1));
                        ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.weakness.id, 160, 1));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            this.worldObj.playSoundAtEntity(this, "random.fizz", 0.5F,
                    2.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.8F);
            this.ticksExisted = 100;
            this.worldObj.setEntityState(this, (byte) 16);
        }
        setDead();
    }
}