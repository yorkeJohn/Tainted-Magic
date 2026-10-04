package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class ItemVoidFortressArmor extends ItemFortressArmor implements IWarpingGear, IVisDiscountGear {

    public ItemVoidFortressArmor(final ArmorMaterial material, final int j, final int k) {
        super(material, j, k);
        setCreativeTab(TaintedMagic.tabTM);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        iconHelm = ir.registerIcon("taintedmagic:ItemVoidFortressHelmet");
        iconChest = ir.registerIcon("taintedmagic:ItemVoidFortressChestplate");
        iconLegs = ir.registerIcon("taintedmagic:ItemVoidFortressLeggings");
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
        super.addInformation(stack, player, list, b);
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "taintedmagic:textures/models/ModelVoidFortressArmor.png";
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 16))
                ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int i, final boolean b) {
        super.onUpdate(stack, world, entity, i, b);
        if (!world.isRemote
                && stack.isItemDamaged()
                && entity.ticksExisted % 20 == 0
                && entity instanceof EntityLivingBase) {
            stack.damageItem(-1, (EntityLivingBase) entity);
        }
    }

    @Override
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
        super.onArmorTick(world, player, stack);
        if (!world.isRemote && stack.isItemDamaged() && player.ticksExisted % 20 == 0) {
            stack.damageItem(-1, player);
        }
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return 3;
    }

    @Override
    public int getVisDiscount(final ItemStack stack, final EntityPlayer player, final Aspect aspect) {
        return 5;
    }
}
