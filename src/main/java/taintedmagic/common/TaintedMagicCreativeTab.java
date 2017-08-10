package taintedmagic.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.lib.LibInfo;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TaintedMagicCreativeTab extends CreativeTabs
{
	public List list = new ArrayList();

	public TaintedMagicCreativeTab ()
	{
		super(LibInfo.MODID);
	}

	@Override
	public void displayAllReleventItems (List l)
	{
		super.displayAllReleventItems(l);
		ItemStack wand = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wandCasting = (ItemWandCasting) wand.getItem();

		wandCasting.setCap(wand, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wandCasting.setRod(wand, ItemRegistry.STAFF_ROD_WARPWOOD);
		wandCasting.storeAllVis(wand, TaintedMagicHelper.getPrimals(50000));

		if (l != null) l.add(0, wand);

		wand = new ItemStack(ConfigItems.itemWandCasting);
		wandCasting = (ItemWandCasting) wand.getItem();

		wandCasting.setCap(wand, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wandCasting.setRod(wand, ItemRegistry.WAND_ROD_WARPWOOD);
		wandCasting.storeAllVis(wand, TaintedMagicHelper.getPrimals(25000));

		if (l != null) l.add(1, wand);
	}

	@Override
	public ItemStack getIconItemStack ()
	{
		ItemStack wand = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wandCasting = (ItemWandCasting) wand.getItem();

		wandCasting.setCap(wand, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wandCasting.setRod(wand, ItemRegistry.WAND_ROD_WARPWOOD);

		return wand;
	}

	@Override
	public Item getTabIconItem ()
	{
		return null;
	}
}
