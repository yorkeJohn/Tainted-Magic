package taintedmagic.common.items.wand.foci;

import java.awt.Color;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.items.wands.ItemWandCasting;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusTime extends ItemFocusBasic
{
	public IIcon depthIcon;
	public IIcon iconOverlay;

	private static final AspectList cost = new AspectList().add(Aspect.AIR, 1000).add(Aspect.WATER, 1000).add(Aspect.FIRE, 1000).add(Aspect.EARTH, 1000).add(Aspect.ORDER, 1000).add(Aspect.ENTROPY, 1000);

	public ItemFocusTime ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusTime");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusTime");
		this.iconOverlay = ir.registerIcon("taintedmagic:ItemFocusTime_overlay");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMeteorology_depth");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "TIME" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		EntityPlayer player = TaintedMagic.proxy.getClientPlayer();
		return player == null ? 0xFFFFFF : Color.HSBtoRGB(player.ticksExisted * 16 % 360 / 360F, 1F, 1F);
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
		return (pass == 0) ? this.icon : this.iconOverlay;
	}

	@SideOnly (Side.CLIENT)
	public int getColorFromItemStack (ItemStack s, int pass)
	{
		if (pass == 0)
		{
			EntityPlayer p = TaintedMagic.proxy.getClientPlayer();
			return p == null ? 0xFFFFFF : Color.HSBtoRGB(p.ticksExisted * 16F % 360 / 360F, 1F, 1F);
		}
		if (pass == 1) return 16777215;

		return 16777215;
	}

	@SideOnly (Side.CLIENT)
	public boolean requiresMultipleRenderPasses ()
	{
		return true;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return new AspectList().add(Aspect.AIR, 1000).add(Aspect.WATER, 1000).add(Aspect.FIRE, 1000).add(Aspect.EARTH, 1000).add(Aspect.ORDER, 1000).add(Aspect.ENTROPY, 1000);
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 30000;
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

		if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
		{
			w.setWorldTime(w.isDaytime() ? 14000 : 24000);
			p.playSound("thaumcraft:wand", 0.5F, 1.0F);

			spawnParticles(w, p);
		}
		return s;
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return TaintedMagic.rarityCreation;
	}

	@SideOnly (Side.CLIENT)
	void spawnParticles (World w, EntityPlayer p)
	{
		if (w.isRemote)
		{
			for (int i = 1; i < 200; i++)
			{
				double xp = (-Math.random() * 2.0F) + (Math.random() * 2.0F);
				double zp = (-Math.random() * 2.0F) + (Math.random() * 2.0F);
				double yp = (-Math.random() * 2.0F) + (Math.random() * 2.0F);
				double off = Math.random() * 0.1;

				float red = p.worldObj.rand.nextFloat();
				float green = p.worldObj.rand.nextFloat();
				float blue = p.worldObj.rand.nextFloat();

				FXWisp ef = new FXWisp(w, p.posX + xp + off, p.posY + 10 + yp + off, p.posZ + zp + off, 0.5F + ((float) Math.random() * 0.25F), red, green, blue);
				ef.setGravity(0.75F);
				ef.shrink = true;
				ef.noClip = true;

				Vector3 movement = TaintedMagicHelper.getDistanceBetween(ef, p);
				ef.addVelocity(movement.x * 0.5, movement.y * 0.5, movement.z * 0.5);

				ParticleEngine.instance.addEffect(w, ef);
			}
		}
	}
}
