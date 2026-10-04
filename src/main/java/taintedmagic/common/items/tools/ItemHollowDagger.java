package taintedmagic.common.items.tools;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRepairable;

public class ItemHollowDagger extends ItemSword implements IRepairable {

    public ItemHollowDagger(final ToolMaterial material) {
        super(material);
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemHollowDagger");
        setMaxStackSize(1);
        setTextureName("taintedmagic:ItemHollowDagger");
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
