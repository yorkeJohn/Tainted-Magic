package taintedmagic.common.items.wand.foci;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityEldritchOrbAttack;
import taintedmagic.common.entities.EntityTaintBubble;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.monster.EntityEldritchGuardian;
import thaumcraft.common.entities.projectile.EntityEldritchOrb;
import thaumcraft.common.entities.projectile.EntityEmber;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.items.wands.WandManager;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

public class ItemFocusEldritch extends ItemFocusBasic
{
	public static FocusUpgradeType sanity = new FocusUpgradeType(57, new ResourceLocation("taintedmagic", "textures/foci/IconSanity.png"), "focus.upgrade.sanity.name", "focus.upgrade.sanity.text", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));
	IIcon depthIcon = null;

	private static final AspectList costBase = new AspectList().add(Aspect.ENTROPY, 20).add(Aspect.FIRE, 20);
	private static final AspectList costSane = new AspectList().add(Aspect.ENTROPY, 20).add(Aspect.FIRE, 50).add(Aspect.ORDER, 20);
	private static final AspectList costCorrosive = new AspectList().add(Aspect.ENTROPY, 50).add(Aspect.FIRE, 20).add(Aspect.WATER, 20);

	long soundDelay = 0L;

	public ItemFocusEldritch ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusEldritch");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusEldritch");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusEldritch_depth");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "ELDRITCH" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0x000018;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, ItemFocusTaint.corrosive) ? costCorrosive : isUpgradedWith(s, sanity) ? costSane : costBase;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 600;
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

		if (!w.isRemote)
		{
			int potency = wand.getFocusPotency(s);
			if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
			{
				EntityEldritchOrbAttack orb = null;
				orb = new EntityEldritchOrbAttack(w, p, isUpgradedWith(wand.getFocusItem(s), ItemFocusTaint.corrosive));
				orb.dmg = (10F + potency);
				w.spawnEntityInWorld(orb);
				if (!isUpgradedWith(wand.getFocusItem(s), sanity))
				{
					Random rand = new Random();
					int randomInt = rand.nextInt(10);
					if (randomInt == 5)
					{
						Thaumcraft.addStickyWarpToPlayer(p, 1);
					}
				}
			}
			w.playSoundAtEntity(p, "thaumcraft:egattack", 0.4F, 1.0F + w.rand.nextFloat() * 0.1F);
		}
		p.swingItem();
		return s;
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
					ItemFocusTaint.corrosive };
		case 4 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					this.sanity };
		}
		return null;
	}
}
