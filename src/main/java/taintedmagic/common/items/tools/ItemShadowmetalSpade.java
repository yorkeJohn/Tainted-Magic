package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalSpade extends ItemSpade implements IRepairable
{
    public ItemShadowmetalSpade (ToolMaterial material)
    {
        super(material);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setTextureName("taintedmagic:ItemShadowmetalSpade");
        this.setUnlocalizedName("ItemShadowmetalSpade");
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
