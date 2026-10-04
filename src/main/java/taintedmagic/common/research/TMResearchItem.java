package taintedmagic.common.research;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.registry.ResearchRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TMResearchItem extends ResearchItem {

    private int warp = 0;

    public TMResearchItem (final String key, final String category) {
        super(key, category);
    }

    public TMResearchItem (final String key, final AspectList tags, final int col, final int row, final ItemStack icon,
            final int complexity, final int warp) {
        super(key, ResearchRegistry.CATEGORY_TM, tags, col, row, complexity, icon);
        this.warp = warp;
    }

    public TMResearchItem (final String key, final AspectList tags, final int col, final int row, final ResourceLocation icon,
            final int complexity, final int warp) {
        super(key, ResearchRegistry.CATEGORY_TM, tags, col, row, complexity, icon);
        this.warp = warp;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public String getName () {
        return StatCollector.translateToLocal("tm.name." + key);
    }

    @Override
    @SideOnly (Side.CLIENT)
    public String getText () {
        return (ConfigHandler.RESEARCH_TAGS ? "[TM] " : "")
                + StatCollector.translateToLocal(new StringBuilder("tm.tag.").append(key).toString());
    }

    @Override
    public ResearchItem setPages (final ResearchPage... pages) {
        for (final ResearchPage page : pages) {
            if (page.type == ResearchPage.PageType.TEXT) {
                page.text = "tm.text." + key + "." + page.text;
            }
        }
        return super.setPages(pages);
    }

    @Override
    public ResearchItem registerResearchItem () {
        resolveInfusionParent();
        super.registerResearchItem();
        if (warp > 0) {
            ThaumcraftApi.addWarpToResearch(key, warp);
        }
        return this;
    }

    /**
     * Research with an infusion page requires Infusion. Done at registration rather than in setPages so that a later
     * setParentsHidden call can't overwrite it.
     */
    private void resolveInfusionParent () {
        if (Arrays.stream(getPages()).noneMatch(page -> page.type == ResearchPage.PageType.INFUSION_CRAFTING)) {
            return;
        }

        final List<String> parents = new ArrayList<>(
                Optional.ofNullable(parentsHidden)
                        .map(Arrays::asList)
                        .orElse(Collections.emptyList())
        );
        if (!parents.contains("INFUSION")) {
            parents.add("INFUSION");
            parentsHidden = parents.toArray(new String[0]);
        }
    }
}
