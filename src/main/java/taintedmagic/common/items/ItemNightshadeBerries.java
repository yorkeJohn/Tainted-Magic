package taintedmagic.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;

public class ItemNightshadeBerries extends ItemFood {

    public ItemNightshadeBerries(final int healAmount, final float saturation, final boolean b) {
        super(healAmount, saturation, b);
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemNightshadeBerries");
        setTextureName("taintedmagic:ItemNightshadeBerries");
        setAlwaysEdible();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    protected void onFoodEaten(final ItemStack stack, final World world, final EntityPlayer player) {
        super.onFoodEaten(stack, world, player);
        if (!world.isRemote) {
            player.attackEntityFrom(new DamageSource("nightshade"), Short.MAX_VALUE);
        }
    }
}
