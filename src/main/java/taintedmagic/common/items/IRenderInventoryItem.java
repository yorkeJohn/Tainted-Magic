package taintedmagic.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IRenderInventoryItem {
    void render(EntityPlayer player, ItemStack stack, float partialTicks);
}
