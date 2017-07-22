package taintedmagic.common.registry;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesWeapons;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.research.TaintedMagicResearchItem;
import taintedmagic.common.research.ThaumcraftResearchItem;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionEnchantmentRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchPage.PageType;
import thaumcraft.api.research.*;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.ConfigItems;

public class ResearchRegistry
{
	public static final String TM = "TM";
	public static HashMap recipes = new HashMap();

	public static void initResearch ()
	{
		ResearchCategories.registerCategory(TM, new ResourceLocation("taintedmagic:textures/misc/IconThaumonomiconTab.png"), TaintedMagic.configHandler.useCustomResearchTabBackground ? new ResourceLocation("taintedmagic:textures/gui/GUIThaumonomiconTab.png") : new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));

		/**
		 * Thaumcraft research items
		 */
		new ThaumcraftResearchItem("TMELDRITCHMAJOR", TM, "ELDRITCHMAJOR", "ELDRITCH", 6, -7, new ResourceLocation("thaumcraft:textures/misc/r_eldritchmajor.png")).setSpecial().setConcealed().setRound().registerResearchItem();
		new ThaumcraftResearchItem("TMFOCUSFIRE", TM, "FOCUSFIRE", "THAUMATURGY", 7, 3, new ItemStack(ConfigItems.itemFocusFire)).setConcealed().registerResearchItem();
		new ThaumcraftResearchItem("TMARMORFORTRESS", TM, "ARMORFORTRESS", "ARTIFICE", 11, 1, new ItemStack(ConfigItems.itemHelmetFortress)).setConcealed().registerResearchItem();

