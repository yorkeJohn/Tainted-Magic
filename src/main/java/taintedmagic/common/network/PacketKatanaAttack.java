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

public class PacketKatanaAttack implements IMessage, IMessageHandler<PacketKatanaAttack, IMessage> {

    private int entityID;
    private int playerID;
    private int dimensionID;
    private float dmg;

    public PacketKatanaAttack() {}

    public PacketKatanaAttack(final Entity entity, final EntityPlayer player, final float dmg) {
        entityID = entity.getEntityId();
        playerID = player.getEntityId();
        dimensionID = entity.dimension;
        this.dmg = dmg;
    }

    @Override
    public IMessage onMessage(final PacketKatanaAttack message, final MessageContext ctx) {
        final World world = DimensionManager.getWorld(message.dimensionID);
        if (world == null) return null;

        final Entity entity = world.getEntityByID(message.entityID);
        final Entity player = world.getEntityByID(message.playerID);

        if (entity != null && entity instanceof EntityLivingBase && player != null && player instanceof EntityPlayer) {
            entity.attackEntityFrom(
                    DamageSource.causePlayerDamage((EntityPlayer) player).setDamageBypassesArmor(), message.dmg);
        }

        return null;
    }

    @Override
    public void fromBytes(final ByteBuf buf) {
        entityID = buf.readInt();
        playerID = buf.readInt();
        dimensionID = buf.readInt();
        dmg = buf.readFloat();
    }

    @Override
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(playerID);
        buf.writeInt(dimensionID);
        buf.writeFloat(dmg);
    }
}
