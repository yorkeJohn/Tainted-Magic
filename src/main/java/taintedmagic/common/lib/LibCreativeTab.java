package taintedmagic.common.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class LibCreativeTab extends CreativeTabs
{
	public List list = new ArrayList();

	public LibCreativeTab ()
	{
		super(LibStrings.MODID);
	}

	@Override
	public void displayAllReleventItems (List l)
	{
		super.displayAllReleventItems(l);
		ItemStack wand = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wand2 = (ItemWandCasting) wand.getItem();

		wand2.setCap(wand, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wand2.setRod(wand, ItemRegistry.STAFF_ROD_WARPWOOD);
		wand2.storeAllVis(wand, TaintedMagicHelper.getPrimals(50000));

		if (l != null) l.add(0, wand);

		ItemStack wand3 = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wand4 = (ItemWandCasting) wand.getItem();

		wand4.setCap(wand3, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wand4.setRod(wand3, ItemRegistry.WAND_ROD_WARPWOOD);
		wand4.storeAllVis(wand3, TaintedMagicHelper.getPrimals(25000));

		if (l != null) l.add(1, wand3);
	}

	@Override
	public ItemStack getIconItemStack ()
	{
		ItemStack wand = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wand2 = (ItemWandCasting) wand.getItem();

		wand2.setCap(wand, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wand2.setRod(wand, ItemRegistry.WAND_ROD_WARPWOOD);

		return wand;
	}

	@Override
	public Item getTabIconItem ()
	{
		return null;
	}
}
