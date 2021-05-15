package taintedmagic.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import taintedmagic.common.lib.LibInfo;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LibInfo.MODID.toLowerCase());

    public static void initPackets ()
    {
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, 0, Side.SERVER);
    }
}
