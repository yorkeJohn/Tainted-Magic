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
	Aspect aspect;
	Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);
	private static final String TAG_PERIOD = "period";

	public void onUpdate (ItemStack s, EntityPlayer p)
	{
		if (!p.isPotionActive(Config.potionWarpWardID))
		{
			int permwarp = Thaumcraft.proxy.getPlayerKnowledge().getWarpPerm(p.getCommandSenderName());

			if (permwarp == 0)
			{
				if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
				s.getTagCompound().setInteger(TAG_PERIOD, 0);
				return;
			}

			// exponential decay at rate of mul / warp (1 / x)
			float mul = 10000.0F;
			float T = mul / (float) permwarp;

			int rT = Math.round(T);

			if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
			s.getTagCompound().setInteger(TAG_PERIOD, rT);

			if (p.ticksExisted % rT == 0)
			{
				for (int i = 0; i < primals.length; i++)
				{
					((ItemWandCasting) s.getItem()).addVis(s, this.primals[i], 1, true);
				}
			}
		}
	}
}
