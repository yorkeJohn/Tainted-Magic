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
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

public class ResearchRegistry {

    public static final String CATEGORY_TM = "TAINTEDMAGIC";
    public static HashMap<String, Object> recipes = new HashMap<>();

    public static void initResearch() {
        ResearchCategories.registerCategory(
                CATEGORY_TM,
                new ResourceLocation("taintedmagic:textures/misc/tab_tm.png"),
                ConfigHandler.CUSTOM_RESEARCH_TAB_BACK
                        ? new ResourceLocation("taintedmagic:textures/gui/gui_tm_researchback.png")
                        : new ResourceLocation("thaumcraft:textures/gui/gui_researchback.png"));

        /**
         * Copied Thaumcraft research
         */
        new ThaumcraftResearchItem(
                        "TMELDRITCHMAJOR",
                        "ELDRITCHMAJOR",
                        "ELDRITCH",
                        7,
                        -7,
                        new ResourceLocation("thaumcraft:textures/misc/r_eldritchmajor.png"))
                .setSpecial()
                .setConcealed()
                .setRound()
                .registerResearchItem();

        /**
         * Tainted Magic research
         */
        new TMResearchItem(
                        "TAINTEDMAGIC", new AspectList(), 2, -1, new ItemStack(ConfigItems.itemResource, 1, 13), 0, 0)
                .setPages(new ResearchPage("1"), new ResearchPage("2"))
                .setRound()
                .setAutoUnlock()
                .registerResearchItem();

        new TMResearchItem(
                        "SHADOWMETAL",
                        new AspectList()
                                .add(Aspect.METAL, 1)
                                .add(Aspect.DARKNESS, 1)
                                .add(Aspect.MAGIC, 1),
                        0,
                        1,
                        new ItemStack(ItemRegistry.ItemMaterial),
                        1,
                        1)
                .setPages(
                        new ResearchPage("1"),
                        cruciblePage("ItemMaterial:0"),
                        recipePage("ItemShadowmetalPick"),
                        recipePage("ItemShadowmetalSpade"),
                        recipePage("ItemShadowmetalAxe"),
                        recipePage("ItemShadowmetalHoe"),
                        recipePage("ItemShadowmetalSword"))
                .setParents("TAINTEDMAGIC")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "CAP_shadowmetal",
                        new AspectList()
                                .add(Aspect.METAL, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ELDRITCH, 4)
                                .add(Aspect.MAGIC, 4),
                        -3,
                        2,
                        new ItemStack(ItemRegistry.ItemWandCap, 1, 0),
                        2,
                        4)
                .setPages(new ResearchPage("1"), infusionPage("ItemWandCap:0"))
                .setParents("SHADOWMETAL", "CAP_void", "PRIMPEARL")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "SHADOWCLOTH",
                        new AspectList().add(Aspect.CLOTH, 1).add(Aspect.DARKNESS, 1),
                        -2,
                        4,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 1),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemMaterial:1"))
                .setConcealed()
                .setSecondary()
                .setParents("ENCHFABRIC", "SHADOWMETAL")
                .registerResearchItem();

        new TMResearchItem(
                        "ROD_warpwood",
                        new AspectList()
                                .add(Aspect.TREE, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ELDRITCH, 4),
                        8,
                        0,
                        new ItemStack(ItemRegistry.ItemWandRod, 1, 0),
                        3,
                        5)
                .setPages(new ResearchPage("1"), infusionPage("ItemWandRod:0"))
                .setParents("WARPTREE", "VOIDMETAL", "PRIMPEARL", "ROD_primal_staff")
                .setParentsHidden("SHADOWMETAL")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "ROD_warpwood_staff",
                        new AspectList()
                                .add(Aspect.TREE, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ELDRITCH, 4),
                        10,
                        -1,
                        new ItemStack(ItemRegistry.ItemWandRod, 1, 1),
                        2,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemWandRod:1"))
                .setConcealed()
                .setParents("ROD_warpwood")
                .setParentsHidden("CREATIONSHARD")
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "UNBALANCEDSHARDS",
                        new AspectList().add(Aspect.CRYSTAL, 4).add(Aspect.DARKNESS, 4),
                        4,
                        -1,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 4),
                        0,
                        1)
                .setPages(new ResearchPage("1"), cruciblePage("ItemMaterial:4"), cruciblePage("ItemMaterial:3"))
                .setParents("TAINTEDMAGIC")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "WARPTREE",
                        new AspectList()
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.TREE, 4),
                        6,
                        -2,
                        new ItemStack(ItemRegistry.ItemWarpFertilizer),
                        3,
                        2)
                .setPages(new ResearchPage("1"), arcanePage("ItemWarpFertilizer"))
                .setParents("UNBALANCEDSHARDS")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "CRIMSONROBES",
                        new AspectList()
                                .add(Aspect.CLOTH, 4)
                                .add(Aspect.EXCHANGE, 4)
                                .add(Aspect.ARMOR, 4),
                        -2,
                        -1,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 2),
                        2,
                        0)
                .setPages(
                        new ResearchPage("1"),
                        arcanePage("ItemMaterial:2"),
                        arcanePage("ItemHelmetCultistRobe"),
                        arcanePage("ItemChestCultistRobe"),
                        arcanePage("ItemLegsCultistRobe"),
                        arcanePage("ItemBootsCultist"))
                .setParents("HOLLOWDAGGER")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDFORTRESS",
                        new AspectList()
                                .add(Aspect.ARMOR, 5)
                                .add(Aspect.ELDRITCH, 3)
                                .add(Aspect.DARKNESS, 3)
                                .add(Aspect.VOID, 5),
                        8,
                        -9,
                        new ItemStack(ItemRegistry.ItemVoidFortressHelmet),
                        2,
                        2)
                .setPages(
                        new ResearchPage("1"),
                        infusionPage("ItemVoidFortressHelmet"),
                        infusionPage("ItemVoidFortressChestplate"),
                        infusionPage("ItemVoidFortressLeggings"))
                .setParentsHidden("ARMORVOIDFORTRESS", "ARMORFORTRESS")
                .setParents("TMELDRITCHMAJOR")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "WARPEDGOGGLES",
                        new AspectList()
                                .add(Aspect.ARMOR, 4)
                                .add(Aspect.ELDRITCH, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ARMOR, 4),
                        2,
                        4,
                        new ItemStack(ItemRegistry.ItemWarpedGoggles),
                        1,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemWarpedGoggles"))
                .setParentsHidden("TAINTEDMAGIC", "GOGGLES")
                .setConcealed()
                .setSecondary()
                .setParents("SHADOWMETAL")
                .registerResearchItem();

        new TMResearchItem(
                        "KNIGHTROBES",
                        new AspectList()
                                .add(Aspect.CLOTH, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ARMOR, 4),
                        -4,
                        -2,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 7),
                        2,
                        0)
                .setPages(
                        new ResearchPage("1"),
                        infusionPage("ItemMaterial:7"),
                        arcanePage("ItemHelmetCultistPlate"),
                        arcanePage("ItemChestCultistPlate"),
                        arcanePage("ItemLegsCultistPlate"))
                .setParentsHidden("ELDRITCHMINOR")
                .setParents("CRIMSONROBES")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "PRAETORARMOR",
                        new AspectList()
                                .add(Aspect.CLOTH, 4)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.ARMOR, 4)
                                .add(Aspect.ELDRITCH, 4),
                        -6,
                        0,
                        ItemApi.getItem("itemHelmetCultistLeaderPlate", 0),
                        2,
                        0)
                .setPages(
                        new ResearchPage("1"),
                        arcanePage("ItemHelmetCultistLeaderPlate"),
                        arcanePage("ItemChestCultistLeaderPlate"),
                        arcanePage("ItemLegsCultistLeaderPlate"))
                .setParents("KNIGHTROBES")
                .setParentsHidden("CRIMSONROBES", "TMELDRITCHMAJOR", "VOIDMETAL")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDBLOOD",
                        new AspectList()
                                .add(Aspect.VOID, 14)
                                .add(Aspect.DARKNESS, 8)
                                .add(Aspect.ARMOR, 18)
                                .add(Aspect.AURA, 4),
                        -2,
                        -5,
                        new ItemStack(ItemRegistry.ItemVoidBlood),
                        0,
                        3)
                .setPages(new ResearchPage("1"), arcanePage("ItemVoidBlood"))
                .setSecondary()
                .setParents("HOLLOWDAGGER")
                .setParentsHidden("ELDRITCHMAJOR", "BREAKPEARL")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDWALKERBOOTS",
                        new AspectList()
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.DARKNESS, 8)
                                .add(Aspect.ARMOR, 8)
                                .add(Aspect.ELDRITCH, 8),
                        4,
                        -8,
                        new ItemStack(ItemRegistry.ItemVoidwalkerBoots),
                        3,
                        4)
                .setPages(new ResearchPage("1"), infusionPage("ItemVoidwalkerBoots"))
                .setParentsHidden("PRIMPEARL", "BOOTSTRAVELLER", "SHADOWCLOTH", "ARMORVOIDFORTRESS")
                .setConcealed()
                .setParents("TMELDRITCHMAJOR")
                .registerResearchItem();

        new TMResearchItem(
                        "BREAKPEARL",
                        TaintedMagicHelper.getPrimals(4).add(Aspect.ENTROPY, 12),
                        8,
                        -6,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 9),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemMaterial:9"), arcanePage("ItemMaterial:10"))
                .setConcealed()
                .setParents("PRIMPEARL")
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "CREATIONSHARD",
                        TaintedMagicHelper.getPrimals(16),
                        9,
                        -7,
                        new ItemStack(ItemRegistry.ItemMaterial, 1, 5),
                        3,
                        9)
                .setPages(new ResearchPage("1"), infusionPage("ItemMaterial:5"), arcanePage("ItemMaterial:11"))
                .setConcealed()
                .setParents("TMELDRITCHMAJOR")
                .setParentsHidden("PRIMPEARL")
                .setSpecial()
                .registerResearchItem();

        new TMResearchItem(
                        "CREATION",
                        new AspectList(),
                        11,
                        -5,
                        new ResourceLocation("taintedmagic:textures/misc/r_creation.png"),
                        0,
                        0)
                .setPages(
                        new ResearchPage("1"),
                        new ResearchPage("2"),
                        new ResearchPage("3"),
                        new ResearchPage("4"),
                        new ResearchPage("5"),
                        new ResearchPage("6"))
                .setParents("CREATIONSHARD")
                .setConcealed()
                .setRound()
                .setSpecial()
                .setHidden()
                .registerResearchItem();

        new TMResearchItem(
                        "SKYSALT",
                        new AspectList().add(TaintedMagicHelper.getPrimals(4)).add(Aspect.WEATHER, 4),
                        12,
                        -7,
                        new ItemStack(ItemRegistry.ItemSalis, 1, 0),
                        0,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemSalis:0"))
                .setConcealed()
                .setParents("CREATION")
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "TIMESALT",
                        new AspectList().add(TaintedMagicHelper.getPrimals(4)).add(Aspect.EXCHANGE, 4),
                        13,
                        -6,
                        new ItemStack(ItemRegistry.ItemSalis, 1, 1),
                        0,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemSalis:1"))
                .setConcealed()
                .setParents("CREATION")
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "THAUMICDISASSEMBLER",
                        new AspectList()
                                .add(Aspect.METAL, 4)
                                .add(Aspect.WEAPON, 8)
                                .add(Aspect.TOOL, 4),
                        6,
                        -9,
                        new ItemStack(ItemRegistry.ItemThaumicDisassembler),
                        3,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemThaumicDisassembler"), arcanePage("ItemMaterial:6"))
                .setParentsHidden("THAUMIUM", "VOIDMETAL", "PRIMPEARL")
                .setConcealed()
                .setParents("TMELDRITCHMAJOR")
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDSASH",
                        new AspectList()
                                .add(Aspect.VOID, 4)
                                .add(Aspect.METAL, 8)
                                .add(Aspect.ARMOR, 4),
                        3,
                        -9,
                        new ItemStack(ItemRegistry.ItemVoidwalkerSash),
                        0,
                        3)
                .setPages(new ResearchPage("1"), infusionPage("ItemVoidwalkerSash"))
                .setParentsHidden("PRIMPEARL")
                .setParents("VOIDWALKERBOOTS")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "MAGICFUNGUAR",
                        new AspectList()
                                .add(Aspect.MAGIC, 2)
                                .add(Aspect.HUNGER, 4)
                                .add(Aspect.PLANT, 2),
                        2,
                        -4,
                        new ItemStack(ItemRegistry.ItemMagicFunguar),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemMagicFunguar"))
                .setSecondary()
                .setParents("VISHROOMCRAFT")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "VISHROOMCRAFT",
                        new AspectList()
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.CRAFT, 2)
                                .add(Aspect.PLANT, 3),
                        3,
                        -3,
                        new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5),
                        0,
                        0)
                .setPages(new ResearchPage("1"), cruciblePage("Vishroom_red"), cruciblePage("Vishroom_brown"))
                .setSecondary()
                .setConcealed()
                .setHidden()
                .setItemTriggers(new ItemStack(ConfigBlocks.blockCustomPlant, 1, 5))
                .registerResearchItem();

        new TMResearchItem(
                        "HOLLOWDAGGER",
                        new AspectList()
                                .add(Aspect.WEAPON, 4)
                                .add(Aspect.FIRE, 4)
                                .add(Aspect.HEAL, 4),
                        0,
                        -3,
                        new ItemStack(ItemRegistry.ItemHollowDagger, 1, 7),
                        2,
                        2)
                .setPages(new ResearchPage("1"), arcanePage("ItemHollowDagger"))
                .setParentsHidden("ENCHFABRIC", "ESSENTIACRYSTAL", "ELDRITCHMINOR")
                .setParents("TAINTEDMAGIC")
                .registerResearchItem();

        new TMResearchItem(
                        "CRIMSONBLADE",
                        new AspectList()
                                .add(Aspect.ENTROPY, 8)
                                .add(Aspect.ELDRITCH, 2)
                                .add(Aspect.WEAPON, 16)
                                .add(Aspect.VOID, 6),
                        -6,
                        2,
                        new ItemStack(ConfigItems.itemSwordCrimson),
                        0,
                        5)
                .setPages(new ResearchPage("1"), infusionPage("ItemSwordCrimson"))
                .setParents("PRAETORARMOR")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "SHADOWFORTRESS",
                        new AspectList()
                                .add(Aspect.METAL, 4)
                                .add(Aspect.DARKNESS, 8)
                                .add(Aspect.ARMOR, 8)
                                .add(Aspect.VOID, 2),
                        0,
                        3,
                        new ItemStack(ItemRegistry.ItemShadowFortressHelmet),
                        3,
                        3)
                .setPages(
                        new ResearchPage("1"),
                        infusionPage("ItemShadowFortressHelmet"),
                        infusionPage("ItemShadowFortressChestplate"),
                        infusionPage("ItemShadowFortressLeggings"))
                .setParents("SHADOWMETAL")
                .setConcealed()
                .setParentsHidden("VOIDFORTRESS", "TMELDRITCHMAJOR", "UNBALANCEDSHARDS")
                .registerResearchItem();

        new TMResearchItem(
                        "CAP_cloth",
                        new AspectList().add(Aspect.MAGIC, 4).add(Aspect.CLOTH, 4),
                        3,
                        1,
                        new ItemStack(ItemRegistry.ItemWandCap, 1, 1),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemWandCap:1"))
                .setParentsHidden("CAP_gold", "ENCHFABRIC")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "CAP_crimsoncloth",
                        new AspectList()
                                .add(Aspect.MAGIC, 8)
                                .add(Aspect.CLOTH, 4)
                                .add(Aspect.HEAL, 8),
                        -1,
                        -1,
                        new ItemStack(ItemRegistry.ItemWandCap, 1, 2),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemWandCap:2"))
                .setParentsHidden("CAP_cloth")
                .setSecondary()
                .setConcealed()
                .setParents("CRIMSONROBES")
                .registerResearchItem();

        new TMResearchItem(
                        "CAP_shadowcloth",
                        new AspectList()
                                .add(Aspect.MAGIC, 16)
                                .add(Aspect.CLOTH, 8)
                                .add(Aspect.VOID, 16)
                                .add(Aspect.DARKNESS, 8),
                        -3,
                        5,
                        new ItemStack(ItemRegistry.ItemWandCap, 1, 3),
                        0,
                        1)
                .setPages(new ResearchPage("1"), arcanePage("ItemWandCap:3"))
                .setParentsHidden("CAP_cloth")
                .setSecondary()
                .setConcealed()
                .setParents("SHADOWCLOTH")
                .registerResearchItem();

        new TMResearchItem(
                        "PRIMALBLADE",
                        new AspectList()
                                .add(Aspect.MAGIC, 1)
                                .add(Aspect.ELDRITCH, 1)
                                .add(Aspect.WEAPON, 1)
                                .add(Aspect.VOID, 1)
                                .add(Aspect.AURA, 1),
                        11,
                        -3,
                        new ItemStack(ItemRegistry.ItemPrimalBlade),
                        3,
                        5)
                .setPages(new ResearchPage("1"), infusionPage("ItemPrimalBlade"))
                .setParentsHidden("VOIDMETAL", "PRIMALCRUSHER", "UNBALANCEDSHARDS", "HOLLOWDAGGER")
                .setConcealed()
                .setParents("CREATION")
                .registerResearchItem();

        new TMResearchItem(
                        "THAUMIUMKATANA",
                        new AspectList()
                                .add(Aspect.METAL, 8)
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.WEAPON, 6),
                        12,
                        -1,
                        new ItemStack(ItemRegistry.ItemKatana, 1, 0),
                        3,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatana:0"))
                .setParentsHidden("ARMORFORTRESS")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDMETALKATANA",
                        new AspectList()
                                .add(Aspect.METAL, 16)
                                .add(Aspect.MAGIC, 8)
                                .add(Aspect.WEAPON, 12)
                                .add(Aspect.VOID, 12),
                        7,
                        -10,
                        new ItemStack(ItemRegistry.ItemKatana, 1, 1),
                        3,
                        3)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatana:1"))
                .setParentsHidden("THAUMIUMKATANA")
                .setParents("VOIDFORTRESS")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "SHADOWMETALKATANA",
                        new AspectList()
                                .add(Aspect.METAL, 16)
                                .add(Aspect.MAGIC, 8)
                                .add(Aspect.WEAPON, 12)
                                .add(Aspect.VOID, 12)
                                .add(Aspect.DARKNESS, 14),
                        0,
                        5,
                        new ItemStack(ItemRegistry.ItemKatana, 1, 2),
                        3,
                        5)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatana:2"))
                .setParentsHidden("VOIDMETALKATANA")
                .setParents("SHADOWFORTRESS")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "INSCRIPTIONFIRE",
                        new AspectList()
                                .add(Aspect.FIRE, 8)
                                .add(Aspect.ENTROPY, 4)
                                .add(Aspect.METAL, 6),
                        14,
                        0,
                        new ResourceLocation("taintedmagic:textures/misc/r_inscription0.png"),
                        0,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription0"))
                .setParents("THAUMIUMKATANA")
                .setParentsHidden("FOCUSFIRE")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "INSCRIPTIONTHUNDER",
                        new AspectList()
                                .add(Aspect.ENTROPY, 8)
                                .add(Aspect.MOTION, 4)
                                .add(Aspect.METAL, 6),
                        14,
                        1,
                        new ResourceLocation("taintedmagic:textures/misc/r_inscription1.png"),
                        0,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription1"))
                .setParents("THAUMIUMKATANA")
                .setParentsHidden("FOCUSSHOCKWAVE")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "INSCRIPTIONHEAL",
                        new AspectList()
                                .add(Aspect.HEAL, 8)
                                .add(Aspect.UNDEAD, 4)
                                .add(Aspect.METAL, 6),
                        14,
                        2,
                        new ResourceLocation("taintedmagic:textures/misc/r_inscription2.png"),
                        0,
                        2)
                .setPages(new ResearchPage("1"), infusionPage("ItemKatanaThaumium:inscription2"))
                .setParents("THAUMIUMKATANA")
                .setParentsHidden("BATHSALTS")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "VOIDGOGGLES",
                        new AspectList()
                                .add(Aspect.VOID, 8)
                                .add(Aspect.DARKNESS, 4)
                                .add(Aspect.MAGIC, 6)
                                .add(Aspect.SENSES, 12),
                        3,
                        3,
                        new ItemStack(ItemRegistry.ItemVoidmetalGoggles),
                        0,
                        2)
                .setPages(new ResearchPage("1"), infusionPage("ItemVoidmetalGoggles"))
                .setParents("WARPEDGOGGLES")
                .setParentsHidden("VOIDMETAL")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "ELDRITCHFOCUS",
                        new AspectList()
                                .add(Aspect.ELDRITCH, 22)
                                .add(Aspect.ENTROPY, 14)
                                .add(Aspect.AIR, 4)
                                .add(Aspect.DARKNESS, 6),
                        5,
                        -5,
                        new ItemStack(ItemRegistry.ItemFocusDarkMatter),
                        3,
                        7)
                .setPages(new ResearchPage("1"), infusionPage("ItemFocusDarkMatter"))
                .setParentsHidden("OUTERREV")
                .setParents("TMELDRITCHMAJOR")
                .setConcealed()
                .setSpecial()
                .registerResearchItem();

        new TMResearchItem(
                        "DIFFUSIONUPGRADE",
                        new AspectList()
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.DARKNESS, 8)
                                .add(Aspect.WEAPON, 8)
                                .add(Aspect.ELDRITCH, 10),
                        6,
                        -4,
                        new ResourceLocation("taintedmagic:textures/foci/IconDiffusion.png"),
                        0,
                        2)
                .setPages(new ResearchPage("1"))
                .setParentsHidden("FOCALMANIPULATION")
                .setConcealed()
                .setParents("ELDRITCHFOCUS")
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "MACEFOCUS",
                        new AspectList()
                                .add(Aspect.ENTROPY, 10)
                                .add(Aspect.EARTH, 4)
                                .add(Aspect.WEAPON, 6)
                                .add(Aspect.MAGIC, 8),
                        7,
                        3,
                        new ItemStack(ItemRegistry.ItemFocusMageMace),
                        2,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemFocusMageMace"))
                .setConcealed()
                .setParentsHidden("THAUMIUM", "FOCUSFIRE")
                .registerResearchItem();

        new TMResearchItem(
                        "FOCUSSHOCKWAVE",
                        new AspectList()
                                .add(Aspect.MAGIC, 20)
                                .add(Aspect.ENTROPY, 12)
                                .add(Aspect.AIR, 6)
                                .add(Aspect.MOTION, 12),
                        7,
                        2,
                        new ItemStack(ItemRegistry.ItemFocusShockwave),
                        0,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemFocusShockwave"))
                .setParentsHidden("FOCUSSHOCK")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "FOCUSSHARD",
                        new AspectList()
                                .add(Aspect.CRYSTAL, 2)
                                .add(Aspect.MAGIC, 4)
                                .add(Aspect.MOTION, 6),
                        6,
                        1,
                        new ItemStack(ItemRegistry.ItemFocusVisShard),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemFocusVisShard"))
                .setParents("UNBALANCEDSHARDS")
                .setParentsHidden("FOCUSFIRE")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "TAINTFOCUS",
                        new AspectList()
                                .add(Aspect.TAINT, 4)
                                .add(Aspect.LIFE, 4)
                                .add(Aspect.MOTION, 4),
                        5,
                        2,
                        new ItemStack(ItemRegistry.ItemFocusTaintSwarm),
                        3,
                        3)
                .setPages(new ResearchPage("1"), infusionPage("ItemFocusTaintSwarm"))
                .setParentsHidden("INFUSION", "BOTTLETAINT")
                .setParents("FOCUSSHARD")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "FOCUSLUMOS",
                        new AspectList()
                                .add(Aspect.FIRE, 16)
                                .add(Aspect.LIGHT, 24)
                                .add(Aspect.ENERGY, 8),
                        5,
                        3,
                        new ItemStack(ItemRegistry.ItemFocusLumos),
                        0,
                        0)
                .setPages(new ResearchPage("1"), arcanePage("ItemFocusLumos"))
                .setParentsHidden("FOCUSFIRE")
                .setConcealed()
                .setSecondary()
                .registerResearchItem();

        new TMResearchItem(
                        "LUMOSRING",
                        new AspectList()
                                .add(Aspect.ARMOR, 16)
                                .add(Aspect.AURA, 12)
                                .add(Aspect.LIGHT, 24)
                                .add(Aspect.ENERGY, 8),
                        6,
                        4,
                        new ItemStack(ItemRegistry.ItemLumosRing),
                        2,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemLumosRing"))
                .setParents("FOCUSLUMOS")
                .setParentsHidden("RUNICARMOR")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "FLYTECHARM",
                        new AspectList()
                                .add(Aspect.FLIGHT, 15)
                                .add(Aspect.AIR, 20)
                                .add(Aspect.SENSES, 8)
                                .add(Aspect.MAGIC, 12),
                        13,
                        -4,
                        new ItemStack(ItemRegistry.ItemFlyteCharm),
                        3,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemFlyteCharm"))
                .setParents("CREATION")
                .setParentsHidden("VOIDSASH", "PRIMALARROW")
                .setConcealed()
                .registerResearchItem();

        new TMResearchItem(
                        "GATEKEY",
                        new AspectList()
                                .add(Aspect.TRAVEL, 30)
                                .add(Aspect.EXCHANGE, 15)
                                .add(Aspect.AURA, 20)
                                .add(Aspect.FLIGHT, 10),
                        9,
                        -4,
                        new ItemStack(ItemRegistry.ItemGateKey),
                        3,
                        0)
                .setPages(new ResearchPage("1"), infusionPage("ItemGateKey"))
                .setParents("CREATION")
                .setConcealed()
                .registerResearchItem();
    }

    /**
     * ResearchPage helpers
     */
    private static ResearchPage recipePage(final String key) {
        return new ResearchPage((IRecipe) recipes.get(key));
    }

    private static ResearchPage arcanePage(final String key) {
        return new ResearchPage((IArcaneRecipe) recipes.get(key));
    }

    private static ResearchPage infusionPage(final String key) {
        return new ResearchPage((InfusionRecipe) recipes.get(key));
    }

    private static ResearchPage cruciblePage(final String key) {
        return new ResearchPage((CrucibleRecipe) recipes.get(key));
    }
}
