package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalPick extends ItemPickaxe implements IRepairable {

    public ItemShadowmetalPick(final ToolMaterial material) {
        super(material);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemShadowmetalPick");
        setUnlocalizedName("ItemShadowmetalPick");
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
