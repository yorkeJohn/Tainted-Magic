package taintedmagic.common.blocks;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.world.BlockEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;

public class BlockNightshadeBush extends BlockBush
{
    public BlockNightshadeBush ()
    {
        super(Material.plants);
        this.setBlockName("BlockNightshadeBush");
        this.setBlockTextureName("taintedmagic:BlockNightshadeBush");
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setStepSound(soundTypeGrass);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public Item getItemDropped (int a, Random random, int b)
    {
        return ItemRegistry.ItemNightshadeBerries;
    }

    public int quantityDropped (Random random)
    {
        return 1 + random.nextInt(3);
    }

    public void onEntityCollidedWithBlock (World world, int x, int y, int z, Entity entity)
    {
        super.onEntityCollidedWithBlock(world, x, y, z, entity);

        if (entity instanceof EntityLivingBase)
        {
            entity.attackEntityFrom(new DamageSource("nightshade"), 1.0F);
            try
            {
                ((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, 140, 0));
            }
            catch (Exception e)
            {
            }
        }
    }

    public int getFlammability (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }

    public int getFireSpreadSpeed (IBlockAccess world, int x, int y, int z, ForgeDirection face)
    {
        return 0;
    }

    @SubscribeEvent
    public void onBreak (BlockEvent.HarvestDropsEvent event)
    {
        if (event.harvester != null && event.harvester.getHeldItem() != null
                && event.harvester.getHeldItem().getItem() instanceof ItemShears)
        {
            event.drops.clear();
            event.drops.add(new ItemStack(BlockRegistry.BlockNightshadeBush, 1, 0));
        }
    }
}
