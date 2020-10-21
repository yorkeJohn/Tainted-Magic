package taintedmagic.client.handler;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.common.TaintedMagic;

@SideOnly (Side.CLIENT)
public class HUDHandler
{
	@SubscribeEvent
	public void renderGameOverlayEvent (RenderGameOverlayEvent.Post event)
	{
		if (event.type == ElementType.ALL)
		{
			renderHeldItemHUD(event.partialTicks);
			renderString();
		}
	}

	private static float ticksEquipped = 0.0F;
	private static ItemStack s = null;
	private static ItemStack last = null;

	private void renderHeldItemHUD (float pt)
	{
		EntityPlayer p = TaintedMagic.proxy.getClientPlayer();
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

		if (s != null && s.getItem() instanceof IHeldItemHUD) last = s.copy();
		s = p.getCurrentEquippedItem();

		boolean b = false;
		if (s != null && s.getItem() instanceof IHeldItemHUD) b = true;
		else b = false;

		float time = 30.0F;
		if (b) ticksEquipped = Math.min(time, ticksEquipped + pt);
		else ticksEquipped = Math.max(0F, ticksEquipped - pt);

		float fract = ticksEquipped / time;

		if (b) ((IHeldItemHUD) s.getItem()).renderHUD(res, p, s, pt, fract);
		else if (!b && ticksEquipped != 0) ((IHeldItemHUD) last.getItem()).renderHUD(res, p, last, pt, fract);
	}

	private static String currentStr;
	private static int start;
	private static int ticks;
	private static boolean gay;

	public static void displayStringRainbow (String str, int display)
	{
	}

	public static void displayString (String str, int duration, boolean rainbow)
	{
		currentStr = str;
		ticks = start = duration;
		gay = rainbow;
	}

	public static void updateTicks ()
	{
		if (ticks > 0) ticks--;
	}

	private void renderString ()
	{
		if (ticks > 0 && !MathHelper.stringNullOrLengthZero(currentStr))
		{
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			int x = res.getScaledWidth();
			int y = res.getScaledHeight();
			FontRenderer font = mc.fontRenderer;

			int startX = (x - font.getStringWidth(currentStr)) / 2;
			int startY = y - 72;

			int opacity = ticks > ((float) start * 0.25F) ? 255 : (int) (255F * ((float) ticks / ((float) start * 0.25F)));
			if (opacity < 5) opacity = 0;

			int rgb = Color.HSBtoRGB( ((float) (start - ticks) / (float) start), 1.0F, 1.0F);

			if (opacity > 0)
			{
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				font.drawStringWithShadow(currentStr, startX, startY, (gay ? rgb : 0xFFFFFF) + (opacity << 24));
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glPopMatrix();
			}
		}
	}
}
