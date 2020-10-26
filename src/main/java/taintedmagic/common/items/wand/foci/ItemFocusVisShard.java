package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityHomingShard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

/**
 * This class is based off of ItemFocusShard.class created by <Azanor> as part of Thaumcraft 5.
 */
public class ItemFocusVisShard extends ItemFocusBasic
{
    private static final AspectList COST = new AspectList().add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.AIR, 10);
    private static final AspectList COST_PERSISTENT = COST.copy().add(Aspect.WATER, 10);

    public ItemFocusVisShard ()
    {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemFocusVisShard");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusVisShard");
    }

    public String getSortingHelper (ItemStack stack)
    {
        return "SHARD" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0x9929BD;
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return 300;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        Entity look = EntityUtils.getPointedEntity(player.worldObj, player, 0.0D, 32.0D, 1.1F);

        if (look != null && look instanceof EntityLivingBase)
        {
            if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
            {
                EntityHomingShard shard = new EntityHomingShard(world, player, (EntityLivingBase) look,
                        wand.getFocusPotency(stack), isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.persistent));
                if (!world.isRemote) world.spawnEntityInWorld(shard);

                world.playSoundAtEntity(shard, "taintedmagic:shard", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F);

                if (world.isRemote) spawnParticles(world, shard.posX, shard.posY, shard.posZ);
            }
            player.swingItem();
        }
        return stack;
    }

    @SideOnly (Side.CLIENT)
    void spawnParticles (World world, double x, double y, double z)
    {
        for (int a = 0; a < 18; a++)
        {
            FXSparkle fx = new FXSparkle(world, x + world.rand.nextFloat(), y + world.rand.nextFloat(),
                    z + world.rand.nextFloat(), 1.75F, 0, 3 + world.rand.nextInt(3));
            fx.setGravity(0.1F);
            ParticleEngine.instance.addEffect(world, fx);
        }
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.persistent) ? COST_PERSISTENT : COST;
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack stack, int rank)
    {
        switch (rank)
        {
        case 1 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 2 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 3 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 4 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 5 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.persistent };
        }
        return null;
    }
}
