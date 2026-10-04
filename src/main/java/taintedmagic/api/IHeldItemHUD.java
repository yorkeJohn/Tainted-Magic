package taintedmagic.api;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IHeldItemHUD {
    void renderHUD(ScaledResolution res, EntityPlayer player, ItemStack stack, float partialTicks, float fract);
}
