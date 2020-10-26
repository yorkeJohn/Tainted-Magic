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
        implements IVisDiscountGear, IWarpingGear, IRunicArmor, IRepairable, ISpecialArmor
{
    public ItemVoidwalkerBoots (ArmorMaterial material, int j, int k)
    {
        super(material, j, k);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemVoidwalkerBoots");
        this.setTextureName("taintedmagic:ItemVoidwalkerBoots");

        MinecraftForge.EVENT_BUS.register(this);
    }

    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type)
    {
        return "taintedmagic:textures/models/ModelVoidwalkerBoots.png";
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.epic;
    }

    @Override
    public int getRunicCharge (ItemStack stack)
    {
        return 0;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 5;
    }

    @Override
    public int getVisDiscount (ItemStack stack, EntityPlayer player, Aspect aspect)
    {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties (EntityLivingBase entity, ItemStack stack, DamageSource source,
            double dmg, int slot)
    {
        int priority = 0;
        double ratio = this.damageReduceAmount / 90.0D;

        if (source.isMagicDamage())
        {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        }
        else if (source.isFireDamage() || source.isExplosion())
        {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        }
        else if (source.isUnblockable())
        {
            priority = 0;
            ratio = 0.0D;
        }
        return new ISpecialArmor.ArmorProperties(priority, ratio, stack.getMaxDamage() + 1 - stack.getItemDamage());
    }

    @Override
    public int getArmorDisplay (EntityPlayer player, ItemStack stack, int slot)
    {
        return this.damageReduceAmount;
    }

    @Override
    public void damageArmor (EntityLivingBase entity, ItemStack stack, DamageSource source, int dmg, int slot)
    {
        if (source != DamageSource.fall) stack.damageItem(dmg, entity);
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
    }

    public void onUpdate (ItemStack stack, World world, Entity entity, int j, boolean k)
    {
        super.onUpdate(stack, world, entity, j, k);
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase)
            stack.damageItem(-1, (EntityLivingBase) entity);
    }

    public void onArmorTick (World world, EntityPlayer player, ItemStack stack)
    {
        super.onArmorTick(world, player, stack);
        if (!world.isRemote && stack.isItemDamaged() && player.ticksExisted % 20 == 0) stack.damageItem(-1, player);

        double motion = Math.abs(player.motionX) + Math.abs(player.motionZ) + Math.abs(0.5 * player.motionY);
        if (world.isRemote && (motion > 0.1D || !player.onGround) && world.rand.nextInt(5) == 0) particles(world, player);

        if (player.moveForward > 0.0F)
        {
            if (player.worldObj.isRemote && !player.isSneaking())
            {
                if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(Integer.valueOf(player.getEntityId())))
                {
                    Thaumcraft.instance.entityEventHandler.prevStep.put(Integer.valueOf(player.getEntityId()),
                            Float.valueOf(player.stepHeight));
                }
                player.stepHeight = 1.0F;
            }

            if (player.onGround || player.capabilities.isFlying)
            {
                float mul = 0.2F;
                ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
                if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash && ItemVoidwalkerSash.isSpeedEnabled(sash))
                    mul += 0.2F;

                player.moveFlying(0.0F, 1.0F, player.capabilities.isFlying ? (mul - 0.075F) : mul);
                player.jumpMovementFactor = 0.00002F;
            }
            else if (Hover.getHover(player.getEntityId())) player.jumpMovementFactor = 0.03F;
            else player.jumpMovementFactor = 0.05F;
        }
        if (player.fallDistance > 3.0F) player.fallDistance = 1.0F;
    }

    @SubscribeEvent
    public void playerJumps (LivingEvent.LivingJumpEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;

            double mul = 0.0D;

            ItemStack boots = player.inventory.armorItemInSlot(0);
            ItemStack sash = PlayerHandler.getPlayerBaubles(player).getStackInSlot(3);
            if (boots != null && boots.getItem() == ItemRegistry.ItemVoidwalkerBoots) mul += 0.35D;
            if (sash != null && sash.getItem() instanceof ItemVoidwalkerSash && ItemVoidwalkerSash.isSpeedEnabled(sash))
                mul += 0.15D;

            player.motionY += mul;
        }
    }

    @SideOnly (Side.CLIENT)
    private void particles (World world, EntityPlayer player)
    {
        FXWispEG fx = new FXWispEG(world, (double) (player.posX + ( (Math.random() - Math.random()) * 0.5F)),
                (double) (player.boundingBox.minY + .05F + ( (Math.random() - Math.random()) * 0.1F)),
                (double) (player.posZ + ( (Math.random() - Math.random()) * 0.5F)), player);
        ParticleEngine.instance.addEffect(world, fx);
    }
}
