package taintedmagic.common.items.tools;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.api.IBloodlust;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;

public class ItemPrimordialEdge extends ItemSword implements IWarpingGear, IRepairable
{
	public ItemPrimordialEdge (ToolMaterial m)
	{
		super(m);
		this.setTextureName("taintedmagic:ItemPrimordialEdge");
		this.setUnlocalizedName("ItemPrimordialEdge");
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
	}

	public EnumAction getItemUseAction (ItemStack s)
	{
		return EnumAction.block;
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return TaintedMagic.rarityCreation;
	}

	public boolean hitEntity (ItemStack s, EntityLivingBase e, EntityLivingBase p)
	{
		super.hitEntity(s, e, p);

		try
		{
			e.addPotionEffect(new PotionEffect(Potion.wither.id, 5, 1));
			e.addPotionEffect(new PotionEffect(Potion.weakness.id, 5, 1));
			p.worldObj.playSoundAtEntity(p, "thaumcraft:swing", 1.0F, 1.0F);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
	{
		p.setItemInUse(s, this.getMaxItemUseDuration(s));
		return s;
	}

	@Override
	public void onUsingTick (ItemStack s, EntityPlayer p, int count)
	{
		Iterator i$;

		List ents = p.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(p.posX, p.posY, p.posZ, p.posX + 1, p.posY + 1, p.posZ + 1).expand(15.0D, 15.0D, 15.0D));

		p.worldObj.playSoundAtEntity(p, "thaumcraft:brain", 0.05F, 0.5F);
		if ( (ents != null) && (ents.size() > 0)) for (i$ = ents.iterator(); i$.hasNext();)
		{
			Object ent = i$.next();
			Entity eo = (Entity) ent;

			if (eo != p)
			{
				if ( (eo.isEntityAlive()) && (!eo.isEntityInvulnerable()))
				{
					double d = pointDistanceSpace(eo.posX, eo.posY, eo.posZ, p);
					if (d < 2.0D)
					{
						eo.attackEntityFrom(DamageSource.magic, 1.0F);
					}

				}
				double var3 = (p.posX + 0.5D - eo.posX) / 20.0D;
				double var5 = (p.posY + 0.5D - eo.posY) / 20.0D;
				double var7 = (p.posZ + 0.5D - eo.posZ) / 20.0D;
				double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
				double var11 = 1.0D - var9;

				if (var11 > 0.0D)
				{
					var11 *= var11;
					eo.motionX += var3 / var9 * var11 * 0.20D;
					eo.motionY += var5 / var9 * var11 * 0.30D;
					eo.motionZ += var7 / var9 * var11 * 0.20D;
				}
			}
		}
	}

	@Override
	public void onUpdate (ItemStack s, World w, Entity e, int i, boolean b)
	{
		if ( (!w.isRemote) && (s.isItemDamaged()) && (e.ticksExisted % 20 == 0))
		{
			s.damageItem(-1, (EntityLivingBase) e);
		}
	}

	public double pointDistanceSpace (double x, double y, double z, EntityPlayer p)
	{
		double xvar = p.posX + 0.5D - x;
		double yvar = p.posY + 0.5D - y;
		double zvar = p.posZ + 0.5D - z;
		return xvar * xvar + yvar * yvar + zvar * zvar;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		l.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("text.eldritchsapping"));
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 5;
	}
}
