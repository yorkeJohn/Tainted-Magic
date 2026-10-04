package taintedmagic.common.handler;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import java.util.UUID;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import taintedmagic.common.items.equipment.ItemLumosRing;
import taintedmagic.common.items.tools.ItemHollowDagger;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.network.PacketDisplayString;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;

public class TMEventHandler {

    @SubscribeEvent
    public void playerTick(final LivingEvent.LivingUpdateEvent event) {
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
    public void repairItems(final EntityPlayer player) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if (!player.worldObj.isRemote
                    && stack != null
                    && stack.stackTagCompound != null
                    && stack.stackTagCompound.getBoolean("voidtouched")
                    && stack.isItemDamaged())
                if (player.ticksExisted % 20 == 0) {
                    stack.setItemDamage(stack.getItemDamage() - 1);
                }
        }
    }

    // Amplifier used to tell Lumos night vision apart from night vision potions
    private static final int LUMOS_AMPLIFIER = -1;

    /**
     * Apply Night Vision effect when the player is holding a wand or staff
     * with the Lumos focus equipped or when the Lumos ring is equipped.
     */
    public void applyNightVision(final EntityPlayer player) {
        if (player.worldObj.isRemote) return;

        boolean lumos = false;

        final IInventory baub = BaublesApi.getBaubles(player);
        if (baub.getStackInSlot(1) != null && baub.getStackInSlot(1).getItem() instanceof ItemLumosRing
                || baub.getStackInSlot(2) != null && baub.getStackInSlot(2).getItem() instanceof ItemLumosRing) {
            lumos = true;
        } else if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting) {
            final ItemStack held = player.getHeldItem();
            final ItemWandCasting wand = (ItemWandCasting) held.getItem();

            if (wand.getFocus(held) != null && wand.getFocus(held) == ItemRegistry.ItemFocusLumos) {
                lumos = true;
            }
        }

        final PotionEffect current = player.getActivePotionEffect(Potion.nightVision);
        final boolean fromLumos = current != null && current.getAmplifier() == LUMOS_AMPLIFIER;

        if (lumos) {
            // Don't override a night vision potion, Lumos takes over once it runs out
            if (current == null || fromLumos && player.ticksExisted % 20 == 0) {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 260, LUMOS_AMPLIFIER));
            }
        } else if (fromLumos) {
            player.removePotionEffect(Potion.nightVision.id);
        }
    }

    // Vanilla weapon modifier UUID, also used by Thaumcraft for staff damage
    private static final UUID WEAPON_MODIFIER_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    // Thaumcraft's staff attack damage
    private static final double STAFF_DAMAGE = 6.0D;
    // Extra Mage's Mace damage when used on a staff
    private static final double STAFF_MACE_BONUS = 5.0D;

    /**
     * Set the attack damage of wands and staves holding the Mage's Mace focus, and restore it when the focus is removed.
     */
    public void modifyAttackDamage(final EntityPlayer player) {
        if (player.worldObj.isRemote) return;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemWandCasting)) continue;

            final ItemWandCasting wand = (ItemWandCasting) stack.getItem();
            final boolean staff = wand.getRod(stack) instanceof StaffRod;

            if (wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace) {
                final double mace = ConfigHandler.MAGE_MACE_DMG_INC_BASE + wand.getFocusPotency(stack);
                setAttackDamage(stack, staff ? STAFF_MACE_BONUS + mace : mace);
            } else if (staff) {
                setAttackDamage(stack, STAFF_DAMAGE);
            } else if (stack.hasTagCompound()) {
                stack.stackTagCompound.removeTag("AttributeModifiers");
            }
        }
    }

    /**
     * Set the stack's attack damage modifier, only writing to the stack if it changed.
     */
    private static void setAttackDamage(final ItemStack stack, final double amount) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        final NBTTagList current = stack.stackTagCompound.getTagList("AttributeModifiers", 10);
        if (current.tagCount() == 1 && current.getCompoundTagAt(0).getDouble("Amount") == amount) return;

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
        tag.setString("Name", "Weapon modifier");
        tag.setDouble("Amount", amount);
        tag.setInteger("Operation", 0);
        tag.setLong("UUIDMost", WEAPON_MODIFIER_UUID.getMostSignificantBits());
        tag.setLong("UUIDLeast", WEAPON_MODIFIER_UUID.getLeastSignificantBits());

        final NBTTagList tags = new NBTTagList();
        tags.appendTag(tag);
        stack.stackTagCompound.setTag("AttributeModifiers", tags);
    }

    /**
     * Consume vis for Mage's Mace hits, cancelling the attack if there isn't enough
     */
    @SubscribeEvent
    public void entityAttacked(final LivingAttackEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer)) return;

        final EntityPlayer player = (EntityPlayer) event.source.getEntity();
        final ItemStack held = player.getHeldItem();

        if (held != null && held.getItem() instanceof ItemWandCasting) {
            final ItemWandCasting wand = (ItemWandCasting) held.getItem();
            final ItemFocusBasic focus = wand.getFocus(held);

            if (focus instanceof ItemFocusMageMace
                    && !wand.consumeAllVis(held, player, focus.getVisCost(held), true, false)) {
                event.setCanceled(true);
            }
        }
    }

    /**
     * Fill a phial with blood when an entity is hurt by a melee hit from the Hollow Dagger
     */
    @SubscribeEvent
    public void entityHurt(final LivingHurtEvent event) {
        // Direct melee hits only, not projectiles
        if (!(event.source.getEntity() instanceof EntityPlayer)
                || event.source.getSourceOfDamage() != event.source.getEntity()) return;

        final EntityPlayer player = (EntityPlayer) event.source.getEntity();
        if (player.getHeldItem() == null || !(player.getHeldItem().getItem() instanceof ItemHollowDagger)) return;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            final ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemEssence && stack.getItemDamage() == 0) {
                player.inventory.decrStackSize(i, 1);

                if (!player.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.ItemCrimsonBlood))) {
                    player.entityDropItem(new ItemStack(ItemRegistry.ItemCrimsonBlood), 2.0F);
                }
                return;
            }
        }
    }

    /**
     * Detect when the player crafts a Shard of Creation
     */
    @SubscribeEvent
    public void itemCrafted(final ItemCraftedEvent event) {
        if (event.crafting.getItem() == ItemRegistry.ItemMaterial && event.crafting.getItemDamage() == 5) {
            giveResearch(event.player);
        }
    }

    /**
     * Give the player the Creation research upon crafting the Shard of Creation
     */
    public void giveResearch(final EntityPlayer player) {
        // CREATION
        if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")
                && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATIONSHARD")) {
            Thaumcraft.proxy.getResearchManager().completeResearch(player, "CREATION");
            PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CREATION"), (EntityPlayerMP) player);

            // effects
            PacketDisplayString.send((EntityPlayerMP) player, EnumChatFormatting.DARK_PURPLE, "text.creation", 200);
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
    public void itemTooltip(final ItemTooltipEvent event) {
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
