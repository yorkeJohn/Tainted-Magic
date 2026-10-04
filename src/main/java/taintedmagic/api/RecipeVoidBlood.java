package taintedmagic.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import taintedmagic.common.items.ItemVoidBlood;

/**
 * Custom recipe for aplpying Voidsent Blood to armour.
 */
public class RecipeVoidBlood implements IRecipe {

    @Override
    public boolean matches(final InventoryCrafting inv, final World world) {
        boolean foundBlood = false;
        boolean foundArmour = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            final ItemStack stack = inv.getStackInSlot(i);

            if (stack != null) {
                if (stack.getItem() instanceof ItemVoidBlood && !foundBlood) {
                    foundBlood = true;
                } else if (stack.getItem() instanceof ItemArmor && !foundArmour) {
                    foundArmour = true;
                } else return false;
            }
        }
        return foundBlood && foundArmour;
    }

    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack copy = null;
        ItemStack armor = null;
        ItemStack blood = null;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemArmor) {
                armor = inv.getStackInSlot(i);
            } else if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemVoidBlood) {
                blood = inv.getStackInSlot(i);
            }
        }
        if (armor != null
                && blood != null
                && (armor.getTagCompound() == null
                        || armor.getTagCompound() != null
                                && !armor.getTagCompound().getBoolean("voidtouched"))) {
            copy = armor.copy();
            if (copy.getTagCompound() == null) {
                copy.setTagCompound(new NBTTagCompound());
            }
            if (copy.getTagCompound() != null) {
                copy.getTagCompound().setBoolean("voidtouched", true);
            }
        }
        return copy;
    }

    @Override
    public int getRecipeSize() {
        return 10;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
