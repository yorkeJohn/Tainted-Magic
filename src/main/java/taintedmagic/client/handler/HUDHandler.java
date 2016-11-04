package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import taintedmagic.common.items.tools.ItemKatana;
import taintedmagic.common.items.tools.ItemThaumicDisassembler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

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
