package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import thaumcraft.common.config.ConfigItems;

public class ItemCrimsonBlood extends Item {

    public ItemCrimsonBlood() {
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemCrimsonBlood");
        setUnlocalizedName("ItemCrimsonBlood");
        setContainerItem(ConfigItems.itemEssence);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
