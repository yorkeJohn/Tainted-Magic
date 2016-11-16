package taintedmagic.common.handler;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import taintedmagic.api.IBloodlust;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.equipment.ItemCrimsonSword;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class TaintedMagicEventHandler
{
	Random randy = new Random();

	@SubscribeEvent
	public void livingTick (LivingEvent.LivingUpdateEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) event.entity;
			updateAttackDamage(p);

			for (int i = 0; i < p.inventory.getSizeInventory(); i++)
			{
				if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).stackTagCompound != null && p.inventory.getStackInSlot(i).stackTagCompound.getBoolean("voidtouched"))
				{
					ItemStack s = p.inventory.getStackInSlot(i);
					if (!p.worldObj.isRemote && s.isItemDamaged() && p.ticksExisted % 20 == 0) s.damageItem(-1, (EntityLivingBase) p);
				}
			}
		}
	}

	/*
	 * Used to upate the attack damage for the Mage's Mace focus
	 */
	public void updateAttackDamage (EntityPlayer p)
	{
		if (!p.worldObj.isRemote)
		{
			IInventory inv = p.inventory;

			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemWandCasting)
				{
					ItemStack s = inv.getStackInSlot(i);
					ItemWandCasting wand = (ItemWandCasting) inv.getStackInSlot(i).getItem();

					if (wand.getFocus(s) != null && wand.getFocus(s) == ItemRegistry.ItemFocusMageMace && wand.getRod(s) instanceof WandRod)
					{
						NBTTagList tags = new NBTTagList();
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

						UUID u = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
						AttributeModifier am = new AttributeModifier(u, "Weapon modifier", 15.0D + wand.getFocusPotency(s), 0);

						tag.setString("Name", am.getName());
						tag.setDouble("Amount", am.getAmount());
						tag.setInteger("Operation", am.getOperation());
						tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
						tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

						tags.appendTag(tag);
						s.stackTagCompound.setTag("AttributeModifiers", tags);
					}
					else if (wand.getRod(s) instanceof WandRod)
					{
						if (!s.hasTagCompound())
						{
							s.setTagCompound(new NBTTagCompound());
						}
						s.stackTagCompound.removeTag("AttributeModifiers");
					}
					if (wand.getFocus(s) != null && wand.getFocus(s) == ItemRegistry.ItemFocusMageMace && wand.getRod(s) instanceof StaffRod)
					{
						NBTTagList tags = new NBTTagList();
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

						UUID u = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
						AttributeModifier am = new AttributeModifier(u, "Weapon modifier", 21.0D + wand.getFocusPotency(s), 0);

						tag.setString("Name", am.getName());
						tag.setDouble("Amount", am.getAmount());
						tag.setInteger("Operation", am.getOperation());
						tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
						tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

						tags.appendTag(tag);
						s.stackTagCompound.setTag("AttributeModifiers", tags);
					}
					else if (wand.getRod(s) instanceof StaffRod)
					{
						NBTTagList tags = new NBTTagList();
						NBTTagCompound tag = new NBTTagCompound();
						tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

						UUID u = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
						AttributeModifier am = new AttributeModifier(u, "Weapon modifier", 6.0D, 0);

						tag.setString("Name", am.getName());
						tag.setDouble("Amount", am.getAmount());
						tag.setInteger("Operation", am.getOperation());
						tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
						tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

						tags.appendTag(tag);
						s.stackTagCompound.setTag("AttributeModifiers", tags);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void entityAttacked (LivingAttackEvent event)
	{
		if (event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) event.source.getEntity();
			if (p.getHeldItem() != null && p.getHeldItem().getItem() instanceof ItemWandCasting)
			{
				ItemStack s = p.getHeldItem();
				ItemWandCasting wand = (ItemWandCasting) s.getItem();

				if (wand.getFocus(s) != null && wand.getFocus(s) instanceof ItemFocusMageMace)
				{
					final AspectList aspects = new AspectList().add(Aspect.EARTH, 20).add(Aspect.ENTROPY, 20).add(Aspect.ORDER, 20);
					if (wand.consumeAllVis(s, p, aspects, true, false))
					{
						wand.consumeAllVis(s, p, aspects, true, false);
					}
					else
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerLogin (PlayerEvent.PlayerLoggedInEvent event)
	{
		if (UpdateHandler.show) event.player.addChatMessage(new ChatComponentText(UpdateHandler.updateStatus));
	}

	@SubscribeEvent
	public void onCrafting (ItemCraftedEvent event)
	{
		if (event.crafting == new ItemStack(ItemRegistry.ItemWandCap, 1, 0))
		{
			EntityItem ent = event.player.entityDropItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), 0);
			ent.motionY += randy.nextFloat() * 0.05F;
			ent.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
			ent.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
		}
		if (event.crafting == new ItemStack(ItemRegistry.ItemWandRod, 1, 0))
		{
			EntityItem ent = event.player.entityDropItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), 0);
			ent.motionY += randy.nextFloat() * 0.05F;
			ent.motionX += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
			ent.motionZ += (randy.nextFloat() - randy.nextFloat()) * 0.1F;
		}
		if (event.crafting.getItem() == ItemRegistry.ItemMaterial && event.crafting.getItemDamage() == 5) giveResearch(event.player);
	}

	public void giveResearch (EntityPlayer player)
	{
		if ( (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION") && (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATIONSHARD") && !player.worldObj.isRemote)))
		{
			Thaumcraft.proxy.getResearchManager().completeResearch(player, "CREATION");
			PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CREATION"), (EntityPlayerMP) player);
			player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("text.1")));
			player.playSound("thaumcraft:wind", 1.0F, 5.0F);
			try
			{
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 80, 0));
				player.addPotionEffect(new PotionEffect(Config.potionBlurredID, 200, 0));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if ( (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "OUTERREV") && (ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION") && !player.worldObj.isRemote)))
			{
				Thaumcraft.proxy.getResearchManager().completeResearch(player, "OUTERREV");
				PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("OUTERREV"), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void livingDrop (LivingDropsEvent event)
	{
		if (event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) event.source.getEntity();
			if (p.getHeldItem() != null && (p.getHeldItem().getItem() instanceof IBloodlust || p.getHeldItem().getItem() instanceof ItemCrimsonSword))
			{
				Random r = new Random();
				ItemStack drops = new ItemStack(ItemRegistry.ItemMaterial, r.nextInt(5), 7);
				addDropItem(event, drops);
			}
			else if (p.getHeldItem() != null && p.getHeldItem().getItem() instanceof ItemWandCasting && ((ItemWandCasting) p.getHeldItem().getItem()).getFocus(p.getHeldItem()) != null && ((ItemWandCasting) p.getHeldItem().getItem()).getFocus(p.getHeldItem()).isUpgradedWith( ((ItemWandCasting) p.getHeldItem().getItem()).getFocusItem(p.getHeldItem()), ItemFocusMageMace.bloodlust))
			{
				Random r = new Random();
				ItemStack drops = new ItemStack(ItemRegistry.ItemMaterial, r.nextInt(5), 7);
				addDropItem(event, drops);
			}
		}
	}

	public void addDropItem (LivingDropsEvent event, ItemStack drop)
	{
		EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, drop);
		entityitem.delayBeforeCanPickup = 10;
		event.drops.add(entityitem);
	}

	@SubscribeEvent
	public void tooltipEvent (ItemTooltipEvent event)
	{
		if (event.itemStack.getItem() instanceof ItemFocusMageMace)
		{
			if (event.toolTip.contains(StatCollector.translateToLocal("item.Focus.cost1")))
			{
				event.toolTip.remove(StatCollector.translateToLocal("item.Focus.cost1"));
				event.toolTip.add(1, StatCollector.translateToLocal("item.Focus.cost3"));
			}
		}
		if (event.itemStack.getItem() instanceof IBloodlust) event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));
		if (event.itemStack.getItem() instanceof ItemCrimsonSword) event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));
		if ( (event.itemStack.getItem() instanceof ItemFocusMageMace && ((ItemFocusBasic) event.itemStack.getItem()).isUpgradedWith(event.itemStack, ItemFocusMageMace.bloodlust)) || (event.itemStack.getItem() instanceof ItemWandCasting && ((ItemWandCasting) event.itemStack.getItem()).getFocus(event.itemStack) != null && ((ItemWandCasting) event.itemStack.getItem()).getFocus(event.itemStack) instanceof ItemFocusMageMace && ((ItemWandCasting) event.itemStack.getItem()).getFocus(event.itemStack).isUpgradedWith( ((ItemWandCasting) event.itemStack.getItem()).getFocusItem(event.itemStack), ItemFocusMageMace.bloodlust))) event.toolTip.add("\u00A74" + StatCollector.translateToLocal("text.bloodlust"));

		if (event.itemStack.stackTagCompound != null && event.itemStack.stackTagCompound.getBoolean("voidtouched")) event.toolTip.add("\u00A75" + StatCollector.translateToLocal("text.voidtouched"));
	}
}
