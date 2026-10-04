package taintedmagic.common.registry;

import cpw.mods.fml.common.registry.EntityRegistry;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityDarkMatter;
import taintedmagic.common.entities.EntityDiffusion;
import taintedmagic.common.entities.EntityHomingShard;

public class TMEntityRegistry {

    public static void initEntities() {
        int id = 0;
        EntityRegistry.registerModEntity(
                EntityDarkMatter.class, "EntityDarkMatter", id++, TaintedMagic.instance, 64, 21, true);
        EntityRegistry.registerModEntity(
                EntityHomingShard.class, "EntityHomingShard", id++, TaintedMagic.instance, 64, 3, true);
        EntityRegistry.registerModEntity(
                EntityDiffusion.class, "EntityDiffusion", id++, TaintedMagic.instance, 64, 20, true);
    }
}
