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

public class ItemNightshadeBerries extends ItemFood
{
    public ItemNightshadeBerries (int healAmount, float saturation, boolean b)
    {
        super(healAmount, saturation, b);
        this.setCreativeTab(TaintedMagic.tabTM);
        this.setUnlocalizedName("ItemNightshadeBerries");
        this.setTextureName("taintedmagic:ItemNightshadeBerries");
        this.setAlwaysEdible();
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    @Override
    protected void onFoodEaten (ItemStack stack, World world, EntityPlayer player)
    {
        super.onFoodEaten(stack, world, player);
        if (!world.isRemote) player.attackEntityFrom(new DamageSource("nightshade"), Short.MAX_VALUE);
    }
}
