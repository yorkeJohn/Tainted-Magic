package taintedmagic.common.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import taintedmagic.common.handler.WandHandler;
import taintedmagic.common.items.ItemMagicFunguar;
import taintedmagic.common.items.ItemMaterial;
import taintedmagic.common.items.ItemVoidsentBlood;
import taintedmagic.common.items.equipment.ItemShadowFortressArmor;
import taintedmagic.common.items.equipment.ItemVoidFortressArmor;
import taintedmagic.common.items.equipment.ItemVoidmetalGoggles;
import taintedmagic.common.items.equipment.ItemVoidwalkerBoots;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;
import taintedmagic.common.items.equipment.ItemWarpedGoggles;
import taintedmagic.common.items.tools.ItemCrystalDagger;
import taintedmagic.common.items.tools.ItemKatana;
import taintedmagic.common.items.tools.ItemPrimordialEdge;
import taintedmagic.common.items.tools.ItemShadowmetalAxe;
import taintedmagic.common.items.tools.ItemShadowmetalHoe;
import taintedmagic.common.items.tools.ItemShadowmetalPick;
import taintedmagic.common.items.tools.ItemShadowmetalSpade;
import taintedmagic.common.items.tools.ItemShadowmetalSword;
import taintedmagic.common.items.tools.ItemThaumicDisassembler;
import taintedmagic.common.items.wand.ItemWandCap;
import taintedmagic.common.items.wand.ItemWandRod;
import taintedmagic.common.items.wand.foci.ItemFocusLumos;
import taintedmagic.common.items.wand.foci.ItemFocusDarkMatter;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.items.wand.foci.ItemFocusMeteorology;
import taintedmagic.common.items.wand.foci.ItemFocusTaint;
import taintedmagic.common.items.wand.foci.ItemFocusTaintedShockwave;
import taintedmagic.common.items.wand.foci.ItemFocusTime;
import taintedmagic.common.items.wand.foci.ItemFocusVisShard;
import taintedmagic.common.lib.LibToolMaterials;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandCap;
import thaumcraft.api.wands.WandRod;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegistry
{
	public static void init ()
	{
		ItemMaterial = new ItemMaterial();
		GameRegistry.registerItem(ItemMaterial, "ItemMaterial");

		ItemVoidsentBlood = new ItemVoidsentBlood();
		GameRegistry.registerItem(ItemVoidsentBlood, "ItemVoidsentBlood");

		ItemMagicFunguar = new ItemMagicFunguar(5, 1.0F, false);
		GameRegistry.registerItem(ItemMagicFunguar, "ItemMagicFunguar");

		// Armor
		ItemWarpedGoggles = new ItemWarpedGoggles(LibToolMaterials.armorMatSpecial, 4, 0);
		GameRegistry.registerItem(ItemWarpedGoggles, "ItemWarpedGoggles");

		ItemVoidFortressHelmet = new ItemVoidFortressArmor(LibToolMaterials.armorMatVoidFortress, 4, 0).setUnlocalizedName("ItemVoidFortressHelmet");
		GameRegistry.registerItem(ItemVoidFortressHelmet, "ItemVoidFortressHelmet");

		ItemVoidFortressChestplate = new ItemVoidFortressArmor(LibToolMaterials.armorMatVoidFortress, 4, 1).setUnlocalizedName("ItemVoidFortressChestplate");
		GameRegistry.registerItem(ItemVoidFortressChestplate, "ItemVoidFortressChestplate");

		ItemVoidFortressLeggings = new ItemVoidFortressArmor(LibToolMaterials.armorMatVoidFortress, 4, 2).setUnlocalizedName("ItemVoidFortressLeggings");
		GameRegistry.registerItem(ItemVoidFortressLeggings, "ItemVoidFortressLeggings");

		ItemVoidwalkerBoots = new ItemVoidwalkerBoots(LibToolMaterials.armorMatSpecial, 4, 3).setUnlocalizedName("ItemVoidwalkerBoots");
		GameRegistry.registerItem(ItemVoidwalkerBoots, "ItemVoidwalkerBoots");

		ItemVoidwalkerSash = new ItemVoidwalkerSash();
		GameRegistry.registerItem(ItemVoidwalkerSash, "ItemVoidwalkerSash");

		ItemShadowFortressHelmet = new ItemShadowFortressArmor(LibToolMaterials.armorMatShadowFortress, 4, 0).setUnlocalizedName("ItemShadowFortressHelmet");
		GameRegistry.registerItem(ItemShadowFortressHelmet, "ItemShadowFortressHelmet");

		ItemShadowFortressChestplate = new ItemShadowFortressArmor(LibToolMaterials.armorMatShadowFortress, 4, 1).setUnlocalizedName("ItemShadowFortressChestplate");
		GameRegistry.registerItem(ItemShadowFortressChestplate, "ItemShadowFortressChestplate");

		ItemShadowFortressLeggings = new ItemShadowFortressArmor(LibToolMaterials.armorMatShadowFortress, 4, 2).setUnlocalizedName("ItemShadowFortressLeggings");
		GameRegistry.registerItem(ItemShadowFortressLeggings, "ItemShadowFortressLeggings");

		ItemVoidmetalGoggles = new ItemVoidmetalGoggles(ArmorMaterial.IRON, 4, 0);
		GameRegistry.registerItem(ItemVoidmetalGoggles, "ItemVoidmetalGoggles");

		// Wands and Staves
		ItemWandRod = new ItemWandRod();
		GameRegistry.registerItem(ItemWandRod, "ItemWandRod");

		WAND_ROD_WARPWOOD = new WandRod("warpwood", 250, new ItemStack(ItemWandRod, 1, 0), 16, new WandHandler(), new ResourceLocation("taintedmagic:textures/models/ModelWAND_ROD_WARPWOOD.png"));
		WAND_ROD_WARPWOOD.setGlowing(true);

		STAFF_ROD_WARPWOOD = new StaffRod("warpwood", 500, new ItemStack(ItemWandRod, 1, 1), 20, new WandHandler(), new ResourceLocation("taintedmagic:textures/models/ModelWAND_ROD_WARPWOOD.png"));
		STAFF_ROD_WARPWOOD.setRunes(true);
		STAFF_ROD_WARPWOOD.setGlowing(true);

		ItemWandCap = new ItemWandCap();
		GameRegistry.registerItem(ItemWandCap, "ItemWandCap");

		WAND_CAP_SHADOWMETAL = new WandCap("shadowmetal", 0.70F, new ItemStack(ItemWandCap, 1, 0), 12);
		WAND_CAP_SHADOWMETAL.setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_SHADOWMETAL.png"));

		WAND_CAP_CLOTH = new WandCap("cloth", 0.95F, new ItemStack(ItemWandCap, 1, 1), 3);
		WAND_CAP_CLOTH.setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_CLOTH.png"));

		WAND_CAP_CRIMSON_CLOTH = new WandCap("crimsoncloth", 0.85F, new ItemStack(ItemWandCap, 1, 2), 5);
		WAND_CAP_CRIMSON_CLOTH.setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_CRIMSON_CLOTH.png"));

		WAND_CAP_SHADOW_CLOTH = new WandCap("shadowcloth", 0.90F, new ItemStack(ItemWandCap, 1, 3), 3);
		WAND_CAP_SHADOW_CLOTH.setTexture(new ResourceLocation("taintedmagic:textures/models/ModelWAND_CAP_SHADOW_CLOTH.png"));

		// Foci
		ItemFocusTaint = new ItemFocusTaint();
		GameRegistry.registerItem(ItemFocusTaint, "ItemFocusTaint");

		ItemFocusDarkMatter = new ItemFocusDarkMatter();
		GameRegistry.registerItem(ItemFocusDarkMatter, "ItemFocusDarkMatter");

		ItemFocusMeteorology = new ItemFocusMeteorology();
		GameRegistry.registerItem(ItemFocusMeteorology, "ItemFocusMeteorology");

		ItemFocusTime = new ItemFocusTime();
		GameRegistry.registerItem(ItemFocusTime, "ItemFocusTime");

		ItemFocusMageMace = new ItemFocusMageMace();
		GameRegistry.registerItem(ItemFocusMageMace, "ItemFocusMageMace");

		ItemFocusTaintedShockwave = new ItemFocusTaintedShockwave();
		GameRegistry.registerItem(ItemFocusTaintedShockwave, "ItemFocusTaintedShockwave");

		ItemFocusVisShard = new ItemFocusVisShard();
		GameRegistry.registerItem(ItemFocusVisShard, "ItemFocusVisShard");

		ItemFocusLumos = new ItemFocusLumos();
		GameRegistry.registerItem(ItemFocusLumos, "ItemFocusLumos");

		// Tools
		ItemThaumicDisassembler = new ItemThaumicDisassembler();
		GameRegistry.registerItem(ItemThaumicDisassembler, "ItemThaumicDisassembler");

		ItemCrystalDagger = new ItemCrystalDagger(LibToolMaterials.toolMatCrystal);
		GameRegistry.registerItem(ItemCrystalDagger, "ItemCrystalDagger");

		ItemShadowmetalHoe = new ItemShadowmetalHoe(LibToolMaterials.toolMatShadow);
		GameRegistry.registerItem(ItemShadowmetalHoe, "ItemShadowmetalHoe");

		ItemShadowmetalPick = new ItemShadowmetalPick(LibToolMaterials.toolMatShadow);
		GameRegistry.registerItem(ItemShadowmetalPick, "ItemShadowmetalPick");

		ItemShadowmetalAxe = new ItemShadowmetalAxe(LibToolMaterials.toolMatShadow);
		GameRegistry.registerItem(ItemShadowmetalAxe, "ItemShadowmetalAxe");

		ItemShadowmetalSpade = new ItemShadowmetalSpade(LibToolMaterials.toolMatShadow);
		GameRegistry.registerItem(ItemShadowmetalSpade, "ItemShadowmetalSpade");

		ItemShadowmetalSword = new ItemShadowmetalSword(LibToolMaterials.toolMatShadow);
		GameRegistry.registerItem(ItemShadowmetalSword, "ItemShadowmetalSword");

		ItemPrimordialEdge = new ItemPrimordialEdge(LibToolMaterials.toolMatPrimal);
		GameRegistry.registerItem(ItemPrimordialEdge, "ItemPrimordialEdge");

		ItemKatana = new ItemKatana();
		GameRegistry.registerItem(ItemKatana, "ItemKatana");
	}

	public static Item ItemMagicFunguar;
	public static Item ItemMaterial;
	public static Item ItemVoidsentBlood;

	// Wand
	public static Item ItemWandRod;

	public static WandRod WAND_ROD_WARPWOOD;
	public static StaffRod STAFF_ROD_WARPWOOD;

	public static Item ItemWandCap;

	public static WandCap WAND_CAP_SHADOWMETAL;
	public static WandCap WAND_CAP_CLOTH;
	public static WandCap WAND_CAP_CRIMSON_CLOTH;
	public static WandCap WAND_CAP_SHADOW_CLOTH;

	// Foci
	public static Item ItemFocusTaint;
	public static Item ItemFocusDarkMatter;
	public static Item ItemFocusMeteorology;
	public static Item ItemFocusTime;
	public static Item ItemFocusMageMace;
	public static Item ItemFocusTaintedShockwave;
	public static Item ItemFocusVisShard;
	public static Item ItemFocusLumos;

	// Armor and Baubles
	public static Item ItemWarpedGoggles;
	public static Item ItemVoidmetalGoggles;

	public static Item ItemVoidwalkerBoots;
	public static Item ItemVoidwalkerSash;

	public static Item ItemVoidFortressHelmet;
	public static Item ItemVoidFortressChestplate;
	public static Item ItemVoidFortressLeggings;

	public static Item ItemShadowFortressHelmet;
	public static Item ItemShadowFortressChestplate;
	public static Item ItemShadowFortressLeggings;

	// Tools
	public static Item ItemThaumicDisassembler;
	public static Item ItemCrystalDagger;

	public static ItemHoe ItemShadowmetalHoe;
	public static ItemPickaxe ItemShadowmetalPick;
	public static ItemSpade ItemShadowmetalSpade;
	public static ItemAxe ItemShadowmetalAxe;
	public static ItemSword ItemShadowmetalSword;

	public static ItemSword ItemPrimordialEdge;

	public static Item ItemKatana;
}
