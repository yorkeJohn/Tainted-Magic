package taintedmagic.common.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import taintedmagic.common.registry.ResearchRegistry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

/**
 * This class was created by <flaxbeard> as part of Thaumic Exploration.
 * It is being distributed as part of Tainted Magic.
 */
public class ThaumcraftResearchItem extends ResearchItem {

    private final ResearchItem original;

    public ThaumcraftResearchItem(
            final String tag,
            final String origin,
            final String originCategory,
            final int col,
            final int row,
            final ResourceLocation icon) {
        super(tag, ResearchRegistry.CATEGORY_TM, new AspectList(), col, row, 1, icon);
        original = ResearchCategories.researchCategories
                .get(originCategory)
                .research
                .get(origin);
        bindToOriginal();
        setStub();
        setHidden();
    }

    public ThaumcraftResearchItem(
            final String tag,
            final String origin,
            final String originCategory,
            final int col,
            final int row,
            final ItemStack icon) {
        super(tag, ResearchRegistry.CATEGORY_TM, new AspectList(), col, row, 1, icon);
        original = ResearchCategories.researchCategories
                .get(originCategory)
                .research
                .get(origin);
        bindToOriginal();
        setStub();
        setHidden();
    }

    private void bindToOriginal() {
        if (original.siblings == null) {
            original.setSiblings(key);
        } else {
            final String[] family = original.siblings;
            final String[] newFamily = new String[family.length + 1];
            for (int x = 0; x < family.length; x++) {
                newFamily[x] = family[x];
            }
            newFamily[family.length] = key;
            original.setSiblings(newFamily);
        }
        if (original.isSecondary()) {
            setSecondary();
        }
    }

    @Override
    public ResearchPage[] getPages() {
        return original.getPages();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getName() {
        return original.getName();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getText() {
        return original.getText();
    }

    @Override
    public boolean isStub() {
        return true;
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public int getComplexity() {
        return 1;
    }
}
