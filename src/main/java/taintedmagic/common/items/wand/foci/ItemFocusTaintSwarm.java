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

	private static final AspectList cost = new AspectList().add(Aspect.EARTH, 500).add(Aspect.WATER, 500);
	private static final AspectList costAntibody = new AspectList().add(Aspect.EARTH, 500).add(Aspect.WATER, 500).add(Aspect.ORDER, 200);

	public ItemFocusTaintSwarm ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusTaintSwarm");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaintSwarm_depth");
		this.ornIcon = ir.registerIcon("thaumcraft:focus_whatever_orn");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public IIcon getOrnament (ItemStack s)
	{
		return this.ornIcon;
	}

	@SideOnly (Side.CLIENT)
	public boolean requiresMultipleRenderPasses ()
	{
		return true;
	}

	@SideOnly (Side.CLIENT)
	public int getRenderPasses (int m)
	{
		return 2;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass (int meta, int pass)
	{
		return (pass == 0) ? this.ornIcon : this.icon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "TAINT" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0x9929BD;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, FocusUpgrades.antibody) ? costAntibody : cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 3000;
	}

	public boolean isVisCostPerTick (ItemStack s)
	{
		return false;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.WAVE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		Entity look = EntityUtils.getPointedEntity(w, p, 0.0D, 32.0D, 1.1F);

		if (look != null && look instanceof EntityLivingBase)
		{
			if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
			{
				EntityTaintSwarm e = new EntityTaintSwarm(w);
				Vec3 v = p.getLookVec();
				e.setLocationAndAngles(p.posX + v.xCoord / 2.0D, p.posY + p.getEyeHeight() + v.yCoord / 2.0D, p.posZ + v.zCoord / 2.0D, p.rotationYaw, p.rotationPitch);

				e.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D + wand.getFocusPotency(s));
				e.setTarget(look);
				e.setIsSummoned(true);

				if (!w.isRemote) w.spawnEntityInWorld(e);

				if (!isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.antibody))
				{
					if (p.worldObj.rand.nextInt(3) == 0)
					{
						try
						{
							p.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 40, 2));
						}
						catch (Exception err)
						{
							err.printStackTrace();
						}
					}
				}
			}
			p.swingItem();
		}
		return s;
	}

	public void onPlayerStoppedUsingFocus (ItemStack s, World w, EntityPlayer p, int count)
	{
	}

	public void onUsingFocusTick (ItemStack s, EntityPlayer p, int count)
	{
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgrades.antibody };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		}
		return null;
	}
}
