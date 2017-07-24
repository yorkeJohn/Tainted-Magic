package taintedmagic.common.items.wand.foci;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
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
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.items.wands.ItemWandCasting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusTaintedShockwave extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.ENTROPY, 1000).add(Aspect.EARTH, 1000).add(Aspect.WATER, 1000).add(Aspect.ORDER, 500);

	public static IIcon depthIcon;
	public static IIcon ornIcon;

	public ItemFocusTaintedShockwave ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusTaintedShockwave");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusTaintedShockwave");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusTaint_depth");
		this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusTaintedShockwave_orn");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public IIcon getOrnament (ItemStack s)
	{
		return this.ornIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "SHOCKWAVE" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 13107455;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 15000;
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
		l.add(EnumChatFormatting.BLUE + "+" + new String(this.isUpgradedWith(s, FocusUpgradeType.enlarge) ? Integer.toString(15 + this.getUpgradeLevel(s, FocusUpgradeType.enlarge)) : "15") + " " + StatCollector.translateToLocal("text.radius"));
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		int pot = wand.getFocusPotency(s);
		if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
		{
			List<EntityLivingBase> ents = w.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(p.posX, p.posY, p.posZ, p.posX + 1, p.posY + 1, p.posZ + 1).expand(15.0D + this.getUpgradeLevel(s, FocusUpgradeType.enlarge), 15.0D + this.getUpgradeLevel(s, FocusUpgradeType.enlarge), 15.0D + this.getUpgradeLevel(s, FocusUpgradeType.enlarge)));
			if (ents != null && ents.size() > 0)
			{
				for (int a = 0; a < ents.size(); a++)
				{
					EntityLivingBase e = ents.get(a);

					if (e != p && e.isEntityAlive() && !e.isEntityInvulnerable())
					{
						double dist = TaintedMagicHelper.getDistanceTo(e.posX, e.posY, e.posZ, p);
						if (dist < 7.0D) e.attackEntityFrom(DamageSource.magic, 2.0F);
						Vector3 movement = TaintedMagicHelper.getDistanceBetween(e, p);
						e.addVelocity(movement.x * 3, 0.8, movement.z * 3);
					}
				}
			}
			w.playSoundAtEntity(p, "taintedmagic:shockwave", 5.0F, 1.5F * (float) Math.random());
			if (w.isRemote) spawnParticles(w, p);
			return s;
		}
		return null;
	}

	@SideOnly (Side.CLIENT)
	public void spawnParticles (World w, EntityPlayer p)
	{
		for (int i = 1; i < 360; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				double r = 4.0D;
				double xp = (Math.cos(i * Math.PI / 180.0D)) * r;
				double zp = (Math.sin(i * Math.PI / 180.0D)) * r;

				float red = 0.75F + w.rand.nextFloat() * 0.25F;
				float green = w.rand.nextFloat() * 0.5F;
				float blue = 0.75F + w.rand.nextFloat() * 0.25F;

				double off = j * 0.25F;

				FXWisp ef = new FXWisp(w, p.posX + xp + off, p.posY - 1, p.posZ + zp + off, 0.5F + (float) Math.random() * 0.25F, red, green, blue);
				ef.setGravity(0.0F);
				ef.shrink = true;
				ef.noClip = true;

				ef.addVelocity(xp * 0.3D, 0, zp * 0.3D);

				ParticleEngine.instance.addEffect(w, ef);
			}
		}
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.enlarge };
		}
		return null;
	}
}
