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
public class ItemFocusVisShard extends ItemFocusBasic {

    private static final AspectList COST =
            new AspectList().add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 10).add(Aspect.AIR, 10);
    private static final AspectList COST_PERSISTENT = COST.copy().add(Aspect.WATER, 10);

    public ItemFocusVisShard() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFocusVisShard");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemFocusVisShard");
    }

    @Override
    public String getSortingHelper(final ItemStack stack) {
        return "SHARD" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(final ItemStack stack) {
        return 0x9929BD;
    }

    @Override
    public int getActivationCooldown(final ItemStack stack) {
        return 300;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(final ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(
            final ItemStack stack, final World world, final EntityPlayer player, final MovingObjectPosition mop) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
        final Entity look = EntityUtils.getPointedEntity(player.worldObj, player, 0.0D, 32.0D, 1.1F);

        if (look != null && look instanceof EntityLivingBase) {
            if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
                final EntityHomingShard shard = new EntityHomingShard(
                        world,
                        player,
                        (EntityLivingBase) look,
                        wand.getFocusPotency(stack),
                        isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.persistent));
                if (!world.isRemote) {
                    world.spawnEntityInWorld(shard);
                }

                world.playSoundAtEntity(shard, "taintedmagic:shard", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F);

                if (world.isRemote) {
                    spawnParticles(world, shard.posX, shard.posY, shard.posZ);
                }
            }
            player.swingItem();
        }
        return stack;
    }

    @SideOnly(Side.CLIENT)
    void spawnParticles(final World world, final double x, final double y, final double z) {
        for (int a = 0; a < 18; a++) {
            final FXSparkle fx = new FXSparkle(
                    world,
                    x + world.rand.nextFloat(),
                    y + world.rand.nextFloat(),
                    z + world.rand.nextFloat(),
                    1.75F,
                    0,
                    3 + world.rand.nextInt(3));
            fx.setGravity(0.1F);
            ParticleEngine.instance.addEffect(world, fx);
        }
    }

    @Override
    public AspectList getVisCost(final ItemStack stack) {
        return isUpgradedWith(stack, TMFocusUpgrades.persistent) ? COST_PERSISTENT : COST;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack stack, final int rank) {
        switch (rank) {
            case 1:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 2:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 3:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 4:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 5:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.persistent
                };
        }
        return null;
    }
}
