package taintedmagic.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
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
		
		ItemStack s = new ItemStack(ConfigItems.itemWandCasting);
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		wand.setCap(s, ItemRegistry.WAND_CAP_SHADOWMETAL);
		wand.setRod(s, ItemRegistry.STAFF_ROD_WARPWOOD);
		wand.storeAllVis(s, TaintedMagicHelper.getPrimals(wand.getMaxVis(s)));

		if (l != null) l.add(0, s);

		ItemStack s2 = s.copy();
		ItemWandCasting wand2 = (ItemWandCasting) s2.getItem();

		
		wand2.setRod(s2, ItemRegistry.WAND_ROD_WARPWOOD);
		wand2.storeAllVis(s2, TaintedMagicHelper.getPrimals(wand2.getMaxVis(s2)));

		if (l != null) l.add(1, s2);

		ItemStack s3 = s2.copy();
		ItemWandCasting wand3 = (ItemWandCasting) s3.getItem();
		
		s3.setTagInfo("sceptre", new NBTTagByte((byte) 1));
		wand3.storeAllVis(s3, TaintedMagicHelper.getPrimals(wand3.getMaxVis(s3)));

		if (l != null) l.add(2, s3);
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
