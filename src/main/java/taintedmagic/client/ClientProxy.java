package taintedmagic.client;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import taintedmagic.client.handler.HUDHandler;
import taintedmagic.client.renderer.RenderEntityHomingShard;
import taintedmagic.client.renderer.RenderEntityTaintBubble;
import taintedmagic.client.renderer.RenderItemKatana;
import taintedmagic.common.CommonProxy;
import taintedmagic.common.entities.EntityEldritchOrbAttack;
import taintedmagic.common.entities.EntityHomingShard;
import taintedmagic.common.entities.EntityTaintBubble;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXWisp;
import thaumcraft.client.renderers.entity.RenderEldritchOrb;
import thaumcraft.codechicken.lib.vec.Vector3;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders ()
	{
		// Entities
		RenderingRegistry.registerEntityRenderingHandler(EntityTaintBubble.class, new RenderEntityTaintBubble());
		RenderingRegistry.registerEntityRenderingHandler(EntityEldritchOrbAttack.class, new RenderEldritchOrb());
		RenderingRegistry.registerEntityRenderingHandler(EntityHomingShard.class, new RenderEntityHomingShard());

		// Items
		MinecraftForgeClient.registerItemRenderer(ItemRegistry.ItemKatana, new RenderItemKatana());
	}

	@Override
	public EntityPlayer getClientPlayer ()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void registerClientHandlers ()
	{
		MinecraftForge.EVENT_BUS.register(new HUDHandler());
	}

	@Override
	public void spawnShockwaveParticles (World w)
	{
		EntityPlayer p = getClientPlayer();

		double xp = 0;
		double zp = 0;

		for (int i = 1; i < 360; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				xp = (Math.cos(i * Math.PI / 180F));
				zp = (Math.sin(i * Math.PI / 180F));

				float red = (0.05F + p.worldObj.rand.nextFloat() * 0.1F);
				float blue = (0.1F + p.worldObj.rand.nextFloat() * 0.1F);

				double off = Math.random() * 0.1;

				FXWisp ef = new FXWisp(w, p.posX + xp + off, p.posY - 1, p.posZ + zp + off, 2.0F * (float) Math.random(), red, 0.0F, blue);
				ef.setGravity(0.0F);
				ef.shrink = true;
				ef.noClip = true;
				Vector3 movement = TaintedMagicHelper.getDistanceBetween(ef, p);
				ef.addVelocity(movement.x * 2.5, 0, movement.z * 2.5);

				ParticleEngine.instance.addEffect(w, ef);
			}
		}
	}
}
