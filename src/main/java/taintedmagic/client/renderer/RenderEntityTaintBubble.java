package taintedmagic.client.renderer;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import taintedmagic.common.entities.EntityTaintBubble;

public class RenderEntityTaintBubble extends Render
{
	private static final ResourceLocation texture = new ResourceLocation("taintedmagic:textures/entities/EntityTaintBubble.png");
	private Random random = new Random();

	public RenderEntityTaintBubble ()
	{
		this.shadowSize = 0.0F;
	}

	@Override
	public void doRender (Entity e, double x, double y, double z, float f, float pt)
	{
		Tessellator t = Tessellator.instance;
		EntityTaintBubble bubble = (EntityTaintBubble) e;

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.75F);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int p = (int) (7.0F * ((float) bubble.ticksExisted / (float) bubble.duration));
		float f2 = 0;
		float f3 = 1;
		float f4 = p / 8.0F;
		float f5 = (p + 1) / 8.0F;

		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;

		float s = 0.25F;

		GL11.glScalef(s, s, s);

		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		t.startDrawingQuads();
		t.setBrightness(240);
		t.setNormal(0.0F, 1.0F, 0.0F);
		t.setColorRGBA_F(1.0F, 1.0F, 1.0F, 0.9F);

		t.addVertexWithUV(-f7, -f8, 0.0D, f2, f5);
		t.addVertexWithUV(f6 - f7, -f8, 0.0D, f3, f5);
		t.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
		t.addVertexWithUV(-f7, 1.0F - f8, 0.0D, f2, f4);

		t.draw();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(32826);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture (Entity e)
	{
		return null;
	}
}