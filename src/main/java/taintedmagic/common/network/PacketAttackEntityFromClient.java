package taintedmagic.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketAttackEntityFromClient implements IMessage, IMessageHandler<PacketAttackEntityFromClient, IMessage>
{
	private int entityID;
	private int playerID;
	private int dimensionID;
	private float dmg;

	public PacketAttackEntityFromClient ()
	{
	}

	public PacketAttackEntityFromClient (Entity e, EntityPlayer p, float dmg)
	{
		this.entityID = e.getEntityId();
		this.playerID = p.getEntityId();
		this.dimensionID = e.dimension;
		this.dmg = dmg;
	}

	@Override
	public IMessage onMessage (PacketAttackEntityFromClient message, MessageContext ctx)
	{
		World w = DimensionManager.getWorld(message.dimensionID);
		if (w == null) return null;

		Entity e = w.getEntityByID(message.entityID);
		Entity p = w.getEntityByID(message.playerID);

		if (e != null && e instanceof EntityLivingBase && p != null && p instanceof EntityPlayer) e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p, e), message.dmg);

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
