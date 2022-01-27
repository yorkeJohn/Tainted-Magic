package taintedmagic.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class PacketKatanaAttack implements IMessage, IMessageHandler<PacketKatanaAttack, IMessage> {

    private int entityID;
    private int playerID;
    private int dimensionID;

    public PacketKatanaAttack () {
    }

    public PacketKatanaAttack (final Entity entity, final EntityPlayer player) {
        entityID = entity.getEntityId();
        playerID = player.getEntityId();
        dimensionID = entity.dimension;
    }

    @Override
    public IMessage onMessage (final PacketKatanaAttack message, final MessageContext ctx) {
		final EntityPlayer attacker = ctx.getServerHandler().playerEntity;
		if (attacker == null || attacker.dimension != message.dimensionID
				|| attacker.getEntityId() != message.playerID) {
			return null;
		}

		final ItemStack heldItem = attacker.getHeldItem();
		if (heldItem == null || heldItem.stackSize <= 0 || !(heldItem.getItem() instanceof ItemKatana)) {
			return null;
		}

		final ItemKatana itemKatana = (ItemKatana) heldItem.getItem();
        if (itemKatana.hasCooldown(heldItem)) {
            return null;
        }

		// charged strike damage multiplier
		float mul = 1.5F;
		if (attacker.getRNG().nextInt(10) == 0) {
			mul += 1.0F; // crit
		}

        float damage = itemKatana.getAttackDamage(heldItem) * mul;

		final Entity target = attacker.worldObj.getEntityByID(message.entityID);
		if (!(target instanceof EntityLivingBase) || target.isDead) {
			return null;
		}

		if (attacker.getDistanceSqToEntity(target) > 64) {
			return null;
		}

		if (target instanceof EntityLivingBase && attacker instanceof EntityPlayer) {
			target.attackEntityFrom(DamageSource.causePlayerDamage(attacker).setDamageBypassesArmor(), damage);
		}

        return null;
    }

    @Override
    public void fromBytes (final ByteBuf buf) {
        entityID = buf.readInt();
        playerID = buf.readInt();
        dimensionID = buf.readInt();
    }

    @Override
    public void toBytes (final ByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(playerID);
        buf.writeInt(dimensionID);
    }
}
