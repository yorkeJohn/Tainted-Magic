package taintedmagic.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import taintedmagic.api.IBloodlust;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.handler.TaintedMagicEventHandler;
import taintedmagic.common.handler.UpdateHandler;
import taintedmagic.common.lib.LibCreativeTab;
import taintedmagic.common.lib.LibRecipes;
import taintedmagic.common.lib.LibStrings;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;
import taintedmagic.common.registry.ModEntityRegistry;
import taintedmagic.common.registry.OreDictionaryRegistry;
import taintedmagic.common.registry.ResearchRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod (modid = LibStrings.MODID, name = LibStrings.NAME, version = LibStrings.VERSION, dependencies = LibStrings.DEPENDENCIES)
public class TaintedMagic
{
	@Mod.Instance ("TaintedMagic")
	public static TaintedMagic instance;

	// Proxies
	@SidedProxy (clientSide = "taintedmagic.client.ClientProxy", serverSide = "taintedmagic.common.CommonProxy")
	public static CommonProxy proxy;

	public static TaintedMagicEventHandler taintedMagicEvents;
	public static CreativeTabs tabTaintedMagic = new LibCreativeTab();
	public static ConfigHandler configHandler;
	public static final Logger log = LogManager.getLogger("TAINTEDMAGIC");
	public static EnumRarity rarityCreation = EnumHelper.addRarity("CREATION", EnumChatFormatting.GOLD, "Creation");

	// Pre init
	@EventHandler
	public static void preInit (FMLPreInitializationEvent event)
	{
		log.info("Please stand back... Pre-initializing Tainted Magic!");

		// Config
		configHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		configHandler.init();

		proxy.registerClientHandlers();

		ItemRegistry.init();
		BlockRegistry.init();
		ModEntityRegistry.init();
		LibRecipes.init();

		if (configHandler.useUpdateHandler) UpdateHandler.init();
	}

	// Init
	@EventHandler
	public static void init (FMLInitializationEvent event)
	{
		log.info("Things seem to be going smoothly... Initializing Tainted Magic!");

		taintedMagicEvents = new TaintedMagicEventHandler();

		MinecraftForge.EVENT_BUS.register(taintedMagicEvents);
		FMLCommonHandler.instance().bus().register(taintedMagicEvents);

		proxy.initRenders();
	}

	// Post init
	@EventHandler
	public static void postInit (FMLPostInitializationEvent event)
	{
		log.info("Almost done... Post-initializing Tainted Magic!");

		ResearchRegistry.initResearch();
		OreDictionaryRegistry.init();

		log.info("Phew! Tainted Magic has finished loading, enjoy!");
	}
}
