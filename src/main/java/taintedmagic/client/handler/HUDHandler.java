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
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.common.TaintedMagic;

public final class HUDHandler
{
    @SubscribeEvent
    @SideOnly (Side.CLIENT)
    public void renderGameOverlayEvent (RenderGameOverlayEvent.Post event)
    {
        if (event.type == ElementType.ALL)
        {
            renderHeldItemHUD(event.partialTicks);
            renderString();
        }
    }

    private static float ticksEquipped = 0.0F;
    private static ItemStack stack = null;
    private static ItemStack last = null;

    @SideOnly (Side.CLIENT)
    private void renderHeldItemHUD (float partialTicks)
    {
        EntityPlayer player = TaintedMagic.proxy.getClientPlayer();
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        if (stack != null && stack.getItem() instanceof IHeldItemHUD) last = stack.copy();
        stack = player.getCurrentEquippedItem();

        boolean b = false;
        if (stack != null && stack.getItem() instanceof IHeldItemHUD) b = true;
        else b = false;

        float time = 30.0F;
        if (b) ticksEquipped = Math.min(time, ticksEquipped + partialTicks);
        else ticksEquipped = Math.max(0F, ticksEquipped - partialTicks);

        float fract = ticksEquipped / time;

        if (b) ((IHeldItemHUD) stack.getItem()).renderHUD(res, player, stack, partialTicks, fract);
        else if (!b && ticksEquipped != 0) ((IHeldItemHUD) last.getItem()).renderHUD(res, player, last, partialTicks, fract);
    }

    private static String currentText;
    private static int time, ticks;
    private static boolean gay;

    /**
     * Displays text above the health bar for a specified duration.
     * 
     * @param text The string to display.
     * @param duration The duration to display it for (in ticks).
     * @param rainbow Setting this to true makes things absolutely fabulous.
     */
    public static void displayString (String text, int duration, boolean rainbow)
    {
        currentText = text;
        ticks = time = duration;
        gay = rainbow;
    }

    @SideOnly (Side.CLIENT)
    public static void updateTicks ()
    {
        if (ticks > 0) ticks--;
    }

    @SideOnly (Side.CLIENT)
    private void renderString ()
    {
        if (ticks > 0 && !MathHelper.stringNullOrLengthZero(currentText))
        {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int x = res.getScaledWidth();
            int y = res.getScaledHeight();
            FontRenderer font = mc.fontRenderer;

            int startX = (x - font.getStringWidth(currentText)) / 2;
            int startY = y - 72;

            int opacity = ticks > ((float) time * 0.25F) ? 255 : (int) (255F * ((float) ticks / ((float) time * 0.25F)));
            if (opacity < 5) opacity = 0;

            int rgb = Color.HSBtoRGB( ((float) (time - ticks) / (float) time), 1.0F, 1.0F);

            if (opacity > 0)
            {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                font.drawStringWithShadow(currentText, startX, startY, (gay ? rgb : 0xFFFFFF) + (opacity << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }
}
