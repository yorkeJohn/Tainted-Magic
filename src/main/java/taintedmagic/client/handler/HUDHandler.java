package taintedmagic.client.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HUDHandler
{
	@SubscribeEvent
	public void onDrawScreen (RenderGameOverlayEvent.Post event)
	{
		if (event.type == ElementType.ALL)
		{
			Minecraft mc = Minecraft.getMinecraft();
			ItemStack equippedStack = mc.thePlayer.getCurrentEquippedItem();
		}
	}
}
