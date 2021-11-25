package taintedmagic.common.registry;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import taintedmagic.common.handler.ConfigHandler;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.research.TMResearchItem;
import taintedmagic.common.research.ThaumcraftResearchItem;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class ResearchRegistry
{
    public static final String CATEGORY_TM = "TAINTEDMAGIC";
    public static HashMap<String, Object> recipes = new HashMap<String, Object>();

    public static void initResearch ()
    {
        ResearchCategories.registerCategory(CATEGORY_TM, new ResourceLocation("taintedmagic:textures/misc/tab_tm.png"),
                ConfigHandler.CUSTOM_RESEARCH_TAB_BACK
                        ? new ResourceLocation("taintedmagic:textures/gui/gui_tm_researchback.png")
                        : new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));

        ResearchItem r;

        /**
         * Copied Thaumcraft research
         */
        r = new ThaumcraftResearchItem("TMELDRITCHMAJOR", "ELDRITCHMAJOR", "ELDRITCH", 7, -7,
                new ResourceLocation("thaumcraft:textures/misc/r_eldritchmajor.png"));
        r.setSpecial().setConcealed().setRound().registerResearchItem();

        /**
         * Tainted Magic research
         */
        r = new TMResearchItem("TAINTEDMAGIC", new AspectList(), 2, -1, new ItemStack(ConfigItems.itemResource, 1, 13), 0, 0);
        r.setPages(new ResearchPage("1"), new ResearchPage("2"));
        r.setRound().setAutoUnlock().registerResearchItem();

        r = new TMResearchItem("SHADOWMETAL",
                new AspectList().add(Aspect.METAL, 1).add(Aspect.DARKNESS, 1).add(Aspect.MAGIC, 1), 0, 1,
                new ItemStack(ItemRegistry.ItemMaterial), 1, 1);
        r.setPages(new ResearchPage("1"), cruciblePage("ItemMaterial:0"), recipePage("ItemShadowmetalPick"),
                recipePage("ItemShadowmetalSpade"), recipePage("ItemShadowmetalAxe"), recipePage("ItemShadowmetalHoe"),
                recipePage("ItemShadowmetalSword"));
        r.setParents("TAINTEDMAGIC").setConcealed().registerResearchItem();

        r = new TMResearchItem("CAP_shadowmetal",
                new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4).add(Aspect.MAGIC, 4), -3,
                2, new ItemStack(ItemRegistry.ItemWandCap, 1, 0), 2, 4);
        r.setPages(new ResearchPage("1"), infusionPage("ItemWandCap:0"));
        r.setParents("SHADOWMETAL", "CAP_void", "PRIMPEARL").setConcealed().registerResearchItem();

        r = new TMResearchItem("SHADOWCLOTH", new AspectList().add(Aspect.CLOTH, 1).add(Aspect.DARKNESS, 1), -2, 4,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 1), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemMaterial:1"));
        r.setConcealed().setSecondary().setParents("ENCHFABRIC", "SHADOWMETAL").registerResearchItem();

        r = new TMResearchItem("ROD_warpwood",
                new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 8, 0,
                new ItemStack(ItemRegistry.ItemWandRod, 1, 0), 3, 5);
        r.setPages(new ResearchPage("1"), infusionPage("ItemWandRod:0"));
        r.setParents("WARPTREE", "VOIDMETAL", "PRIMPEARL", "ROD_primal_staff").setParentsHidden("SHADOWMETAL").setConcealed()
                .registerResearchItem();

        r = new TMResearchItem("ROD_warpwood_staff",
                new AspectList().add(Aspect.TREE, 4).add(Aspect.DARKNESS, 4).add(Aspect.ELDRITCH, 4), 10, -1,
                new ItemStack(ItemRegistry.ItemWandRod, 1, 1), 2, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemWandRod:1"));
        r.setConcealed().setParents("ROD_warpwood").setParentsHidden("CREATIONSHARD").setSecondary().registerResearchItem();

        r = new TMResearchItem("UNBALANCEDSHARDS", new AspectList().add(Aspect.CRYSTAL, 4).add(Aspect.DARKNESS, 4), 4, -1,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 4), 0, 1);
        r.setPages(new ResearchPage("1"), cruciblePage("ItemMaterial:4"), cruciblePage("ItemMaterial:3"));
        r.setParents("TAINTEDMAGIC").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("WARPTREE", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 4).add(Aspect.TREE, 4), 6,
                -2, new ItemStack(ItemRegistry.ItemWarpFertilizer), 3, 2);
        r.setPages(new ResearchPage("1"), arcanePage("ItemWarpFertilizer"));
        r.setParents("UNBALANCEDSHARDS").setConcealed().registerResearchItem();

        r = new TMResearchItem("CRIMSONROBES",
                new AspectList().add(Aspect.CLOTH, 4).add(Aspect.EXCHANGE, 4).add(Aspect.ARMOR, 4), -2, -1,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 2), 2, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemMaterial:2"), arcanePage("ItemHelmetCultistRobe"),
                arcanePage("ItemChestCultistRobe"), arcanePage("ItemLegsCultistRobe"), arcanePage("ItemBootsCultist"));
        r.setParents("HOLLOWDAGGER").setConcealed().registerResearchItem();

        r = new TMResearchItem("VOIDFORTRESS",
                new AspectList().add(Aspect.ARMOR, 5).add(Aspect.ELDRITCH, 3).add(Aspect.DARKNESS, 3).add(Aspect.VOID, 5), 8,
                -9, new ItemStack(ItemRegistry.ItemVoidFortressHelmet), 2, 2);
        r.setPages(new ResearchPage("1"), infusionPage("ItemVoidFortressHelmet"), infusionPage("ItemVoidFortressChestplate"),
                infusionPage("ItemVoidFortressLeggings"));
        r.setParentsHidden("ARMORVOIDFORTRESS", "ARMORFORTRESS").setParents("TMELDRITCHMAJOR").setConcealed()
                .registerResearchItem();

        r = new TMResearchItem("WARPEDGOGGLES",
                new AspectList().add(Aspect.ARMOR, 4).add(Aspect.ELDRITCH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4), 2,
                4, new ItemStack(ItemRegistry.ItemWarpedGoggles), 1, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemWarpedGoggles"));
        r.setParentsHidden("TAINTEDMAGIC", "GOGGLES").setConcealed().setSecondary().setParents("SHADOWMETAL")
                .registerResearchItem();

        r = new TMResearchItem("KNIGHTROBES",
                new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4), -4, -2,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 7), 2, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemMaterial:7"), arcanePage("ItemHelmetCultistPlate"),
                arcanePage("ItemChestCultistPlate"), arcanePage("ItemLegsCultistPlate"));
        r.setParentsHidden("ELDRITCHMINOR").setParents("CRIMSONROBES").setConcealed().registerResearchItem();

        r = new TMResearchItem("PRAETORARMOR",
                new AspectList().add(Aspect.CLOTH, 4).add(Aspect.DARKNESS, 4).add(Aspect.ARMOR, 4).add(Aspect.ELDRITCH, 4), -6,
                0, ItemApi.getItem("itemHelmetCultistLeaderPlate", 0), 2, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemHelmetCultistLeaderPlate"), arcanePage("ItemChestCultistLeaderPlate"),
                arcanePage("ItemLegsCultistLeaderPlate"));
        r.setParents("KNIGHTROBES").setParentsHidden("CRIMSONROBES", "TMELDRITCHMAJOR", "VOIDMETAL").setConcealed()
                .registerResearchItem();

        r = new TMResearchItem("VOIDBLOOD",
                new AspectList().add(Aspect.VOID, 14).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 18).add(Aspect.AURA, 4), -2, -5,
                new ItemStack(ItemRegistry.ItemVoidBlood), 0, 3);
        r.setPages(new ResearchPage("1"), arcanePage("ItemVoidBlood"));
        r.setSecondary().setParents("HOLLOWDAGGER").setParentsHidden("ELDRITCHMAJOR", "BREAKPEARL").setConcealed()
                .registerResearchItem();

        r = new TMResearchItem("VOIDWALKERBOOTS",
                new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.ELDRITCH, 8), 4,
                -8, new ItemStack(ItemRegistry.ItemVoidwalkerBoots), 3, 4);
        r.setPages(new ResearchPage("1"), infusionPage("ItemVoidwalkerBoots"));
        r.setParentsHidden("PRIMPEARL", "BOOTSTRAVELLER", "SHADOWCLOTH", "ARMORVOIDFORTRESS").setConcealed()
                .setParents("TMELDRITCHMAJOR").registerResearchItem();

        r = new TMResearchItem("BREAKPEARL", TaintedMagicHelper.getPrimals(4).add(Aspect.ENTROPY, 12), 8, -6,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 9), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemMaterial:9"), arcanePage("ItemMaterial:10"));
        r.setConcealed().setParents("PRIMPEARL").setSecondary().registerResearchItem();

        r = new TMResearchItem("CREATIONSHARD", TaintedMagicHelper.getPrimals(16), 9, -7,
                new ItemStack(ItemRegistry.ItemMaterial, 1, 5), 3, 9);
        r.setPages(new ResearchPage("1"), infusionPage("ItemMaterial:5"), arcanePage("ItemMaterial:11"));
        r.setConcealed().setParents("TMELDRITCHMAJOR").setParentsHidden("PRIMPEARL").setSpecial().registerResearchItem();

        r = new TMResearchItem("CREATION", new AspectList(), 11, -5,
                new ResourceLocation("taintedmagic:textures/misc/r_creation.png"), 0, 0);
        r.setPages(new ResearchPage("1"), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4"),
                new ResearchPage("5"), new ResearchPage("6"));
        r.setParents("CREATIONSHARD").setConcealed().setRound().setSpecial().setHidden().registerResearchItem();

        r = new TMResearchItem("SKYSALT", new AspectList().add(TaintedMagicHelper.getPrimals(4)).add(Aspect.WEATHER, 4), 12, -7,
                new ItemStack(ItemRegistry.ItemSalis, 1, 0), 0, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemSalis:0"));
        r.setConcealed().setParents("CREATION").setSecondary().registerResearchItem();

        r = new TMResearchItem("TIMESALT", new AspectList().add(TaintedMagicHelper.getPrimals(4)).add(Aspect.EXCHANGE, 4), 13,
                -6, new ItemStack(ItemRegistry.ItemSalis, 1, 1), 0, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemSalis:1"));
        r.setConcealed().setParents("CREATION").setSecondary().registerResearchItem();

        r = new TMResearchItem("THAUMICDISASSEMBLER",
                new AspectList().add(Aspect.METAL, 4).add(Aspect.WEAPON, 8).add(Aspect.TOOL, 4), 6, -9,
                new ItemStack(ItemRegistry.ItemThaumicDisassembler), 3, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemThaumicDisassembler"), arcanePage("ItemMaterial:6"),
                new ResearchPage("2"));
        r.setParentsHidden("THAUMIUM", "VOIDMETAL", "PRIMPEARL").setConcealed().setParents("TMELDRITCHMAJOR")
                .registerResearchItem();

        r = new TMResearchItem("VOIDSASH", new AspectList().add(Aspect.VOID, 4).add(Aspect.METAL, 8).add(Aspect.ARMOR, 4), 3,
                -9, new ItemStack(ItemRegistry.ItemVoidwalkerSash), 0, 3);
        r.setPages(new ResearchPage("1"), infusionPage("ItemVoidwalkerSash"));
        r.setParentsHidden("PRIMPEARL").setParents("VOIDWALKERBOOTS").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("MAGICFUNGUAR", new AspectList().add(Aspect.MAGIC, 2).add(Aspect.HUNGER, 4).add(Aspect.PLANT, 2),
                2, -4, new ItemStack(ItemRegistry.ItemMagicFunguar), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemMagicFunguar"));
        r.setSecondary().setParents("VISHROOMCRAFT").setConcealed().registerResearchItem();

        r = new TMResearchItem("VISHROOMCRAFT", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CRAFT, 2).add(Aspect.PLANT, 3),
                3, -3, new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5), 0, 0);
        r.setPages(new ResearchPage("1"), cruciblePage("Vishroom_red"), cruciblePage("Vishroom_brown"));
        r.setSecondary().setConcealed().setHidden().setItemTriggers(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5))
                .registerResearchItem();

        r = new TMResearchItem("HOLLOWDAGGER", new AspectList().add(Aspect.WEAPON, 4).add(Aspect.FIRE, 4).add(Aspect.HEAL, 4),
                0, -3, new ItemStack(ItemRegistry.ItemHollowDagger, 1, 7), 2, 2);
        r.setPages(new ResearchPage("1"), arcanePage("ItemHollowDagger"));
        r.setParentsHidden("ENCHFABRIC", "ESSENTIACRYSTAL", "ELDRITCHMINOR").setParents("TAINTEDMAGIC").registerResearchItem();

        r = new TMResearchItem("CRIMSONBLADE",
                new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.ELDRITCH, 2).add(Aspect.WEAPON, 16).add(Aspect.VOID, 6), -6,
                2, new ItemStack(ConfigItems.itemSwordCrimson), 0, 5);
        r.setPages(new ResearchPage("1"), infusionPage("ItemSwordCrimson"));
        r.setParents("PRAETORARMOR").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("SHADOWFORTRESS",
                new AspectList().add(Aspect.METAL, 4).add(Aspect.DARKNESS, 8).add(Aspect.ARMOR, 8).add(Aspect.VOID, 2), 0, 3,
                new ItemStack(ItemRegistry.ItemShadowFortressHelmet), 3, 3);
        r.setPages(new ResearchPage("1"), infusionPage("ItemShadowFortressHelmet"),
                infusionPage("ItemShadowFortressChestplate"), infusionPage("ItemShadowFortressLeggings"));
        r.setParents("SHADOWMETAL").setConcealed().setParentsHidden("VOIDFORTRESS", "TMELDRITCHMAJOR", "UNBALANCEDSHARDS")
                .registerResearchItem();

        r = new TMResearchItem("CAP_cloth", new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CLOTH, 4), 3, 1,
                new ItemStack(ItemRegistry.ItemWandCap, 1, 1), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemWandCap:1"));
        r.setParentsHidden("CAP_gold", "ENCHFABRIC").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("CAP_crimsoncloth",
                new AspectList().add(Aspect.MAGIC, 8).add(Aspect.CLOTH, 4).add(Aspect.HEAL, 8), -1, -1,
                new ItemStack(ItemRegistry.ItemWandCap, 1, 2), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemWandCap:2"));
        r.setParentsHidden("CAP_cloth").setSecondary().setConcealed().setParents("CRIMSONROBES").registerResearchItem();

        r = new TMResearchItem("CAP_shadowcloth",
                new AspectList().add(Aspect.MAGIC, 16).add(Aspect.CLOTH, 8).add(Aspect.VOID, 16).add(Aspect.DARKNESS, 8), -3, 5,
                new ItemStack(ItemRegistry.ItemWandCap, 1, 3), 0, 1);
        r.setPages(new ResearchPage("1"), arcanePage("ItemWandCap:3"));
        r.setParentsHidden("CAP_cloth").setSecondary().setConcealed().setParents("SHADOWCLOTH").registerResearchItem();

        r = new TMResearchItem("PRIMALBLADE", new AspectList().add(Aspect.MAGIC, 1).add(Aspect.ELDRITCH, 1)
                .add(Aspect.WEAPON, 1).add(Aspect.VOID, 1).add(Aspect.AURA, 1), 11, -3,
                new ItemStack(ItemRegistry.ItemPrimalBlade), 3, 5);
        r.setPages(new ResearchPage("1"), infusionPage("ItemPrimalBlade"));
        r.setParentsHidden("VOIDMETAL", "PRIMALCRUSHER", "UNBALANCEDSHARDS", "HOLLOWDAGGER").setConcealed()
                .setParents("CREATION").registerResearchItem();

        r = new TMResearchItem("THAUMIUMKATANA",
                new AspectList().add(Aspect.METAL, 8).add(Aspect.MAGIC, 4).add(Aspect.WEAPON, 6), 12, -1,
                new ItemStack(ItemRegistry.ItemKatana, 1, 0), 3, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatana:0"));
        r.setParentsHidden("ARMORFORTRESS").setConcealed().registerResearchItem();

        r = new TMResearchItem("VOIDMETALKATANA",
                new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12).add(Aspect.VOID, 12), 7, -10,
                new ItemStack(ItemRegistry.ItemKatana, 1, 1), 3, 3);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatana:1"));
        r.setParentsHidden("THAUMIUMKATANA").setParents("VOIDFORTRESS").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem(
                "SHADOWMETALKATANA", new AspectList().add(Aspect.METAL, 16).add(Aspect.MAGIC, 8).add(Aspect.WEAPON, 12)
                        .add(Aspect.VOID, 12).add(Aspect.DARKNESS, 14),
                0, 5, new ItemStack(ItemRegistry.ItemKatana, 1, 2), 3, 5);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatana:2"));
        r.setParentsHidden("VOIDMETALKATANA").setParents("SHADOWFORTRESS").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("INSCRIPTIONFIRE",
                new AspectList().add(Aspect.FIRE, 8).add(Aspect.ENTROPY, 4).add(Aspect.METAL, 6), 14, 0,
                new ResourceLocation("taintedmagic:textures/misc/r_inscription0.png"), 0, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription0"));
        r.setParents("THAUMIUMKATANA").setParentsHidden("FOCUSFIRE").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("INSCRIPTIONTHUNDER",
                new AspectList().add(Aspect.ENTROPY, 8).add(Aspect.MOTION, 4).add(Aspect.METAL, 6), 14, 1,
                new ResourceLocation("taintedmagic:textures/misc/r_inscription1.png"), 0, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription1"));
        r.setParents("THAUMIUMKATANA").setParentsHidden("FOCUSSHOCKWAVE").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("INSCRIPTIONHEAL",
                new AspectList().add(Aspect.HEAL, 8).add(Aspect.UNDEAD, 4).add(Aspect.METAL, 6), 14, 2,
                new ResourceLocation("taintedmagic:textures/misc/r_inscription2.png"), 0, 2);
        r.setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription2"));
        r.setParents("THAUMIUMKATANA").setParentsHidden("BATHSALTS").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("VOIDGOGGLES",
                new AspectList().add(Aspect.VOID, 8).add(Aspect.DARKNESS, 4).add(Aspect.MAGIC, 6).add(Aspect.SENSES, 12), 3, 3,
                new ItemStack(ItemRegistry.ItemVoidmetalGoggles), 0, 2);
        r.setPages(new ResearchPage("1"), infusionPage("ItemVoidmetalGoggles"));
        r.setParents("WARPEDGOGGLES").setParentsHidden("VOIDMETAL").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("ELDRITCHFOCUS",
                new AspectList().add(Aspect.ELDRITCH, 22).add(Aspect.ENTROPY, 14).add(Aspect.AIR, 4).add(Aspect.DARKNESS, 6), 5,
                -5, new ItemStack(ItemRegistry.ItemFocusDarkMatter), 3, 7);
        r.setPages(new ResearchPage("1"), infusionPage("ItemFocusDarkMatter"));
        r.setParentsHidden("OUTERREV").setParents("TMELDRITCHMAJOR").setConcealed().setSpecial().registerResearchItem();

        r = new TMResearchItem("DIFFUSIONUPGRADE",
                new AspectList().add(Aspect.MAGIC, 4).add(Aspect.DARKNESS, 8).add(Aspect.WEAPON, 8).add(Aspect.ELDRITCH, 10), 6,
                -4, new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"), 0, 2);
        r.setPages(new ResearchPage("1"));
        r.setParentsHidden("FOCALMANIPULATION").setConcealed().setParents("ELDRITCHFOCUS").setSecondary()
                .registerResearchItem();

        r = new TMResearchItem("MACEFOCUS",
                new AspectList().add(Aspect.ENTROPY, 10).add(Aspect.EARTH, 4).add(Aspect.WEAPON, 6).add(Aspect.MAGIC, 8), 7, 3,
                new ItemStack(ItemRegistry.ItemFocusMageMace), 2, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemFocusMageMace"));
        r.setConcealed().setParentsHidden("THAUMIUM", "FOCUSFIRE").registerResearchItem();

        r = new TMResearchItem("FOCUSSHOCKWAVE",
                new AspectList().add(Aspect.MAGIC, 20).add(Aspect.ENTROPY, 12).add(Aspect.AIR, 6).add(Aspect.MOTION, 12), 7, 2,
                new ItemStack(ItemRegistry.ItemFocusShockwave), 0, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemFocusShockwave"));
        r.setParentsHidden("FOCUSSHOCK").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("FOCUSSHARD", new AspectList().add(Aspect.CRYSTAL, 2).add(Aspect.MAGIC, 4).add(Aspect.MOTION, 6),
                6, 1, new ItemStack(ItemRegistry.ItemFocusVisShard), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemFocusVisShard"));
        r.setParents("UNBALANCEDSHARDS").setParentsHidden("FOCUSFIRE").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("TAINTFOCUS", new AspectList().add(Aspect.TAINT, 4).add(Aspect.LIFE, 4).add(Aspect.MOTION, 4), 5,
                2, new ItemStack(ItemRegistry.ItemFocusTaintSwarm), 3, 3);
        r.setPages(new ResearchPage("1"), infusionPage("ItemFocusTaintSwarm"));
        r.setParentsHidden("INFUSION", "BOTTLETAINT").setParents("FOCUSSHARD").setConcealed().registerResearchItem();

        r = new TMResearchItem("FOCUSLUMOS", new AspectList().add(Aspect.FIRE, 16).add(Aspect.LIGHT, 24).add(Aspect.ENERGY, 8),
                5, 3, new ItemStack(ItemRegistry.ItemFocusLumos), 0, 0);
        r.setPages(new ResearchPage("1"), arcanePage("ItemFocusLumos"));
        r.setParentsHidden("FOCUSFIRE").setConcealed().setSecondary().registerResearchItem();

        r = new TMResearchItem("LUMOSRING",
                new AspectList().add(Aspect.ARMOR, 16).add(Aspect.AURA, 12).add(Aspect.LIGHT, 24).add(Aspect.ENERGY, 8), 6, 4,
                new ItemStack(ItemRegistry.ItemLumosRing), 2, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemLumosRing"));
        r.setParents("FOCUSLUMOS").setParentsHidden("RUNICARMOR").setConcealed().registerResearchItem();

        r = new TMResearchItem("FLYTECHARM",
                new AspectList().add(Aspect.FLIGHT, 15).add(Aspect.AIR, 20).add(Aspect.SENSES, 8).add(Aspect.MAGIC, 12), 13, -4,
                new ItemStack(ItemRegistry.ItemFlyteCharm), 3, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemFlyteCharm"));
        r.setParents("CREATION").setParentsHidden("VOIDSASH", "PRIMALARROW").setConcealed().registerResearchItem();

        r = new TMResearchItem("GATEKEY",
                new AspectList().add(Aspect.TRAVEL, 30).add(Aspect.EXCHANGE, 15).add(Aspect.AURA, 20).add(Aspect.FLIGHT, 10), 9,
                -4, new ItemStack(ItemRegistry.ItemGateKey), 3, 0);
        r.setPages(new ResearchPage("1"), infusionPage("ItemGateKey"));
        r.setParents("CREATION").setConcealed().registerResearchItem();
    }

    /**
     * ResearchPage helpers
     */

    private static ResearchPage recipePage (String key)
    {
        return new ResearchPage((IRecipe) recipes.get(key));
    }

    private static ResearchPage arcanePage (String key)
    {
        return new ResearchPage((IArcaneRecipe) recipes.get(key));
    }

    private static ResearchPage infusionPage (String key)
    {
        return new ResearchPage((InfusionRecipe) recipes.get(key));
    }

    private static ResearchPage cruciblePage (String key)
    {
        return new ResearchPage((CrucibleRecipe) recipes.get(key));
    }
}
