package taintedmagic.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL21;

import taintedmagic.client.helper.TaintedMagicClientHelper;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.tools.ItemKatana;
import thaumcraft.client.lib.UtilsFX;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly (Side.CLIENT)
public class RenderItemKatana implements IItemRenderer
{
	@Override
	public boolean handleRenderType (ItemStack s, ItemRenderType t)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper (ItemRenderType t, ItemStack s, ItemRendererHelper h)
	{
		return true;
	}

	@Override
	public void renderItem (ItemRenderType t, ItemStack s, Object... data)
	{
		EntityPlayer p = TaintedMagic.proxy.getClientPlayer();

		GL11.glPushMatrix();

		GL11.glDisable(GL11.GL_CULL_FACE);

		GL11.glScalef(1.4F, 1.4F, 1.4F);

		if (p.getHeldItem() == null || ! (p.getHeldItem() == s))
		{
			GL11.glPushMatrix();

			if (t == ItemRenderType.ENTITY)
			{
				GL11.glScalef(0.5F, 0.5F, 0.5F);
				GL11.glTranslatef(0F, 0.5F, 0F);
				GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
			}
			else if (t == ItemRenderType.INVENTORY)
			{
				GL11.glScalef(0.4F, 0.4F, 0.4F);
				GL11.glRotatef(-65, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(-50, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(1F, 1F, 0.0F);
			}
			else
			{
				GL11.glTranslatef(0.0F, -0.7F, 0.0F);
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(ItemKatana.getTexture(s));
			ItemKatana.saya.render(0.0625F);
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();

		if (t == ItemRenderType.ENTITY)
		{
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(0F, 0.5F, 0F);
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		}

		else if (t == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			if (p.isUsingItem())
			{
				GL11.glTranslatef(5.0F, 1.0F, -0.2F);
				GL11.glRotatef(155.0F, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(-20.0F, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.25F, 0.0F);
			}
			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glTranslatef(2.0F, 1.85F, -0.25F);
			GL11.glRotatef(-10F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-10F, 0.0F, 0.0F, 1.0F);
		}
		else if (t == ItemRenderType.INVENTORY)
		{
			GL11.glScalef(0.4F, 0.4F, 0.4F);
			GL11.glRotatef(-65, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-50, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(1F, 1F, 0.0F);
		}
		else
		{
			GL11.glRotatef(60, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(-40, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(70, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(0.0F, 2F, 0.65F);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(ItemKatana.getTexture(s));

		ItemKatana.katana.render(0.0625F);

		// Runes
		if (ItemKatana.hasAnyInscription(s))
		{
			GL11.glPushMatrix();
			int j = 200;
			int k = j % 65536;
			int l = j / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0F, l / 1.0F);

			GL11.glEnable(GL11.GL_BLEND);

			GL11.glBlendFunc(770, 1);
			GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
			for (int a = 0; a < 14; a++)
			{
				int rune = (a * 3) % 16;
				TaintedMagicClientHelper.drawRune(-1.65D + a * 0.14D, 0D, -0.03D, rune, p);
			}
			GL11.glRotated(180.0D, 0.0D, 1.0D, 0.0D);
			for (int a = 0; a < 14; a++)
			{
				int rune = (a + 1 * 3) % 16;
				TaintedMagicClientHelper.drawRune(-1.65D + a * 0.14D, 0D, -0.03D, rune, p);
			}
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(GL11.GL_BLEND);

			GL11.glPopMatrix();
		}

		GL11.glPopMatrix();

		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_CULL_FACE);

		GL11.glPopMatrix();
	}
}
