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
	private static final ResourceLocation texture = new ResourceLocation("thaumcraft:textures/misc/particles.png");

	public RenderEntityTaintBubble ()
	{
		this.shadowSize = 0.0F;
	}

	@Override
	public void doRender (Entity e, double x, double y, double z, float f, float pt)
	{
		Tessellator t = Tessellator.instance;
		EntityTaintBubble bubble = (EntityTaintBubble) e;

		float r = 0.8F + e.worldObj.rand.nextFloat() * 0.2F;
		float g = e.worldObj.rand.nextFloat() * 0.4F;
		float b = 1.0F - e.worldObj.rand.nextFloat() * 0.2F;
		float a = (50.0F - (float) e.ticksExisted) / 50.0F;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glDepthMask(false);

		GL11.glColor4f(r, g, b, a);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		int p = 7 + bubble.ticksExisted % 8;
		float f2 = p / 16.0F;
		float f3 = f2 + 0.0624375F;
		float f4 = 0.25F;
		float f5 = f4 + 0.0624375F;

		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;

		float s = 0.3F;

		GL11.glScalef(s, s, s);

		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

		t.startDrawingQuads();

		t.setBrightness(240);
		t.setNormal(0.0F, 1.0F, 0.0F);
		t.setColorRGBA_F(r, g, b, a);

		t.addVertexWithUV(-f7, -f8, 0.0D, f2, f5);
		t.addVertexWithUV(f6 - f7, -f8, 0.0D, f3, f5);
		t.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
		t.addVertexWithUV(-f7, 1.0F - f8, 0.0D, f2, f4);

		t.draw();

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture (Entity e)
	{
		return null;
	}
}