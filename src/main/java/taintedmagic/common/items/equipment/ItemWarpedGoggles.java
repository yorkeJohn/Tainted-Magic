package taintedmagic.common.items.equipment;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IGoggles;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.common.items.armor.ItemGoggles;

public class ItemWarpedGoggles extends ItemGoggles implements IWarpingGear
{
	public ItemWarpedGoggles (ArmorMaterial m, int j, int k)
	{
		super(m, j, k);
		this.setMaxDamage(500);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemWarpedGoggles");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemWarpedGoggles");
	}

	public String getArmorTexture (ItemStack s, Entity e, int slot, String type)
	{
		return "taintedmagic:textures/models/ModelWarpedGoggles.png";
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 1;
	}
}
