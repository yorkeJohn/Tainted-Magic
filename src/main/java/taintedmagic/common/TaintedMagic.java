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
import taintedmagic.Tags;

@Mod (modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION,
        dependencies = "required-after:Forge@[10.13.4.1448,);required-after:Thaumcraft@[4.2.3.5,)")
public class TaintedMagic {

    @Mod.Instance (Tags.MOD_ID)
    public static TaintedMagic instance;

    // Proxies
    @SidedProxy (clientSide = "taintedmagic.client.ClientProxy", serverSide = "taintedmagic.common.CommonProxy")
    public static CommonProxy proxy;

    // TM creative tab
    public static final CreativeTabs tabTM = new TMCreativeTab();

    // TM logger
    public static final Logger logger = LogManager.getLogger("TAINTED MAGIC");

    // Creation EnumRarity
    public static EnumRarity rarityCreation = EnumHelper.addRarity("CREATION", EnumChatFormatting.GOLD, "Creation");

    // Pre init
    @EventHandler
    public static void preInit (final FMLPreInitializationEvent event) {
        logger.info("Pre-initializing Tainted Magic!");
        proxy.preInit(event);
    }

    // Init
    @EventHandler
    public static void init (final FMLInitializationEvent event) {
        logger.info("Initializing Tainted Magic!");
        proxy.init(event);
    }

    // Post init
    @EventHandler
    public static void postInit (final FMLPostInitializationEvent event) {
        logger.info("Post-initializing Tainted Magic!");
        proxy.postInit(event);
        logger.info("Loading complete!");
    }
}
