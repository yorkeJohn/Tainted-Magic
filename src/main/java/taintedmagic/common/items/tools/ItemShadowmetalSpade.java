package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalSpade extends ItemSpade implements IRepairable {

    public ItemShadowmetalSpade(final ToolMaterial material) {
        super(material);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemShadowmetalSpade");
        setUnlocalizedName("ItemShadowmetalSpade");
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0))
                ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
