package taintedmagic.common.items.wand.foci;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.items.wands.ItemWandCasting;

public class ItemFocusLumos extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.AIR, 500).add(Aspect.FIRE, 500);

	public ItemFocusLumos ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusLumos");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusLumos");
	}

	public String getSortingHelper (ItemStack s)
	{
		return "LUMOS" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0xC9D2F8;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);
	}

	public AspectList getVisCost (ItemStack s)
	{
		return this.cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 1000;
	}

	public boolean isVisCostPerTick (ItemStack s)
	{
		return false;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.WAVE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
		{
			int x = mop.blockX;
			int y = mop.blockY;
			int z = mop.blockZ;

			if (!w.getBlock(x, y, z).getMaterial().isReplaceable()) switch (mop.sideHit)
			{
			case 0 :
				y--;
				break;
			case 1 :
				y++;
				break;
			case 2 :
				z--;
				break;
			case 3 :
				z++;
				break;
			case 4 :
				x--;
				break;
			case 5 :
				x++;
			}
			if (w.getBlock(x, y, z).isReplaceable(w, x, y, z))
			{
				if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
				{
					if (!w.isRemote) w.setBlock(x, y, z, BlockRegistry.BlockLumos, 0, 3);
					w.playSoundAtEntity(p, "thaumcraft:ice", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);

					for (int a = 0; a < 9; a++)
						if (w.isRemote) spawnLumosParticles(w, x, y, z);
				}
			}
		}
		p.swingItem();
		return s;
	}

	@SideOnly (Side.CLIENT)
	void spawnLumosParticles (World w, double x, double y, double z)
	{
		FXSparkle fx = new FXSparkle(w, x + w.rand.nextFloat(), y + w.rand.nextFloat(), z + w.rand.nextFloat(), 1.75F, 6, 3 + w.rand.nextInt(3));
		fx.setGravity(0.1F);
		ParticleEngine.instance.addEffect(w, fx);
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int r)
	{
		switch (r)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal };
		}
		return null;
	}
}
