package taintedmagic.client.helper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import thaumcraft.client.lib.UtilsFX;

public class TaintedMagicClientHelper
{
	/**
	 * TC method to draw runes on runed staves
	 */
	public static void drawRune (double x, double y, double z, int rune, EntityPlayer player)
	{
		GL11.glPushMatrix();
		UtilsFX.bindTexture("textures/misc/script.png");
		float r = MathHelper.sin( (player.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.88F;
		float g = MathHelper.sin( (player.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.63F;
		float alpha = MathHelper.sin( (player.ticksExisted + rune * 5) / 10.0F) * 0.3F;

		GL11.glColor4f(r, g, 0.2F, alpha + 0.6F);
		GL11.glRotated(90.0D, 0.0D, 0.0D, 1.0D);
		GL11.glTranslated(x, y, z);

		Tessellator tessellator = Tessellator.instance;
		float f = 0.0625F * rune;
		float f1 = f + 0.0625F;
		float f2 = 0.0F;
		float f3 = 1.0F;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(r, g, 0.2F, alpha + 0.6F);
		tessellator.addVertexWithUV(-0.06D - alpha / 40.0F, 0.06D + alpha / 40.0F, 0.0D, f1, f3);
		tessellator.addVertexWithUV(0.06D + alpha / 40.0F, 0.06D + alpha / 40.0F, 0.0D, f1, f2);
		tessellator.addVertexWithUV(0.06D + alpha / 40.0F, -0.06D - alpha / 40.0F, 0.0D, f, f2);
		tessellator.addVertexWithUV(-0.06D - alpha / 40.0F, -0.06D - alpha / 40.0F, 0.0D, f, f3);
		tessellator.draw();

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
