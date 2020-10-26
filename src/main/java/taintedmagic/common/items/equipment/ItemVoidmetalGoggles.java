package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemGoggles;

public class ItemVoidmetalGoggles extends ItemGoggles implements IWarpingGear
{
    public ItemVoidmetalGoggles (ArmorMaterial material, int j, int k)
    {
        super(material, j, k);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemVoidmetalGoggles");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemVoidmetalGoggles");
    }

    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type)
    {
        return "taintedmagic:textures/models/ModelVoidmetalGoggles.png";
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.rare;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 5;
    }

    @Override
    public int getVisDiscount (ItemStack stack, EntityPlayer player, Aspect aspect)
    {
        return 12;
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
}
