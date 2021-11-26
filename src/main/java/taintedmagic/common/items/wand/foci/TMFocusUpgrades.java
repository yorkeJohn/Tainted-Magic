package taintedmagic.common.items.wand.foci;

import net.minecraft.util.ResourceLocation;
import taintedmagic.common.handler.ConfigHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;

public class TMFocusUpgrades {

    public static FocusUpgradeType sanity;
    public static FocusUpgradeType antibody;
    public static FocusUpgradeType corrosive;
    public static FocusUpgradeType persistent;
    public static FocusUpgradeType diffusion;

    public static void initFocusUpgrades () {

        sanity = new FocusUpgradeType(ConfigHandler.SANITY_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconSanity.png"), "focus.upgrade.sanity.name",
                "focus.upgrade.sanity.text", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));

        antibody = new FocusUpgradeType(ConfigHandler.ANTIBODY_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconAntibody.png"), "focus.upgrade.antibody.name",
                "focus.upgrade.antibody.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));

        corrosive = new FocusUpgradeType(ConfigHandler.CORROSIVE_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconCorrosive.png"), "focus.upgrade.corrosive.name",
                "focus.upgrade.corrosive.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));

        persistent = new FocusUpgradeType(ConfigHandler.PERSISTENT_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconPersistent.png"), "focus.upgrade.persistent.name",
                "focus.upgrade.persistent.text",
                new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));

        diffusion = new FocusUpgradeType(ConfigHandler.DIFFUSION_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"), "focus.upgrade.diffusion.name",
                "focus.upgrade.diffusion.text",
                new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ELDRITCH, 2).add(Aspect.AURA, 4));
    }
}
