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
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;

public class ItemSalis extends Item
{
	public int SUBTYPES = 2;
	public IIcon[] icons = new IIcon[SUBTYPES];

	public ItemSalis ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.hasSubtypes = true;
		this.setUnlocalizedName("ItemSalis");
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return TaintedMagic.rarityCreation;
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		for (int i = 0; i < icons.length; i++)
			this.icons[i] = ir.registerIcon("taintedmagic:ItemSalis" + i);
	}

	@SideOnly (Side.CLIENT)
	public IIcon getIconFromDamage (int i)
	{
		return this.icons[i];
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

	@Override
	public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
	{
		int m = s.getItemDamage();
		switch (m)
		{
		// Weather
		case 0 :
		{
			w.getWorldInfo().setRainTime(w.isRaining() ? 24000 : 0);
			w.getWorldInfo().setRaining(!w.isRaining());
			p.playSound("thaumcraft:wind", 0.3F, 1.0F + w.rand.nextFloat() * 0.5F);
			p.inventory.decrStackSize(p.inventory.currentItem, 1);
			for (int a = 0; a < 15; a++)
			{
				FXSparkle fx = new FXSparkle(w, p.posX + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D, p.boundingBox.minY + 1.25D + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D,
						p.posZ + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D, 1.75F, 7, 3 + w.rand.nextInt(3));
				Vec3 look = p.getLookVec();
				fx.motionX += (look.xCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.motionY += (look.yCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.motionZ += (look.zCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.setGravity(0.5F);
				ParticleEngine.instance.addEffect(w, fx);
			}
			return s;
		}
		// Time
		case 1 :
		{
			w.setWorldTime(w.isDaytime() ? 14000 : 24000);
			p.playSound("thaumcraft:wind", 0.3F, 1.0F + w.rand.nextFloat() * 0.5F);
			p.inventory.decrStackSize(p.inventory.currentItem, 1);
			for (int a = 0; a < 15; a++)
			{
				FXSparkle fx = new FXSparkle(w, p.posX + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D, p.boundingBox.minY + 1.25D + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D,
						p.posZ + (w.rand.nextDouble() - w.rand.nextDouble()) * 0.2D, 1.75F, 6, 3 + w.rand.nextInt(3));
				Vec3 look = p.getLookVec();
				fx.motionX += (look.xCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.motionY += (look.yCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.motionZ += (look.zCoord * 0.25D) + w.rand.nextGaussian() * 0.0075D * 5.0D;
				fx.setGravity(0.5F);
				ParticleEngine.instance.addEffect(w, fx);
			}
			return s;
		}
		}
		return s;
	}
}
