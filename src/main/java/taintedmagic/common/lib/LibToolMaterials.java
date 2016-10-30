package taintedmagic.common.lib;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class LibToolMaterials
{
	public static ToolMaterial toolMatCrystal = EnumHelper.addToolMaterial("CRYSTAL", 3, 2000, 16.0F, 2.0F, 30);
	public static ToolMaterial toolMatShadow = EnumHelper.addToolMaterial("SHADOW", 3, 2200, 17.0F, 6.0F, 30);
	public static ToolMaterial toolMatPrimal = EnumHelper.addToolMaterial("PRIMAL", 3, 512, 16.0F, 14.5F, 28);

	public static ArmorMaterial armorMatShadow = EnumHelper.addArmorMaterial("SHADOW", 800, new int[]{
			4,
			8,
			6,
			2 }, 25);
	public static ArmorMaterial armorMatSpecial = EnumHelper.addArmorMaterial("SPECIAL", 500, new int[]{
			4,
			6,
			6,
			4 }, 25);
	public static ArmorMaterial armorMatVoidFortress = EnumHelper.addArmorMaterial("VOIDFORTRESS", 300, new int[]{
			4,
			8,
			6,
			0 }, 20);

	public static ArmorMaterial armorMatShadowFortress = EnumHelper.addArmorMaterial("SHADOWFORTRESS", 2500, new int[]{
			4,
			10,
			6,
			0 }, 30);
}
