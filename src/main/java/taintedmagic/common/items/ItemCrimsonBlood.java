package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import taintedmagic.common.TaintedMagic;
import thaumcraft.common.config.ConfigItems;

public class ItemCrimsonBlood extends Item
{
    public ItemCrimsonBlood ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setTextureName("taintedmagic:ItemCrimsonBlood");
        this.setUnlocalizedName("ItemCrimsonBlood");
        this.setContainerItem(ConfigItems.itemEssence);
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
