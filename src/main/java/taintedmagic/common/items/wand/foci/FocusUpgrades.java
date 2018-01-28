package taintedmagic.common.items.wand.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;

public class FocusUpgrades
{
	public static FocusUpgradeType sanity;
	public static FocusUpgradeType bloodlust;
	public static FocusUpgradeType antibody;
	public static FocusUpgradeType corrosive;
	public static FocusUpgradeType persistent;
	public static FocusUpgradeType maxima;
	public static FocusUpgradeType glowpet;
	public static FocusUpgradeType diffusion;

	public static void init ()
	{
		int ID = 60;
		sanity = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconSanity.png"), "focus.upgrade.sanity.name", "focus.upgrade.sanity.text", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));

		bloodlust = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconBloodlust.png"), "focus.upgrade.bloodlust.name", "focus.upgrade.bloodlust.text", new AspectList().add(Aspect.WEAPON, 1).add(Aspect.HEAL, 1));

		antibody = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconAntibody.png"), "focus.upgrade.antibody.name", "focus.upgrade.antibody.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));

		corrosive = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconCorrosive.png"), "focus.upgrade.corrosive.name", "focus.upgrade.corrosive.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));

		persistent = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconPersistent.png"), "focus.upgrade.persistent.name", "focus.upgrade.persistent.text", new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));

		maxima = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconMaxima.png"), "focus.upgrade.maxima.name", "focus.upgrade.maxima.text", new AspectList().add(Aspect.LIGHT, 4).add(Aspect.AURA, 1).add(Aspect.ENERGY, 4));

		glowpet = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconGlowpet.png"), "focus.upgrade.glowpet.name", "focus.upgrade.glowpet.text", new AspectList().add(Aspect.MOTION, 1).add(Aspect.LIGHT, 4).add(Aspect.ENERGY, 4));

		diffusion = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"), "focus.upgrade.diffusion.name", "focus.upgrade.diffusion.text", new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ELDRITCH, 4).add(Aspect.AURA, 4));
	}
}
