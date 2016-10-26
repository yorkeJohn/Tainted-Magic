package taintedmagic.common.registry;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityEldritchOrbAttack;
import taintedmagic.common.entities.EntityTaintBubble;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntityRegistry
{
	public static void init ()
	{
		int id = 0;
		EntityRegistry.registerModEntity(EntityTaintBubble.class, "EntityTaintBubble", id++, TaintedMagic.instance, 64, 20, true);
		EntityRegistry.registerModEntity(EntityEldritchOrbAttack.class, "EntityEldritchOrbAttack", id++, TaintedMagic.instance, 64, 21, true);
	}
}
