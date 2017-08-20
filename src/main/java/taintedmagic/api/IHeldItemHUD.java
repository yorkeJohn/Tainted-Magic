package taintedmagic.api;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHeldItemHUD
{
	abstract void renderHUD (ScaledResolution res, EntityPlayer p, ItemStack s, float partialTicks, float fract);
}
