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
	private boolean leech;

	public PacketKatanaAttack ()
	{
	}

	public PacketKatanaAttack (Entity e, EntityPlayer p, float dmg, boolean leech)
	{
		this.entityID = e.getEntityId();
		this.playerID = p.getEntityId();
		this.dimensionID = e.dimension;
		this.dmg = dmg;
		this.leech = leech;
	}

	@Override
	public IMessage onMessage (PacketKatanaAttack message, MessageContext ctx)
	{
		World w = DimensionManager.getWorld(message.dimensionID);
		if (w == null) return null;

		Entity e = w.getEntityByID(message.entityID);
		Entity p = w.getEntityByID(message.playerID);

		if (e != null && e instanceof EntityLivingBase && p != null && p instanceof EntityPlayer) e.attackEntityFrom(DamageSource.causeIndirectMagicDamage(p, e), message.dmg);

		if (message.leech && p instanceof EntityPlayer)
		{
			((EntityPlayer) p).heal(message.dmg * 0.25F);
			w.playSoundAtEntity(p, "thaumcraft:runicShieldEffect", 0.5F, 0.5F + ((float) Math.random() * 0.5F));
		}

		return null;
	}

	@Override
	public void fromBytes (ByteBuf buf)
	{
		this.entityID = buf.readInt();
		this.playerID = buf.readInt();
		this.dimensionID = buf.readInt();
		this.dmg = buf.readFloat();
		this.leech = buf.readBoolean();
	}

	@Override
	public void toBytes (ByteBuf buf)
	{
		buf.writeInt(this.entityID);
		buf.writeInt(this.playerID);
		buf.writeInt(this.dimensionID);
		buf.writeFloat(this.dmg);
		buf.writeBoolean(this.leech);
	}
}
