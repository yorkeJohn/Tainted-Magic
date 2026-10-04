package taintedmagic.common.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.TaintedMagic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.common.config.ConfigItems;

public class BlockWarpwoodLog extends BlockRotatedPillar {

    private IIcon top;
    private IIcon side;
    private IIcon knot;

    public BlockWarpwoodLog() {
        super(Material.wood);
        setCreativeTab(TaintedMagic.tabTM);
        setHardness(2.5F);
        setStepSound(soundTypeWood);
        setBlockName("BlockWarpwoodLog");
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(final int meta) {
        return top;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(final int meta) {
        return meta == 2 ? knot : side;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister ir) {
        side = ir.registerIcon("taintedmagic:BlockWarpwoodLog_side");
        top = ir.registerIcon("taintedmagic:BlockWarpwoodLog_top");
        knot = ir.registerIcon("taintedmagic:BlockWarpwoodLog_knot");
    }

    @Override
    public float getBlockHardness(final World world, final int x, final int y, final int z) {
        if (world.getBlockMetadata(x, y, z) == 2) return 25.0F;
        return super.getBlockHardness(world, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops(
            final World world, final int x, final int y, final int z, final int meta, final int fortune) {
        final ArrayList<ItemStack> drops = new ArrayList<>();

        if (meta == 2) {
            drops.add(new ItemStack(ConfigItems.itemResource, world.rand.nextInt(5) + 1, 17));
        } else {
            drops.add(new ItemStack(this, 1, 0));
        }
        return drops;
    }

    @Override
    public boolean addDestroyEffects(
            final World world, final int x, final int y, final int z, final int meta, final EffectRenderer ef) {
        if (meta == 2) {
            if (world.isRemote) {
                particles(world, x, y, z);
            }
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "thaumcraft:craftfail", 1.0F, 1.0F, false);
        }
        return super.addDestroyEffects(world, x, y, z, meta, ef);
    }

    @SideOnly(Side.CLIENT)
    private void particles(final World world, final int x, final int y, final int z) {
        for (int i = 0; i < 15; i++) {
            final FXWisp fx = new FXWisp(
                    world,
                    x + 0.5D + (Math.random() - Math.random()) * 0.5D,
                    y + 0.5D + (Math.random() - Math.random()) * 0.5D,
                    z + 0.5D + (Math.random() - Math.random()) * 0.5D,
                    0.25F + (float) Math.random() * 0.25F,
                    5);

            fx.setGravity(0.01F);

            ParticleEngine.instance.addEffect(world, fx);

            fx.motionX = (fx.posX - x - 0.5D) * 0.025D;
            fx.motionZ = (fx.posZ - z - 0.5D) * 0.025D;
        }
    }

    @Override
    public boolean canSustainLeaves(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public boolean isWood(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }

    @Override
    public int getFlammability(
            final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeed(
            final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection face) {
        return 0;
    }
}
