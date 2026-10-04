package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityTaintSwarm;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

public class ItemFocusTaintSwarm extends ItemFocusBasic {

    private IIcon depthIcon;
    private IIcon ornIcon;

    private static final AspectList COST =
            new AspectList().add(Aspect.EARTH, 50).add(Aspect.WATER, 50);
    private static final AspectList COST_ANTIBODY = COST.copy().add(Aspect.ORDER, 10);

    public ItemFocusTaintSwarm() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFocusTaintSwarm");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm");
        depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm_depth");
        ornIcon = ir.registerIcon("thaumcraft:focus_whatever_orn");
    }

    @Override
    public IIcon getFocusDepthLayerIcon(final ItemStack stack) {
        return depthIcon;
    }

    @Override
    public IIcon getOrnament(final ItemStack stack) {
        return ornIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(final int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int meta, final int pass) {
        return pass == 0 ? ornIcon : icon;
    }

    @Override
    public String getSortingHelper(final ItemStack stack) {
        return "TAINT" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(final ItemStack stack) {
        return 0x9929BD;
    }

    @Override
    public AspectList getVisCost(final ItemStack stack) {
        return isUpgradedWith(stack, TMFocusUpgrades.antibody) ? COST_ANTIBODY : COST;
    }

    @Override
    public int getActivationCooldown(final ItemStack stack) {
        return 3000;
    }

    @Override
    public boolean isVisCostPerTick(final ItemStack stack) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(final ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(
            final ItemStack stack, final World world, final EntityPlayer player, final MovingObjectPosition mop) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        final Entity target = EntityUtils.getPointedEntity(world, player, 0.0D, 32.0D, 1.1F);

        if (target != null && target instanceof EntityLivingBase) {
            if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
                final EntityTaintSwarm swarm = new EntityTaintSwarm(world);

                final Vec3 look = player.getLookVec();
                swarm.setLocationAndAngles(
                        player.posX + look.xCoord / 2.0D,
                        player.posY + player.getEyeHeight() + look.yCoord / 2.0D,
                        player.posZ + look.zCoord / 2.0D,
                        player.rotationYaw,
                        player.rotationPitch);

                swarm.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                        .setBaseValue(5.0D + wand.getFocusPotency(stack));
                swarm.setTarget(target);
                swarm.setIsSummoned(true);

                if (!world.isRemote) {
                    world.spawnEntityInWorld(swarm);
                }

                if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.antibody)
                        && player.worldObj.rand.nextInt(3) == 0) {
                    player.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 40, 2));
                }
            }
            player.swingItem();
        }
        return stack;
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack stack, final int rank) {
        switch (rank) {
            case 1:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 2:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 3:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.antibody
                };
            case 4:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 5:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
        }
        return null;
    }
}
