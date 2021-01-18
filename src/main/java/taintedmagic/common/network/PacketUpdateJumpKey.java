package taintedmagic.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import taintedmagic.common.items.ItemFlyteCharm;

public class PacketUpdateJumpKey implements IMessage, IMessageHandler<PacketUpdateJumpKey, IMessage>
{
    private long leastSigBits, mostSigBits;
    private boolean state;

    public PacketUpdateJumpKey ()
    {
    }

    public PacketUpdateJumpKey (UUID uuid, boolean state)
    {
        this.leastSigBits = uuid.getLeastSignificantBits();
        this.mostSigBits = uuid.getMostSignificantBits();
        this.state = state;
    }

    @Override
    public IMessage onMessage (PacketUpdateJumpKey message, MessageContext ctx)
    {
        ItemFlyteCharm.jumpKeyState.replace(new UUID(message.leastSigBits, message.mostSigBits), message.state);
        return null;
    }

    @Override
    public void fromBytes (ByteBuf buf)
    {
        this.leastSigBits = buf.readLong();
        this.mostSigBits = buf.readLong();
        this.state = buf.readBoolean();
    }

    @Override
    public void toBytes (ByteBuf buf)
    {
        buf.writeLong(leastSigBits);
        buf.writeLong(mostSigBits);
        buf.writeBoolean(state);
    }
}
