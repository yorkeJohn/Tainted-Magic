package taintedmagic.common.handler;

import net.minecraftforge.common.config.Configuration;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.wands.FocusUpgradeType;

public class ConfigHandler
{
    public static Configuration config;

    public static boolean NOTIFY_UPDATE = true;

    public static boolean RESEARCH_TAGS = true;
    public static boolean CUSTOM_RESEARCH_TAB_BACK = false;

    public static double MAGE_MACE_DMG_INC_BASE = 8.0D;

    public static float WARP_WAND_REFRESH_BASE = 10000.0F;

    public static int SANITY_UPGRADE_ID = 64;
    public static int ANTIBODY_UPGRADE_ID = 65;
    public static int CORROSIVE_UPGRADE_ID = 66;
    public static int PERSISTENT_UPGRADE_ID = 67;
    public static int DIFFUSION_UPGRADE_ID = 68;

    public static void initConfig ()
    {
        TaintedMagic.logger.info("Loading config...");

        config.load();

        NOTIFY_UPDATE = config.getBoolean("NOTIFY_UPDATE", "misc", true,
                "Setting this to false will disable Tainted Magic update notifications.");

        RESEARCH_TAGS = config.getBoolean("RESEARCH_TAGS", "research", true,
                "Setting this to false will disable the '[TM]' tag on Tainted Magic research items.");

        CUSTOM_RESEARCH_TAB_BACK = config.getBoolean("CUSTOM_RESEARCH_TAB_BACK", "research", false,
                "Setting this to true will bring back the old Tainted Magic research tab background.");

        MAGE_MACE_DMG_INC_BASE = (double) config.getFloat("MAGE_MACE_DMG_INC_BASE", "wands_and_foci", 8.0F, 1.0F, 100.0F,
                "Defines the base amount of damage to increase wand / staff attack damage by "
                        + "when the Mage's Mace focus is equipped.");

        WARP_WAND_REFRESH_BASE = config.getFloat("WARP_WAND_REFRESH_BASE", "wands_and_foci", 10000.0F, 100.0F, 100000.0F,
                "Defines the base refresh period in ticks for the Warpwood Wand. The delay period between vis refreshes "
                        + "is calculated by dividing this number by the amount of permanent warp the player has accumulated.");

        /**
         * Focus upgrade IDs
         */
        SANITY_UPGRADE_ID = config.getInt("SANITY_UPGRADE_ID", "wands_and_foci", 64, FocusUpgradeType.types.length + 1,
                Short.MAX_VALUE, "The ID for the Sanity focus upgrade.");

        ANTIBODY_UPGRADE_ID = config.getInt("ANTIBODY_UPGRADE_ID", "wands_and_foci", 65, FocusUpgradeType.types.length + 1,
                Short.MAX_VALUE, "The ID for the Antibody focus upgrade.");

        CORROSIVE_UPGRADE_ID = config.getInt("CORROSIVE_UPGRADE_ID", "wands_and_foci", 66, FocusUpgradeType.types.length + 1,
                Short.MAX_VALUE, "The ID for the Corrosive focus upgrade.");

        PERSISTENT_UPGRADE_ID = config.getInt("PERSISTENT_UPGRADE_ID", "wands_and_foci", 67, FocusUpgradeType.types.length + 1,
                Short.MAX_VALUE, "The ID for the Persistent focus upgrade.");

        DIFFUSION_UPGRADE_ID = config.getInt("DIFFUSION_UPGRADE_ID", "wands_and_foci", 68, FocusUpgradeType.types.length + 1,
                Short.MAX_VALUE, "The ID for the Diffusion focus upgrade.");

        config.save();
    }
}
