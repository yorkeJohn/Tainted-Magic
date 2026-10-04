package taintedmagic.common;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import taintedmagic.Tags;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TMCreativeTab extends CreativeTabs {

    public TMCreativeTab() {
        super(Tags.MOD_ID);
    }

    @Override
    public void displayAllReleventItems(final List list) {
        super.displayAllReleventItems(list);

        list.add(0, createWand(ItemRegistry.STAFF_ROD_WARPWOOD, false));
        list.add(1, createWand(ItemRegistry.WAND_ROD_WARPWOOD, false));
        list.add(2, createWand(ItemRegistry.WAND_ROD_WARPWOOD, true));
    }

    /**
     * A fully charged Shadow Metal capped wand, staff or sceptre.
     */
    private static ItemStack createWand(final WandRod rod, final boolean sceptre) {
        final ItemStack stack = new ItemStack(ConfigItems.itemWandCasting);
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        wand.setCap(stack, ItemRegistry.WAND_CAP_SHADOWMETAL);
        wand.setRod(stack, rod);
        if (sceptre) {
            stack.setTagInfo("sceptre", new NBTTagByte((byte) 1));
        }
        wand.storeAllVis(stack, TaintedMagicHelper.getPrimals(wand.getMaxVis(stack)));

        return stack;
    }

    @Override
    public ItemStack getIconItemStack() {
        final ItemStack stack = new ItemStack(ConfigItems.itemWandCasting);
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        wand.setCap(stack, ItemRegistry.WAND_CAP_SHADOWMETAL);
        wand.setRod(stack, ItemRegistry.WAND_ROD_WARPWOOD);

        return stack;
    }

    @Override
    public Item getTabIconItem() {
        return null;
    }
}
