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
	public static FocusUpgradeType tainturgy = new FocusUpgradeType(55, new ResourceLocation("taintedmagic", "textures/foci/IconTainturgy.png"), "focus.upgrade.tainturgy.name", "focus.upgrade.tainturgy.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));
	public static FocusUpgradeType corrosive = new FocusUpgradeType(56, new ResourceLocation("taintedmagic", "textures/foci/IconCorrosive.png"), "focus.upgrade.corrosive.name", "focus.upgrade.corrosive.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));
	IIcon depthIcon = null;

	private static final AspectList costBase = new AspectList().add(Aspect.EARTH, 10).add(Aspect.WATER, 10);
	private static final AspectList costTainturgist = new AspectList().add(Aspect.EARTH, 10).add(Aspect.WATER, 10).add(Aspect.ORDER, 10);

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
		return isUpgradedWith(s, tainturgy) ? costTainturgist : costBase;
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
		WandManager.setCooldown(p, -1);
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
		int range = 8;
		Vec3 vec3d = p.getLook(range);
		if (!p.worldObj.isRemote && p.ticksExisted % 5 == 0) p.worldObj.playSoundAtEntity(p, "thaumcraft:bubble", 0.33F, 5.0F * (float) Math.random());

		int potency = wand.getFocusPotency(s);
		if ( (!p.worldObj.isRemote) && (wand.consumeAllVis(s, p, getVisCost(s), true, false)))
		{
			float scatter = isUpgradedWith(wand.getFocusItem(s), FocusUpgradeType.enlarge) ? 15.0F : 8.0F;
			for (int a = 0; a < 2 + wand.getFocusPotency(s); a++)
			{
				EntityTaintBubble orb = new EntityTaintBubble(p.worldObj, p, scatter, isUpgradedWith(wand.getFocusItem(s), this.corrosive));
				orb.posX += orb.motionX;
				orb.posY += orb.motionY;
				orb.posZ += orb.motionZ;
				orb.damage = 5F + potency;
				p.worldObj.spawnEntityInWorld(orb);
				if (!isUpgradedWith(wand.getFocusItem(s), tainturgy))
				{
					Random rand = new Random();
					int randomInt = rand.nextInt(150);
					if (randomInt == 9)
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
					tainturgy };
		case 4 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					FocusUpgradeType.enlarge };
		case 5 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					corrosive };
		}
		return null;
	}
}
