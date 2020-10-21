package taintedmagic.common.items.equipment;

import java.util.List;

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
    public ItemVoidwalkerBoots (ArmorMaterial m, int j, int k)
    {
        super(m, j, k);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemVoidwalkerBoots");
        this.setTextureName("taintedmagic:ItemVoidwalkerBoots");

        MinecraftForge.EVENT_BUS.register(this);
    }

    public String getArmorTexture (ItemStack s, Entity e, int slot, String t)
    {
        return "taintedmagic:textures/models/ModelVoidwalkerBoots.png";
    }

    public EnumRarity getRarity (ItemStack s)
    {
        return EnumRarity.epic;
    }

    @Override
    public int getRunicCharge (ItemStack s)
    {
        return 0;
    }

    @Override
    public int getWarp (ItemStack s, EntityPlayer p)
    {
        return 5;
    }

    @Override
    public int getVisDiscount (ItemStack s, EntityPlayer p, Aspect a)
    {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties (EntityLivingBase e, ItemStack s, DamageSource source, double dmg,
            int slot)
    {
        int priority = 0;
        double ratio = this.damageReduceAmount / 90.0D;

        if (source.isMagicDamage() == true)
        {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        }
        else if ( (source.isFireDamage() == true) || (source.isExplosion()))
        {
            priority = 1;
            ratio = this.damageReduceAmount / 80.0D;
        }
        else if (source.isUnblockable())
        {
            priority = 0;
            ratio = 0.0D;
        }
        return new ISpecialArmor.ArmorProperties(priority, ratio, s.getMaxDamage() + 1 - s.getItemDamage());
    }

    @Override
    public int getArmorDisplay (EntityPlayer p, ItemStack s, int slot)
    {
        return this.damageReduceAmount;
    }

    @Override
    public void damageArmor (EntityLivingBase e, ItemStack s, DamageSource source, int dmg, int slot)
    {
        if (source != DamageSource.fall) s.damageItem(dmg, e);
    }

    @Override
    public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
    {
        l.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(s, p, null) + "%");
    }

    public void onUpdate (ItemStack s, World w, Entity e, int j, boolean k)
    {
        super.onUpdate(s, w, e, j, k);

        if ( (!w.isRemote) && (s.isItemDamaged()) && (e.ticksExisted % 20 == 0) && ( (e instanceof EntityLivingBase)))
            s.damageItem(-1, (EntityLivingBase) e);
    }

    public void onArmorTick (World w, EntityPlayer p, ItemStack s)
    {
        super.onArmorTick(w, p, s);
        if ( (!w.isRemote) && (s.getItemDamage() > 0) && (p.ticksExisted % 20 == 0)) s.damageItem(-1, p);

        double motion = Math.abs(p.motionX) + Math.abs(p.motionZ) + Math.abs(0.5 * p.motionY);
        if (w.isRemote && (motion > .1D || !p.onGround) && w.rand.nextInt(5) == 0) particles(w, p);

        if (p.moveForward > 0.0F)
        {
            if ( (p.worldObj.isRemote) && (!p.isSneaking()))
            {
                if (!Thaumcraft.instance.entityEventHandler.prevStep.containsKey(Integer.valueOf(p.getEntityId())))
                {
                    Thaumcraft.instance.entityEventHandler.prevStep.put(Integer.valueOf(p.getEntityId()),
                            Float.valueOf(p.stepHeight));
                }
                p.stepHeight = 1.0F;
            }

            if (p.onGround || p.capabilities.isFlying)
            {
                float bonus = 0.20F;
                p.moveFlying(0.0F, 1.0F, p.capabilities.isFlying ? (bonus - 0.075F) : bonus);
                p.jumpMovementFactor = 0.00002F;
            }
            else if (Hover.getHover(p.getEntityId()))
            {
                p.jumpMovementFactor = 0.03F;
            }
            else
            {
                p.jumpMovementFactor = 0.05F;
            }
        }
        if (p.fallDistance > 3.0F) p.fallDistance = 0.0F;
    }

    @SubscribeEvent
    public void playerJumps (LivingEvent.LivingJumpEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;

            if (player.inventory.armorItemInSlot(0) != null
                    && player.inventory.armorItemInSlot(0).getItem() == ItemRegistry.ItemVoidwalkerBoots)
            {
                event.entityLiving.motionY += 0.35D;
            }
        }
    }

    @SideOnly (Side.CLIENT)
    private void particles (World w, EntityPlayer p)
    {
        FXWispEG ef = new FXWispEG(w, (double) (p.posX + ( (Math.random() - Math.random()) * 0.5F)),
                (double) (p.boundingBox.minY + .05F + ( (Math.random() - Math.random()) * 0.1F)),
                (double) (p.posZ + ( (Math.random() - Math.random()) * 0.5F)), p);
        ParticleEngine.instance.addEffect(w, ef);
    }
}
