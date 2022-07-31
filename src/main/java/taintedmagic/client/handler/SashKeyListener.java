package taintedmagic.client.handler;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import taintedmagic.common.items.equipment.ItemVoidwalkerSash;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.network.PacketSashToggle;

@SideOnly(Side.CLIENT)
public class SashKeyListener {

    private static final KeyBinding toggleSash = new KeyBinding("tmisc.toggleSash", Keyboard.CHAR_NONE, "tmisc.keyCategory");

    public SashKeyListener() {
        FMLCommonHandler.instance().bus().register(this);
        ClientRegistry.registerKeyBinding(toggleSash);
    }

    @SubscribeEvent
    public void keyUp(InputEvent.KeyInputEvent event) {
        if (toggleSash.isPressed()) {

            final ItemStack sash = PlayerHandler.getPlayerBaubles(Minecraft.getMinecraft().thePlayer).getStackInSlot(3);
            if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash) {
                PacketHandler.INSTANCE.sendToServer(new PacketSashToggle());
            }
        }
    }
}
