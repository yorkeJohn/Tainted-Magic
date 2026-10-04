package taintedmagic.common.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import taintedmagic.common.blocks.BlockLumos;
import taintedmagic.common.blocks.BlockNightshadeBush;
import taintedmagic.common.blocks.BlockWarpwoodLeaves;
import taintedmagic.common.blocks.BlockWarpwoodLog;
import taintedmagic.common.blocks.BlockWarpwoodPlanks;
import taintedmagic.common.blocks.BlockWarpwoodSapling;
import taintedmagic.common.blocks.tile.TileLumos;

public class BlockRegistry {

    public static Block BlockWarpwoodLog;
    public static Block BlockWarpwoodLeaves;
    public static Block BlockWarpwoodSapling;
    public static Block BlockWarpwoodPlanks;
    public static Block BlockNightshadeBush;
    public static Block BlockLumos;

    public static void initBlocks() {
        BlockWarpwoodLog = new BlockWarpwoodLog();
        GameRegistry.registerBlock(BlockWarpwoodLog, "BlockWarpwoodLog");

        BlockWarpwoodPlanks = new BlockWarpwoodPlanks();
        GameRegistry.registerBlock(BlockWarpwoodPlanks, "BlockWarpwoodPlanks");

        BlockWarpwoodLeaves = new BlockWarpwoodLeaves();
        GameRegistry.registerBlock(BlockWarpwoodLeaves, "BlockWarpwoodLeaves");

        BlockWarpwoodSapling = new BlockWarpwoodSapling();
        GameRegistry.registerBlock(BlockWarpwoodSapling, "BlockWarpwoodSapling");

        BlockNightshadeBush = new BlockNightshadeBush();
        GameRegistry.registerBlock(BlockNightshadeBush, "BlockNightshadeBush");

        BlockLumos = new BlockLumos();
        GameRegistry.registerBlock(BlockLumos, "BlockLumos");
    }

    public static void initTiles() {
        GameRegistry.registerTileEntity(TileLumos.class, "TileLumos");
    }
}
