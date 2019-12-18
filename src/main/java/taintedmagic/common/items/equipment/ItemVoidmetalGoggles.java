package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemGoggles;

public class ItemVoidmetalGoggles extends ItemGoggles implements IWarpingGear
{
	public ItemVoidmetalGoggles (ArmorMaterial m, int j, int k)
	{
		super(m, j, k);
		this.setMaxDamage(250);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemVoidmetalGoggles");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemVoidmetalGoggles");
	}

	public String getArmorTexture (ItemStack s, Entity e, int slot, String type)
	{
		return "taintedmagic:textures/models/ModelVoidmetalGoggles.png";
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.rare;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 5;
	}

	@Override
	public int getVisDiscount (ItemStack stack, EntityPlayer player, Aspect aspect)
	{
		return 10;
	}

	public void onUpdate (ItemStack s, World w, Entity e, int j, boolean k)
	{
		super.onUpdate(s, w, e, j, k);
		if (!w.isRemote && s.isItemDamaged() && e.ticksExisted % 20 == 0 && e instanceof EntityLivingBase) s.damageItem(-1, (EntityLivingBase) e);
	}

	public void onArmorTick (World w, EntityPlayer p, ItemStack s)
	{
		super.onArmorTick(w, p, s);
		if (!w.isRemote && s.getItemDamage() > 0 && p.ticksExisted % 20 == 0) s.damageItem(-1, p);
	}
}
