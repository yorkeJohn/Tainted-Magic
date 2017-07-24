package taintedmagic.common.items.equipment;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.ItemRunic;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.Hover;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemVoidwalkerSash extends ItemRunic implements IRunicArmor, IWarpingGear, IBauble
{
	public static final String TAG_MODE = "mode";

	public ItemVoidwalkerSash ()
	{
		super(20);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemVoidwalkerSash");
		this.setMaxDamage(-1);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("ItemVoidwalkerSash");

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.epic;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 2;
	}

	@Override
	public int getRunicCharge (ItemStack s)
	{
		return 20;
	}

	@Override
	public boolean canEquip (ItemStack s, EntityLivingBase e)
	{
		return true;
	}

	@Override
	public boolean canUnequip (ItemStack s, EntityLivingBase e)
	{
		return true;
	}

	@Override
	public BaubleType getBaubleType (ItemStack s)
	{
		return BaubleType.BELT;
	}

	@Override
	public void onEquipped (ItemStack s, EntityLivingBase e)
	{
		Thaumcraft.instance.runicEventHandler.isDirty = true;
	}

	@Override
	public void onUnequipped (ItemStack s, EntityLivingBase e)
	{
		Thaumcraft.instance.runicEventHandler.isDirty = true;
	}

	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		l.add("\u00A78" + StatCollector.translateToLocal("text.sash.mode") + (hasSpeedBoost(s) ? " \u00A7a" : " \u00A7c") + StatCollector.translateToLocal(hasSpeedBoost(s) ? "text.sash.on" : "text.sash.off"));
	}

	@Override
	public void onWornTick (ItemStack s, EntityLivingBase e)
	{
		EntityPlayer p = (EntityPlayer) e;
		if (p.inventory.armorItemInSlot(0) != null && p.inventory.armorItemInSlot(0).getItem() instanceof ItemVoidwalkerBoots)
		{
			if (hasSpeedBoost(s))
			{
				if (p.moveForward > 0.0F)
				{
					if (p.worldObj.isRemote && !p.isSneaking())
					{
						if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(Integer.valueOf(p.getEntityId())))
						{
							Thaumcraft.instance.entityEventHandler.prevStep.put(Integer.valueOf(p.getEntityId()), Float.valueOf(p.stepHeight));
						}
						p.stepHeight = 1.0F;
					}

					if (p.onGround || p.capabilities.isFlying)
					{
						float bonus = 0.4F;
						p.moveFlying(0.0F, 1.0F, p.capabilities.isFlying ? (bonus - 0.050F) : bonus);
					}
					else if (Hover.getHover(p.getEntityId()))
					{
						p.jumpMovementFactor = 0.015F;

					}
					else
					{
						p.jumpMovementFactor = 0.025F;
					}
				}
				if (p.fallDistance > 0.0F) p.fallDistance = 0.0F;
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump (LivingEvent.LivingJumpEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
			ItemStack boots = player.inventory.armorItemInSlot(0);
			if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash)
			{
				if (boots != null && boots.getItem() instanceof ItemVoidwalkerBoots)
				{
					if (hasSpeedBoost(sash))
					{
						player.motionY += 0.15F;
					}
				}
			}
		}
	}

	@Override
	public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
	{
		if (!w.isRemote && p.isSneaking())
		{
			if (s.stackTagCompound == null)
			{
				s.setTagCompound(new NBTTagCompound());
				s.stackTagCompound.setBoolean(TAG_MODE, false);
			}
			if (s.stackTagCompound != null) s.stackTagCompound.setBoolean(TAG_MODE, !s.stackTagCompound.getBoolean(TAG_MODE));
		}
		return s;
	}

	public boolean hasSpeedBoost (ItemStack s)
	{
		if (s.stackTagCompound == null) return true;

		else return s.stackTagCompound.getBoolean(TAG_MODE);
	}
}
