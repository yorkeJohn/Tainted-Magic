package taintedmagic.common.items.equipment;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

public class ItemVoidFortressArmor extends ItemFortressArmor implements IWarpingGear, IVisDiscountGear
{
    public ItemVoidFortressArmor (ArmorMaterial material, int j, int k)
    {
        super(material, j, k);
        this.setCreativeTab(TaintedMagic.tabTM);
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.iconHelm = ir.registerIcon("taintedmagic:ItemVoidFortressHelmet");
        this.iconChest = ir.registerIcon("taintedmagic:ItemVoidFortressChestplate");
        this.iconLegs = ir.registerIcon("taintedmagic:ItemVoidFortressLeggings");
    }

    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
        super.addInformation(stack, player, list, b);
    }

    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type)
    {
        return "taintedmagic:textures/models/ModelVoidFortressArmor.png";
    }

    public boolean getIsRepairable (ItemStack stack, ItemStack repairItem)
    {
        return repairItem.isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 16)) ? true
                : super.getIsRepairable(stack, repairItem);
    }

    public void onUpdate (ItemStack stack, World world, Entity entity, int i, boolean b)
    {
        super.onUpdate(stack, world, entity, i, b);
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0 && entity instanceof EntityLivingBase)
            stack.damageItem(-1, (EntityLivingBase) entity);
    }

    public void onArmorTick (World world, EntityPlayer player, ItemStack stack)
    {
        super.onArmorTick(world, player, stack);
        if (!world.isRemote && stack.isItemDamaged() && player.ticksExisted % 20 == 0) stack.damageItem(-1, player);
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 3;
    }

    @Override
    public int getVisDiscount (ItemStack stack, EntityPlayer player, Aspect aspect)
    {
        return 5;
    }
}