package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalSword extends ItemSword implements IRepairable
{
    public ItemShadowmetalSword (ToolMaterial m)
    {
        super(m);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemShadowmetalSword");
        this.setUnlocalizedName("ItemShadowmetalSword");
    }

    public boolean getIsRepairable (ItemStack stack, ItemStack repairItem)
    {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
