package taintedmagic.common.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import taintedmagic.api.IEquipmentItemRenderer;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.items.tools.ItemKatana;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.vec.Vector3;

public class ItemFlyteCharm extends Item implements IWarpingGear, IEquipmentItemRenderer
{
	boolean isFlying = false;

	static final int maxBurstCooldown = 40;
	static final AspectList cost = new AspectList().add(Aspect.AIR, 10);

	static final String TAG_SPRINTING = "isSprinting";
	static final String TAG_COOLDOWN = "cooldown";

	// Flight manager
	static List<String> playersWithFlight = new ArrayList();

	private static final ResourceLocation circle = new ResourceLocation("taintedmagic:textures/misc/circle.png");

	public ItemFlyteCharm ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFlyteCharm");
		this.setMaxStackSize(1);
		this.setTextureName("taintedmagic:ItemFlyteCharm");

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack s)
	{
		return TaintedMagic.rarityCreation;
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

			InventoryPlayer inv = p.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack stackInSlot = inv.getStackInSlot(i);
				if (stackInSlot != null && stackInSlot.getItem() instanceof ItemKatana)
				{
					s = stackInSlot;
					break;
				}
				else
				{
					s = null;
				}
			}

			if (s != null)
			{
				if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();

				// Sprint stuff
				boolean wasSprinting = s.stackTagCompound.getBoolean(TAG_SPRINTING);
				boolean isSprinting = p.isSprinting();
				if (isSprinting != wasSprinting) s.stackTagCompound.setBoolean(TAG_SPRINTING, isSprinting);

				// Burst CD
				if (getBurstCooldown(s) > 0) setBurstCooldown(s, (int) Math.max(getBurstCooldown(s) - 1, 0));

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

					// Burst
					if (!wasSprinting && isSprinting && getBurstCooldown(s) == 0 && TaintedMagicHelper.consumeVisFromInventory(p, new AspectList().add(Aspect.FIRE, 500), true))
					{
						p.worldObj.playSound(p.posX, p.posY, p.posZ, "taintedmagic:burst", 5.0F, 1.0F + (float) Math.random() * 0.1F, true);
						p.motionX += look.x * 1.5D;
						p.motionZ += look.z * 1.5D;

						setBurstCooldown(s, this.maxBurstCooldown);
					}
					else if (getBurstCooldown(s) > 0)
					{
						if (this.maxBurstCooldown - getBurstCooldown(s) < 2) p.moveFlying(0F, 1.0F, 5.0F);
						else if (this.maxBurstCooldown - getBurstCooldown(s) < 10) p.setSprinting(false);
					}
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

	private String playerStr (EntityPlayer p)
	{
		return p.getGameProfile().getName() + ":" + p.worldObj.isRemote;
	}

	private boolean shouldPlayerHaveFlight (EntityPlayer p)
	{
		for (int i = 0; i < p.inventory.getSizeInventory(); i++)
			if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).getItem() instanceof ItemFlyteCharm && TaintedMagicHelper.consumeVisFromInventory(p, cost, false)) return true;
		return false;
	}

	private void setBurstCooldown (ItemStack s, int cooldown)
	{
		if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
		s.getTagCompound().setInteger(TAG_COOLDOWN, cooldown);
	}

	private float getBurstCooldown (ItemStack s)
	{
		if (s.stackTagCompound == null) s.stackTagCompound = new NBTTagCompound();
		return s.stackTagCompound.getInteger(TAG_COOLDOWN);
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return 5;
	}

	@Override
	public void render (EntityPlayer p, ItemStack s, float pT)
	{
		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

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
		GL11.glRotatef(p.ticksExisted + pT, 0F, 1F, 0F);

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
