package taintedmagic.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import taintedmagic.common.entities.EntityDiffusion;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class RenderEntityDiffusion extends Render {

    @Override
    public void doRender(
            final Entity entity,
            final double x,
            final double y,
            final double z,
            final float f,
            final float partialTicks) {
        render((EntityDiffusion) entity, x, y, z, f, partialTicks);
    }

    public void render(
            final EntityDiffusion entity,
            final double x,
            final double y,
            final double z,
            final float f,
            final float partialTicks) {
        final Tessellator t = Tessellator.instance;

        GL11.glPushMatrix();

        GL11.glTranslated(x, y, z);

        final float color = 0.1F;
        final float alpha = (20.0F - entity.ticksExisted) / 40.0F;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        UtilsFX.bindTexture(ParticleEngine.particleTexture);

        final float f2 = entity.ticksExisted % 13 / 16.0F;
        final float f3 = f2 + 0.0625F;
        final float f4 = 0.1875F;
        final float f5 = f4 + 0.0625F;
        final float f6 = 1.0F;
        final float f7 = 0.5F;

        GL11.glColor4f(color, color, color, alpha);

        GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        final float s = 0.5F;
        GL11.glScalef(s, s, s);

        t.startDrawingQuads();

        t.setNormal(0.0F, 1.0F, 0.0F);
        t.setColorRGBA_F(color, color, color, alpha);

        t.addVertexWithUV(0.0F - f7, 0.0F - f7, 0.0D, f2, f5);
        t.addVertexWithUV(f6 - f7, 0.0F - f7, 0.0D, f3, f5);
        t.addVertexWithUV(f6 - f7, 1.0F - f7, 0.0D, f3, f4);
        t.addVertexWithUV(0.0F - f7, 1.0F - f7, 0.0D, f2, f4);

        t.draw();

        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return null;
    }
}
