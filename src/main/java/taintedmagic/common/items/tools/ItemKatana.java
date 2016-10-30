package taintedmagic.common.items.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import taintedmagic.client.model.ModelKatana;
import taintedmagic.client.model.ModelSaya;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityTaintBubble;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.helper.Vector3;
import taintedmagic.common.network.PacketAttackEntityFromClient;
import taintedmagic.common.network.PacketHandler;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.projectile.EntityEmber;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKatana extends Item implements IWarpingGear, IRepairable
{
	public static final int SUBTYPES = 3;

	public static final ResourceLocation textureThaumium = new ResourceLocation("taintedmagic:textures/models/ModelKatanaThaumium.png");
	public static final ResourceLocation textureVoidmetal = new ResourceLocation("taintedmagic:textures/models/ModelKatanaVoidmetal.png");
	public static final ResourceLocation textureShadowmetal = new ResourceLocation("taintedmagic:textures/models/ModelKatanaShadowmetal.png");

	public static final ModelKatana katana = new ModelKatana();
	public static final ModelSaya saya = new ModelSaya();

	public ItemKatana ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemKatana");
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);

		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public boolean hitEntity (ItemStack s, EntityLivingBase e, EntityLivingBase p)
	{
		if (!e.worldObj.isRemote && (! (e instanceof EntityPlayer) || ! (p instanceof EntityPlayer) || MinecraftServer.getServer().isPVPEnabled()))
		{
			try
			{
				if (s.getItemDamage() == 1)
				{
					e.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 60));
					e.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 120));
				}
				if (s.getItemDamage() == 2)
				{
					e.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 120, 2));
					e.addPotionEffect(new PotionEffect(Config.potionBlurredID, 120));
				}
			}
			catch (Exception ex)
			{
			}
			if (hasAnyInscription(s))
			{
				switch (getInscription(s))
				{
				case 0 :
				{
					e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p).setFireDamage().setDamageBypassesArmor(), getAttackDamage(s));
					e.setFire(5);
					break;
				}
				case 1 :
				{
					e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p).setMagicDamage().setDamageBypassesArmor(), getAttackDamage(s));

					try
					{
						e.addPotionEffect(new PotionEffect(Config.potionTaintPoisonID, 100));
					}
					catch (Exception ex)
					{
					}
					break;
				}
				case 2 :
				{
					e.attackEntityFrom(new DamageSource("wind").setMagicDamage().setDamageBypassesArmor(), getAttackDamage(s));

					try
					{
						e.addPotionEffect(new PotionEffect(Potion.wither.id, 60));
					}
					catch (Exception ex)
					{
					}
					break;
				}
				}
			}
			else e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p), getAttackDamage(s));
		}
		p.worldObj.playSoundAtEntity(p, "thaumcraft:swing", 0.5F + (float) Math.random(), 0.5F + (float) Math.random());

		return super.hitEntity(s, e, p);
	}

	@Override
	public boolean isFull3D ()
	{
		return true;
	}

	@Override
	public boolean canHarvestBlock (Block b, ItemStack s)
	{
		return false;
	}

	@Override
	public EnumAction getItemUseAction (ItemStack s)
	{
		return EnumAction.bow;
	}

	@SideOnly (Side.CLIENT)
	public void getSubItems (Item item, CreativeTabs c, List l)
	{
		for (int i = 0; i < SUBTYPES; i++)
			l.add(new ItemStack(this, 1, i));
	}

	public String getUnlocalizedName (ItemStack s)
	{
		return super.getUnlocalizedName() + "." + s.getItemDamage();
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		if (s.getItemDamage() == 1) l.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("enchantment.special.sapgreat"));
		l.add(" ");
		l.add("\u00A79+" + getAttackDamage(s) + " " + StatCollector.translateToLocal("text.attackdamage"));

		if (s.hasTagCompound() && s.stackTagCompound.hasKey("inscription"))
		{
			l.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("text.katana.inscription." + s.stackTagCompound.getInteger("inscription")));
		}
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}

	public float getAttackDamage (ItemStack s)
	{
		switch (s.getItemDamage())
		{
		case 0 :
			return 14.25F;
		case 1 :
			return 17.55F;
		case 2 :
			return 20.75F;
		}
		return 0;
	}

	@Override
	public int getMaxItemUseDuration (ItemStack s)
	{
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
	{
		p.setItemInUse(s, getMaxItemUseDuration(s));
		return s;
	}

	@Override
	public void onUsingTick (ItemStack s, EntityPlayer p, int i)
	{
		super.onUsingTick(s, p, i);
		float j = 1.0F + (Math.min(1.0F * (float) p.getItemInUseDuration() / 30, 1.0F) + ((float) Math.random() * 0.25F));
		if (p.ticksExisted % 5 == 0) p.worldObj.playSoundAtEntity(p, "thaumcraft:wind", j * 0.1F, j);
	}

	@Override
	public void onPlayerStoppedUsing (ItemStack s, World w, EntityPlayer p, int i)
	{
		super.onPlayerStoppedUsing(s, w, p, i);
		if (!hasAnyInscription(s) || castType(p) == 0 || p.isSneaking())
		{
			if (w.isRemote)
			{
				MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
				float mul = Math.min(1.0F + (float) p.getItemInUseDuration() / 40.0F, 2.0F);

				if (mop.entityHit != null) PacketHandler.INSTANCE.sendToServer(new PacketAttackEntityFromClient(mop.entityHit, p, this.getAttackDamage(s) * mul));

				p.swingItem();
			}
			p.worldObj.playSoundAtEntity(p, "thaumcraft:swing", 0.5F + (float) Math.random(), 0.5F + (float) Math.random());
		}
		else if (hasAnyInscription(s) && castType(p) == 1 && !p.isSneaking())
		{
			switch (getInscription(s))
			{
			case 0 :
			{
				for (int a = 0; a < 50; a++)
				{
					EntityEmber proj = new EntityEmber(p.worldObj, p, 5.0F);
					proj.posX += proj.motionX;
					proj.posY += proj.motionY;
					proj.posZ += proj.motionZ;
					proj.damage = 10.0F;
					p.worldObj.spawnEntityInWorld(proj);
					p.swingItem();
				}
				break;
			}
			case 1 :
			{
				for (int a = 0; a < 50; a++)
				{
					EntityTaintBubble proj = new EntityTaintBubble(p.worldObj, p, 5.0F, false);
					proj.posX += proj.motionX;
					proj.posY += proj.motionY;
					proj.posZ += proj.motionZ;
					proj.damage = 10.0F;
					p.worldObj.spawnEntityInWorld(proj);
					p.swingItem();
				}
				break;
			}
			case 2 :
			{
				List<EntityLivingBase> ents = p.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(p.posX, p.posY, p.posZ, p.posX + 1, p.posY + 1, p.posZ + 1).expand(5.0D, 5.0D, 5.0D));
				if (ents != null && ents.size() > 0)
				{
					for (int a = 0; a < ents.size(); a++)
					{
						EntityLivingBase e = ents.get(a);

						if (e != p && e.isEntityAlive() && !e.isEntityInvulnerable())
						{
							double dist = TaintedMagicHelper.getDistanceTo(e.posX, e.posY, e.posZ, p);
							if (dist < 2) e.attackEntityFrom(DamageSource.causePlayerDamage(p).setMagicDamage().setDamageBypassesArmor(), 5.0F);
							Vector3 movement = TaintedMagicHelper.getDistanceBetween(e, p);
							e.addVelocity(movement.x * 3, 0.8, movement.z * 3);
						}
					}
				}
				p.swingItem();
				TaintedMagic.proxy.spawnWindParticles(w);
				break;
			}
			default :
				break;
			}
		}
	}

	public int castType (EntityPlayer p)
	{
		float f = Math.min((float) p.getItemInUseDuration() / 15.0F, 2.0F);

		if (f == 2.0F) return 1;
		else return 0;
	}

	@SideOnly (Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRender (RenderPlayerEvent.Specials.Post event)
	{
		EntityPlayer p = event.entityPlayer;

		if (event.entityLiving.getActivePotionEffect(Potion.invisibility) != null) return;

		for (int i = 0; i < p.inventory.getSizeInventory(); i++)
		{
			if (p.inventory.getStackInSlot(i) != null && p.inventory.getStackInSlot(i).getItem() == this)
			{
				ItemStack s = p.inventory.getStackInSlot(i);

				GL11.glPushMatrix();

				int light = p.getBrightnessForRender(0);
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				if (p.getHeldItem() == null || p.getHeldItem().getItem() != this)
				{
					GL11.glPushMatrix();

					GL11.glScalef(0.5F, 0.5F, 0.5F);
					GL11.glRotatef(55, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

					GL11.glTranslatef(-0.6F, 2.25F, 1.25F);

					Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(s));
					katana.render(0.0625F);

					GL11.glPopMatrix();
				}

				GL11.glPushMatrix();

				GL11.glScalef(0.5F, 0.5F, 0.5F);
				GL11.glRotatef(55, 1.0F, 0.0F, 0.0F);
				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

				GL11.glTranslatef(-0.6F, 2.25F, 1.25F);

				Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(s));
				saya.render(0.0625F);

				GL11.glPopMatrix();

				GL11.glPopMatrix();

				break;
			}
		}
	}

	@SideOnly (Side.CLIENT)
	public static void renderHUD (ScaledResolution r, EntityPlayer p, float pt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ItemStack s = mc.thePlayer.getCurrentEquippedItem();
		Tessellator t = Tessellator.instance;

		if (s != null && s.getItem() instanceof ItemKatana)
		{
			float tickFract = Math.min((float) p.getItemInUseDuration() / 30.0F, 1.0F);

			int x = r.getScaledWidth() / 2 + 725;
			int x2 = x + 16;
			int y = r.getScaledHeight() / 2 + (p.capabilities.isCreativeMode ? 810 : 755);
			int y2 = y + 16;

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			float sc = 0.315F;

			GL11.glScalef(sc, sc, sc);

			mc.renderEngine.bindTexture(new ResourceLocation("thaumcraft:textures/misc/script.png"));

			for (int rune = 0; rune < 16; rune++)
			{
				float red = MathHelper.sin( (p.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.8F;
				float green = MathHelper.sin( (p.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.7F;
				float alpha = MathHelper.sin( (p.ticksExisted + rune * 5) / 10.0F) * 0.3F;

				float f = 0.0625F * rune;
				float f1 = f + 0.0625F;
				float f2 = 0.0F;
				float f3 = 1.0F;

				t.startDrawingQuads();

				t.setBrightness(240);
				t.setColorRGBA_F(red, green, 0.4F, alpha + 0.7F);
				t.addVertexWithUV(x + (rune * 16) - alpha, y2 + alpha, 0, f, f3);
				t.addVertexWithUV(x2 + (rune * 16) + alpha, y2 + alpha, 0, f1, f3);
				t.addVertexWithUV(x2 + (rune * 16) + alpha, y - alpha, 0, f1, f2);
				t.addVertexWithUV(x + (rune * 16) - alpha, y - alpha, 0, f, f2);

				t.draw();

				if ((int) (16 * tickFract) > rune)
				{
					t.startDrawingQuads();

					t.setBrightness(240);
					t.setColorRGBA_F(1.0F - (0.0625F * rune), 0.0F + (0.0625F * rune), 0.0F, 0.9F + alpha);
					t.addVertexWithUV(x + (rune * 16) - alpha, y2 + alpha, 0, f, f3);
					t.addVertexWithUV(x2 + (rune * 16) + alpha, y2 + alpha, 0, f1, f3);
					t.addVertexWithUV(x2 + (rune * 16) + alpha, y - alpha, 0, f1, f2);
					t.addVertexWithUV(x + (rune * 16) - alpha, y - alpha, 0, f, f2);

					t.draw();
				}
			}
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
		}
	}

	public static boolean hasAnyInscription (ItemStack s)
	{
		return s.hasTagCompound() && s.stackTagCompound.hasKey("inscription");
	}

	public static int getInscription (ItemStack s)
	{
		if (s.hasTagCompound() && s.stackTagCompound.hasKey("inscription")) return s.stackTagCompound.getInteger("inscription");
		else return 0;
	}

	public static ResourceLocation getTexture (ItemStack s)
	{
		switch (s.getItemDamage())
		{
		case 0 :
			return textureThaumium;
		case 1 :
			return textureVoidmetal;
		case 2 :
			return textureShadowmetal;
		}
		return null;
	}

	@Override
	public int getWarp (ItemStack s, EntityPlayer p)
	{
		return s.getItemDamage() == 0 ? 0 : s.getItemDamage() == 1 ? 3 : 7;
	}
}
