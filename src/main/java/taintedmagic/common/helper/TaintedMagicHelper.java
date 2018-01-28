package taintedmagic.common.helper;

import baubles.api.BaublesApi;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.items.baubles.ItemAmuletVis;
import thaumcraft.common.items.wands.ItemWandCasting;

public class TaintedMagicHelper
{
	public static AspectList getPrimals (int amount)
	{
		return new AspectList().add(Aspect.FIRE, amount).add(Aspect.WATER, amount).add(Aspect.EARTH, amount).add(Aspect.AIR, amount).add(Aspect.ORDER, amount).add(Aspect.ENTROPY, amount);
	}

	public static boolean consumeVisFromInventory (EntityPlayer p, AspectList l, boolean b)
	{
		IInventory baubles = BaublesApi.getBaubles(p);

		for (int a = 0; a < 4; a++)
		{
			if (baubles.getStackInSlot(a) != null && baubles.getStackInSlot(a).getItem() instanceof ItemAmuletVis)
			{
				if ( ((ItemAmuletVis) baubles.getStackInSlot(a).getItem()).consumeAllVis(baubles.getStackInSlot(a), p, l, b, true)) return true;
			}

		}
		for (int a = p.inventory.mainInventory.length - 1; a >= 0; a--)
		{
			ItemStack s = p.inventory.mainInventory[a];
			if (s != null && s.getItem() instanceof ItemWandCasting)
			{
				if ( ((ItemWandCasting) s.getItem()).consumeAllVis(s, p, l, b, true)) return true;
			}
		}
		return false;
	}

	public static Vector3 getVectorBetweenEntities (Entity e, Entity target)
	{
		Vector3 fromPosition = new Vector3(e.posX, e.posY, e.posZ);
		Vector3 toPosition = new Vector3(target.posX, target.posY, target.posZ);
		Vector3 dist = fromPosition.sub(toPosition);
		dist.normalize();
		return dist;
	}

	public static double getDistanceTo (double x, double y, double z, EntityPlayer p)
	{
		double distX = p.posX + 0.5D - x;
		double distY = p.posY + 0.5D - y;
		double distZ = p.posZ + 0.5D - z;
		return distX * distX + distY * distY + distZ * distZ;
	}
}
