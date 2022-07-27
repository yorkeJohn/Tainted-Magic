package taintedmagic.common.network;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;

public class PacketSashToggleAck implements IMessage, IMessageHandler<PacketSashToggleAck, IMessage> {

    @Override
    public void toBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        // do nothing
    }

    @Override
    public IMessage onMessage(PacketSashToggleAck message, MessageContext ctx) {
        EntityPlayer player = TaintedMagic.proxy.getClientPlayer();

        final int beltSlot = 3;
        final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(beltSlot);
        if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash) {
            ItemVoidwalkerSash.sendToggleNotification(sash);
        }

        return null;
    }
}
