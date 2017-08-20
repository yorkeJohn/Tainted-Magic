package taintedmagic.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.EquipmentItemRenderHandler;

public class CommonProxy
{
	public void registerClientHandlers ()
	{
	}
	
	public void registerRenderers ()
	{
	}

	public EntityPlayer getClientPlayer ()
	{
		return null;
	}

	public World getClientWorld ()
	{
		return null;
	}
}
