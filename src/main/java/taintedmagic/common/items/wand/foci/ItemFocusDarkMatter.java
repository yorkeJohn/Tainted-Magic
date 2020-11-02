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

public class ItemFocusDarkMatter extends ItemFocusBasic
{
    IIcon depthIcon = null;
    IIcon ornIcon = null;

    private static final AspectList COST = new AspectList().add(Aspect.ENTROPY, 150).add(Aspect.FIRE, 100);
    private static final AspectList COST_SANITY = COST.copy().add(Aspect.ORDER, 50);
    private static final AspectList COST_CORROSIVE = COST.copy().add(Aspect.WATER, 50);

    private static final AspectList COST_DIFFUSION = new AspectList().add(Aspect.ENTROPY, 15).add(Aspect.FIRE, 10);
    private static final AspectList COST_DIFFUSION_SANITY = COST_DIFFUSION.copy().add(Aspect.ORDER, 5);
    private static final AspectList COST_DIFFUSION_CORROSIVE = COST_DIFFUSION.copy().add(Aspect.WATER, 5);

    public ItemFocusDarkMatter ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemFocusDarkMatter");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusDarkMatter");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusDarkMatter_depth");
        this.ornIcon = ir.registerIcon("thaumcraft:focus_warding_orn");
    }

    public IIcon getFocusDepthLayerIcon (ItemStack stack)
    {
        return this.depthIcon;
    }

    public IIcon getOrnament (ItemStack stack)
    {
        return this.ornIcon;
    }

    @SideOnly (Side.CLIENT)
    public boolean requiresMultipleRenderPasses ()
    {
        return true;
    }

    @SideOnly (Side.CLIENT)
    public int getRenderPasses (int meta)
    {
        return 2;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass (int meta, int pass)
    {
        return (pass == 0) ? this.ornIcon : this.icon;
    }

    public String getSortingHelper (ItemStack stack)
    {
        return "ELDRITCH" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0x000018;
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion)
                ? (isUpgradedWith(stack, TMFocusUpgrades.sanity) ? COST_DIFFUSION_SANITY
                        : isUpgradedWith(stack, TMFocusUpgrades.corrosive) ? COST_DIFFUSION_CORROSIVE : COST_DIFFUSION)
                : (isUpgradedWith(stack, TMFocusUpgrades.sanity) ? COST_SANITY
                        : isUpgradedWith(stack, TMFocusUpgrades.corrosive) ? COST_CORROSIVE : COST);
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion) ? -1 : 1000;
    }

    public boolean isVisCostPerTick (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion);
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.diffusion) ? ItemFocusBasic.WandFocusAnimation.CHARGE
                : ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.diffusion))
        {
            player.setItemInUse(stack, 2147483647);
        }
        else
        {
            if (!world.isRemote)
            {
                if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
                {
                    EntityDarkMatter proj = new EntityDarkMatter(world, player, 16F + wand.getFocusPotency(stack),
                            isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.corrosive));
                    world.spawnEntityInWorld(proj);

                    if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.sanity) && world.rand.nextInt(20) == 0)
                        Thaumcraft.addStickyWarpToPlayer(player, 1);

                    world.playSoundAtEntity(player, "thaumcraft:egattack", 0.4F, 1.0F + world.rand.nextFloat() * 0.1F);
                }
            }
            player.swingItem();
        }
        return stack;
    }

    public void onUsingFocusTick (ItemStack stack, EntityPlayer player, int i)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (!wand.consumeAllVis(stack, player, getVisCost(stack), false, false))
        {
            player.stopUsingItem();
            return;
        }
        if (!player.worldObj.isRemote && player.ticksExisted % 5 == 0)
            player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", 0.33F, 5.0F * (float) Math.random());

        if (!player.worldObj.isRemote && wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
        {
            for (int a = 0; a < 2 + wand.getFocusPotency(stack); a++)
            {
                EntityDiffusion proj = new EntityDiffusion(player.worldObj, player,
                        isUpgradedWith(wand.getFocusItem(stack), FocusUpgradeType.enlarge) ? 15.0F : 9.0F,
                        12F + wand.getFocusPotency(stack), isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.corrosive));

                proj.posX += proj.motionX;
                proj.posY += proj.motionY;
                proj.posZ += proj.motionZ;

                player.worldObj.spawnEntityInWorld(proj);

                if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.sanity) && player.worldObj.rand.nextInt(200) == 0)
                    Thaumcraft.addStickyWarpToPlayer(player, 1);
            }
        }
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
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.corrosive,
                    TMFocusUpgrades.sanity };
        case 4 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 5 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.diffusion };
        }
        return null;
    }

    public boolean canApplyUpgrade (ItemStack stack, EntityPlayer player, FocusUpgradeType type, int rank)
    {
        return (!type.equals(TMFocusUpgrades.diffusion))
                || (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "DIFFUSIONUPGRADE"));
    }
}
