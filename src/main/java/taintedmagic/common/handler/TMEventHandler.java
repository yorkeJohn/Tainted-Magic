package taintedmagic.common.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import taintedmagic.common.items.ItemMaterial;
import taintedmagic.common.items.equipment.ItemLumosRing;
import taintedmagic.common.items.tools.ItemHollowDagger;
import taintedmagic.common.items.wand.foci.ItemFocusMageMace;
import taintedmagic.common.registry.BlockRegistry;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.api.wands.StaffRod;
import thaumcraft.api.wands.WandRod;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockCustomPlant;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.items.ItemEssence;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;

public class TMEventHandler
{
    Random random = new Random();

    @SubscribeEvent
    public void playerTick (LivingEvent.LivingUpdateEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;

            repairItems(player);
            createLumos(player);
            modifyAttackDamage(player);

            for (int i = 0; i < saps.size(); i++)
                saps.get(i).doWarpSap(player.worldObj, i);

            // DELETE THIS UPON RELEASE
            if (Keyboard.getEventKey() == Keyboard.KEY_T && Keyboard.isKeyDown(Keyboard.KEY_DELETE))
                Minecraft.getMinecraft().refreshResources();
        }
    }

    /**
     * Repair "Voidtouched" items
     */
    public void repairItems (EntityPlayer player)
    {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (!player.worldObj.isRemote && stack != null && stack.stackTagCompound != null
                    && stack.stackTagCompound.getBoolean("voidtouched") && stack.isItemDamaged())
            {
                if (player.ticksExisted % 20 == 0) stack.setItemDamage(stack.getItemDamage() - 1);
            }
        }
    }

    /**
     * Create Lumos lightsource blocks when the player is holding a wand or staff
     * with the Lumos focus equipped or when the Lumos ring is equipped.
     */
    public void createLumos (EntityPlayer player)
    {
        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY + 1);
        int z = (int) Math.floor(player.posZ);

        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting)
        {
            ItemStack held = player.getHeldItem();
            ItemWandCasting wand = (ItemWandCasting) held.getItem();

            if (wand.getFocus(held) != null && wand.getFocus(held) == ItemRegistry.ItemFocusLumos && !player.worldObj.isRemote
                    && player.worldObj.getBlock(x, y, z) == Blocks.air)
                player.worldObj.setBlock(x, y, z, BlockRegistry.BlockLumos, 1, 3);
        }

        IInventory baub = BaublesApi.getBaubles(player);
        if ( (baub.getStackInSlot(1) != null && baub.getStackInSlot(1).getItem() instanceof ItemLumosRing)
                || (baub.getStackInSlot(2) != null && baub.getStackInSlot(2).getItem() instanceof ItemLumosRing)
                        && player.worldObj.getBlock(x, y, z) == Blocks.air)
            player.worldObj.setBlock(x, y, z, BlockRegistry.BlockLumos, 2, 3);
    }

    /*
     * Some hacky code to make the Mage's Mace work...
     */
    public void modifyAttackDamage (EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
        {
            IInventory inv = player.inventory;

            for (int i = 0; i < inv.getSizeInventory(); i++)
            {
                if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() instanceof ItemWandCasting)
                {
                    ItemStack stack = inv.getStackInSlot(i);
                    ItemWandCasting wand = (ItemWandCasting) inv.getStackInSlot(i).getItem();

                    if (wand.getFocus(stack) != null && wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace
                            && wand.getRod(stack) instanceof WandRod)
                    {
                        NBTTagList tags = new NBTTagList();
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                        UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                        AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier",
                                ConfigHandler.MAGE_MACE_DMG_INC_BASE + wand.getFocusPotency(stack), 0);

                        tag.setString("Name", am.getName());
                        tag.setDouble("Amount", am.getAmount());
                        tag.setInteger("Operation", am.getOperation());
                        tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
                        tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

                        tags.appendTag(tag);
                        stack.stackTagCompound.setTag("AttributeModifiers", tags);
                    }
                    else if (wand.getRod(stack) instanceof WandRod)
                    {
                        if (!stack.hasTagCompound())
                        {
                            stack.setTagCompound(new NBTTagCompound());
                        }
                        stack.stackTagCompound.removeTag("AttributeModifiers");
                    }
                    if (wand.getFocus(stack) != null && wand.getFocus(stack) == ItemRegistry.ItemFocusMageMace
                            && wand.getRod(stack) instanceof StaffRod)
                    {
                        NBTTagList tags = new NBTTagList();
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                        UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                        AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier",
                                5.0D + ConfigHandler.MAGE_MACE_DMG_INC_BASE + wand.getFocusPotency(stack), 0);

                        tag.setString("Name", am.getName());
                        tag.setDouble("Amount", am.getAmount());
                        tag.setInteger("Operation", am.getOperation());
                        tag.setLong("UUIDMost", am.getID().getMostSignificantBits());
                        tag.setLong("UUIDLeast", am.getID().getLeastSignificantBits());

                        tags.appendTag(tag);
                        stack.stackTagCompound.setTag("AttributeModifiers", tags);
                    }
                    else if (wand.getRod(stack) instanceof StaffRod)
                    {
                        NBTTagList tags = new NBTTagList();
                        NBTTagCompound tag = new NBTTagCompound();
                        tag.setString("AttributeName", SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());

                        UUID uuid = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
                        AttributeModifier am = new AttributeModifier(uuid, "Weapon modifier", 6.0D, 0);

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
    }

    /**
     * Turn Silverwood Saplings into Warpwood Saplings using Warping Fertilizer
     */
    @SubscribeEvent
    public void createWarpSap (PlayerInteractEvent event)
    {
        EntityPlayer player = event.entityPlayer;
        if (player != null)
        {
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemMaterial
                    && player.getHeldItem().getItemDamage() == 12 && event.action == event.action.RIGHT_CLICK_BLOCK
                    && event.world.getBlock(event.x, event.y, event.z) instanceof BlockCustomPlant
                    && event.world.getBlockMetadata(event.x, event.y, event.z) == 1)
            {
                if (player.worldObj.isRemote) player.swingItem();
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
                event.world.playSoundAtEntity(player, "thaumcraft:roots", 2.0F, 1.0F);
                saps.add(new WarpSapHandler(event.x, event.y, event.z));
            }
        }
    }

    List<WarpSapHandler> saps = new ArrayList<WarpSapHandler>();

    private class WarpSapHandler
    {
        int time = 0;
        int sapX, sapY, sapZ;

        public WarpSapHandler (int x, int y, int z)
        {
            sapX = x;
            sapY = y;
            sapZ = z;
        }

        public void doWarpSap (World world, int index)
        {
            if (world.getBlock(sapX, sapY, sapZ) == ConfigBlocks.blockCustomPlant
                    && world.getBlockMetadata(sapX, sapY, sapZ) == 1)
            {
                time++;

                if (world.isRemote && world.rand.nextBoolean())
                    warpSapParticles(world, sapX, sapY, sapZ, world.rand.nextInt(2) + 1);

                if (time == 100)
                {
                    warpSapParticles(world, sapX, sapY, sapZ, 25);
                    world.setBlock(sapX, sapY, sapZ, BlockRegistry.BlockWarpwoodSapling);
                    time = 0;
                    saps.remove(index);
                }
            }
            else saps.remove(index);
        }

        @SideOnly (Side.CLIENT)
        public void warpSapParticles (World world, int x, int y, int z, int count)
        {
            for (int i = 0; i < count; i++)
            {
                FXSparkle fx = new FXSparkle(world, x + world.rand.nextFloat(), y + world.rand.nextFloat(),
                        z + world.rand.nextFloat(), 1.75F, 5, 3 + world.rand.nextInt(3));
                fx.setGravity(0.1F);

                ParticleEngine.instance.addEffect(world, fx);
            }
        }
    }

    @SubscribeEvent
    public void entityAttacked (LivingAttackEvent event)
    {
        if (event.source.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.source.getEntity();

            /**
             * Consume vis for Mage's Mace hits
             */
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemWandCasting)
            {
                ItemStack held = player.getHeldItem();
                ItemWandCasting wand = (ItemWandCasting) held.getItem();
                ItemFocusBasic focus = wand.getFocus(held);

                if (focus != null && focus instanceof ItemFocusMageMace)
                {
                    if (wand.consumeAllVis(held, player, focus.getVisCost(held), true, false));
                    else event.setCanceled(true);
                }
            }

            /**
             * Fill phials with blood when an entity is hit using the Hollow Dagger
             */
            if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemHollowDagger)
            {
                for (int i = 0; i < player.inventory.getSizeInventory(); i++)
                {
                    if (player.inventory.getStackInSlot(i) != null
                            && player.inventory.getStackInSlot(i).getItem() instanceof ItemEssence
                            && player.inventory.getStackInSlot(i).getItemDamage() == 0)
                    {
                        player.inventory.decrStackSize(i, 1);

                        if (player.inventory.addItemStackToInventory(new ItemStack(ItemRegistry.ItemCrimsonBlood)) == false)
                            if (!player.worldObj.isRemote)
                                player.entityDropItem(new ItemStack(ItemRegistry.ItemCrimsonBlood), 2.0F);
                    }
                }
            }
        }
    }

    /**
     * Update notifications
     */
    @SubscribeEvent
    public void playerLoggedIn (PlayerEvent.PlayerLoggedInEvent event)
    {
        if (UpdateHandler.show) event.player.addChatMessage(new ChatComponentText(UpdateHandler.updateStatus));
    }

    /**
     * Detect when the player crafts a Shard of Creation
     */
    @SubscribeEvent
    public void itemCrafted (ItemCraftedEvent event)
    {
        if (event.crafting.getItem() == ItemRegistry.ItemMaterial && event.crafting.getItemDamage() == 5)
            giveResearch(event.player);
    }

    /**
     * Give the player the Creation research upon crafting the Shard of Creation
     */
    public void giveResearch (EntityPlayer player)
    {
        if (!player.worldObj.isRemote)
        {
            if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION")
                    && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATIONSHARD"))
            {
                Thaumcraft.proxy.getResearchManager().completeResearch(player, "CREATION");
                thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("CREATION"),
                        (EntityPlayerMP) player);
                player.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("text.creation")));
                player.playSound("thaumcraft:wind", 1.0F, 5.0F);

                if (!ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "OUTERREV")
                        && ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), "CREATION"))
                {
                    Thaumcraft.proxy.getResearchManager().completeResearch(player, "OUTERREV");
                    thaumcraft.common.lib.network.PacketHandler.INSTANCE.sendTo(new PacketResearchComplete("OUTERREV"),
                            (EntityPlayerMP) player);
                }

                try
                {
                    player.addPotionEffect(new PotionEffect(Potion.blindness.id, 80, 0));
                    player.addPotionEffect(new PotionEffect(Config.potionBlurredID, 200, 0));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Modify certain tooltips
     */
    @SubscribeEvent
    public void itemTooltip (ItemTooltipEvent event)
    {
        if (event.itemStack.getItem() instanceof ItemFocusMageMace)
        {
            if (event.toolTip.contains(StatCollector.translateToLocal("item.Focus.cost1")))
            {
                event.toolTip.remove(StatCollector.translateToLocal("item.Focus.cost1"));
                event.toolTip.add(1, StatCollector.translateToLocal("item.Focus.cost3"));
            }
        }

        if (event.itemStack.stackTagCompound != null && event.itemStack.stackTagCompound.getBoolean("voidtouched"))
            event.toolTip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("text.voidtouched"));
    }
}
