package taintedmagic.client.handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import taintedmagic.common.items.equipment.ItemWarpedGoggles;
import taintedmagic.common.items.tools.ItemKatana;
import taintedmagic.common.items.tools.ItemThaumicDisassembler;
import thaumcraft.client.lib.ClientTickEventsFML;
import thaumcraft.client.lib.RenderEventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class HUDHandler
{
	@SubscribeEvent
	public void onDrawScreen (RenderGameOverlayEvent.Post event)
	{
		if (event.type == ElementType.ALL)
		{
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer p = mc.thePlayer;
			ScaledResolution r = event.resolution;
			float pt = event.partialTicks;

			if (mc.currentScreen == null)
			{
				ItemKatana.renderHUD(r, p, pt);
				ItemThaumicDisassembler.renderHUD(r, p, pt);
			}
		}
	}
}
