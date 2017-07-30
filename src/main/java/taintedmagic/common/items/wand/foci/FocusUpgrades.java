package taintedmagic.common.items.wand.foci;

import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;

public class FocusUpgrades
{
	public static int ID = Short.MAX_VALUE;
	public static FocusUpgradeType sanity = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconSanity.png"), "focus.upgrade.sanity.name", "focus.upgrade.sanity.text", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));
	public static FocusUpgradeType bloodlust = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconBloodlust.png"), "focus.upgrade.bloodlust.name", "focus.upgrade.bloodlust.text", new AspectList().add(Aspect.WEAPON, 1).add(Aspect.HEAL, 1));
	public static FocusUpgradeType antibody = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconAntibody.png"), "focus.upgrade.antibody.name", "focus.upgrade.antibody.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));
	public static FocusUpgradeType corrosive = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconCorrosive.png"), "focus.upgrade.corrosive.name", "focus.upgrade.corrosive.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));
	public static FocusUpgradeType persistent = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconPersistent.png"), "focus.upgrade.persistent.name", "focus.upgrade.persistent.text", new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));
	public static FocusUpgradeType maxima = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconMaxima.png"), "focus.upgrade.maxima.name", "focus.upgrade.maxima.text", new AspectList().add(Aspect.LIGHT, 4).add(Aspect.AURA, 1).add(Aspect.ENERGY, 4));
	public static FocusUpgradeType glowpet = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconGlowpet.png"), "focus.upgrade.glowpet.name", "focus.upgrade.glowpet.text", new AspectList().add(Aspect.MOTION, 1).add(Aspect.LIGHT, 4).add(Aspect.ENERGY, 4));
	public static FocusUpgradeType diffusion = new FocusUpgradeType(ID--, new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"), "focus.upgrade.diffusion.name", "focus.upgrade.diffusion.text", new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ELDRITCH, 4).add(Aspect.AURA, 4));
}
