package taintedmagic.common.items.wand.foci;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityTaintBubble;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.projectile.EntityEmber;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;

public class ItemFocusTaint extends ItemFocusBasic
{
	IIcon depthIcon = null;

	private static final AspectList cost = new AspectList().add(Aspect.EARTH, 10).add(Aspect.WATER, 10);
	private static final AspectList costAntibody = new AspectList().add(Aspect.EARTH, 10).add(Aspect.WATER, 10).add(Aspect.ORDER, 10);

	public ItemFocusTaint ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusTaint");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusTaint");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaint_depth");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "TAINT" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 8073200;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, FocusUpgrades.antibody) ? costAntibody : cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return -1;
	}

	public boolean isVisCostPerTick (ItemStack s)
	{
		return true;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.CHARGE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();
		p.setItemInUse(s, 2147483647);
		return s;
	}

	public void onUsingFocusTick (ItemStack s, EntityPlayer p, int count)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();
		if (!wand.consumeAllVis(s, p, getVisCost(s), false, false))
		{
			p.stopUsingItem();
			return;
		}
		if (!p.worldObj.isRemote && p.ticksExisted % 5 == 0) p.worldObj.playSoundAtEntity(p, "thaumcraft:bubble", 0.33F, 5.0F * (float) Math.random());

		if (!p.worldObj.isRemote && wand.consumeAllVis(s, p, getVisCost(s), true, false))
		{
			for (int a = 0; a < 2 + wand.getFocusPotency(s); a++)
			{
				EntityTaintBubble proj = new EntityTaintBubble(p.worldObj, p, isUpgradedWith(wand.getFocusItem(s), FocusUpgradeType.enlarge) ? 15.0F : 8.0F, 4F + wand.getFocusPotency(s), isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.corrosive));
				proj.posX += proj.motionX;
				proj.posY += proj.motionY;
				proj.posZ += proj.motionZ;
				p.worldObj.spawnEntityInWorld(proj);
				if (!isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.antibody))
				{
					if (p.worldObj.rand.nextInt(150) == 0)
					{
						try
						{
							p.addPotionEffect(new PotionEffect(Config.potionVisExhaustID, 1000, 2));
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					FocusUpgrades.antibody };
		case 4 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					FocusUpgradeType.enlarge };
		case 5 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					FocusUpgrades.corrosive };
		}
		return null;
	}
}
