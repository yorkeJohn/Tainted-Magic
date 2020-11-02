package taintedmagic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import taintedmagic.common.TaintedMagic;

public class BlockWarpwoodPlanks extends Block
{
    public BlockWarpwoodPlanks ()
    {
        super(Material.wood);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setBlockName("BlockWarpwoodPlanks");
        this.setBlockTextureName("taintedmagic:BlockWarpwoodPlanks");
        this.setHardness(0.8F);
        this.setStepSound(soundTypeWood);
    }
}
