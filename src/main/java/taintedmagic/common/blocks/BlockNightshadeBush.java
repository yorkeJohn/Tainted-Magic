package taintedmagic.common.blocks;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;

public class BlockNightshadeBush extends BlockBush
{
	public BlockNightshadeBush ()
	{
		super(Material.plants);
		this.setBlockName("BlockNightshadeBush");
		this.setBlockTextureName("taintedmagic:BlockNightshadeBush");
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setStepSound(soundTypeGrass);
	}

	public Item getItemDropped (int a, Random r, int b)
	{
		return ItemRegistry.ItemNightshadeBerries;
	}

	public int quantityDropped (Random r)
	{
		return 1 + r.nextInt(3);
	}

	public void onEntityCollidedWithBlock (World w, int x, int y, int z, Entity e)
	{
		super.onEntityCollidedWithBlock(w, x, y, z, e);

		if (e instanceof EntityLivingBase)
		{
			e.attackEntityFrom(new DamageSource("nightshade"), 1.0F);
			((EntityLivingBase) e).addPotionEffect(new PotionEffect(Potion.poison.id, 100, 0));
		}
	}

	public int getFlammability (IBlockAccess w, int x, int y, int z, ForgeDirection face)
	{
		return 100;
	}

	public int getFireSpreadSpeed (IBlockAccess w, int x, int y, int z, ForgeDirection face)
	{
		return 60;
	}
}
