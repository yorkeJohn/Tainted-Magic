package taintedmagic.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import taintedmagic.api.RecipeVoidsentBlood;
import taintedmagic.common.TaintedMagic;
import thaumcraft.common.config.ConfigItems;

public class ItemVoidsentBlood extends Item
{
	public ItemVoidsentBlood ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemVoidsentBlood");
		this.setUnlocalizedName("ItemVoidsentBlood");
		this.setContainerItem(ConfigItems.itemEssence);

		GameRegistry.addRecipe(new RecipeVoidsentBlood());
		RecipeSorter.register("taintedmagic:ItemVoidsentBlood", RecipeVoidsentBlood.class, Category.SHAPELESS, "");
	}
	
	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.rare;
	}
}
