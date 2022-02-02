package taintedmagic.common.handler;

import java.util.UUID;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.common.items.equipment.ItemLumosRing;
import taintedmagic.common.items.tools.ItemHollowDagger;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandRod;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;

public class TMEventHandler {

    @SubscribeEvent
    public void playerTick (final LivingEvent.LivingUpdateEvent event) {
        if (event.entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) event.entity;

            repairItems(player);
            applyNightVision(player);
            modifyAttackDamage(player);
        }
    }

    /**
     * Repair "Voidtouched" items
     */
    public void repairItems (final EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if (!player.worldObj.isRemote && stack != null && stack.stackTagCompound != null
                    && stack.stackTagCompound.getBoolean("voidtouched") && stack.isItemDamaged())
                if (player.ticksExisted % 20 == 0) {
                    stack.setItemDamage(stack.getItemDamage() - 1);
                }
        }
    }

    private boolean hasNightVision = false;

    /**
     * Apply Night Vision effect when the player is holding a wand or staff
     * with the Lumos focus equipped or when the Lumos ring is equipped.
     */
    public void applyNightVision (final EntityPlayer player) {
        if (player.worldObj.isRemote)
            return;

        boolean lumos = false;

        final IInventory baub = BaublesApi.getBaubles(player);
        if (baub.getStackInSlot(1) != null && baub.getStackInSlot(1).getItem() instanceof ItemLumosRing
                || baub.getStackInSlot(2) != null && baub.getStackInSlot(2).getItem() instanceof ItemLumosRing) {
            lumos = true;
        }
        else if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting) {
            final ItemStack held = player.getHeldItem();
            final ItemWandCasting wand = (ItemWandCasting) held.getItem();

            if (wand.getFocus(held) != null && wand.getFocus(held) == ItemRegistry.ItemFocusLumos) {
                lumos = true;
            }
        }

        if (lumos) {
            if (!hasNightVision) {
                if (!player.isPotionActive(Potion.nightVision.id)) {
                    player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 260, -1));
                    hasNightVision = true;
                }
            }
            else if (player.ticksExisted % 20 == 0) {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 260, -1));
            }
        }
        else if (hasNightVision) {
            player.removePotionEffect(Potion.nightVision.id);
            hasNightVision = false;
        }
    }

    /*
     * Some hacky code to make the Mage's Mace work...
     */
    public void modifyAttackDamage (final EntityPlayer player) {
        if (player.worldObj.isRemote)
            return;

        final IInventory inv = player.inventory;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemWandCasting) {
                final ItemStack stack = inv.getStackInSlot(i);
                final ItemWandCasting wand = (ItemWandCasting) inv.getStackInSlot(i).getItem();

                if (wand.getFocus(stack) != null && wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace
                        && wand.getRod(stack) instanceof WandRod) {
                    final NBTTagList tags = new NBTTagList();
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                    final UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                    final AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier",
                            ConfigHandler.MAGE_MACE_DMG_INC_BASE + wand.getFocusPotency(stack), 0);

                    tag.setString("Name", am.getName());
                    tag.setDouble("Amount", am.getAmount());
                    tag.setInteger("Operation", am.getOperation());
                    tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
                    tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

                    tags.appendTag(tag);
                    stack.stackTagCompound.setTag("AttributeModifiers", tags);
                }
                else if (wand.getRod(stack) instanceof WandRod) {
                    if (!stack.hasTagCompound()) {
                        stack.setTagCompound(new NBTTagCompound());
                    }
                    stack.stackTagCompound.removeTag("AttributeModifiers");
                }
                if (wand.getFocus(stack) != null && wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace
                        && wand.getRod(stack) instanceof StaffRod) {
                    final NBTTagList tags = new NBTTagList();
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                    final UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                    final AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier",
                            5.0D + ConfigHandler.MAGE_MACE_DMG_INC_BASE + wand.getFocusPotency(stack), 0);

                    tag.setString("Name", am.getName());
                    tag.setDouble("Amount", am.getAmount());
                    tag.setInteger("Operation", am.getOperation());
                    tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
                    tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

                    tags.appendTag(tag);
                    stack.stackTagCompound.setTag("AttributeModifiers", tags);
                }
                else if (wand.getRod(stack) instanceof StaffRod) {
                    final NBTTagList tags = new NBTTagList();
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                    final UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                    final AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier", 6.0D, 0);

                    tag.setString("Name", am.getName());
                    tag.setDouble("Amount", am.getAmount());
                    tag.setInteger("Operation", am.getOperation());
                    tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
                    tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

                    tags.appendTag(tag);
                    stack.stackTagCompound.setTag("AttributeModifiers", tags);
                }
            }
        }
    }

    @SubscribeEvent
    public void entityAttacked (final LivingAttackEvent event) {
        if (event.source.getEntity() instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) event.source.getEntity();

            /**
             * Consume vis for Mage's Mace hits
             */
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting) {
                final ItemStack held = player.getHeldItem();
                final ItemWandCasting wand = (ItemWandCasting) held.getItem();
                final ItemFocusBasic focus = wand.getFocus(held);

                if (focus != null && focus instanceof ItemFocusMageMace) {
                    if (wand.consumeAllVis(held, player, focus.getVisCost(held), true, false)) {
                    }
                    else {
                        event.setCanceled(true);
                    }
                }
            }

            /**
             * Fill phials with blood when an entity is hit using the Hollow Dagger
             */
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemHollowDagger) {
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    if (player.inventory.getStackInSlot(i) != null
                            && player.inventory.getStackInSlot(i).getItem() instanceof ItemEssence
                            && player.inventory.getStackInSlot(i).getItemDamage() == 0) {
                        player.inventory.decrStackSize(i, 1);

                        if (!player.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.ItemCrimsonBlood))
                                && !player.worldObj.isRemote) {
                            player.entityDropItem(new ItemStack(ItemRegistry.ItemCrimsonBlood), 2.0F);
                        }
                    }
                }
            }
        }
    }

    /**
     * Update notifications
     */
    @SubscribeEvent
    public void playerLoggedIn (final PlayerEvent.PlayerLoggedInEvent event) {
        if (UpdateHandler.message != null) {
            event.player.addChatMessage(new ChatComponentText(UpdateHandler.message));
        }
    }

    /**
     * Detect when the player crafts a Shard of Creation
     */
    @SubscribeEvent
    public void itemCrafted (final ItemCraftedEvent event) {
        if (event.crafting.getItem() == ItemRegistry.ItemMaterial && event.crafting.getItemDamage() == 5) {
            giveResearch(event.player);
        }
    }

    /**
     * Give the player the Creation research upon crafting the Shard of Creation
     */
    public void giveResearch (final EntityPlayer player) {
        // CREATION
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")
                && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATIONSHARD")) {
            Thaumcraft.proxy.getResearchManager().completeResearch(player, "CREATION");
            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CREATION"), (EntityPlayerMP) player);

            // effects
            HUDHandler.displayString(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("text.creation"), 3200,
                    false);
            player.worldObj.playSoundAtEntity(player, "thaumcraft:egidle", 1.0F, 1.0F);
            player.worldObj.playSoundAtEntity(player, "thaumcraft:heartbeat", 1.0F, 1.0F);
            player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200, -1));
        }
        // OUTERREV
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "OUTERREV")
                && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")) {
            Thaumcraft.proxy.getResearchManager().completeResearch(player, "OUTERREV");
            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("OUTERREV"), (EntityPlayerMP) player);
        }
    }

    /**
     * Modify certain tooltips
     */
    @SubscribeEvent
    public void itemTooltip (final ItemTooltipEvent event) {
        // mage mace attack dmg
        if (event.itemStack.getItem() instanceof ItemFocusMageMace
                && event.toolTip.contains(StatCollector.translateToLocal("item.Focus.cost1"))) {
            event.toolTip.remove(StatCollector.translateToLocal("item.Focus.cost1"));
            event.toolTip.add(1, StatCollector.translateToLocal("item.Focus.cost3"));
        }

        // voidtouched items
        if (event.itemStack.stackTagCompound != null && event.itemStack.stackTagCompound.getBoolean("voidtouched")) {
            event.toolTip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("text.voidtouched"));
        }
    }
}
