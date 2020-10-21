package taintedmagic.common.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ResearchRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class TMResearchItem extends ResearchItem
{
	int warp = 0;

	public TMResearchItem (String key, String category)
	{
		super(key, category);
	}

	public TMResearchItem (String key, AspectList tags, int col, int row, ItemStack icon, int complex, int warp)
	{
		super(key, ResearchRegistry.categoryTM, tags, col, row, complex, icon);
		this.warp = warp;
	}

	public TMResearchItem (String key, AspectList tags, int col, int row, ResourceLocation icon, int complex, int warp)
	{
		super(key, ResearchRegistry.categoryTM, tags, col, row, complex, icon);
		this.warp = warp;
	}

	@SideOnly (Side.CLIENT)
	public String getName ()
	{
		return StatCollector.translateToLocal("tm.name." + this.key);
	}

	@SideOnly (Side.CLIENT)
	public String getText ()
	{
		return (TaintedMagic.configHandler.RESEARCH_TAGS ? "[TM] " : "") + StatCollector.translateToLocal(new StringBuilder("tm.tag.").append(this.key).toString());
	}

	@Override
	public ResearchItem setPages (ResearchPage... par)
	{
		for (ResearchPage page : par)
		{
			if (page.type == ResearchPage.PageType.TEXT)
			{
				page.text = ("tm.text." + this.key + "." + page.text);
			}
			if (page.type != ResearchPage.PageType.INFUSION_CRAFTING) continue;
			if ( (this.parentsHidden == null) || (this.parentsHidden.length == 0))
			{
				this.parentsHidden = new String[]{ "INFUSION" };
			}
			else
			{
				String[] newParents = new String[this.parentsHidden.length + 1];
				newParents[0] = "INFUSION";
				for (int i = 0; i < this.parentsHidden.length; i++)
					newParents[ (i + 1)] = this.parentsHidden[i];
				this.parentsHidden = newParents;
			}
		}
		return super.setPages(par);
	}

	@Override
	public ResearchItem registerResearchItem ()
	{
		super.registerResearchItem();
		if (warp > 0) ThaumcraftApi.addWarpToResearch(key, warp);
		return this;
	}
}