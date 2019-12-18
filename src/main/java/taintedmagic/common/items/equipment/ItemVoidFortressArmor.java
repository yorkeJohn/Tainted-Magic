package taintedmagic.common.items.equipment;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.ItemApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class ItemVoidFortressArmor extends ItemFortressArmor implements IWarpingGear, IVisDiscountGear
{
	public ItemVoidFortressArmor (ArmorMaterial m, int j, int k)
	{
		super(m, j, k);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.iconHelm = ir.registerIcon("taintedmagic:ItemVoidFortressHelmet");
		this.iconChest = ir.registerIcon("taintedmagic:ItemVoidFortressChestplate");
		this.iconLegs = ir.registerIcon("taintedmagic:ItemVoidFortressLeggings");
	}

	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		l.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(s, p, null) + "%");
		super.addInformation(s, p, l, b);
	}

	public String getArmorTexture (ItemStack s, Entity e, int slot, String t)
	{
		return "taintedmagic:textures/models/ModelVoidFortressArmor.png";
	}

	public boolean getIsRepairable (ItemStack s, ItemStack s2)
	{
		return s2.isItemEqual(ItemApi.getItem("itemResource", 16)) ? true : super.getIsRepairable(s, s2);
	}

	public void onUpdate (ItemStack s, World w, Entity e, int j, boolean k)
	{
		super.onUpdate(s, w, e, j, k);

		if ( (!w.isRemote) && (s.isItemDamaged()) && (e.ticksExisted % 20 == 0) && ( (e instanceof EntityLivingBase))) s.damageItem(-1, (EntityLivingBase) e);
	}

	public void onArmorTick (World w, EntityPlayer p, ItemStack s)
	{
		super.onArmorTick(w, p, s);
		if ( (!w.isRemote) && (s.getItemDamage() > 0) && (p.ticksExisted % 20 == 0)) s.damageItem(-1, p);
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 3;
	}

	@Override
	public int getVisDiscount (ItemStack s, EntityPlayer p, Aspect a)
	{
		return 5;
	}
}