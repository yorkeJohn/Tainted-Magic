package taintedmagic.common.items.wand.foci;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusShockwave extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.AIR, 500).add(Aspect.ENTROPY, 300);

	public static IIcon depthIcon;

	public ItemFocusShockwave ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusShockwave");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusShockwave");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusShockwave_depth");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "SHOCKWAVE" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0xB0B7C4;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 10000;
	}

	public boolean isVisCostPerTick (ItemStack s)
	{
		return false;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.WAVE;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);
		l.add(" ");
		l.add(EnumChatFormatting.BLUE + "+" + new String(this.isUpgradedWith(s, FocusUpgradeType.enlarge) ? Integer.toString(15 + this.getUpgradeLevel(s, FocusUpgradeType.enlarge)) : "15") + " "
				+ StatCollector.translateToLocal("text.radius"));
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		int pot = wand.getFocusPotency(s);
		if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
		{
			List<EntityLivingBase> ents = w.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(p.posX, p.posY, p.posZ, p.posX + 1, p.posY + 1, p.posZ + 1).expand(15.0D
					+ this.getUpgradeLevel(s, FocusUpgradeType.enlarge), 15.0D + this.getUpgradeLevel(s, FocusUpgradeType.enlarge), 15.0D + this.getUpgradeLevel(s, FocusUpgradeType.enlarge)));
			if (ents != null && ents.size() > 0)
			{
				for (int a = 0; a < ents.size(); a++)
				{
					EntityLivingBase e = ents.get(a);

					if (e != p && e.isEntityAlive() && !e.isEntityInvulnerable())
					{
						double dist = TaintedMagicHelper.getDistanceTo(e.posX, e.posY, e.posZ, p);
						if (dist < 7.0D) e.attackEntityFrom(DamageSource.magic, 2.0F + this.getUpgradeLevel(s, FocusUpgradeType.potency));
						Vector3 movement = TaintedMagicHelper.getVectorBetweenEntities(e, p);
						e.addVelocity(movement.x * (5.0D + this.getUpgradeLevel(s, FocusUpgradeType.potency)), 1.5D + (this.getUpgradeLevel(s, FocusUpgradeType.potency) * 0.1D), movement.z
								* (5.0D + this.getUpgradeLevel(s, FocusUpgradeType.potency)));
						if (w.isRemote) spawnParticles(w, p, e);
					}
				}
			}
			w.playSoundAtEntity(p, "taintedmagic:shockwave", 5.0F, 1.5F * (float) Math.random());
			return s;
		}
		return null;
	}

	@SideOnly (Side.CLIENT)
	public static void spawnParticles (World w, EntityPlayer p, Entity e)
	{
		FXLightningBolt bolt = new FXLightningBolt(w, p, e, w.rand.nextLong(), 4);

		bolt.defaultFractal();
		bolt.setType(2);
		bolt.setWidth(0.125F);
		bolt.finalizeBolt();
		for (int a = 0; a < 5; a++)
		{
			Thaumcraft.proxy.sparkle((float) e.posX + (p.worldObj.rand.nextFloat() - p.worldObj.rand.nextFloat()) * 0.6F, (float) e.posY + (p.worldObj.rand.nextFloat() - p.worldObj.rand.nextFloat())
					* 0.6F, (float) e.posZ + (p.worldObj.rand.nextFloat() - p.worldObj.rand.nextFloat()) * 0.6F, 2.0F + p.worldObj.rand.nextFloat(), 2, 0.05F + p.worldObj.rand.nextFloat() * 0.05F);
		}
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge, FocusUpgradeType.potency };
		}
		return null;
	}
}
