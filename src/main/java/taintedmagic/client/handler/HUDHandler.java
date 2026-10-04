package taintedmagic.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.common.TaintedMagic;

public final class HUDHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderGameOverlayEvent(final RenderGameOverlayEvent.Post event) {
        if (event.type == ElementType.ALL) {
            renderHeldItemHUD(event.partialTicks);
            renderString();
        }
    }

    private static float ticksEquipped = 0.0F;
    private static ItemStack stack = null;
    private static ItemStack last = null;

    @SideOnly(Side.CLIENT)
    private void renderHeldItemHUD(final float partialTicks) {
        final EntityPlayer player = TaintedMagic.proxy.getClientPlayer();
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        if (stack != null && stack.getItem() instanceof IHeldItemHUD) {
            last = stack.copy();
        }
        stack = player.getCurrentEquippedItem();

        boolean b = false;
        if (stack != null && stack.getItem() instanceof IHeldItemHUD) {
            b = true;
        } else {
            b = false;
        }

        final float time = 30.0F;
        if (b) {
            ticksEquipped = Math.min(time, ticksEquipped + partialTicks);
        } else {
            ticksEquipped = Math.max(0F, ticksEquipped - partialTicks);
        }

        final float fract = ticksEquipped / time;

        if (b) {
            ((IHeldItemHUD) stack.getItem()).renderHUD(res, player, stack, partialTicks, fract);
        } else if (!b && ticksEquipped != 0) {
            ((IHeldItemHUD) last.getItem()).renderHUD(res, player, last, partialTicks, fract);
        }
    }

    private static String currentText;
    private static int time;
    private static int ticks;
    private static boolean isRainbow;

    /**
     * Displays text above the health bar for a specified duration.
     *
     * @param text The string to display.
     * @param duration The duration to display for (in ticks).
     * @param rainbow Display the string in rainbow.
     */
    public static void displayString(final String text, final int duration, final boolean rainbow) {
        currentText = text;
        ticks = time = duration;
        isRainbow = rainbow;
    }

    @SideOnly(Side.CLIENT)
    public static void updateTicks() {
        if (ticks > 0) {
            ticks--;
        }
    }

    @SideOnly(Side.CLIENT)
    private void renderString() {
        if (ticks > 0 && !MathHelper.stringNullOrLengthZero(currentText)) {
            final Minecraft mc = Minecraft.getMinecraft();
            final ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            final int x = res.getScaledWidth();
            final int y = res.getScaledHeight();
            final FontRenderer font = mc.fontRenderer;

            final int startX = (x - font.getStringWidth(currentText)) / 2;
            final int startY = y - 72;

            int opacity = ticks > time * 0.25F ? 255 : (int) (255F * (ticks / (time * 0.25F)));
            if (opacity < 5) {
                opacity = 0;
            }

            final int rgb = Color.HSBtoRGB((float) (time - ticks) / (float) time, 1.0F, 1.0F);

            if (opacity > 0) {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                font.drawStringWithShadow(currentText, startX, startY, (isRainbow ? rgb : 0xFFFFFF) + (opacity << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }
}
