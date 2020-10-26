package taintedmagic.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import taintedmagic.common.TaintedMagic;

public class BlockShadowOre extends Block
{
    public BlockShadowOre ()
    {
        super(Material.rock);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setBlockTextureName("taintedmagic:BlockShadowOre");
        this.setBlockName("BlockShadowOre");
        this.setHardness(5.0F);
    }
}
