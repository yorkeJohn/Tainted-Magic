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

public class TaintedMagicHelper {
    /**
     * Returns an aspect list containing the specified amount of each primal aspect.
     *
     * @param amount The amount of each primal aspect.
     */
    public static AspectList getPrimals(final int amount) {
        return new AspectList()
                .add(Aspect.FIRE, amount)
                .add(Aspect.WATER, amount)
                .add(Aspect.EARTH, amount)
                .add(Aspect.AIR, amount)
                .add(Aspect.ORDER, amount)
                .add(Aspect.ENTROPY, amount);
    }

    /**
     * Returns true if the player has adaquate vis to be consumed from their inventory (wands, baubles, etc)
     *
     * @param player The player
     * @param aspects The cost to be consumed
     * @param doit Should the vis actually be consumed
     */
    public static boolean consumeVisFromInventory(
            final EntityPlayer player, final AspectList aspects, final boolean doit) {
        final IInventory baubles = BaublesApi.getBaubles(player);

        for (int i = 0; i < 4; i++) {
            if (baubles.getStackInSlot(i) != null
                    && baubles.getStackInSlot(i).getItem() instanceof ItemAmuletVis
                    && ((ItemAmuletVis) baubles.getStackInSlot(i).getItem())
                            .consumeAllVis(baubles.getStackInSlot(i), player, aspects, doit, true)) return true;
        }
        for (final ItemStack stack : player.inventory.mainInventory) {
            if (stack != null
                    && stack.getItem() instanceof ItemWandCasting
                    && ((ItemWandCasting) stack.getItem()).consumeAllVis(stack, player, aspects, doit, true))
                return true;
        }
        return false;
    }

    /**
     * Gets a vector between two entities.
     *
     * @param origin The origin entity.
     * @param target The target entity.
     */
    public static Vector3 getVectorBetweenEntities(final Entity origin, final Entity target) {
        final Vector3 fromPosition = new Vector3(origin.posX, origin.posY, origin.posZ);
        final Vector3 toPosition = new Vector3(target.posX, target.posY, target.posZ);
        final Vector3 dist = fromPosition.sub(toPosition);
        dist.normalize();
        return dist;
    }

    /**
     * Gets the distance from the player to a point (x, y, z)
     *
     * @param player The player.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     */
    public static double getDistanceTo(final EntityPlayer player, final double x, final double y, final double z) {
        final double distX = player.posX - x;
        final double distY = player.posY - y;
        final double distZ = player.posZ - z;
        return distX * distX + distY * distY + distZ * distZ;
    }
}
