package taintedmagic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.client.renderer.RenderEntityDiffusion;
import taintedmagic.client.renderer.RenderEntityGlowpet;
import taintedmagic.client.renderer.RenderEntityHomingShard;
import taintedmagic.client.renderer.RenderEntityTaintBubble;
import taintedmagic.client.renderer.RenderItemKatana;
import taintedmagic.common.CommonProxy;
import taintedmagic.common.entities.EntityDarkMatter;
import taintedmagic.common.entities.EntityDiffusion;
import taintedmagic.common.entities.EntityGlowpet;
import taintedmagic.common.entities.EntityHomingShard;
import taintedmagic.common.entities.EntityTaintBubble;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.client.renderers.entity.RenderEldritchOrb;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.lib.network.PacketHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders ()
	{
		// Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityTaintBubble.class, new RenderEntityTaintBubble());
		RenderingRegistry.registerEntityRenderingHandler(EntityDarkMatter.class, new RenderEldritchOrb());
		RenderingRegistry.registerEntityRenderingHandler(EntityHomingShard.class, new RenderEntityHomingShard());
		RenderingRegistry.registerEntityRenderingHandler(EntityGlowpet.class, new RenderEntityGlowpet());
		RenderingRegistry.registerEntityRenderingHandler(EntityDiffusion.class, new RenderEntityDiffusion());

		// Items
		MinecraftForgeClient.registerItemRenderer(ItemRegistry.ItemKatana, new RenderItemKatana());
	}

	@Override
	public EntityPlayer getClientPlayer ()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public WorldClient getClientWorld ()
	{
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public void registerClientHandlers ()
	{
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
		FMLCommonHandler.instance().bus().register(new HUDHandler());
	}
}
