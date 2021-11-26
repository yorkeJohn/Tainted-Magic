package taintedmagic.common.research;

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

            if (page.type != ResearchPage.PageType.INFUSION_CRAFTING) {
                continue;
            }

            if (parentsHidden == null || parentsHidden.length == 0) {
                parentsHidden = new String[]{ "INFUSION" };
            }
            else {
                final String[] newParents = new String[parentsHidden.length + 1];
                newParents[0] = "INFUSION";

                for (int i = 0; i < parentsHidden.length; i++) {
                    newParents[i + 1] = parentsHidden[i];
                }

                parentsHidden = newParents;
            }
        }
        return super.setPages(pages);
    }

    @Override
    public ResearchItem registerResearchItem () {
        super.registerResearchItem();
        if (warp > 0) {
            ThaumcraftApi.addWarpToResearch(key, warp);
        }
        return this;
    }
}