		/**
		 * Tainted Magic research items
		 */
		new TaintedMagicResearchItem("TAINTEDMAGIC", TM, new AspectList(), 2, -1, 0, new ItemStack(ConfigItems.itemResource, 1, 13)).setRound().setAutoUnlock().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage("2") }).registerResearchItem();

		new TaintedMagicResearchItem("SHADOWMETAL", TM, new AspectList().add(Aspect.METAL, 1).add(Aspect.DARKNESS, 1).add(Aspect.MAGIC, 1), 0, 1, 0, new ItemStack(BlockRegistry.BlockShadowOre)).setParents("TAINTEDMAGIC").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((CrucibleRecipe) recipes.get("BlockShadowOre")), new ResearchPage(new ItemStack(BlockRegistry.BlockShadowOre)), new ResearchPage((IRecipe) recipes.get("ItemShadowmetalPick")), new ResearchPage((IRecipe) recipes.get("ItemShadowmetalSpade")), new ResearchPage((IRecipe) recipes.get("ItemShadowmetalAxe")), new ResearchPage((IRecipe) recipes.get("ItemShadowmetalHoe")), new ResearchPage((IRecipe) recipes.get("ItemShadowmetalSword")) }).registerResearchItem();

		ThaumcraftApi.addWarpToResearch("SHADOWMETAL", 2);

		new TaintedMagicResearchItem("CAP_shadowmetal", TM, new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4).add(Aspect.MAGIC, 4), -3, 2, 0, new ItemStack(ItemRegistry.ItemWandCap, 1, 0)).setParents(new String[]{ "SHADOWMETAL", "CAP_void", "PRIMPEARL" }).setComplexity(2).setParentsHidden("SHADOWCLOTH").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemWandCap:0")) }).setConcealed().registerResearchItem();

		ThaumcraftApi.addWarpToResearch("CAPSHADOWMETAL", 5);

		new TaintedMagicResearchItem("SHADOWCLOTH", TM, new AspectList().add(Aspect.CLOTH, 1).add(Aspect.DARKNESS, 1), -1, 4, 0, new ItemStack(ItemRegistry.ItemMaterial, 1, 1)).setConcealed().setSecondary().setParents(new String[]{ "ENCHFABRIC", "SHADOWMETAL" }).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemMaterial:1")) }).registerResearchItem();

		new TaintedMagicResearchItem("ROD_warpwood", TM, new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 8, 0, 0, new ItemStack(ItemRegistry.ItemWandRod, 1, 0)).setParents(new String[]{ "WARPTREE", "VOIDMETAL", "PRIMPEARL", "ROD_primal_staff" }).setParentsHidden("SHADOWMETAL").setConcealed().setComplexity(3).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemWandRod:0")), new ResearchPage("2") }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("WANDRODWARP", 3);

		new TaintedMagicResearchItem("ROD_warpwood_staff", TM, new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 10, -1, 0, new ItemStack(ItemRegistry.ItemWandRod, 1, 1)).setConcealed().setParents("ROD_warpwood").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemWandRod:1")) }).setSecondary().registerResearchItem();

		new TaintedMagicResearchItem("EVILSHARDS", TM, new AspectList().add(Aspect.CRYSTAL, 4).add(Aspect.DARKNESS, 4), 4, -1, 0, new ItemStack(ItemRegistry.ItemMaterial, 1, 4)).setParents("TAINTEDMAGIC").setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((CrucibleRecipe) recipes.get("ItemMaterial:3")), new ResearchPage((CrucibleRecipe) recipes.get("ItemMaterial:4")) }).registerResearchItem();

		new TaintedMagicResearchItem("WARPTREE", TM, new AspectList().add(Aspect.ELDRITCH, 4).add(Aspect.DARKNESS, 4).add(Aspect.TAINT, 4).add(Aspect.TREE, 4), 6, -2, 0, new ItemStack(BlockRegistry.BlockWarpwoodSapling)).setParentsHidden("SHADOWMETAL").setParents("EVILSHARDS").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("BlockWarpwoodSapling")), new ResearchPage((IRecipe) recipes.get("BlockWarpwoodPlanks")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("WARPTREE", 3);

		new TaintedMagicResearchItem("CRIMSONROBES", TM, new AspectList().add(Aspect.CLOTH, 4).add(Aspect.EXCHANGE, 4).add(Aspect.ARMOR, 4), -2, -1, 0, new ItemStack(ItemRegistry.ItemMaterial, 1, 2)).setParents("CRYSTALDAGGER").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemMaterial:2")), new ResearchPage((IArcaneRecipe) recipes.get("ItemHelmetCultistRobe")), new ResearchPage((IArcaneRecipe) recipes.get("ItemChestCultistRobe")), new ResearchPage((IArcaneRecipe) recipes.get("ItemLegsCultistRobe")), new ResearchPage((IArcaneRecipe) recipes.get("ItemBootsCultist")) }).setConcealed().registerResearchItem();

		new TaintedMagicResearchItem("VOIDFORTRESS", TM, new AspectList().add(Aspect.ARMOR, 5).add(Aspect.ELDRITCH, 3).add(Aspect.DARKNESS, 3).add(Aspect.VOID, 5), 4, -9, 0, new ItemStack(ItemRegistry.ItemVoidFortressHelmet)).setParentsHidden(new String[]{ "ARMORVOIDFORTRESS", "ARMORFORTRESS", }).setParents(new String[]{ "TMELDRITCHMAJOR" }).setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidFortressHelmet")), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidFortressChestplate")), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidFortressLeggings")) }).registerResearchItem();

		new TaintedMagicResearchItem("WARPEDGOGGLES", TM, new AspectList().add(Aspect.ARMOR, 4).add(Aspect.ELDRITCH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4), 1, 4, 0, new ItemStack(ItemRegistry.ItemWarpedGoggles)).setParentsHidden(new String[]{ "TAINTEDMAGIC", "GOGGLES", }).setConcealed().setSecondary().setParents("SHADOWMETAL").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemWarpedGoggles")) }).registerResearchItem();

		new TaintedMagicResearchItem("TAINTFOCUS", TM, new AspectList().add(Aspect.TAINT, 4).add(Aspect.WATER, 4).add(Aspect.SLIME, 4).add(Aspect.AIR, 4), 9, 4, 0, new ItemStack(ItemRegistry.ItemFocusTaint)).setParentsHidden(new String[]{ "SHADOWMETAL", "INFUSION", "BOTTLETAINT", "FOCUSFIRE", "EVILSHARDS" }).setParents(new String[]{ "TMFOCUSFIRE" }).setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusTaint")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("TAINTFOCUS", 4);

		new TaintedMagicResearchItem("ELDRITCHFOCUS", TM, new AspectList().add(Aspect.ELDRITCH, 22).add(Aspect.ENTROPY, 14).add(Aspect.AIR, 4).add(Aspect.DARKNESS, 6), 7, 5, 0, new ItemStack(ItemRegistry.ItemFocusDarkMatter)).setParentsHidden(new String[]{ "OUTERREV" }).setParents(new String[]{ "TMFOCUSFIRE", "TAINTFOCUS", "FOCUSSHARD", "FOCUSLUMOS", "MACEFOCUS", "FOCUSTAINTEDBLAST", "BLOODLUSTUPGRADE" }).setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusDarkMatter")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("ELDRITCHFOCUS", 2);

		new TaintedMagicResearchItem("KNIGHTROBES", TM, new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4), -4, -2, 0, new ItemStack(ItemRegistry.ItemMaterial, 1, 8)).setParentsHidden("ELDRITCHMINOR").setParents("CRIMSONROBES").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemMaterial:8")), new ResearchPage((IArcaneRecipe) recipes.get("ItemHelmetCultistPlate")), new ResearchPage((IArcaneRecipe) recipes.get("ItemChestCultistPlate")), new ResearchPage((IArcaneRecipe) recipes.get("ItemLegsCultistPlate")) }).registerResearchItem();

		new TaintedMagicResearchItem("PRAETORARMOR", TM, new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4).add(Aspect.ELDRITCH, 4), -6, 0, 0, ItemApi.getItem("itemHelmetCultistLeaderPlate", 0)).setParents("KNIGHTROBES").setParentsHidden(new String[]{ "CRIMSONROBES", "TMELDRITCHMAJOR", "VOIDMETAL" }).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemHelmetCultistLeaderPlate")), new ResearchPage((InfusionRecipe) recipes.get("ItemChestCultistLeaderPlate")), new ResearchPage((InfusionRecipe) recipes.get("ItemLegsCultistLeaderPlate")) }).setConcealed().registerResearchItem();

		new TaintedMagicResearchItem("VOIDSENTBLOOD", TM, new AspectList().add(Aspect.VOID, 14).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 18).add(Aspect.AURA, 4), -7, -1, 0, new ItemStack(ItemRegistry.ItemVoidsentBlood)).setSecondary().setParents("PRAETORARMOR").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((CrucibleRecipe) recipes.get("ItemVoidsentBlood")) }).registerResearchItem();

		new TaintedMagicResearchItem("VOIDWALKERBOOTS", TM, new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.ELDRITCH, 8), 4, -5, 0, new ItemStack(ItemRegistry.ItemVoidwalkerBoots)).setParentsHidden(new String[]{ "TMELDRITCHMAJOR", "BOOTSTRAVELLER", "EVILSHARDS", "SHADOWCLOTH", "ARMORVOIDFORTRESS" }).setConcealed().setParents(new String[]{ "TMELDRITCHMAJOR" }).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidwalkerBoots")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("VOIDWALKERBOOTS", 3);

		new TaintedMagicResearchItem("CREATIONSHARD", TM, TaintedMagicHelper.getPrimals(16), 7, -7, 0, new ItemStack(ItemRegistry.ItemMaterial, 1, 5)).setConcealed().setParents("TMELDRITCHMAJOR").setParentsHidden("PRIMPEARL").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemMaterial:5")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("CREATIONSHARD", 5);

		new TaintedMagicResearchItem("CREATION", TM, new AspectList().add(Aspect.MAGIC, 4), 8, -7, 0, new ResourceLocation("taintedmagic:textures/misc/IconCREATION.png")).setParents("CREATIONSHARD").setConcealed().setRound().setSpecial().setHidden().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4"), new ResearchPage("5") }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("CREATION", 5);

		new TaintedMagicResearchItem("FOCUSTIME", TM, new AspectList().add(Aspect.FIRE, 4).add(Aspect.WATER, 8).add(Aspect.EARTH, 4).add(Aspect.AIR, 4).add(Aspect.ORDER, 4).add(Aspect.ENTROPY, 4).add(Aspect.EXCHANGE, 4), 10, -9, 0, new ItemStack(ItemRegistry.ItemFocusTime)).setConcealed().setParents("CREATION").setParentsHidden("FOCUSPORTABLEHOLE", "FOCUSFIRE").setComplexity(3).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusTime")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("FOCUSTIME", 3);

		new TaintedMagicResearchItem("FOCUSWEATHER", TM, new AspectList().add(Aspect.FIRE, 4).add(Aspect.WATER, 8).add(Aspect.EARTH, 4).add(Aspect.AIR, 4).add(Aspect.ORDER, 4).add(Aspect.ENTROPY, 4).add(Aspect.WEATHER, 4), 10, -5, 0, new ItemStack(ItemRegistry.ItemFocusMeteorology)).setParentsHidden(new String[]{ "FOCUSFROST", "FOCUSSHOCK" }).setConcealed().setParents("CREATION").setComplexity(3).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusMeteorology")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("FOCUSWEATHER", 3);

		new TaintedMagicResearchItem("THAUMICDISASSEMBLER", TM, new AspectList().add(Aspect.METAL, 4).add(Aspect.WEAPON, 8).add(Aspect.TOOL, 4), 3, -7, 0, new ItemStack(ItemRegistry.ItemThaumicDisassembler)).setParentsHidden(new String[]{ "THAUMIUM", "VOIDMETAL" }).setConcealed().setParents("TMELDRITCHMAJOR").setComplexity(2).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemThaumicDisassembler")), new ResearchPage((IArcaneRecipe) recipes.get("ItemMaterial:6")), new ResearchPage("2") }).registerResearchItem();

		new TaintedMagicResearchItem("VOIDSASH", TM, new AspectList().add(Aspect.VOID, 4).add(Aspect.METAL, 8).add(Aspect.ARMOR, 4), 7, -4, 0, new ItemStack(ItemRegistry.ItemVoidwalkerSash)).setParentsHidden("PRIMPEARL").setParents("VOIDWALKERBOOTS").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidwalkerSash")) }).setConcealed().setSecondary().registerResearchItem();

		new TaintedMagicResearchItem("MAGICFUNGUAR", TM, new AspectList().add(Aspect.MAGIC, 2).add(Aspect.HUNGER, 4).add(Aspect.PLANT, 2), 3, -3, 0, new ItemStack(ItemRegistry.ItemMagicFunguar)).setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemMagicFunguar")) }).setConcealed().registerResearchItem();

		new TaintedMagicResearchItem("CRYSTALDAGGER", TM, new AspectList().add(Aspect.WEAPON, 4).add(Aspect.FIRE, 4).add(Aspect.HEAL, 4), 0, -3, 0, new ItemStack(ItemRegistry.ItemCrystalDagger, 1, 7)).setParentsHidden("ENCHFABRIC", "ESSENTIACRYSTAL", "VOIDMETAL").setParents("TAINTEDMAGIC").setComplexity(2).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemCrystalDagger")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("CRYSTALDAGGER", 2);

		new TaintedMagicResearchItem("MACEFOCUS", TM, new AspectList().add(Aspect.ENTROPY, 10).add(Aspect.EARTH, 4).add(Aspect.WEAPON, 6).add(Aspect.MAGIC, 8), 5, 4, 0, new ItemStack(ItemRegistry.ItemFocusMageMace)).setParents("TMFOCUSFIRE").setConcealed().setComplexity(2).setParentsHidden("FOCUSFIRE", "SHADOWMETAL").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusMageMace")) }).registerResearchItem();

		new TaintedMagicResearchItem("CRIMSONBLADE", TM, new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.ELDRITCH, 2).add(Aspect.WEAPON, 16).add(Aspect.VOID, 6), -7, 1, 0, ItemApi.getItem("itemSwordCrimson", 0)).setParents("PRAETORARMOR").setConcealed().setComplexity(3).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemSwordCrimson")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("CRIMSONBLADE", 3);

		new TaintedMagicResearchItem("SHADOWFORTRESSARMOR", TM, new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.VOID, 2), 0, 3, 0, new ItemStack(ItemRegistry.ItemShadowFortressHelmet)).setParents("SHADOWMETAL").setConcealed().setComplexity(3).setParentsHidden("VOIDFORTRESS", "TMELDRITCHMAJOR", "EVILSHARDS").setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemShadowFortressHelmet")), new ResearchPage((InfusionRecipe) recipes.get("ItemShadowFortressChestplate")), new ResearchPage((InfusionRecipe) recipes.get("ItemShadowFortressLeggings")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("SHADOWFORTRESSARMOR", 3);

		new TaintedMagicResearchItem("CAP_cloth", TM, new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CLOTH, 4), 3, 1, 0, new ItemStack(ItemRegistry.ItemWandCap, 1, 1)).setParentsHidden("CAP_gold", "ENCHFABRIC").setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemWandCap:1")) }).registerResearchItem();

		new TaintedMagicResearchItem("CAP_crimsoncloth", TM, new AspectList().add(Aspect.MAGIC, 8).add(Aspect.CLOTH, 4).add(Aspect.HEAL, 8), -2, 0, 0, new ItemStack(ItemRegistry.ItemWandCap, 1, 2)).setParentsHidden("CAP_cloth").setSecondary().setConcealed().setParents("CRIMSONROBES").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemWandCap:2")) }).registerResearchItem();

		new TaintedMagicResearchItem("CAP_shadowcloth", TM, new AspectList().add(Aspect.MAGIC, 16).add(Aspect.CLOTH, 8).add(Aspect.VOID, 16).add(Aspect.DARKNESS, 8), -2, 3, 0, new ItemStack(ItemRegistry.ItemWandCap, 1, 3)).setParentsHidden("CAP_cloth", "CAP_thaumium").setSecondary().setConcealed().setParents("SHADOWCLOTH").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemWandCap:3")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("CAPSHADOWCLOTH", 1);

		new TaintedMagicResearchItem("FOCUSTAINTEDBLAST", TM, new AspectList().add(Aspect.MAGIC, 20).add(Aspect.TAINT, 12).add(Aspect.AIR, 6).add(Aspect.MOTION, 12), 10, 4, 0, new ItemStack(ItemRegistry.ItemFocusTaintedShockwave)).setParentsHidden("FOCUSFIRE", "ELDRITCHMINOR", "FOCUSSHOCK").setConcealed().setParents("TAINTFOCUS").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemFocusTaintedShockwave")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("FOCUSTAINTEDBLAST", 2);

		new TaintedMagicResearchItem("PRIMALBLADE", TM, new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ELDRITCH, 1).add(Aspect.WEAPON, 1).add(Aspect.VOID, 1).add(Aspect.AURA, 1), 11, -7, 0, new ItemStack(ItemRegistry.ItemPrimordialEdge)).setParentsHidden("VOIDMETAL", "PRIMALCRUSHER", "EVILSHARDS", "CRYSTALDAGGER").setConcealed().setParents("CREATION").setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemPrimordialEdge")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("PRIMALBLADE", 4);

		new TaintedMagicResearchItem("BLOODLUSTUPGRADE", TM, new AspectList().add(Aspect.MAGIC, 4).add(Aspect.HEAL, 8).add(Aspect.WEAPON, 8), 4, 4, 0, new ResourceLocation("taintedmagic:textures/foci/IconBloodlust.png")).setParentsHidden("CRYSTALDAGGER", "FOCALMANIPULATION").setConcealed().setParents("MACEFOCUS").setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1") }).registerResearchItem();

		new TaintedMagicResearchItem("THAUMIUMKATANA", TM, new AspectList().add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 6), 13, 1, 0, new ItemStack(ItemRegistry.ItemKatana, 1, 0)).setParents("TMARMORFORTRESS").setParentsHidden("ARMORFORTRESS").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatana:0")) }).registerResearchItem();

		new TaintedMagicResearchItem("VOIDMETALKATANA", TM, new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12).add(Aspect.VOID, 12), 7, -10, 0, new ItemStack(ItemRegistry.ItemKatana, 1, 1)).setParentsHidden("THAUMIUMKATANA").setParents("VOIDFORTRESS").setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatana:1")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("VOIDMETALKATANA", 4);

		new TaintedMagicResearchItem("SHADOWMETALKATANA", TM, new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12).add(Aspect.VOID, 12).add(Aspect.DARKNESS, 14), 0, 6, 0, new ItemStack(ItemRegistry.ItemKatana, 1, 2)).setParentsHidden("VOIDMETALKATANA").setParents("SHADOWFORTRESSARMOR").setConcealed().setSecondary().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatana:2")) }).registerResearchItem();
		ThaumcraftApi.addWarpToResearch("SHADOWMETALKATANA", 6);

		new TaintedMagicResearchItem("INSCRIPTIONFIRE", TM, new AspectList().add(Aspect.FIRE, 8).add(Aspect.LIGHT, 4).add(Aspect.METAL, 6), 14, -1, 0, new ResourceLocation("taintedmagic:textures/misc/IconINSCRIPTIONFIRE.png")).setParents("THAUMIUMKATANA").setParentsHidden("FOCUSFIRE").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatanaThaumium:inscription0")) }).setSecondary().registerResearchItem();

		new TaintedMagicResearchItem("INSCRIPTIONTAINT", TM, new AspectList().add(Aspect.TAINT, 8).add(Aspect.FLESH, 4).add(Aspect.METAL, 6), 15, 1, 0, new ResourceLocation("taintedmagic:textures/misc/IconINSCRIPTIONTAINT.png")).setParents("THAUMIUMKATANA").setParentsHidden("TAINTFOCUS").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatanaThaumium:inscription1")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("INSCRIPTIONTAINT", 2);

		new TaintedMagicResearchItem("INSCRIPTIONUNDEAD", TM, new AspectList().add(Aspect.HEAL, 8).add(Aspect.UNDEAD, 4).add(Aspect.METAL, 6), 14, 3, 0, new ResourceLocation("taintedmagic:textures/misc/IconINSCRIPTIONUNDEAD.png")).setParents("THAUMIUMKATANA").setParentsHidden("BATHSALTS").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemKatanaThaumium:inscription2")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("INSCRIPTIONUNDEAD", 5);

		new TaintedMagicResearchItem("VOIDGOGGLES", TM, new AspectList().add(Aspect.VOID, 8).add(Aspect.DARKNESS, 4).add(Aspect.MAGIC, 6).add(Aspect.SENSES, 12).add(Aspect.CRYSTAL, 2), 2, 3, 0, new ItemStack(ItemRegistry.ItemVoidmetalGoggles)).setParents("WARPEDGOGGLES").setParentsHidden("VOIDMETAL").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((InfusionRecipe) recipes.get("ItemVoidmetalGoggles")) }).setSecondary().registerResearchItem();
		ThaumcraftApi.addWarpToResearch("VOIDGOGGLES", 2);

		new TaintedMagicResearchItem("FOCUSSHARD", TM, new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.MAGIC, 4).add(Aspect.MOTION, 6), 6, 6, 0, new ItemStack(ItemRegistry.ItemFocusVisShard)).setParents("TMFOCUSFIRE").setParentsHidden("FOCUSFIRE", "EVILSHARDS").setConcealed().setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemFocusVisShard")) }).setSecondary().registerResearchItem();

		new TaintedMagicResearchItem("FOCUSLUMOS", TM, new AspectList().add(Aspect.FIRE, 16).add(Aspect.LIGHT, 24).add(Aspect.ENERGY, 8), 8, 6, 0, new ItemStack(ItemRegistry.ItemFocusLumos)).setPages(new ResearchPage[]{ new ResearchPage("1"), new ResearchPage((IArcaneRecipe) recipes.get("ItemFocusLumos")) }).setParents("TMFOCUSFIRE").setParentsHidden("FOCUSFIRE").setSecondary().registerResearchItem();
	}
}
