package taintedmagic.common.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncInv implements IMessage, IMessageHandler<PacketSyncInv, IMessage>
{
	int playerID;
	ItemStack[] inv = null;

	public PacketSyncInv ()
	{
	}

	public PacketSyncInv (EntityPlayer p)
	{
		this.playerID = p.getEntityId();
		this.inv = p.inventory.mainInventory;
	}

	@Override
	public IMessage onMessage (PacketSyncInv message, MessageContext ctx)
	{
		World w = TaintedMagic.proxy.getClientWorld();
		if (w == null) return null;

		Entity p = w.getEntityByID(message.playerID);
		if (p != null && p instanceof EntityPlayer) ((EntityPlayer) p).inventory.mainInventory = message.inv;

		return null;
	}

	@Override
	public void fromBytes (ByteBuf buf)
	{
		playerID = buf.readInt();
		PacketBuffer pb = new PacketBuffer(buf);
		try
		{
			ItemStack[] s = new ItemStack[36];
			for (int i = 0; i < 36; i++)
				s[i] = pb.readItemStackFromBuffer();
			inv = s;
		}
		catch (IOException e)
		{
		}
	}

	@Override
	public void toBytes (ByteBuf buf)
	{
		buf.writeInt(playerID);
		PacketBuffer pb = new PacketBuffer(buf);
		try
		{
			for (int i = 0; i < 36; i++)
				pb.writeItemStackToBuffer(inv[i]);
		}
		catch (IOException e)
		{
		}
	}
}
