package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalPick extends ItemPickaxe implements IRepairable
{
    public ItemShadowmetalPick (ToolMaterial material)
    {
        super(material);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemShadowmetalPick");
        this.setUnlocalizedName("ItemShadowmetalPick");
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
