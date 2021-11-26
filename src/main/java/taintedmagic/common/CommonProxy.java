package taintedmagic.common;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.handler.TMEventHandler;
import taintedmagic.common.handler.UpdateHandler;
import taintedmagic.common.items.wand.foci.TMFocusUpgrades;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;
import taintedmagic.common.registry.OreDictRegistry;
import taintedmagic.common.registry.RecipeRegistry;
import taintedmagic.common.registry.ResearchRegistry;
import taintedmagic.common.registry.TMEntityRegistry;

public class CommonProxy {

    public void preInit (final FMLPreInitializationEvent event) {
        ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
        ConfigHandler.initConfig();

        PacketHandler.initPackets();
        ItemRegistry.initItems();
        BlockRegistry.initBlocks();
        BlockRegistry.initTiles();
        TMEntityRegistry.initEntities();
        RecipeRegistry.initRecipes();
        OreDictRegistry.initOreDict();

        if (ConfigHandler.NOTIFY_UPDATE) {
            UpdateHandler.checkForUpdate();
        }
    }

    public void init (final FMLInitializationEvent event) {
        registerHandlers();
        TMFocusUpgrades.initFocusUpgrades();
    }

    public void postInit (final FMLPostInitializationEvent event) {
        ResearchRegistry.initResearch();
    }

    public void registerHandlers () {
        MinecraftForge.EVENT_BUS.register(new TMEventHandler());
        FMLCommonHandler.instance().bus().register(new TMEventHandler());
    }

    public void registerRenderers () {
    }

    public EntityPlayer getClientPlayer () {
        return null;
    }

    public World getClientWorld () {
        return null;
    }
}
