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

        checkIdAvailable(ConfigHandler.SANITY_UPGRADE_ID, "SANITY_UPGRADE_ID");
        sanity = new FocusUpgradeType(ConfigHandler.SANITY_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconSanity.png"), "focus.upgrade.sanity.name",
                "focus.upgrade.sanity.text", new AspectList().add(Aspect.MIND, 1).add(Aspect.HEAL, 1));

        checkIdAvailable(ConfigHandler.ANTIBODY_UPGRADE_ID, "ANTIBODY_UPGRADE_ID");
        antibody = new FocusUpgradeType(ConfigHandler.ANTIBODY_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconAntibody.png"), "focus.upgrade.antibody.name",
                "focus.upgrade.antibody.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.HEAL, 1));

        checkIdAvailable(ConfigHandler.CORROSIVE_UPGRADE_ID, "CORROSIVE_UPGRADE_ID");
        corrosive = new FocusUpgradeType(ConfigHandler.CORROSIVE_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconCorrosive.png"), "focus.upgrade.corrosive.name",
                "focus.upgrade.corrosive.text", new AspectList().add(Aspect.TAINT, 1).add(Aspect.POISON, 1));

        checkIdAvailable(ConfigHandler.PERSISTENT_UPGRADE_ID, "PERSISTENT_UPGRADE_ID");
        persistent = new FocusUpgradeType(ConfigHandler.PERSISTENT_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconPersistent.png"), "focus.upgrade.persistent.name",
                "focus.upgrade.persistent.text",
                new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));

        checkIdAvailable(ConfigHandler.DIFFUSION_UPGRADE_ID, "DIFFUSION_UPGRADE_ID");
        diffusion = new FocusUpgradeType(ConfigHandler.DIFFUSION_UPGRADE_ID,
                new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"), "focus.upgrade.diffusion.name",
                "focus.upgrade.diffusion.text",
                new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.ELDRITCH, 2).add(Aspect.AURA, 4));
    }

    /**
     * Thaumcraft silently ignores upgrades registered with an occupied ID, while the upgrade object still reports that ID,
     * causing the other mod's upgrade to be applied instead. Fail loudly so the conflict gets fixed in the config.
     */
    private static void checkIdAvailable (final int id, final String configOption) {
        if (id < FocusUpgradeType.types.length && FocusUpgradeType.types[id] != null) {
            throw new IllegalStateException(String.format(
                    "Tainted Magic focus upgrade ID %d is already used by focus upgrade '%s'. "
                            + "Change %s in the Tainted Magic config (wands_and_foci) to an unused ID.",
                    id, FocusUpgradeType.types[id].name, configOption));
        }
    }
}
