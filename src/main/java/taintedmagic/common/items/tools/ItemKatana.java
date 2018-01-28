package taintedmagic.common.items.tools;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.api.IRenderInventoryItem;
import taintedmagic.client.model.ModelKatana;
import taintedmagic.client.model.ModelSaya;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityTaintBubble;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.network.PacketKatanaAttack;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;

public class ItemKatana extends Item implements IWarpingGear, IRepairable, IRenderInventoryItem, IHeldItemHUD
{
	public static final int SUBTYPES = 3;

	public static final ResourceLocation textureThaumium = new ResourceLocation("taintedmagic:textures/models/ModelKatanaThaumium.png");
	public static final ResourceLocation textureVoidmetal = new ResourceLocation("taintedmagic:textures/models/ModelKatanaVoidmetal.png");
	public static final ResourceLocation textureShadowmetal = new ResourceLocation("taintedmagic:textures/models/ModelKatanaShadowmetal.png");

	public static final ModelKatana katana = new ModelKatana();
	public static final ModelSaya saya = new ModelSaya();

	int ticksInUse = 0;

	public ItemKatana ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemKatana");
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
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
					e.attackEntityFrom(DamageSource.wither.setMagicDamage().setDamageBypassesArmor(), getAttackDamage(s));

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
	public void getSubItems (Item i, CreativeTabs c, List l)
	{
		for (int a = 0; a < SUBTYPES; a++)
			l.add(new ItemStack(this, 1, a));
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

		this.ticksInUse = getMaxItemUseDuration(s) - i;

		float j = 1.0F + ((float) Math.random() * 0.25F);
		if (p.ticksExisted % 5 == 0) p.worldObj.playSoundAtEntity(p, "thaumcraft:wind", j * 0.1F, j);
	}

	@Override
	public void onPlayerStoppedUsing (ItemStack s, World w, EntityPlayer p, int i)
	{
		super.onPlayerStoppedUsing(s, w, p, i);
		Random r = new Random();

		if (!hasAnyInscription(s) || !isFullyCharged(p) || p.isSneaking() || getInscription(s) == 2)
		{
			boolean leech = (getInscription(s) == 2 && isFullyCharged(p));
			boolean b = false;

			if (w.isRemote)
			{
				MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
				float mul = Math.min(1.0F + (float) this.ticksInUse / 40.0F, 2.0F);

				if (mop.entityHit != null) PacketHandler.INSTANCE.sendToServer(new PacketKatanaAttack(mop.entityHit, p, this.getAttackDamage(s) * mul, leech));
				p.swingItem();
			}
			p.worldObj.playSoundAtEntity(p, "thaumcraft:swing", 0.5F + (float) Math.random(), 0.5F + (float) Math.random());
		}
		else if (hasAnyInscription(s) && isFullyCharged(p) && !p.isSneaking())
		{
			switch (getInscription(s))
			{
			case 0 :
			{
				EntityExplosiveOrb proj = new EntityExplosiveOrb(w, p);
				proj.strength = getAttackDamage(s) * 0.5F;
				proj.posX += proj.motionX;
				proj.posY += proj.motionY;
				proj.posZ += proj.motionZ;
				if (!w.isRemote) w.spawnEntityInWorld(proj);
				p.swingItem();
				break;
			}
			case 1 :
			{
				for (int a = 0; a < 75; a++)
				{
					EntityTaintBubble proj = new EntityTaintBubble(w, p, 5.0F, getAttackDamage(s) * 2.0F, false);
					proj.posX += proj.motionX;
					proj.posY += proj.motionY;
					proj.posZ += proj.motionZ;
					if (!w.isRemote) w.spawnEntityInWorld(proj);
					p.swingItem();
				}
				break;
			}
			default :
				break;
			}
		}
		s.stackTagCompound = new NBTTagCompound();
		s.getTagCompound().setInteger("inscription", 1);
	}

	private boolean isFullyCharged (EntityPlayer p)
	{
		float f = Math.min((float) this.ticksInUse / 15.0F, 2.0F);

		if (f == 2.0F) return true;
		else return false;
	}

	@Override
	public void renderHUD (ScaledResolution res, EntityPlayer p, ItemStack s, float partialTicks, float fract)
	{
		Tessellator t = Tessellator.instance;

		float tickFract = Math.min((float) p.getItemInUseDuration() / 30.0F, 1.0F);

		int x = res.getScaledWidth() / 2 + 725;
		int x2 = x + 16;
		int y = res.getScaledHeight() / 2 + (p.capabilities.isCreativeMode ? 805 : 755);
		int y2 = y + 16;

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float sc = 0.315F;
		GL11.glScalef(sc, sc, sc);

		UtilsFX.bindTexture(new ResourceLocation("thaumcraft:textures/misc/script.png"));

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
			t.setColorRGBA_F(red, green, 0.4F, (alpha + 0.7F) * fract);
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

	@Override
	public void render (EntityPlayer p, ItemStack s, float pT)
	{
		GL11.glPushMatrix();

		int light = p.getBrightnessForRender(0);
		int lightmapX = light % 65536;
		int lightmapY = light / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		GL11.glPushMatrix();

		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef(55, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

		GL11.glTranslatef(-0.6F, 2.25F, 1.25F);

		Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(s));
		saya.render(0.0625F);

		GL11.glPopMatrix();

		if (p.getHeldItem() == null || p.getHeldItem() != s)
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

		GL11.glPopMatrix();
	}
}
