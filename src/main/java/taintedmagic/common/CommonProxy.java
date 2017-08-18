package taintedmagic.common;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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
