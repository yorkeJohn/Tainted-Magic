package taintedmagic.common.blocks;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

public class BlockWarpwoodLog extends BlockRotatedPillar
{
    IIcon top, side, knot;

    public BlockWarpwoodLog ()
    {
        super(Material.wood);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setHardness(2.5F);
        this.setStepSound(soundTypeWood);
        this.setBlockName("BlockWarpwoodLog");
    }

    @SideOnly (Side.CLIENT)
    protected IIcon getTopIcon (int meta)
    {
        return top;
    }

    @SideOnly (Side.CLIENT)
    protected IIcon getSideIcon (int meta)
    {
        return meta == 2 ? knot : side;
    }

    @SideOnly (Side.CLIENT)
    public void registerBlockIcons (IIconRegister ir)
    {
        side = ir.registerIcon("taintedmagic:BlockWarpwoodLog_side");
        top = ir.registerIcon("taintedmagic:BlockWarpwoodLog_top");
        knot = ir.registerIcon("taintedmagic:BlockWarpwoodLog_knot");
    }

    @Override
    public float getBlockHardness (World world, int x, int y, int z)
    {
        if (world.getBlockMetadata(x, y, z) == 2) return 25.0F;
        return super.getBlockHardness(world, x, y, z);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int meta, int fortune)
    {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();

        if (meta == 2) drops.add(new ItemStack(ConfigItems.itemResource, world.rand.nextInt(5) + 1, 17));
        else drops.add(new ItemStack(this, 1, 0));

        return drops;
    }

    public boolean addDestroyEffects (World world, int x, int y, int z, int meta, EffectRenderer ef)
    {
        if (meta == 2)
        {
            if (world.isRemote) particles(world, x, y, z);
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "thaumcraft:craftfail", 1.0F, 1.0F, false);
        }
        return super.addDestroyEffects(world, x, y, z, meta, ef);
    }

    @SideOnly (Side.CLIENT)
    private void particles (World world, int x, int y, int z)
    {
        for (int i = 0; i < 15; i++)
        {
            FXWisp fx = new FXWisp(world, x + 0.5D + (Math.random() - Math.random()) * 0.5D,
                    y + 0.5D + (Math.random() - Math.random()) * 0.5D, z + 0.5D + (Math.random() - Math.random()) * 0.5D,
                    0.25F + (float) Math.random() * 0.25F, 5);

            fx.setGravity(0.01F);

            ParticleEngine.instance.addEffect(world, fx);

            fx.motionX = (fx.posX - x - 0.5D) * 0.025D;
            fx.motionZ = (fx.posZ - z - 0.5D) * 0.025D;
        }
    }

    public boolean canSustainLeaves (IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    public boolean isWood (IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    public int getFlammability (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }

    public int getFireSpreadSpeed (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }
}
