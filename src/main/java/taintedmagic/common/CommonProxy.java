package taintedmagic.common;

import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.WandManager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class CommonProxy
{
	public void initRenders ()
	{
	}

	public EntityPlayer getClientPlayer ()
	{
		return null;
	}

	public void registerClientHandlers ()
	{
	}

	public void spawnShockwaveParticles (World w)
	{
	}
}
