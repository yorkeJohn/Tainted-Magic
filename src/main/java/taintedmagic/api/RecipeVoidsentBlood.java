package taintedmagic.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import taintedmagic.common.items.ItemVoidsentBlood;
import thaumcraft.common.items.armor.ItemCultistLeaderArmor;
import thaumcraft.common.items.armor.ItemCultistPlateArmor;
import thaumcraft.common.items.armor.ItemCultistRobeArmor;

/**
 * Custom recipe for aplpying Voidsent Blood to Cult Attire
 */
public class RecipeVoidsentBlood implements IRecipe
{
	@Override
	public boolean matches (InventoryCrafting inv, World w)
	{
		boolean foundBlood = false;
		boolean foundArmor = false;

		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack s = inv.getStackInSlot(i);

			if (s != null)
			{
				if (s.getItem() instanceof ItemVoidsentBlood && !foundBlood) foundBlood = true;
				else if ( (s.getItem() instanceof ItemCultistRobeArmor || s.getItem() instanceof ItemCultistPlateArmor || s.getItem() instanceof ItemCultistLeaderArmor) && !foundArmor) foundArmor = true;

				else return false;
			}
		}
		return foundBlood && foundArmor;
	}

	@Override
	public ItemStack getCraftingResult (InventoryCrafting inv)
	{
		ItemStack copy = null;
		ItemStack armor = null;
		ItemStack blood = null;

		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			if (inv.getStackInSlot(i) != null && (inv.getStackInSlot(i).getItem() instanceof ItemCultistRobeArmor || inv.getStackInSlot(i).getItem() instanceof ItemCultistPlateArmor || inv.getStackInSlot(i).getItem() instanceof ItemCultistLeaderArmor)) armor = inv.getStackInSlot(i);
			else if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemVoidsentBlood) blood = inv.getStackInSlot(i);
		}
		if (armor != null && blood != null && (armor.getTagCompound() == null || (armor.getTagCompound() != null && !armor.getTagCompound().getBoolean("voidtouched"))))
		{
			copy = armor.copy();
			if (copy.getTagCompound() == null) copy.setTagCompound(new NBTTagCompound());
			if (copy.getTagCompound() != null) copy.getTagCompound().setBoolean("voidtouched", true);
		}
		return copy;
	}

	@Override
	public int getRecipeSize ()
	{
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput ()
	{
		return null;
	}
}
