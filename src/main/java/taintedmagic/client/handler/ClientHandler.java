package taintedmagic.client.handler;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.client.event.RenderPlayerEvent;
import taintedmagic.api.IRenderInventoryItem;

@SideOnly (Side.CLIENT)
public class ClientHandler
{
    public static int ticks;

    @SubscribeEvent
    public void tickEnd (TickEvent event)
    {
        if (event.phase == TickEvent.Phase.END)
        {
            HUDHandler.updateTicks();
            ticks++;
        }
    }

    /*
     * Render items implementing IRenderInventoryItem
     */
    @SubscribeEvent
    public void onPlayerRender (RenderPlayerEvent.Specials.Post event)
    {
        EntityPlayer player = event.entityPlayer;
        if (player.getActivePotionEffect(Potion.invisibility) != null) return;

        ItemStack[] inv = player.inventory.mainInventory;
        List<Item> rendering = new ArrayList<Item>();
        for (int i = 0; i < 36; i++)
        {
            ItemStack stack = inv[i];
            if (stack != null && stack.getItem() instanceof IRenderInventoryItem && !rendering.contains(stack.getItem()))
            {
                ((IRenderInventoryItem) stack.getItem()).render(player, stack, event.partialRenderTick);
                rendering.add(stack.getItem());
            }
            if (i == 36) rendering.clear();
        }
    }
}
