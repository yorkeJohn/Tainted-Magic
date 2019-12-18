package taintedmagic.common.items.equipment;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class ItemShadowFortressArmor extends ItemFortressArmor implements IWarpingGear, IVisDiscountGear, ISpecialArmor
{
	public ItemShadowFortressArmor (ArmorMaterial m, int j, int k)
	{
		super(m, j, k);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.iconHelm = ir.registerIcon("taintedmagic:ItemShadowFortressHelmet");
		this.iconChest = ir.registerIcon("taintedmagic:ItemShadowFortressChestplate");
		this.iconLegs = ir.registerIcon("taintedmagic:ItemShadowFortressLeggings");
	}

	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		l.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": " + getVisDiscount(s, p, null) + "%");
		super.addInformation(s, p, l, b);
	}

	public String getArmorTexture (ItemStack s, Entity e, int slot, String t)
	{
		return "taintedmagic:textures/models/ModelShadowFortressArmor.png";
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.epic;
	}

	public boolean getIsRepairable (ItemStack s, ItemStack s2)
	{
		return (s2.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial)) && s2.getItemDamage() == 0) ? true : super.getIsRepairable(s, s2);
	}

	@Override
	public int getVisDiscount (ItemStack s, EntityPlayer p, Aspect a)
	{
		return 5;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 5;
	}

	@Override
	public ISpecialArmor.ArmorProperties getProperties (EntityLivingBase p, ItemStack s, DamageSource source, double dmg, int slot)
	{
		int priority = 0;
		double ratio = this.damageReduceAmount / 25.0D;

		if (source.isMagicDamage() == true)
		{
			priority = 1;
			ratio = this.damageReduceAmount / 35.0D;
		}
		else if ( (source.isFireDamage() == true) || (source.isExplosion()))
		{
			priority = 1;
			ratio = this.damageReduceAmount / 20.0D;
		}
		else if (source.isUnblockable())
		{
			priority = 0;
			ratio = 0.0D;
		}

		if ( (p instanceof EntityPlayer))
		{
			double set = 0.875D;
			for (int a = 1; a < 4; a++)
			{
				ItemStack piece = ((EntityPlayer) p).inventory.armorInventory[a];
				if ( (piece != null) && ( (piece.getItem() instanceof ItemFortressArmor)))
				{
					set += 0.250D;
					if ( (piece.hasTagCompound()) && (piece.stackTagCompound.hasKey("mask")))
					{
						set += 0.05D;
					}
				}
			}
			ratio *= set;
		}

		return new ISpecialArmor.ArmorProperties(priority, ratio, s.getMaxDamage() + 1 - s.getItemDamage());
	}

	public int getArmorDisplay (EntityPlayer p, ItemStack s, int slot)
	{
		return this.damageReduceAmount;
	}

	public void damageArmor (EntityLivingBase e, ItemStack s, DamageSource source, int dmg, int slot)
	{
		if (source != DamageSource.fall) s.damageItem(dmg, e);
	}
}