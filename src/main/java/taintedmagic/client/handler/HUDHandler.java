package taintedmagic.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.common.TaintedMagic;

@SideOnly (Side.CLIENT)
public class HUDHandler
{
	float ticksEquipped = 0.0F;
	ItemStack s = null;
	ItemStack last = null;

	@SubscribeEvent
	public void onDrawScreen (RenderGameOverlayEvent.Post event)
	{
		if (event.type == ElementType.ALL)
		{
			EntityPlayer p = TaintedMagic.proxy.getClientPlayer();
			float partialTicks = event.partialTicks;
			if (s != null && s.getItem() instanceof IHeldItemHUD) last = s.copy();
			s = p.getCurrentEquippedItem();

			if (Minecraft.getMinecraft().currentScreen == null)
			{
				boolean b = false;
				if (s != null && s.getItem() instanceof IHeldItemHUD) b = true;
				else b = false;

				float time = 30F;
				if (b) ticksEquipped = Math.min(time, ticksEquipped + partialTicks);
				else ticksEquipped = Math.max(0F, ticksEquipped - partialTicks);

				float fract = ticksEquipped / time;

				if (b)
				{
					((IHeldItemHUD) s.getItem()).renderHUD(event.resolution, p, s, partialTicks, fract);
				}
				if (!b && ticksEquipped != 0)
				{
					((IHeldItemHUD) last.getItem()).renderHUD(event.resolution, p, last, partialTicks, fract);
				}
			}
		}
	}
}
