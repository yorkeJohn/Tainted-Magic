package taintedmagic.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import taintedmagic.common.TaintedMagic;

/**
 * Displays a HUD message on the client. The message is sent as a translation key so it is shown in the client's
 * language.
 */
public class PacketDisplayString implements IMessage, IMessageHandler<PacketDisplayString, IMessage> {

    private String formatting;
    private String key;
    private int duration;

    public PacketDisplayString() {}

    /**
     * @param duration Display duration in ticks, or 0 for the default duration.
     */
    public PacketDisplayString(final EnumChatFormatting formatting, final String key, final int duration) {
        this.formatting = formatting.toString();
        this.key = key;
        this.duration = duration;
    }

    public static void send(final EntityPlayerMP player, final EnumChatFormatting formatting, final String key) {
        send(player, formatting, key, 0);
    }

    public static void send(
            final EntityPlayerMP player, final EnumChatFormatting formatting, final String key, final int duration) {
        PacketHandler.INSTANCE.sendTo(new PacketDisplayString(formatting, key, duration), player);
    }

    @Override
    public IMessage onMessage(final PacketDisplayString message, final MessageContext ctx) {
        final String text = message.formatting + StatCollector.translateToLocal(message.key);
        if (message.duration > 0) {
            TaintedMagic.proxy.displayString(text, message.duration, false);
        } else {
            TaintedMagic.proxy.displayString(text);
        }
        return null;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        formatting = ByteBufUtils.readUTF8String(buf);
        key = ByteBufUtils.readUTF8String(buf);
        duration = buf.readInt();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, formatting);
        ByteBufUtils.writeUTF8String(buf, key);
        buf.writeInt(duration);
    }
}
