package taintedmagic.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.ClientHandler;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.client.handler.SashKeyListener;
import taintedmagic.client.renderer.RenderEntityDiffusion;
import taintedmagic.client.renderer.RenderEntityHomingShard;
import taintedmagic.client.renderer.RenderItemKatana;
import taintedmagic.common.CommonProxy;
import taintedmagic.common.entities.EntityDarkMatter;
import taintedmagic.common.entities.EntityDiffusion;
import taintedmagic.common.entities.EntityHomingShard;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.client.renderers.entity.RenderEldritchOrb;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit (final FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init (final FMLInitializationEvent event) {
        super.init(event);
        registerRenderers();
    }

    @Override
    public void postInit (final FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void registerHandlers () {
        super.registerHandlers();

        MinecraftForge.EVENT_BUS.register(new HUDHandler());

        FMLCommonHandler.instance().bus().register(new ClientHandler());
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
        MinecraftForge.EVENT_BUS.register(new SashKeyListener());
    }

    @Override
    public void registerRenderers () {
        // Entities
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkMatter.class, new RenderEldritchOrb());
        RenderingRegistry.registerEntityRenderingHandler(EntityHomingShard.class, new RenderEntityHomingShard());
        RenderingRegistry.registerEntityRenderingHandler(EntityDiffusion.class, new RenderEntityDiffusion());

        // Items
        MinecraftForgeClient.registerItemRenderer(ItemRegistry.ItemKatana, new RenderItemKatana());
    }

    @Override
    public EntityPlayer getClientPlayer () {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getClientWorld () {
        return FMLClientHandler.instance().getClient().theWorld;
    }
}
