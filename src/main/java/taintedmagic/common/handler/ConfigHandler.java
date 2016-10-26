package taintedmagic.common.handler;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import taintedmagic.common.TaintedMagic;

public class ConfigHandler
{
	public static Configuration config;

	public static boolean useUpdateHandler = true;
	public static boolean researchTags = true;
	public static boolean useCustomResearchTabBackground = false;
	
	public static void init ()
	{
		TaintedMagic.log.info("Loading config");

		config.load();

		useUpdateHandler = config.getBoolean("use_update_handler", "misc", true, "Should update notifications be enabled?");
		researchTags = config.getBoolean("research_tags", "research", true, "Setting this to false will disable the '[TaintedMagic]' tag on the research");
		useCustomResearchTabBackground = config.getBoolean("use_custom_research_tab_background", "research", false, "Setting this to true will enable the old custom tab background");

		config.save();
	}
}
