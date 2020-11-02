package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusLumos extends ItemFocusBasic
{
    private static final AspectList COST = new AspectList().add(Aspect.AIR, 10).add(Aspect.FIRE, 25);

    public ItemFocusLumos ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemFocusLumos");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemFocusLumos");
    }

    public String getSortingHelper (ItemStack stack)
    {
        return "LUMOS" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0xC9D2F8;
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return this.COST;
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return 1000;
    }

    public boolean isVisCostPerTick (ItemStack stack)
    {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;

            if (!world.getBlock(x, y, z).getMaterial().isReplaceable()) switch (mop.sideHit)
            {
            case 0 :
                y--;
                break;
            case 1 :
                y++;
                break;
            case 2 :
                z--;
                break;
            case 3 :
                z++;
                break;
            case 4 :
                x--;
                break;
            case 5 :
                x++;
            }
            if (world.getBlock(x, y, z).isReplaceable(world, x, y, z))
            {
                if (wand.consumeAllVis(stack, player, getVisCost(stack), true, false))
                {
                    if (!world.isRemote) world.setBlock(x, y, z, BlockRegistry.BlockLumos, 0, 3);
                    world.playSoundAtEntity(player, "thaumcraft:ice", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F);

                    for (int a = 0; a < 9; a++)
                        if (world.isRemote) spawnLumosParticles(world, x, y, z);
                }
            }
        }
        player.swingItem();
        return stack;
    }

    @SideOnly (Side.CLIENT)
    void spawnLumosParticles (World world, double x, double y, double z)
    {
        FXSparkle fx = new FXSparkle(world, x + world.rand.nextFloat(), y + world.rand.nextFloat(), z + world.rand.nextFloat(),
                1.75F, 6, 3 + world.rand.nextInt(3));
        fx.setGravity(0.1F);
        ParticleEngine.instance.addEffect(world, fx);
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack stack, int rank)
    {
        switch (rank)
        {
        case 1 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
        case 2 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
        case 3 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
        case 4 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
        case 5 :
            return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
        }
        return null;
    }
}
