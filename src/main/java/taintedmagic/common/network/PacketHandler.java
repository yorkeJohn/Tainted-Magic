package taintedmagic.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import taintedmagic.common.TaintedMagic;

public class PacketHandler {

    private PacketHandler() {}

    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(TaintedMagic.MOD_ID.toLowerCase());

    public static void initPackets () {
        INSTANCE.registerMessage(PacketKatanaAttack.class, PacketKatanaAttack.class, 0, Side.SERVER);
        INSTANCE.registerMessage(PacketSashToggle.class, PacketSashToggle.class, 1, Side.SERVER);
        INSTANCE.registerMessage(PacketSashToggleAck.class, PacketSashToggleAck.class, 2, Side.CLIENT);
    }
}
