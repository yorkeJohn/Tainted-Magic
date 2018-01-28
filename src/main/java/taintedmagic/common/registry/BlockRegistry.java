package taintedmagic.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import taintedmagic.common.blocks.BlockLumos;
import taintedmagic.common.blocks.BlockShadowOre;
import taintedmagic.common.blocks.BlockShadowmetal;
import taintedmagic.common.blocks.BlockWarpwoodLeaves;
import taintedmagic.common.blocks.BlockWarpwoodLog;
import taintedmagic.common.blocks.BlockWarpwoodPlanks;
import taintedmagic.common.blocks.BlockWarpwoodSapling;
import taintedmagic.common.blocks.tile.TileLumos;
import taintedmagic.common.items.blocks.ItemBlockShadowOre;
import taintedmagic.common.items.blocks.ItemBlockWarpwoodSapling;

public class BlockRegistry
{
	public static void init ()
	{
		// Shadow Ore
		BlockShadowOre = new BlockShadowOre();
		GameRegistry.registerBlock(BlockShadowOre, ItemBlockShadowOre.class, "BlockShadowOre");

		// Warpwood Log
		BlockWarpwoodLog = new BlockWarpwoodLog();
		GameRegistry.registerBlock(BlockWarpwoodLog, "BlockWarpwoodLog");

		// Block of Shadowmetal
		BlockShadowmetal = new BlockShadowmetal();
		GameRegistry.registerBlock(BlockShadowmetal, "BlockShadowmetal");

		// Warpwood Planks
		BlockWarpwoodPlanks = new BlockWarpwoodPlanks();
		GameRegistry.registerBlock(BlockWarpwoodPlanks, "BlockWarpwoodPlanks");

		// Warpwood Leaves
		BlockWarpwoodLeaves = new BlockWarpwoodLeaves();
		GameRegistry.registerBlock(BlockWarpwoodLeaves, "BlockWarpwoodLeaves");

		// Warpwood Sapling
		BlockWarpwoodSapling = new BlockWarpwoodSapling();
		GameRegistry.registerBlock(BlockWarpwoodSapling, ItemBlockWarpwoodSapling.class, "BlockWarpwoodSapling");

		// Lumos sparkles
		BlockLumos = new BlockLumos();
		GameRegistry.registerBlock(BlockLumos, "BlockLumos");
	}

	public static void initTiles ()
	{
		// Lumos
		GameRegistry.registerTileEntity(TileLumos.class, "TileLumos");
	}

	public static Block BlockShadowOre;
	public static Block BlockWarpwoodLog;
	public static Block BlockWarpwoodLeaves;
	public static Block BlockWarpwoodSapling;
	public static Block BlockShadowmetal;
	public static Block BlockWarpwoodPlanks;
	public static Block BlockLumos;
}