package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class ItemShadowFortressArmor extends ItemFortressArmor
        implements IWarpingGear, IVisDiscountGear, ISpecialArmor {

    public ItemShadowFortressArmor(final ArmorMaterial material, final int j, final int k) {
        super(material, j, k);
        setCreativeTab(TaintedMagic.tabTM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        iconHelm = ir.registerIcon("taintedmagic:ItemShadowFortressHelmet");
        iconChest = ir.registerIcon("taintedmagic:ItemShadowFortressChestplate");
        iconLegs = ir.registerIcon("taintedmagic:ItemShadowFortressLeggings");
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
        super.addInformation(stack, player, list, b);
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "taintedmagic:textures/models/ModelShadowFortressArmor.png";
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0))
                ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public int getVisDiscount(final ItemStack stack, final EntityPlayer player, final Aspect aspect) {
        return 5;
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(
            final EntityLivingBase entity,
            final ItemStack stack,
            final DamageSource source,
            final double dmg,
            final int slot) {
        int priority = 0;
        double ratio = damageReduceAmount / 25.0D;

        if (source.isMagicDamage()) {
            priority = 1;
            ratio = damageReduceAmount / 35.0D;
        } else if (source.isFireDamage() || source.isExplosion()) {
            priority = 1;
            ratio = damageReduceAmount / 20.0D;
        } else if (source.isUnblockable()) {
            priority = 0;
            ratio = 0.0D;
        }

        if (entity instanceof EntityPlayer) {
            double set = 0.750D;
            for (int a = 1; a < 4; a++) {
                final ItemStack piece = ((EntityPlayer) entity).inventory.armorInventory[a];
                if (piece != null && piece.getItem() instanceof ItemFortressArmor) {
                    set += 0.150D;
                    if (piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask")) {
                        set += 0.05D;
                    }
                }
            }
            ratio *= set;
        }

        return new ISpecialArmor.ArmorProperties(priority, ratio, stack.getMaxDamage() + 1 - stack.getItemDamage());
    }

    @Override
    public int getArmorDisplay(final EntityPlayer player, final ItemStack stack, final int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor(
            final EntityLivingBase entity,
            final ItemStack stack,
            final DamageSource source,
            final int dmg,
            final int slot) {
        if (source != DamageSource.fall) {
            stack.damageItem(dmg, entity);
        }
    }
}
