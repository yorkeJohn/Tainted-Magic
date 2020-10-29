package taintedmagic.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import taintedmagic.common.lib.LibInfo;

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
        proxy.preInit(event);
    }

    // Init
    @EventHandler
    public static void init (FMLInitializationEvent event)
    {
        log.info("...Initializing Tainted Magic!");

        proxy.init(event);
    }

    // Post init
    @EventHandler
    public static void postInit (FMLPostInitializationEvent event)
    {
        log.info("...Post-initializing Tainted Magic!");

        proxy.postInit(event);

        log.info("...Loading complete, enjoy!");
    }
}
