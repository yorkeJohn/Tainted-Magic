package taintedmagic.common.network;

import taintedmagic.common.lib.LibStrings;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibStrings.MODID.toLowerCase());

	public static void init ()
	{
		int i = 0;
		INSTANCE.registerMessage(PacketAttackEntityFromClient.class, PacketAttackEntityFromClient.class, i++, Side.SERVER);
	}
}
