package taintedmagic.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.world.WorldGenWarpwoodTree;

public class BlockWarpwoodSapling extends BlockSapling {

    public BlockWarpwoodSapling() {
        setCreativeTab(TaintedMagic.tabTM);
        setBlockName("BlockWarpwoodSapling");
        setStepSound(soundTypeGrass);
    }

    @Override
    public void func_149878_d(final World world, final int x, final int y, final int z, final Random random) {
        if (!world.isRemote) {
            final int meta = world.getBlockMetadata(x, y, z);

            world.setBlockToAir(x, y, z);

            final WorldGenWarpwoodTree tree = new WorldGenWarpwoodTree(true, 7, 5);

            if (!tree.generate(world, random, x, y, z)) {
                world.setBlock(x, y, z, this, meta, 2);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public Item getItemDropped(final int i, final Random random, final int i2) {
        return Item.getItemFromBlock(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister ir) {
        blockIcon = ir.registerIcon("taintedmagic:BlockWarpwoodSapling");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(final int side, final int meta) {
        return blockIcon;
    }
}
