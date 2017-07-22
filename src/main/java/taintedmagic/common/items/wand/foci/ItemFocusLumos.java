package taintedmagic.common.items.wand.foci;

import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.blocks.BlockLumos;
import taintedmagic.common.entities.EntityGlowpet;
import taintedmagic.common.registry.BlockRegistry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.items.wands.ItemWandCasting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusLumos extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.AIR, 500).add(Aspect.FIRE, 500);
	private static final AspectList costGlowpet = new AspectList().add(Aspect.ORDER, 500).add(Aspect.AIR, 500).add(Aspect.FIRE, 1000);
	private static final AspectList costMaxima = new AspectList().add(Aspect.ORDER, 1000).add(Aspect.FIRE, 1500).add(Aspect.AIR, 500);

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
		return "GLOWPET" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0xFFC27C;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);
		if (this.isUpgradedWith(s, FocusUpgrades.maxima))
		{
			l.add(" ");
			l.add(EnumChatFormatting.BLUE + "+" + new String(this.isUpgradedWith(s, FocusUpgradeType.enlarge) ? Integer.toString(11 + this.getUpgradeLevel(s, FocusUpgradeType.enlarge)) : "11") + " " + StatCollector.translateToLocal("text.radius"));
		}
		if (this.isUpgradedWith(s, FocusUpgrades.glowpet))
		{
			l.add(" ");
			l.add(EnumChatFormatting.BLUE + new String(this.isUpgradedWith(s, FocusUpgradeType.extend) ? Integer.toString(1 + this.getUpgradeLevel(s, FocusUpgradeType.extend)) : "1") + "x " + StatCollector.translateToLocal("text.burnTime"));
		}
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, FocusUpgrades.maxima) ? this.costMaxima : isUpgradedWith(s, FocusUpgrades.glowpet) ? this.costGlowpet : this.cost;
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

		if (!w.isRemote)
		{
			if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !p.isSneaking())
			{
				if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
				{
					int x = mop.blockX;
					int y = mop.blockY;
					int z = mop.blockZ;

					switch (mop.sideHit)
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
					w.setBlock(x, y, z, BlockRegistry.BlockLumos, 2, 3);
					w.playSoundAtEntity(p, "thaumcraft:ice", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);
					for (int a = 0; a < 9; a++)
					{
						spawnParticles(w, x, y, z);
					}
				}
			}
			else
			{
				if (isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.glowpet))
				{
					if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
					{
						EntityGlowpet pet = new EntityGlowpet(w, p, wand.getFocusExtend(s) + 1);
						w.spawnEntityInWorld(pet);
						w.playSoundAtEntity(pet, "taintedmagic:shard", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);
						for (int a = 0; a < 18; a++)
						{
							spawnParticles(w, pet.posX, pet.posY, pet.posZ);
						}
					}
				}
				else if (isUpgradedWith(wand.getFocusItem(s), FocusUpgrades.maxima))
				{
					int r = 11 + wand.getFocusEnlarge(s);
					if (wand.consumeAllVis(s, p, getVisCost(s), true, false))
					{
						for (int xOff = -r; xOff <= r; xOff++)
						{
							for (int yOff = -r; yOff <= r; yOff++)
							{
								for (int zOff = -r; zOff <= r; zOff++)
								{
									int x = (int) (p.posX + xOff);
									int y = (int) (p.posY + yOff);
									int z = (int) (p.posZ + zOff);

									if (w.isAirBlock(x, y, z) && w.getBlock(x, y, z) != BlockRegistry.BlockLumos && w.getBlockLightValue(x, y, z) < 9)
									{
										BlockLumos src = (BlockLumos) BlockRegistry.BlockLumos;
										src.setGlowDuration(200);
										w.setBlock(x, y, z, src, 1, 3);
										if (w.rand.nextInt(12) == 0)
										{
											for (int a = 0; a < 9; a++)
											{
												spawnParticles(w, x, y, z);
											}
										}
									}
								}
							}
						}
						w.playSoundAtEntity(p, "thaumcraft:ice", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);
					}
				}
			}
		}
		p.swingItem();
		return s;
	}
	
	@SideOnly(Side.CLIENT)
	void spawnParticles(World w, double x, double y, double z)
	{
		FXSparkle fx = new FXSparkle(w, x + w.rand.nextFloat(), y + w.rand.nextFloat(), z + w.rand.nextFloat(), 1.75F, w.rand.nextInt(5), 3 + w.rand.nextInt(3));
		fx.setGravity(0.1F);
		ParticleEngine.instance.addEffect(w, fx);
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int r)
	{
		switch (r)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgrades.glowpet, FocusUpgrades.maxima, FocusUpgradeType.frugal };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, isUpgradedWith(s, FocusUpgrades.glowpet) ? FocusUpgradeType.extend : isUpgradedWith(s, FocusUpgrades.maxima) ? FocusUpgradeType.enlarge : null };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, isUpgradedWith(s, FocusUpgrades.glowpet) ? FocusUpgradeType.extend : isUpgradedWith(s, FocusUpgrades.maxima) ? FocusUpgradeType.enlarge : null };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, isUpgradedWith(s, FocusUpgrades.glowpet) ? FocusUpgradeType.extend : isUpgradedWith(s, FocusUpgrades.maxima) ? FocusUpgradeType.enlarge : null };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, isUpgradedWith(s, FocusUpgrades.glowpet) ? FocusUpgradeType.extend : isUpgradedWith(s, FocusUpgrades.maxima) ? FocusUpgradeType.enlarge : null };
		}
		return null;
	}

	public boolean canApplyUpgrade (ItemStack s, EntityPlayer p, FocusUpgradeType t, int rank)
	{
		return !t.equals(FocusUpgradeType.enlarge) || ((ItemFocusBasic) s.getItem()).isUpgradedWith(s, FocusUpgrades.maxima);
	}
}
