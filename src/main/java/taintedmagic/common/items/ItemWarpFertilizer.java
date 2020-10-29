package taintedmagic.common.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.common.blocks.BlockCustomPlant;

public class ItemWarpFertilizer extends Item
{
    public ItemWarpFertilizer ()
    {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemWarpFertilizer");
        this.setTextureName("taintedmagic:ItemWarpFertilizer");

        FMLCommonHandler.instance().bus().register(this);
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    /**
     * Turn Silverwood Saplings into Warpwood Saplings
     */
    @Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
            float hitY, float hitZ)
    {
        super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
        if (world.getBlock(x, y, z) instanceof BlockCustomPlant && world.getBlockMetadata(x, y, z) == 1)
        {
            if (world.isRemote) particles(world, x, y, z, 10 + world.rand.nextInt(11));

            world.playSound((double) x, (double) y, (double) z, "thaumcraft:roots", 1.0F, 1.0F, false);
            world.setBlock(x, y, z, BlockRegistry.BlockWarpwoodSapling, 0, 1 | 2);
            stack.stackSize--;

            return true;
        }
        else return false;
    }

    @SideOnly (Side.CLIENT)
    public void particles (World world, int x, int y, int z, int count)
    {
        for (int i = 0; i < count; i++)
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
}
