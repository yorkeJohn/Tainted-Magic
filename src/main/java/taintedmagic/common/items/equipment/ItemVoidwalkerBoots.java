package taintedmagic.common.items.equipment;

import java.util.List;

import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IRunicArmor;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWispEG;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.armor.Hover;

public class ItemVoidwalkerBoots extends ItemArmor
        implements IVisDiscountGear, IWarpingGear, IRunicArmor, IRepairable, ISpecialArmor {

    public ItemVoidwalkerBoots (final ArmorMaterial material, final int j, final int k) {
        super(material, j, k);
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemVoidwalkerBoots");
        setTextureName("taintedmagic:ItemVoidwalkerBoots");

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getArmorTexture (final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "taintedmagic:textures/models/ModelVoidwalkerBoots.png";
    }

    @Override
    public EnumRarity getRarity (final ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public int getRunicCharge (final ItemStack stack) {
        return 0;
    }

    @Override
    public int getWarp (final ItemStack stack, final EntityPlayer player) {
        return 5;
    }

    @Override
    public int getVisDiscount (final ItemStack stack, final EntityPlayer player, final Aspect aspect) {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties (final EntityLivingBase entity, final ItemStack stack,
            final DamageSource source, final double dmg, final int slot) {
        int priority = 0;
        double ratio = damageReduceAmount / 90.0D;

        if (source.isMagicDamage() || (source.isFireDamage() || source.isExplosion())) {
            priority = 1;
            ratio = damageReduceAmount / 80.0D;
        }
        else if (source.isUnblockable()) {
            priority = 0;
            ratio = 0.0D;
        }
        return new ISpecialArmor.ArmorProperties(priority, ratio, stack.getMaxDamage() + 1 - stack.getItemDamage());
    }

    @Override
    public int getArmorDisplay (final EntityPlayer player, final ItemStack stack, final int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor (final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int dmg,
            final int slot) {
        if (source != DamageSource.fall) {
            stack.damageItem(dmg, entity);
        }
    }

    @Override
    public void addInformation (final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
    }

    @Override
    public void onUpdate (final ItemStack stack, final World world, final Entity entity, final int j, final boolean k) {
        super.onUpdate(stack, world, entity, j, k);
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase) {
            stack.damageItem(-1, (EntityLivingBase) entity);
        }
    }

    @Override
    public void onArmorTick (final World world, final EntityPlayer player, final ItemStack stack) {
        super.onArmorTick(world, player, stack);
        // repair
        if (!world.isRemote && stack.isItemDamaged() && player.ticksExisted % 20 == 0) {
            stack.damageItem(-1, player);
        }

        // particles
        final double motion = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(0.5 * player.motionY);
        if (world.isRemote && (motion > 0.1D || !player.onGround) && world.rand.nextInt(3) == 0) {
            particles(world, player);
        }

        if (player.moveForward > 0.0F) {
            // increased step height
            if (player.worldObj.isRemote && !player.isSneaking()) {
                if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(player.getEntityId())) {
                    Thaumcraft.instance.entityEventHandler.prevStep.put(player.getEntityId(), player.stepHeight);
                }
                player.stepHeight = 1.0F;
            }

            // speed boost
            if (player.onGround || player.capabilities.isFlying) {
                float bonus = 0.12F;
                final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
                if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash && ItemVoidwalkerSash.isSpeedEnabled(sash)) {
                    bonus *= 2.0F;
                }

                player.moveFlying(0.0F, 1.0F, player.capabilities.isFlying ? bonus * 0.75F : bonus);
            }
            else if (Hover.getHover(player.getEntityId())) {
                player.jumpMovementFactor = 0.03F;
            }
            else {
                player.jumpMovementFactor = player.isSprinting() ? 0.045F : 0.04F;
            }
        }
        // negate fall damage
        if (player.fallDistance > 3.0F) {
            player.fallDistance = 1.0F;
        }
    }

    @SubscribeEvent
    public void playerJumps (final LivingEvent.LivingJumpEvent event) {
        if (event.entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) event.entity;
            final ItemStack boots = player.inventory.armorItemInSlot(0);
            final ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);

            if (boots != null && boots.getItem() == ItemRegistry.ItemVoidwalkerBoots) {
                player.motionY *= 1.25D;
            }
            if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash && ItemVoidwalkerSash.isSpeedEnabled(sash)) {
                player.motionY *= 1.05D;
            }
        }
    }

    @SideOnly (Side.CLIENT)
    private void particles (final World world, final EntityPlayer player) {
        final FXWispEG fx = new FXWispEG(world, player.posX + (Math.random() - Math.random()) * 0.5D,
                player.boundingBox.minY + 0.05D + (Math.random() - Math.random()) * 0.1D,
                player.posZ + (Math.random() - Math.random()) * 0.5D, player);
        ParticleEngine.instance.addEffect(world, fx);
    }
}
