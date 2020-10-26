package taintedmagic.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandHandler implements IWandRodOnUpdate
{
    Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

    public void onUpdate (ItemStack stack, EntityPlayer player)
    {
        if (!player.isPotionActive(Config.potionWarpWardID))
        {
            int permwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(player.getCommandSenderName());

            if (permwarp == 0) return;

            // Exponential decay in the form 1/x
            float base = ConfigHandler.WARP_WAND_REFRESH_BASE;
            float period = base / (float) permwarp;

            int roundPeriod = (period < 1.0F) ? 1 : Math.round(period);

            if (player.ticksExisted % roundPeriod == 0) for (int i = 0; i < primals.length; i++)
                ((ItemWandCasting) stack.getItem()).addVis(stack, this.primals[i], 1, true);
        }
    }
}
