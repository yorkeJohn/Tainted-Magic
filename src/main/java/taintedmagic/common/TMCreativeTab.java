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

public class TMCreativeTab extends CreativeTabs {

    public TMCreativeTab () {
        super(LibInfo.MODID);
    }

    @Override
    public void displayAllReleventItems (final List list) {
        super.displayAllReleventItems(list);

        final ItemStack stack = new ItemStack(ConfigItems.itemWandCasting);
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        wand.setCap(stack, ItemRegistry.WAND_CAP_SHADOWMETAL);
        wand.setRod(stack, ItemRegistry.STAFF_ROD_WARPWOOD);
        wand.storeAllVis(stack, TaintedMagicHelper.getPrimals(wand.getMaxVis(stack)));

        if (list != null) {
            list.add(0, stack);
        }

        final ItemStack stack2 = stack.copy();
        final ItemWandCasting wand2 = (ItemWandCasting) stack2.getItem();

        wand2.setRod(stack2, ItemRegistry.WAND_ROD_WARPWOOD);
        wand2.storeAllVis(stack2, TaintedMagicHelper.getPrimals(wand2.getMaxVis(stack2)));

        if (list != null) {
            list.add(1, stack2);
        }

        final ItemStack stack3 = stack2.copy();
        final ItemWandCasting wand3 = (ItemWandCasting) stack3.getItem();

        stack3.setTagInfo("sceptre", new NBTTagByte((byte) 1));
        wand3.storeAllVis(stack3, TaintedMagicHelper.getPrimals(wand3.getMaxVis(stack3)));

        if (list != null) {
            list.add(2, stack3);
        }
    }

    @Override
    public ItemStack getIconItemStack () {
        final ItemStack stack = new ItemStack(ConfigItems.itemWandCasting);
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        wand.setCap(stack, ItemRegistry.WAND_CAP_SHADOWMETAL);
        wand.setRod(stack, ItemRegistry.WAND_ROD_WARPWOOD);

        return stack;
    }

    @Override
    public Item getTabIconItem () {
        return null;
    }
}
