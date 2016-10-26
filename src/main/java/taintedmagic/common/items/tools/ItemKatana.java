package taintedmagic.common.items.tools;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import taintedmagic.client.model.ModelKatana;
import taintedmagic.client.model.ModelSaya;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.common.config.Config;
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
					e.addPotionEffect(new PotionEffect(Potion.wither.getId(), 60));
					e.addPotionEffect(new PotionEffect(Config.potionBlurredID, 120));
				}
			}
			catch (Exception ex)
			{
			}
			e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p), getAttackDamage(s));
		}
		p.worldObj.playSoundAtEntity(p, "thaumcraft:swing", 1.0F, 1.0F);

		return super.hitEntity(s, e, p);
	}

	@Override
	public boolean isFull3D ()
	{
		return true;
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
	}

	@Override
	public EnumAction getItemUseAction (ItemStack s)
	{
		return EnumAction.block;
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
			return 17.75F;
		case 2 :
			return 20.5F;
		}
		return 0;
	}

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

				break;
			}
		}
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
