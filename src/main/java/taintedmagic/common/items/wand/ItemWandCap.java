package taintedmagic.common.items.wand;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import taintedmagic.common.TaintedMagic;

public class ItemWandCap extends Item
{
    int SUBTYPES = 4;
    IIcon[] icons = new IIcon[SUBTYPES];

    public ItemWandCap ()
    {
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemWandCap");
        this.setHasSubtypes(true);
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        for (int i = 0; i < icons.length; i++)
            this.icons[i] = ir.registerIcon("taintedmagic:ItemWandCap" + i);
    }

    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamage (int i)
    {
        return this.icons[i];
    }

    @SideOnly (Side.CLIENT)
    public void getSubItems (Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < SUBTYPES; i++)
            list.add(new ItemStack(this, 1, i));
    }

    public String getUnlocalizedName (ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
