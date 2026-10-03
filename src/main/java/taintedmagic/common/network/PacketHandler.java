package taintedmagic.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import taintedmagic.Tags;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MOD_ID.toLowerCase());

    public static void initPackets () {
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, 0, Side.SERVER);
    }
}
