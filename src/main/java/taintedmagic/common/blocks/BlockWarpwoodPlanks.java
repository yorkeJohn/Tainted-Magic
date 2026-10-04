package taintedmagic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import taintedmagic.common.TaintedMagic;

public class BlockWarpwoodPlanks extends Block {

    public BlockWarpwoodPlanks() {
        super(Material.wood);
        setCreativeTab(TaintedMagic.tabTM);
        setBlockName("BlockWarpwoodPlanks");
        setBlockTextureName("taintedmagic:BlockWarpwoodPlanks");
        setHardness(0.8F);
        setStepSound(soundTypeWood);
    }
}
