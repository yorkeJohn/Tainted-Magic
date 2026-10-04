package taintedmagic.common.items.tools;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;

public class ItemPrimalBlade extends ItemSword implements IWarpingGear, IRepairable {

    public ItemPrimalBlade(final ToolMaterial material) {
        super(material);
        setTextureName("taintedmagic:ItemPrimalBlade");
        setUnlocalizedName("ItemPrimalBlade");
        setCreativeTab(TaintedMagic.tabTM);
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.block;
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase entity, final EntityLivingBase player) {
        super.hitEntity(stack, entity, player);

        entity.addPotionEffect(new PotionEffect(Potion.wither.id, 60, 1));
        entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 120, 1));

        player.worldObj.playSoundAtEntity(player, "thaumcraft:swing", 1.0F, 1.0F);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int i) {

        final List<Entity> ents = player.worldObj.getEntitiesWithinAABB(
                Entity.class,
                AxisAlignedBB.getBoundingBox(
                                player.posX,
                                player.posY,
                                player.posZ,
                                player.posX + 1,
                                player.posY + 1,
                                player.posZ + 1)
                        .expand(15.0D, 15.0D, 15.0D));

        if (player.ticksExisted % 10 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:brain", 0.05F, 0.5F);
        }

        if (ents != null && ents.size() > 0) {
            for (final Entity entity : ents) {
                if (entity != player) {
                    if (entity.isEntityAlive() && !entity.isEntityInvulnerable()) {
                        final double dist =
                                TaintedMagicHelper.getDistanceTo(player, entity.posX, entity.posY, entity.posZ);
                        if (dist < 2.0D) {
                            entity.attackEntityFrom(DamageSource.magic, 3.0F);
                        }
                    }

                    final double x = (player.posX + 0.5D - entity.posX) / 20.0D;
                    final double y = (player.posY + 0.5D - entity.posY) / 20.0D;
                    final double z = (player.posZ + 0.5D - entity.posZ) / 20.0D;
                    final double vec = Math.sqrt(x * x + y * y + z * z);
                    double vec2 = 1.0D - vec;

                    if (vec2 > 0.0D) {
                        vec2 *= vec2;
                        entity.motionX += x / vec * vec2 * 0.20D;
                        entity.motionY += y / vec * vec2 * 0.30D;
                        entity.motionZ += z / vec * vec2 * 0.20D;
                    }
                }
            }
        }
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int i, final boolean b) {
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0) {
            stack.damageItem(-1, (EntityLivingBase) entity);
        }
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("text.sapprimal"));
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 5;
    }
}
