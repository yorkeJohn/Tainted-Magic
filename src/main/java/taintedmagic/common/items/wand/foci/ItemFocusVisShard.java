package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityHomingShard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

/**
 * this class is based off of ItemFocusShard.class created by <Azanor> as part
 * of Thaumcraft 5
 */
public class ItemFocusVisShard extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 100).add(Aspect.AIR, 100);
	private static final AspectList costPersistent = new AspectList().add(Aspect.FIRE, 100).add(Aspect.ENTROPY, 100).add(Aspect.WATER, 100).add(Aspect.AIR, 100);

	public ItemFocusVisShard ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusVisShard");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusVisShard");
	}

	public String getSortingHelper (ItemStack s)
	{
		return "SHARD" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 10037693;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 300;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.WAVE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		Entity look = EntityUtils.getPointedEntity(p.worldObj, p, 0.0D, 32.0D, 1.1F);
		if (look != null && look instanceof EntityLivingBase)
		{
			if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
			{
				EntityHomingShard shard = new EntityHomingShard(w, p, (EntityLivingBase) look, wand.getFocusPotency(s), isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.persistent));
				if (!w.isRemote) w.spawnEntityInWorld(shard);
				w.playSoundAtEntity(shard, "taintedmagic:shard", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);

				for (int a = 0; a < 18; a++)
				{
					TaintedMagic.proxy.spawnLumosParticles(w, shard.posX, shard.posY, shard.posZ, 0);
				}
			}
			p.swingItem();
		}
		return s;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, FocusUpgrades.persistent) ? costPersistent : cost;
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int r)
	{
		switch (r)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency, FocusUpgrades.persistent };
		}
		return null;
	}
}
