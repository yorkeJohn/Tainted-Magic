package taintedmagic.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import taintedmagic.api.IRenderInventoryItem;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.network.PacketSyncRenderItems;

public class RenderItemSyncHandler {

    // Server: the items last sent for each player
    private final Map<UUID, List<RenderedItem>> lastSent = new HashMap<>();

    // Client: the items carried by other players, by entity ID. Written from the network thread.
    public static final Map<Integer, List<RenderedItem>> CLIENT_RENDERED_ITEMS = new ConcurrentHashMap<>();

    /**
     * The first stack of each IRenderInventoryItem in the player's main inventory.
     */
    public static List<RenderedItem> getRenderedItems(final EntityPlayer player) {
        final List<RenderedItem> items = new ArrayList<>();
        final List<Item> found = new ArrayList<>();
        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() instanceof IRenderInventoryItem && !found.contains(stack.getItem())) {
                items.add(new RenderedItem(stack, stack == player.getHeldItem()));
                found.add(stack.getItem());
            }
        }
        return items;
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER) return;

        final EntityPlayer player = event.player;
        final List<RenderedItem> items = getRenderedItems(player);
        final List<RenderedItem> previous = lastSent.getOrDefault(player.getUniqueID(), Collections.emptyList());
        if (!items.equals(previous)) {
            lastSent.put(player.getUniqueID(), items);
            ((WorldServer) player.worldObj)
                    .getEntityTracker()
                    .func_151247_a(
                            player, PacketHandler.INSTANCE.getPacketFrom(new PacketSyncRenderItems(player, items)));
        }
    }

    @SubscribeEvent
    public void onStartTracking(final StartTracking event) {
        if (event.target instanceof EntityPlayer && event.entityPlayer instanceof EntityPlayerMP) {
            final EntityPlayer target = (EntityPlayer) event.target;
            PacketHandler.INSTANCE.sendTo(
                    new PacketSyncRenderItems(target, getRenderedItems(target)), (EntityPlayerMP) event.entityPlayer);
        }
    }

    @SubscribeEvent
    public void onLogout(final PlayerLoggedOutEvent event) {
        lastSent.remove(event.player.getUniqueID());
    }

    @SubscribeEvent
    public void onClientDisconnect(final ClientDisconnectionFromServerEvent event) {
        CLIENT_RENDERED_ITEMS.clear();
    }

    public static class RenderedItem {
        public final ItemStack stack;
        public final boolean held;

        public RenderedItem(final ItemStack stack, final boolean held) {
            this.stack = stack;
            this.held = held;
        }

        // Compare by item and metadata only, NBT (e.g. katana cooldown) changes too often to sync on
        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof RenderedItem)) return false;
            final RenderedItem other = (RenderedItem) obj;
            return held == other.held
                    && stack.getItem() == other.stack.getItem()
                    && stack.getItemDamage() == other.stack.getItemDamage();
        }

        @Override
        public int hashCode() {
            return (Item.getIdFromItem(stack.getItem()) * 31 + stack.getItemDamage()) * 2 + (held ? 1 : 0);
        }
    }
}
