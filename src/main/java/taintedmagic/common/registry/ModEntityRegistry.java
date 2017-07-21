package taintedmagic.common.registry;

import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityDarkMatter;
import taintedmagic.common.entities.EntityDiffusion;
import taintedmagic.common.entities.EntityGlowpet;
import taintedmagic.common.entities.EntityHomingShard;
import taintedmagic.common.entities.EntityTaintBubble;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntityRegistry
{
	public static void init ()
	{
		int ID = 0;
		EntityRegistry.registerModEntity(EntityTaintBubble.class, "EntityTaintBubble", ID++, TaintedMagic.instance, 64, 20, true);
		EntityRegistry.registerModEntity(EntityDarkMatter.class, "EntityDarkMatter", ID++, TaintedMagic.instance, 64, 21, true);
		EntityRegistry.registerModEntity(EntityHomingShard.class, "EntityHomingShard", ID++, TaintedMagic.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityGlowpet.class, "EntityGlowpet", ID++, TaintedMagic.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityDiffusion.class, "EntityDiffusion", ID++, TaintedMagic.instance, 64, 20, true);
	}
}
