package taintedmagic.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PacketKatanaAttack implements IMessage, IMessageHandler<PacketKatanaAttack, IMessage>
{
    private int entityID;
    private int playerID;
    private int dimensionID;
    private float dmg;

    public PacketKatanaAttack ()
    {
    }

    public PacketKatanaAttack (Entity entity, EntityPlayer player, float dmg)
    {
        this.entityID = entity.getEntityId();
        this.playerID = player.getEntityId();
        this.dimensionID = entity.dimension;
        this.dmg = dmg;
    }

    @Override
    public IMessage onMessage (PacketKatanaAttack message, MessageContext ctx)
    {
        World world = DimensionManager.getWorld(message.dimensionID);
        if (world == null) return null;

        Entity entity = world.getEntityByID(message.entityID);
        Entity player = world.getEntityByID(message.playerID);

        if (entity != null && entity instanceof EntityLivingBase && player != null && player instanceof EntityPlayer) entity
                .attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player).setDamageBypassesArmor(), message.dmg);

        return null;
    }

    @Override
    public void fromBytes (ByteBuf buf)
    {
        this.entityID = buf.readInt();
        this.playerID = buf.readInt();
        this.dimensionID = buf.readInt();
        this.dmg = buf.readFloat();
    }

    @Override
    public void toBytes (ByteBuf buf)
    {
        buf.writeInt(this.entityID);
        buf.writeInt(this.playerID);
        buf.writeInt(this.dimensionID);
        buf.writeFloat(this.dmg);
    }
}
