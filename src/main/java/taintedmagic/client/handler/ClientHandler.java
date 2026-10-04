package taintedmagic.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import taintedmagic.api.IRenderInventoryItem;
import taintedmagic.common.handler.RenderItemSyncHandler;
import taintedmagic.common.handler.RenderItemSyncHandler.RenderedItem;

@SideOnly(Side.CLIENT)
public class ClientHandler {

    @SubscribeEvent
    public void tickEnd(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            HUDHandler.updateTicks();
        }
    }

    /*
     * Render items implementing IRenderInventoryItem
     */
    @SubscribeEvent
    public void onPlayerRender(final RenderPlayerEvent.Specials.Post event) {
        final EntityPlayer player = event.entityPlayer;
        // Invisibility potion effects aren't synced for other players, but the invisible flag is
        if (player.isInvisible()) return;

        // Only the local player's inventory is known on the client, other players' items come from the server
        final List<RenderedItem> items = player == Minecraft.getMinecraft().thePlayer
                ? RenderItemSyncHandler.getRenderedItems(player)
                : RenderItemSyncHandler.CLIENT_RENDERED_ITEMS.getOrDefault(
                        player.getEntityId(), Collections.emptyList());

        for (final RenderedItem item : items) {
            // Pass the held stack itself so renderers can tell the item is in the player's hand
            final ItemStack held = player.getHeldItem();
            final ItemStack stack =
                    item.held && held != null && held.getItem() == item.stack.getItem() ? held : item.stack;
            ((IRenderInventoryItem) stack.getItem()).render(player, stack, event.partialRenderTick);
        }
    }
}
