package taintedmagic.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * katana - wiiv Created using Tabula 4.1.1
 */
public class ModelSaya extends ModelBase
{
	public ModelRenderer saya;

	public ModelSaya ()
	{
		this.textureWidth = 32;
		this.textureHeight = 64;
		
		this.saya = new ModelRenderer(this, 10, 0);
		this.saya.setRotationPoint(0.0F, -40.0F, 0.0F);
		this.saya.addBox(-1.0F, 0.5F, -2.0F, 2, 48, 4, 0.0F);
	}

	public void render (float size)
	{
		this.saya.render(size);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotation (ModelRenderer m, float x, float y, float z)
	{
		m.rotateAngleX = x;
		m.rotateAngleY = y;
		m.rotateAngleZ = z;
	}
}
