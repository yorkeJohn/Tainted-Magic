package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;

public class ItemNightshadeBerries extends ItemFood
{
	public ItemNightshadeBerries (int i, float j, boolean k)
	{
		super(i, j, k);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemNightshadeBerries");
		this.setTextureName("taintedmagic:ItemNightshadeBerries");
		this.setAlwaysEdible();
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}

	@Override
	protected void onFoodEaten (ItemStack s, World w, EntityPlayer p)
	{
		super.onFoodEaten(s, w, p);
		if (!w.isRemote) p.attackEntityFrom(new DamageSource("nightshade"), Float.MAX_VALUE);
	}
}
