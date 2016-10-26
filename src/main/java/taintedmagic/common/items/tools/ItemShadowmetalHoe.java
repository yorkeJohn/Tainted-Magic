package taintedmagic.common.items.tools;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemShadowmetalHoe extends ItemHoe implements IRepairable
{
	public ItemShadowmetalHoe (ToolMaterial m)
	{
		super(m);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemShadowmetalHoe");
		this.setUnlocalizedName("ItemShadowmetalHoe");
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public boolean getIsRepairable (ItemStack s, ItemStack s2)
	{
		return (s2.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial)) && s2.getItemDamage() == 0) ? true : super.getIsRepairable(s, s2);
	}

	@Override
	public boolean onItemUse (ItemStack s, EntityPlayer p, World w, int x, int y, int z, int i, float a, float b, float c)
	{
		super.onItemUse(s, p, w, x, y, z, i, a, b, c);
		if (!p.worldObj.isRemote)
		{
			Block block = Blocks.dirt;

			if (p.worldObj.getBlock(x, y, z) == Blocks.farmland)
			{
				p.worldObj.setBlock(x, y, z, Blocks.dirt);
				s.damageItem(1, p);
				w.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			}
			else if (p.worldObj.getBlock(x, y, z) == Blocks.dirt || p.worldObj.getBlock(x, y, z) == Blocks.grass)
			{
				p.worldObj.setBlock(x, y, z, Blocks.farmland);
				s.damageItem(1, p);
				w.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
			}
		}
		if (p.worldObj.getBlock(x, y, z) == Blocks.dirt || p.worldObj.getBlock(x, y, z) == Blocks.grass || p.worldObj.getBlock(x, y, z) == Blocks.farmland) p.swingItem();
		return false;
	}

	@SubscribeEvent
	public void useHoe (UseHoeEvent event)
	{
		if (event.current.getItem() == ItemRegistry.ItemShadowmetalHoe)
		{
			event.setCanceled(true);
		}
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}
}
