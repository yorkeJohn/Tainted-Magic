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

public class ItemFocusTaintSwarm extends ItemFocusBasic
{
    IIcon depthIcon = null;
    IIcon ornIcon = null;

    private static final AspectList COST = new AspectList().add(Aspect.EARTH, 50).add(Aspect.WATER, 50);
    private static final AspectList COST_ANTIBODY = COST.copy().add(Aspect.ORDER, 10);

    public ItemFocusTaintSwarm ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemFocusTaintSwarm");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm");
        this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm_depth");
        this.ornIcon = ir.registerIcon("thaumcraft:focus_whatever_orn");
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
        return "TAINT" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0x9929BD;
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return isUpgradedWith(stack, TMFocusUpgrades.antibody) ? COST_ANTIBODY : COST;
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return 3000;
    }

    public boolean isVisCostPerTick (ItemStack stack)
    {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        Entity target = EntityUtils.getPointedEntity(world, player, 0.0D, 32.0D, 1.1F);

        if (target != null && target instanceof EntityLivingBase)
        {
            if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
            {
                EntityTaintSwarm swarm = new EntityTaintSwarm(world);

                Vec3 look = player.getLookVec();
                swarm.setLocationAndAngles(player.posX + look.xCoord / 2.0D,
                        player.posY + player.getEyeHeight() + look.yCoord / 2.0D, player.posZ + look.zCoord / 2.0D,
                        player.rotationYaw, player.rotationPitch);

                swarm.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D + wand.getFocusPotency(stack));
                swarm.setTarget(target);
                swarm.setIsSummoned(true);

                if (!world.isRemote) world.spawnEntityInWorld(swarm);

                if (!isUpgradedWith(wand.getFocusItem(stack), TMFocusUpgrades.antibody) && player.worldObj.rand.nextInt(3) == 0)
                {
                    try
                    {
                        player.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 40, 2));
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
            player.swingItem();
        }
        return stack;
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
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, TMFocusUpgrades.antibody };
        case 4 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        case 5 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
        }
        return null;
    }
}
