package taintedmagic.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.handler.RenderItemSyncHandler;
import taintedmagic.common.handler.RenderItemSyncHandler.RenderedItem;

public class PacketSyncRenderItems implements IMessage, IMessageHandler<PacketSyncRenderItems, IMessage> {

    private int playerID;
    private List<RenderedItem> items;

    public PacketSyncRenderItems() {}

    public PacketSyncRenderItems(final EntityPlayer player, final List<RenderedItem> items) {
        playerID = player.getEntityId();
        this.items = items;
    }

    @Override
    public IMessage onMessage(final PacketSyncRenderItems message, final MessageContext ctx) {
        RenderItemSyncHandler.CLIENT_RENDERED_ITEMS.put(message.playerID, message.items);
        return null;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        playerID = buf.readInt();
        final int size = buf.readByte();
        items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            final Item item = Item.getItemById(buf.readInt());
            final int meta = buf.readShort();
            final boolean held = buf.readBoolean();
            if (item != null) {
                items.add(new RenderedItem(new ItemStack(item, 1, meta), held));
            }
        }
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(playerID);
        buf.writeByte(items.size());
        for (final RenderedItem item : items) {
            buf.writeInt(Item.getIdFromItem(item.stack.getItem()));
            buf.writeShort(item.stack.getItemDamage());
            buf.writeBoolean(item.held);
        }
    }
}
