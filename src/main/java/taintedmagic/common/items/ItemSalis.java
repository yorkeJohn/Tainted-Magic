package taintedmagic.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;

public class ItemSalis extends Item {

    private static final int SUBTYPES = 2;
    private final IIcon[] icons = new IIcon[SUBTYPES];

    public ItemSalis () {
        setCreativeTab(TaintedMagic.tabTM);
        hasSubtypes = true;
        setUnlocalizedName("ItemSalis");
    }

    @Override
    public EnumRarity getRarity (final ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerIcons (final IIconRegister ir) {
        for (int i = 0; i < icons.length; i++) {
            icons[i] = ir.registerIcon("taintedmagic:ItemSalis" + i);
        }
    }

    @Override
    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamage (final int meta) {
        return icons[meta];
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void getSubItems (final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < SUBTYPES; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName (final ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public ItemStack onItemRightClick (final ItemStack stack, final World world, final EntityPlayer player) {
        final int meta = stack.getItemDamage();
        switch (meta) {
        // Weather
        case 0 : {
            world.getWorldInfo().setRainTime(world.isRaining() ? 24000 : 0);
            world.getWorldInfo().setRaining(!world.isRaining());
            player.playSound("thaumcraft:wind", 0.3F, 1.0F + world.rand.nextFloat() * 0.5F);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            return stack;
        }
        // Time
        case 1 : {
            world.setWorldTime(world.isDaytime() ? 14000 : 24000);
            player.playSound("thaumcraft:wind", 0.3F, 1.0F + world.rand.nextFloat() * 0.5F);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            return stack;
        }
        }
        return stack;
    }
}
