package taintedmagic.common.lib;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class LibMaterials
{
    // Tool materials
    public static ToolMaterial toolMatHollow = EnumHelper.addToolMaterial("HOLLOW", 1, 200, 4.0F, -0.5F, 5);
    public static ToolMaterial toolMatShadow = EnumHelper.addToolMaterial("SHADOW", 3, 2500, 17.0F, 6.0F, 30);
    public static ToolMaterial toolMatPrimal = EnumHelper.addToolMaterial("PRIMAL", 3, 500, 16.0F, 16.75F, 30);

    // Armour materials
    public static ArmorMaterial armorMatWarped = EnumHelper.addArmorMaterial("WARPED", 500, new int[]{ 4, 0, 0, 0 }, 20);
    public static ArmorMaterial armorMatVoidwalker =
            EnumHelper.addArmorMaterial("VOIDWALKER", 300, new int[]{ 0, 0, 0, 4 }, 30);
    public static ArmorMaterial armorMatVoidFortress =
            EnumHelper.addArmorMaterial("VOIDFORTRESS", 300, new int[]{ 4, 8, 6, 0 }, 30);
    public static ArmorMaterial armorMatShadowFortress =
            EnumHelper.addArmorMaterial("SHADOWFORTRESS", 3000, new int[]{ 4, 10, 6, 0 }, 30);
}
