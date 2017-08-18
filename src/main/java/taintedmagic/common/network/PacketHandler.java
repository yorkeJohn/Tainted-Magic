package taintedmagic.common.network;

import taintedmagic.common.lib.LibInfo;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibInfo.MODID.toLowerCase());

	public static void init ()
	{
		int idx = 0;
		INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, idx++, Side.SERVER);
		INSTANCE.registerMessage(PacketSyncInv.class, PacketSyncInv.class, idx++, Side.CLIENT);
	}
}
