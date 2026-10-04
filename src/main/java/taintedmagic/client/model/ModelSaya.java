package taintedmagic.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Saya model - created by <wiiv> using Tabula 4.1.1
 */
public class ModelSaya extends ModelBase {
    public ModelRenderer saya;

    public ModelSaya() {
        textureWidth = 32;
        textureHeight = 64;

        saya = new ModelRenderer(this, 10, 0);
        saya.setRotationPoint(0.0F, -40.0F, 0.0F);
        saya.addBox(-1.0F, 0.5F, -2.0F, 2, 48, 4, 0.0F);
    }

    public void render(final float size) {
        saya.render(size);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotation(final ModelRenderer m, final float x, final float y, final float z) {
        m.rotateAngleX = x;
        m.rotateAngleY = y;
        m.rotateAngleZ = z;
    }
}
