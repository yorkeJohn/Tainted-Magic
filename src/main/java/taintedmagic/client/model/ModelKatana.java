package taintedmagic.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * katana - wiiv Created using Tabula 4.1.1
 */
public class ModelKatana extends ModelBase
{
	public ModelRenderer blade;
	public ModelRenderer grip1;
	public ModelRenderer grip2;

	public ModelKatana ()
	{
		this.textureWidth = 32;
		this.textureHeight = 64;
		this.grip2 = new ModelRenderer(this, 22, 0);
		this.grip2.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.grip2.addBox(-1.0F, -12.0F, -1.5F, 2, 12, 3, 0.0F);
		this.blade = new ModelRenderer(this, 0, 0);
		this.blade.setRotationPoint(0.0F, -40.0F, 0.0F);
		this.blade.addBox(-0.5F, 0.0F, -2.0F, 1, 48, 4, -0.75F);
		this.grip1 = new ModelRenderer(this, 0, 52);
		this.grip1.setRotationPoint(0.0F, -40.0F, 0.0F);
		this.grip1.addBox(-2.5F, 0.0F, -3.5F, 5, 1, 7, 0.0F);
		this.grip1.addChild(this.grip2);
	}

	public void render (float size)
	{
		this.blade.render(size);
		this.grip1.render(size);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	private void setRotation (ModelRenderer m, float x, float y, float z)
	{
		m.rotateAngleX = x;
		m.rotateAngleY = y;
		m.rotateAngleZ = z;
	}
}
