package taintedmagic.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

import taintedmagic.client.model.ModelKatana;
import taintedmagic.client.model.ModelSaya;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.tools.ItemKatana;
import thaumcraft.common.config.ConfigItems;
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

		GL11.glPushMatrix();

		if (t == ItemRenderType.ENTITY)
		{
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			GL11.glTranslatef(0F, 0.5F, 0F);
			GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
		}

		else if (t == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glScalef(2F, 2F, 2F);
			GL11.glTranslatef(2.25F, 1.75F, -0.2F);
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
		GL11.glPopMatrix();

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
		GL11.glScalef(1.0F, 1.0F, 1.0F);

		GL11.glEnable(GL11.GL_CULL_FACE);

		GL11.glPopMatrix();
	}
}
