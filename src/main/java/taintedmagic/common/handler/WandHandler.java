package taintedmagic.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandHandler implements IWandRodOnUpdate {

    @Override
    public void onUpdate(final ItemStack stack, final EntityPlayer player) {
        if (!player.isPotionActive(Config.potionWarpWardID)) {
            final int permwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());

            if (permwarp == 0) return;

            // Exponential decay in the form 1/x
            final float base = ConfigHandler.WARP_WAND_REFRESH_BASE;
            final float period = base / permwarp;

            final int roundPeriod = period < 1.0F ? 1 : Math.round(period);

            if (player.ticksExisted % roundPeriod == 0) {
                final Aspect[] primals = Aspect.getPrimalAspects().toArray(new Aspect[0]);
                for (final Aspect primal : primals) {
                    ((ItemWandCasting) stack.getItem()).addVis(stack, primal, 1, true);
                }
            }
        }
    }
}
