package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityDarkMatter;
import taintedmagic.common.entities.EntityDiffusion;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusDarkMatter extends ItemFocusBasic {

    private IIcon depthIcon;
    private IIcon ornIcon;
    private long soundDelay = 0L;

    private static final AspectList COST =
            new AspectList().add(Aspect.ENTROPY, 150).add(Aspect.FIRE, 100);
    private static final AspectList COST_SANITY = COST.copy().add(Aspect.ORDER, 50);
    private static final AspectList COST_CORROSIVE = COST.copy().add(Aspect.WATER, 50);

    public ItemFocusDarkMatter() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFocusDarkMatter");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemFocusDarkMatter");
        depthIcon = ir.registerIcon("taintedmagic:ItemFocusDarkMatter_depth");
        ornIcon = ir.registerIcon("thaumcraft:focus_warding_orn");
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
        return "ELDRITCH" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(final ItemStack stack) {
        return 0x000018;
    }

    @Override
    public AspectList getVisCost(final ItemStack stack) {
        AspectList cost = isUpgradedWith(stack, TMFocusUpgrades.sanity)
                ? COST_SANITY
                : isUpgradedWith(stack, TMFocusUpgrades.corrosive) ? COST_CORROSIVE : COST;

        if (isUpgradedWith(stack, TMFocusUpgrades.diffusion)) {
            cost = cost.copy();
            for (final Aspect aspect : cost.getAspects()) {
                cost.reduce(aspect, (int) (cost.getAmount(aspect) * 0.9));
            }
        }
        return cost;
    }

    @Override
    public int getActivationCooldown(final ItemStack stack) {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion) ? -1 : 1000;
    }

    @Override
    public boolean isVisCostPerTick(final ItemStack stack) {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion);
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(final ItemStack stack) {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion)
                ? ItemFocusBasic.WandFocusAnimation.CHARGE
                : ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(
            final ItemStack stack, final World world, final EntityPlayer player, final MovingObjectPosition mop) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.diffusion)) {
            player.setItemInUse(stack, 2147483647);
        } else {
            if (!world.isRemote
                    && wand.consumeAllVis(stack, player, getVisCost(wand.getFocusItem(stack)), true, false)) {
                final EntityDarkMatter proj = new EntityDarkMatter(
                        world,
                        player,
                        16F + wand.getFocusPotency(stack),
                        wand.getFocusEnlarge(stack),
                        isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.corrosive));
                world.spawnEntityInWorld(proj);

                if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.sanity) && world.rand.nextInt(20) == 0) {
                    Thaumcraft.addStickyWarpToPlayer(player, 1);
                }

                world.playSoundAtEntity(player, "thaumcraft:egattack", 0.4F, 1.0F + world.rand.nextFloat() * 0.1F);
            }
            player.swingItem();
        }
        return stack;
    }

    @Override
    public void onUsingFocusTick(final ItemStack stack, final EntityPlayer player, final int i) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (!wand.consumeAllVis(stack, player, getVisCost(wand.getFocusItem(stack)), false, false)) {
            player.stopUsingItem();
            return;
        }

        if (!player.worldObj.isRemote && soundDelay < System.currentTimeMillis()) {
            player.worldObj.playSoundAtEntity(
                    player,
                    "thaumcraft:wind",
                    0.3F + 0.2F * (float) Math.random(),
                    0.75F + 0.25F * (float) Math.random());
            soundDelay = System.currentTimeMillis() + 750L;
        }

        if (!player.worldObj.isRemote
                && wand.consumeAllVis(stack, player, getVisCost(wand.getFocusItem(stack)), true, false)) {
            for (int a = 0; a < 2 + wand.getFocusPotency(stack); a++) {
                final EntityDiffusion proj = new EntityDiffusion(
                        player.worldObj,
                        player,
                        isUpgradedWith(wand.getFocusItem(stack), FocusUpgradeType.enlarge)
                                ? 12.0F + wand.getFocusEnlarge(stack)
                                : 9.0F,
                        12F + wand.getFocusPotency(stack),
                        isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.corrosive));

                proj.posX += proj.motionX;
                proj.posY += proj.motionY;
                proj.posZ += proj.motionZ;

                player.worldObj.spawnEntityInWorld(proj);
            }

            if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.sanity)
                    && player.worldObj.rand.nextInt(1000) == 0) {
                Thaumcraft.addStickyWarpToPlayer(player, 1);
            }
        }
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack stack, final int rank) {
        switch (rank) {
            case 1:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge
                };
            case 2:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge
                };
            case 3:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal,
                    FocusUpgradeType.potency,
                    FocusUpgradeType.enlarge,
                    TMFocusUpgrades.corrosive,
                    TMFocusUpgrades.sanity
                };
            case 4:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgradeType.enlarge
                };
            case 5:
                return new FocusUpgradeType[] {
                    FocusUpgradeType.frugal,
                    FocusUpgradeType.potency,
                    FocusUpgradeType.enlarge,
                    TMFocusUpgrades.diffusion
                };
        }
        return null;
    }

    @Override
    public boolean canApplyUpgrade(
            final ItemStack stack, final EntityPlayer player, final FocusUpgradeType type, final int rank) {
        return !type.equals(TMFocusUpgrades.diffusion)
                || ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "DIFFUSIONUPGRADE");
    }
}
