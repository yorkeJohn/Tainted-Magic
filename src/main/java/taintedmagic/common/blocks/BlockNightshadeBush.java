package taintedmagic.common.blocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;

public class BlockNightshadeBush extends BlockBush {

    public BlockNightshadeBush() {
        super(Material.plants);
        setBlockName("BlockNightshadeBush");
        setBlockTextureName("taintedmagic:BlockNightshadeBush");
        setCreativeTab(TaintedMagic.tabTM);
        setStepSound(soundTypeGrass);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Item getItemDropped(final int a, final Random random, final int b) {
        return ItemRegistry.ItemNightshadeBerries;
    }

    @Override
    public int quantityDropped(final Random random) {
        return 1 + random.nextInt(3);
    }

    @Override
    public void onEntityCollidedWithBlock(
            final World world, final int x, final int y, final int z, final Entity entity) {
        super.onEntityCollidedWithBlock(world, x, y, z, entity);

        if (entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(new DamageSource("nightshade"), 1.0F);
            ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 140, 0));
        }
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

    @SubscribeEvent
    public void onBreak(final BlockEvent.HarvestDropsEvent event) {
        if (event.block instanceof BlockNightshadeBush
                && event.harvester != null
                && event.harvester.getHeldItem() != null
                && event.harvester.getHeldItem().getItem() instanceof ItemShears) {
            event.drops.clear();
            event.drops.add(new ItemStack(BlockRegistry.BlockNightshadeBush, 1, 0));
        }
    }
}
