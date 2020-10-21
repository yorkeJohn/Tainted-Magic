package taintedmagic.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import taintedmagic.common.TaintedMagic;
import thaumcraft.common.config.ConfigItems;

public class ItemMaterial extends Item
{
	public int SUBTYPES = 13;
	public IIcon[] icons = new IIcon[SUBTYPES];

	public ItemMaterial ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemMaterial");
		this.setHasSubtypes(true);
	}

	public EnumRarity getRarity (ItemStack s)
	{
		int m = s.getItemDamage();
		switch (m)
		{
		case 0 :
			return EnumRarity.uncommon;
		case 1 :
			return EnumRarity.uncommon;
		case 2 :
			return EnumRarity.uncommon;
		case 3 :
			return EnumRarity.common;
		case 4 :
			return EnumRarity.common;
		case 5 :
			return TaintedMagic.rarityCreation;
		case 6 :
			return EnumRarity.uncommon;
		case 7 :
			return EnumRarity.uncommon;
		case 8 :
			return EnumRarity.uncommon;
		case 9 :
			return EnumRarity.epic;
		case 10 :
			return EnumRarity.epic;
		case 11 :
			return TaintedMagic.rarityCreation;
		case 12:
			return EnumRarity.uncommon;
		}
		return EnumRarity.common;
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		for (int i = 0; i < icons.length; i++)
			this.icons[i] = ir.registerIcon("taintedmagic:ItemMaterial" + i);
	}

	@SideOnly (Side.CLIENT)
	public IIcon getIconFromDamage (int i)
	{
		return this.icons[i];
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);

		if (s.getItemDamage() == 5)
		{
			l.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("text.shard.1"));
		}
	}

	@SideOnly (Side.CLIENT)
	public void getSubItems (Item item, CreativeTabs c, List l)
	{
		for (int i = 0; i < SUBTYPES; i++)
			l.add(new ItemStack(this, 1, i));
	}

	public String getUnlocalizedName (ItemStack s)
	{
		return super.getUnlocalizedName() + "." + s.getItemDamage();
	}
}
