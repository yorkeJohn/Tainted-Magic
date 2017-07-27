package taintedmagic.common.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;

import org.lwjgl.opengl.GL11;

import taintedmagic.client.renderer.RenderItemKatana;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.vec.Vector3;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlyteCharm extends Item implements IWarpingGear
{
	boolean isFlying = false;

	final int burstCooldown = 40;
	final AspectList cost = new AspectList().add(Aspect.AIR, 10);

	// Flight manager
	List<String> playersWithFlight = new ArrayList();

	private static ResourceLocation circle = new ResourceLocation("taintedmagic:textures/misc/circle.png");

	public ItemFlyteCharm ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFlyteCharm");
		this.setMaxStackSize(1);

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return TaintedMagic.rarityCreation;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
	}

	@Override
	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		itemIcon = ir.registerIcon("taintedmagic:ItemFlyteCharm");
	}

	// Flight manager
	@SubscribeEvent
	public void updateFlight (LivingEvent.LivingUpdateEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) event.entityLiving;
			isFlying = p.capabilities.isFlying;

			if (playersWithFlight.contains(playerStr(p)))
			{
				if (shouldPlayerHaveFlight(p))
				{
					p.capabilities.allowFlying = true;
				}
				else
				{
					if (!p.capabilities.isCreativeMode)
					{
						p.capabilities.allowFlying = false;
						p.capabilities.isFlying = false;
						p.capabilities.disableDamage = false;
					}
					playersWithFlight.remove(playerStr(p));
				}
			}
			else if (shouldPlayerHaveFlight(p))
			{
				playersWithFlight.add(playerStr(p));
				p.capabilities.allowFlying = true;
			}

			ItemStack s = null;
			boolean b = false;

			for (int i = 0; i < p.inventory.getSizeInventory(); i++)
			{
				if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).getItem() instanceof ItemFlyteCharm)
				{
					s = p.inventory.getStackInSlot(i);
					b = true;
					break;
				}
				else s = null;
			}

			if (s != null)
			{
				// Burst CD
				if (getBurstCooldown(s) > 0.0F) setBurstCooldown(s, (int) Math.max(getBurstCooldown(s) - 1, 0));

				// Negate fall dmg
				if (p.fallDistance > 3.0F) p.fallDistance = 0.0F;

				// Speed boost
				if (p.moveForward > 0.0F)
				{
					float mul = 1.0F / 30.0F;
					p.moveFlying(0.0F, 1.0F, mul);
					p.jumpMovementFactor = 0.00002F;
				}

				Vector3 look = new Vector3(p.getLookVec());
				look.normalize();

				if (isFlying)
				{
					if (!p.capabilities.isCreativeMode) TaintedMagicHelper.consumeVisFromInventory(p, cost, true);
					if (getBurstCooldown(s) == 0 && p.worldObj.isRemote && Minecraft.getMinecraft().gameSettings.keyBindSprint.getIsKeyPressed())
					{
						if (p.onGround) Minecraft.getMinecraft().gameSettings.keyBindSprint.unPressAllKeys();
						else if (!p.onGround)
						{
							p.worldObj.playSound(p.posX, p.posY, p.posZ, "taintedmagic:burst", 5.0F, 1.0F + (float) Math.random() * 0.1F, true);
							p.motionX += look.x * 1.5D;
							p.motionZ += look.z * 1.5D;
							p.moveFlying(0.0F, 1.0F, 5.0F);

							setBurstCooldown(s, this.burstCooldown);
						}
					}
					if (getBurstCooldown(s) < this.burstCooldown / 2) p.setSprinting(false);
				}
				else
				{
					boolean doGlide = p.isSneaking() && !p.onGround && p.fallDistance >= 2.0F;
					if (doGlide)
					{
						p.motionY = Math.max(-0.4F, p.motionY * 2);
						float mul = 3.0F;
						p.motionX = look.x * mul;
						p.motionZ = look.z * mul;
						p.fallDistance = 2.0F;
					}
				}
			}
			else
			{
				if (!p.capabilities.isCreativeMode) p.capabilities.isFlying = false;
				b = false;
			}
		}
	}

	// Flight manager
	@SubscribeEvent
	public void playerLoggedOut (PlayerEvent.PlayerLoggedOutEvent event)
	{
		String username = event.player.getGameProfile().getName();
		playersWithFlight.remove(username + ":false");
		playersWithFlight.remove(username + ":true");
	}

	public String playerStr (EntityPlayer p)
	{
		return p.getGameProfile().getName() + ":" + p.worldObj.isRemote;
	}

	// player flight status
	private boolean shouldPlayerHaveFlight (EntityPlayer p)
	{
		for (int i = 0; i < p.inventory.getSizeInventory(); i++)
			if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).getItem() instanceof ItemFlyteCharm && TaintedMagicHelper.consumeVisFromInventory(p, cost, false)) return true;
		return false;
	}

	public void setBurstCooldown (ItemStack s, int cooldown)
	{
		if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
		s.getTagCompound().setInteger("cooldown", cooldown);
	}

	public float getBurstCooldown (ItemStack s)
	{
		if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
		return s.stackTagCompound.getInteger("cooldown");
	}

	@SideOnly (Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRender (RenderPlayerEvent.Specials.Post event)
	{
		EntityPlayer p = event.entityPlayer;

		boolean render = false;

		for (int i = 0; i < p.inventory.getSizeInventory(); i++)
		{
			if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).getItem() instanceof ItemFlyteCharm)
			{
				render = true;
				break;
			}
			else render = false;
		}

		if (render)
		{
			GL11.glPushMatrix();

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if (p.isSneaking()) GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);

			GL11.glTranslated(0, (p != Minecraft.getMinecraft().thePlayer ? 1.62F : 0F) - p.getDefaultEyeHeight() + (p.isSneaking() ? 0.0625 : 0), 0);

			GL11.glRotatef(45, -1, 0, -1);
			GL11.glTranslatef(0.0F, -0.5F, -0.2F);

			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);

			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glColor4f(1F, 1F, 1F, 0.8F);

			Tessellator t = Tessellator.instance;

			GL11.glScalef(0.4F, 0.4F, 0.4F);
			GL11.glRotatef(p.ticksExisted + event.partialRenderTick, 0F, 1F, 0F);

			UtilsFX.bindTexture(circle);

			t.startDrawingQuads();
			t.addVertexWithUV(-1, 0, -1, 0, 0);
			t.addVertexWithUV(-1, 0, 1, 0, 1);
			t.addVertexWithUV(1, 0, 1, 1, 1);
			t.addVertexWithUV(1, 0, -1, 1, 0);
			t.draw();

			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glShadeModel(GL11.GL_FLAT);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);

			GL11.glPopMatrix();
		}
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 5;
	}
}
