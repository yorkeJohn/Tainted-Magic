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

public class ItemFocusLumos extends ItemFocusBasic {

    private static final AspectList COST = new AspectList().add(Aspect.AIR, 10).add(Aspect.FIRE, 25);

    public ItemFocusLumos() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFocusLumos");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemFocusLumos");
    }

    @Override
    public String getSortingHelper(final ItemStack stack) {
        return "LUMOS" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(final ItemStack stack) {
        return 0xC9D2F8;
    }

    @Override
    public AspectList getVisCost(final ItemStack stack) {
        return COST;
    }

    @Override
    public int getActivationCooldown(final ItemStack stack) {
        return 1000;
    }

    @Override
    public boolean isVisCostPerTick(final ItemStack stack) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(final ItemStack stack) {
        return ItemFocusBasic.WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(
            final ItemStack stack, final World world, final EntityPlayer player, final MovingObjectPosition mop) {
        final ItemWandCasting wand = (ItemWandCasting) stack.getItem();

        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int x = mop.blockX;
            int y = mop.blockY;
            int z = mop.blockZ;

            if (!world.getBlock(x, y, z).getMaterial().isReplaceable()) {
                switch (mop.sideHit) {
                    case 0:
                        y--;
                        break;
                    case 1:
                        y++;
                        break;
                    case 2:
                        z--;
                        break;
                    case 3:
                        z++;
                        break;
                    case 4:
                        x--;
                        break;
                    case 5:
                        x++;
                }
            }
            if (world.getBlock(x, y, z).isReplaceable(world, x, y, z)
                    && wand.consumeAllVis(stack, player, getVisCost(stack), true, false)) {
                if (!world.isRemote) {
                    world.setBlock(x, y, z, BlockRegistry.BlockLumos, 0, 3);
                }
                world.playSoundAtEntity(player, "thaumcraft:ice", 0.3F, 1.1F + world.rand.nextFloat() * 0.1F);

                for (int a = 0; a < 9; a++)
                    if (world.isRemote) {
                        spawnLumosParticles(world, x, y, z);
                    }
            }
        }
        player.swingItem();
        return stack;
    }

    @SideOnly(Side.CLIENT)
    void spawnLumosParticles(final World world, final double x, final double y, final double z) {
        final FXSparkle fx = new FXSparkle(
                world,
                x + world.rand.nextFloat(),
                y + world.rand.nextFloat(),
                z + world.rand.nextFloat(),
                1.75F,
                6,
                3 + world.rand.nextInt(3));
        fx.setGravity(0.1F);
        ParticleEngine.instance.addEffect(world, fx);
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack stack, final int rank) {
        switch (rank) {
            case 1:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal};
            case 2:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal};
            case 3:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal};
            case 4:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal};
            case 5:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal};
        }
        return null;
    }
}
