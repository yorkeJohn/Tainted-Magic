package taintedmagic.common.items;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TMMaterials {
    // Tool materials
    public static final ToolMaterial TOOL_HOLLOW = EnumHelper.addToolMaterial("HOLLOW", 1, 200, 4.0F, -0.5F, 5);
    public static final ToolMaterial TOOL_SHADOW = EnumHelper.addToolMaterial("SHADOW", 3, 2500, 17.0F, 6.0F, 30);
    public static final ToolMaterial TOOL_PRIMAL = EnumHelper.addToolMaterial("PRIMAL", 3, 500, 16.0F, 16.75F, 30);

    // Armour materials
    public static final ArmorMaterial ARMOR_WARPED = EnumHelper.addArmorMaterial("WARPED", 500, new int[]{ 4, 0, 0, 0 }, 20);
    public static final ArmorMaterial ARMOR_VOIDWALKER =
            EnumHelper.addArmorMaterial("VOIDWALKER", 300, new int[]{ 0, 0, 0, 4 }, 30);
    public static final ArmorMaterial ARMOR_VOIDFORTRESS =
            EnumHelper.addArmorMaterial("VOIDFORTRESS", 300, new int[]{ 4, 8, 6, 0 }, 30);
    public static final ArmorMaterial ARMOR_SHADOWFORTRESS =
            EnumHelper.addArmorMaterial("SHADOWFORTRESS", 3000, new int[]{ 4, 10, 6, 0 }, 30);
}
