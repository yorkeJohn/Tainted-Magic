package taintedmagic.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.handler.TMEventHandler;
import taintedmagic.common.handler.UpdateHandler;
import taintedmagic.common.items.wand.foci.TMFocusUpgrades;
import taintedmagic.common.lib.LibInfo;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;
import taintedmagic.common.registry.OreDictRegistry;
import taintedmagic.common.registry.RecipeRegistry;
import taintedmagic.common.registry.ResearchRegistry;
import taintedmagic.common.registry.TMEntityRegistry;

@Mod (modid = LibInfo.MODID, name = LibInfo.NAME, version = LibInfo.VERSION, dependencies = LibInfo.DEPENDENCIES)
public class TaintedMagic
{
    @Mod.Instance ("TaintedMagic")
    public static TaintedMagic instance;

    // Proxies
    @SidedProxy (clientSide = "taintedmagic.client.ClientProxy", serverSide = "taintedmagic.common.CommonProxy")
    public static CommonProxy proxy;

    // Tainted Magic creative tab
    public static CreativeTabs tabTaintedMagic = new TMCreativeTab();

    // TM log
    public static final Logger log = LogManager.getLogger("TAINTEDMAGIC");

    // Creation EnumRarity
    public static EnumRarity rarityCreation = EnumHelper.addRarity("CREATION", EnumChatFormatting.GOLD, "Creation");

    // Pre init
    @EventHandler
    public static void preInit (FMLPreInitializationEvent event)
    {
        log.info("...Pre-initializing Tainted Magic!");

        // Config
        ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigHandler.initConfig();

        proxy.registerClientHandlers();

        PacketHandler.initPackets();
        ItemRegistry.initItems();
        BlockRegistry.initBlocks();
        BlockRegistry.initTiles();
        TMEntityRegistry.initEntities();
        RecipeRegistry.initRecipes();
        OreDictRegistry.initOreDict();

        if (ConfigHandler.NOTIFY_UPDATE) UpdateHandler.checkForUpdate();
    }

    // Init
    @EventHandler
    public static void init (FMLInitializationEvent event)
    {
        log.info("...Initializing Tainted Magic!");

        MinecraftForge.EVENT_BUS.register(new TMEventHandler());
        FMLCommonHandler.instance().bus().register(new TMEventHandler());
        
        proxy.registerRenderers();
        TMFocusUpgrades.initFocusUpgrades();
    }

    // Post init
    @EventHandler
    public static void postInit (FMLPostInitializationEvent event)
    {
        log.info("...Post-initializing Tainted Magic!");

        ResearchRegistry.initResearch();

        log.info("...Loading complete, enjoy!");
    }
}
