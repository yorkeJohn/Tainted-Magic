package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import taintedmagic.common.TaintedMagic;

public class ItemMaterial extends Item {

    /**
     * 0 Shadow Metal Ingot
     * 1 Shadow-imbued Cloth
     * 2 Crimson-stained Cloth
     * 3 Warped Unbalanced Shard
     * 4 Tainted Unbalanced Shard
     * 5 Shard of Creation
     * 6 Thaumic Alloy Plate
     * 7 Crimson Alloy Plate
     * 8 Shadow Metal Nugget
     * 9 Primordial Nodule
     * 10 Primordial Mote
     * 11 Fragment of Creation
     */
    private static final int SUBTYPES = 12;

    private final IIcon[] icons = new IIcon[SUBTYPES];

    public ItemMaterial() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemMaterial");
        setHasSubtypes(true);
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        final int meta = stack.getItemDamage();
        switch (meta) {
            case 0:
                return EnumRarity.uncommon;
            case 1:
                return EnumRarity.uncommon;
            case 2:
                return EnumRarity.uncommon;
            case 3:
                return EnumRarity.common;
            case 4:
                return EnumRarity.common;
            case 5:
                return TaintedMagic.rarityCreation;
            case 6:
                return EnumRarity.uncommon;
            case 7:
                return EnumRarity.uncommon;
            case 8:
                return EnumRarity.uncommon;
            case 9:
                return EnumRarity.epic;
            case 10:
                return EnumRarity.epic;
            case 11:
                return TaintedMagic.rarityCreation;
        }
        return EnumRarity.common;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        for (int i = 0; i < icons.length; i++) {
            icons[i] = ir.registerIcon("taintedmagic:ItemMaterial" + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int meta) {
        return icons[meta];
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        super.addInformation(stack, player, list, b);

        if (stack.getItemDamage() == 5) {
            list.add(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("text.shard.1"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < SUBTYPES; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }
}
