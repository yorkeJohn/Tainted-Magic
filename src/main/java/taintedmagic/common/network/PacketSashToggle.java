package taintedmagic.common.network;

import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;

public class PacketSashToggle implements IMessage, IMessageHandler<PacketSashToggle, IMessage> {

    @Override
    public void fromBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public IMessage onMessage(PacketSashToggle message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;

        final InventoryBaubles inventoryBaubles = PlayerHandler.getPlayerBaubles(player);
        final int beltSlot = 3;
        final ItemStack sash = inventoryBaubles.getStackInSlot(beltSlot);
        if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash) {
            ItemVoidwalkerSash.toggleMode(sash);
            inventoryBaubles.syncSlotToClients(beltSlot);
            PacketHandler.INSTANCE.sendTo(new PacketSashToggleAck(), player);
        }

        return null;
    }
}
