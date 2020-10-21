package taintedmagic.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.items.wands.ItemWandCasting;

public class WandHandler implements IWandRodOnUpdate
{
	Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

	public void onUpdate (ItemStack s, EntityPlayer p)
	{
		if (!p.isPotionActive(Config.potionWarpWardID))
		{
			int permwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(p.getCommandSenderName());

			if (permwarp == 0) return;

			// refresh period of base/warp (1/x type relationship)
			float base = ConfigHandler.WARP_WAND_REFRESH_BASE;
			float T = base / (float) permwarp;

			int rT = (T < 1.0F) ? 1 : Math.round(T);

			if (p.ticksExisted % rT == 0) for (int i = 0; i < primals.length; i++)
				((ItemWandCasting) s.getItem()).addVis(s, this.primals[i], 1, true);
		}
	}
}
