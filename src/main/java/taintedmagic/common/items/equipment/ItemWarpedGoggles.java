package taintedmagic.common.items.equipment;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.items.armor.ItemGoggles;

public class ItemWarpedGoggles extends ItemGoggles implements IWarpingGear
{
    public ItemWarpedGoggles (ArmorMaterial material, int j, int k)
    {
        super(material, j, k);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemWarpedGoggles");
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.icon = ir.registerIcon("taintedmagic:ItemWarpedGoggles");
    }

    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type)
    {
        return "taintedmagic:textures/models/ModelWarpedGoggles.png";
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.rare;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 1;
    }

    public boolean getIsRepairable (ItemStack stack, ItemStack repairItem)
    {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
                : super.getIsRepairable(stack, repairItem);
    }
}
