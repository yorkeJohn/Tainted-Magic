package taintedmagic.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

/**
 * Katana model - created by <wiiv> using Tabula 4.1.1
 */
public class ModelKatana extends ModelBase {
    public ModelRenderer blade;
    public ModelRenderer grip1;
    public ModelRenderer grip2;

    public ModelKatana() {
        textureWidth = 32;
        textureHeight = 64;

        grip2 = new ModelRenderer(this, 22, 0);
        grip2.setRotationPoint(0.0F, 0.0F, 0.0F);
        grip2.addBox(-1.0F, -12.0F, -1.5F, 2, 12, 3, 0.0F);

        blade = new ModelRenderer(this, 0, 0);
        blade.setRotationPoint(0.0F, -40.0F, 0.0F);
        blade.addBox(-0.5F, 0.0F, -2.0F, 1, 48, 4, -0.75F);

        grip1 = new ModelRenderer(this, 0, 52);
        grip1.setRotationPoint(0.0F, -40.0F, 0.0F);
        grip1.addBox(-2.5F, 0.0F, -3.5F, 5, 1, 7, 0.0F);
        grip1.addChild(grip2);
    }

    public void render(final float size) {
        blade.render(size);
        grip1.render(size);
    }
}
