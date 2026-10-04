package taintedmagic.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.tools.ItemKatana;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class RenderItemKatana implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack stack, final ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(
            final ItemRenderType type, final ItemStack stack, final ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack stack, final Object... data) {
        final EntityPlayer player = TaintedMagic.proxy.getClientPlayer();

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glScalef(1.4F, 1.4F, 1.4F);

        if ((player.getHeldItem() == null || player.getHeldItem() != stack) && type != ItemRenderType.EQUIPPED) {
            GL11.glPushMatrix();
            if (type == ItemRenderType.ENTITY) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                GL11.glTranslatef(0F, 0.5F, 0F);
                GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
            } else if (type == ItemRenderType.INVENTORY) {
                GL11.glScalef(0.4F, 0.4F, 0.4F);
                GL11.glRotatef(-65, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-50, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(1F, 1F, 0.0F);
            } else {
                GL11.glTranslatef(0.0F, -0.7F, 0.0F);
            }

            UtilsFX.bindTexture(ItemKatana.getTexture(stack));
            ItemKatana.SAYA.render(0.0625F);

            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();

        if (type == ItemRenderType.ENTITY) {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glTranslatef(0F, 0.5F, 0F);
            GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
        } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            if (player.isUsingItem()) {
                GL11.glTranslatef(5.0F, 1.0F, -0.2F);
                GL11.glRotatef(155.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-20.0F, 1.0F, 0.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.25F, 0.0F);
            }
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            GL11.glTranslatef(2.0F, 1.85F, -0.25F);
            GL11.glRotatef(-10F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-10F, 0.0F, 0.0F, 1.0F);
        } else if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(0.4F, 0.4F, 0.4F);
            GL11.glRotatef(-65, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-50, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(1F, 1F, 0.0F);
        } else {
            GL11.glRotatef(60, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-40, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(70, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 2F, 0.65F);
        }

        UtilsFX.bindTexture(ItemKatana.getTexture(stack));

        ItemKatana.KATANA.render(0.0625F);

        // Runes
        if (ItemKatana.hasAnyInscription(stack)) {
            GL11.glPushMatrix();
            final int j = 200;
            final int k = j % 65536;
            final int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);

            GL11.glEnable(GL11.GL_BLEND);

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
            for (int a = 0; a < 14; a++) {
                final int rune = a * 3 % 16;
                drawRune(-1.65D + a * 0.14D, 0D, -0.03D, rune, player);
            }
            GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
            for (int a = 0; a < 14; a++) {
                final int rune = (a + 1 * 3) % 16;
                drawRune(-1.65D + a * 0.14D, 0D, -0.03D, rune, player);
            }
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();

        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_CULL_FACE);

        GL11.glPopMatrix();
    }

    /**
     * TC method to draw runes on runed staves
     */
    public static void drawRune(
            final double x, final double y, final double z, final int rune, final EntityPlayer player) {
        GL11.glPushMatrix();

        UtilsFX.bindTexture("textures/misc/script.png");

        final float r = MathHelper.sin((player.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.88F;
        final float g = MathHelper.sin((player.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.63F;
        final float alpha = MathHelper.sin((player.ticksExisted + rune * 5) / 10.0F) * 0.3F;

        GL11.glColor4f(r, g, 0.2F, alpha + 0.6F);

        GL11.glRotated(90.0D, 0.0D, 0.0D, 1.0D);
        GL11.glTranslated(x, y, z);

        final Tessellator t = Tessellator.instance;

        final float f = 0.0625F * rune;
        final float f1 = f + 0.0625F;
        final float f2 = 0.0F;
        final float f3 = 1.0F;

        t.startDrawingQuads();

        t.setColorRGBA_F(r, g, 0.2F, alpha + 0.6F);

        t.addVertexWithUV(-0.06D - alpha / 40.0F, 0.06D + alpha / 40.0F, 0.0D, f1, f3);
        t.addVertexWithUV(0.06D + alpha / 40.0F, 0.06D + alpha / 40.0F, 0.0D, f1, f2);
        t.addVertexWithUV(0.06D + alpha / 40.0F, -0.06D - alpha / 40.0F, 0.0D, f, f2);
        t.addVertexWithUV(-0.06D - alpha / 40.0F, -0.06D - alpha / 40.0F, 0.0D, f, f3);

        t.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }
}
