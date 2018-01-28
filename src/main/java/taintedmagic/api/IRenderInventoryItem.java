package taintedmagic.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IRenderInventoryItem
{
	abstract void render (EntityPlayer p, ItemStack s, float partialTicks);
}
