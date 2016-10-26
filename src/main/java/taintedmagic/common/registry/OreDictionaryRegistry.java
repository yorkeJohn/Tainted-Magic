package taintedmagic.common.registry;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictionaryRegistry
{
	public static void init ()
	{
		OreDictionary.registerOre("oreShadow", new ItemStack(BlockRegistry.BlockShadowOre));
		OreDictionary.registerOre("logWood", new ItemStack(BlockRegistry.BlockWarpwoodLog));
		OreDictionary.registerOre("treeSapling", new ItemStack(BlockRegistry.BlockWarpwoodSapling));
		OreDictionary.registerOre("ingotShadow", new ItemStack(ItemRegistry.ItemMaterial));
		OreDictionary.registerOre("plankWood", new ItemStack(BlockRegistry.BlockWarpwoodPlanks));
	}
}
