package taintedmagic.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import taintedmagic.common.entities.EntityDiffusion;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;

public class RenderEntityDiffusion extends Render
{
	public void render (EntityDiffusion e, double x, double y, double z, float f, float pT)
	{
		Tessellator t = Tessellator.instance;

		GL11.glPushMatrix();

		GL11.glTranslated(x, y, z);

		float color = 0.1F;
		float alpha = (20.0F - (float) e.ticksExisted) / 40.0F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		UtilsFX.bindTexture(ParticleEngine.particleTexture);

		float f2 = e.ticksExisted % 13 / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.1875F;
		float f5 = f4 + 0.0624375F;
		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;

		GL11.glColor4f(color, color, color, alpha);

		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		float s = 0.5F;
		GL11.glScalef(s, s, s);

		t.startDrawingQuads();

		t.setNormal(0.0F, 1.0F, 0.0F);
		t.setColorRGBA_F(color, color, color, alpha);

		t.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0D, f2, f5);
		t.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0D, f3, f5);
		t.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
		t.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0D, f2, f4);

		t.draw();

		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPopMatrix();
	}

	@Override
	public void doRender (Entity e, double x, double y, double z, float f, float pT)
	{
		render((EntityDiffusion) e, x, y, z, f, pT);
	}

	@Override
	protected ResourceLocation getEntityTexture (Entity e)
	{
		return null;
	}
}
