package taintedmagic.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import taintedmagic.common.entities.EntityGlowpet;
import thaumcraft.client.fx.ParticleEngine;

public class RenderEntityGlowpet extends Render
{
	public RenderEntityGlowpet ()
	{
		super();
		this.shadowSize = 0.0F;
	}

	public void renderEntityAt (EntityGlowpet e, double x, double y, double z, float f, float pT)
	{
		Tessellator t = Tessellator.instance;

		GL11.glPushMatrix();
		GL11.glDepthMask(false);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);

		bindTexture(ParticleEngine.particleTexture);

		float f2 = (8 + e.ticksExisted % 8) / 16.0F;
		float f3 = f2 + 0.0625F;
		float f4 = 0.25F;
		float f5 = f4 + 0.0625F;

		float f6 = 1.0F;
		float f7 = 0.5F;
		float f8 = 0.5F;

		GL11.glColor4f(1.0F, 0.75F, 0.45F, 1.0F);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(0.4F, 0.4F, 0.4F);

		t.startDrawingQuads();

		t.setColorRGBA_F(1.0F, 0.75F, 0.45F, 1.0F);
		t.setNormal(0.0F, 1.0F, 0.0F);
		t.setBrightness(240);

		t.addVertexWithUV(-f7, -f8, 0.0D, f2, f5);
		t.addVertexWithUV(f6 - f7, -f8, 0.0D, f3, f5);
		t.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0D, f3, f4);
		t.addVertexWithUV(-f7, 1.0F - f8, 0.0D, f2, f4);

		t.draw();

		GL11.glPopMatrix();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glBlendFunc(770, 771);
		GL11.glDisable(3042);
		GL11.glDisable(32826);

		GL11.glDepthMask(true);
		GL11.glPopMatrix();
	}

	public void doRender (Entity e, double x, double y, double z, float f, float pT)
	{
		renderEntityAt((EntityGlowpet) e, x, y, z, f, pT);
	}

	protected ResourceLocation getEntityTexture (Entity e)
	{
		return TextureMap.locationBlocksTexture;
	}
}
