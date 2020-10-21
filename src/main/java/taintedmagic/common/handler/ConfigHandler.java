package taintedmagic.common.handler;

import net.minecraftforge.common.config.Configuration;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.wands.FocusUpgradeType;

public class ConfigHandler
{
	public static Configuration config;

	public static boolean UPDATE_HANDLER = true;
	public static boolean RESEARCH_TAGS = true;
	public static boolean CUSTOM_RESEARCH_TAB_BACK = false;
	public static double MAGE_MACE_DMG_INC_BASE = 8.0D;
	public static float WARP_WAND_REFRESH_BASE = 10000.0F;
	public static int FOCUS_UPGRADES_START_ID = 64;

	public static void init ()
	{
		TaintedMagic.log.info("Loading config");

		config.load();

		UPDATE_HANDLER = config.getBoolean("UPDATE_HANDLER", "misc", true, "Setting this to false will disable ingame chat Tainted Magic update notifications.");

		RESEARCH_TAGS = config.getBoolean("RESEARCH_TAGS", "research", true, "Setting this to false will disable the '[categoryTM]' tag on Tainted Magic research items.");

		CUSTOM_RESEARCH_TAB_BACK = config.getBoolean("CUSTOM_RESEARCH_TAB_BACK", "research", false, "Setting this to true will bring back the old Tainted Magic research tab background.");

		MAGE_MACE_DMG_INC_BASE = (double) config
				.getFloat("MAGE_MACE_DMG_INC_BASE", "misc", 8.0F, 1.0F, 100.0F, "Defines the base amount of damage to increase wand / staff attack damage by when the Mage's Mace focus is equipped.");

		WARP_WAND_REFRESH_BASE = config
				.getFloat("WARP_WAND_REFRESH_BASE", "misc", 10000.0F, 100.0F, 100000.0F, "Defines the base refresh period in ticks for the Warpwood Wand. The delay period between refreshs is calculated by dividing this number by the amount of permanant warp the player has accumulated.");

		FOCUS_UPGRADES_START_ID = config.getInt("FOCUS_UPGRADES_START_ID", "misc", 64, FocusUpgradeType.types.length + 1, Short.MAX_VALUE
				- 1, "Defines the starting ID for categoryTM focus upgrades. The first upgrade will be registered under this ID, the next under this ID +1, etc.");

		config.save();
	}
}
