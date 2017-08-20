package taintedmagic.common.items.tools;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.WandManager;

public class ItemThaumicDisassembler extends Item implements IHeldItemHUD
{
	public static final String TAG_MODE = "mode";
	public static List<Integer> data = new ArrayList<Integer>();

	public ItemThaumicDisassembler ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setMaxDamage(500);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("ItemThaumicDisassembler");
		this.setTextureName("taintedmagic:ItemThaumicDisassembler");
	}

	@Override
	public boolean canHarvestBlock (Block b, ItemStack s)
	{
		return b != Blocks.bedrock;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);

		l.add("\u00A78" + StatCollector.translateToLocal("text.disassembler.mode") + ": " + getModeName(s));
		l.add("\u00A78" + StatCollector.translateToLocal("text.disassembler.efficiency") + ": " + (getMode(s) == 3 ? "\u00A7c" : "\u00A7a") + getEfficiency(s));
		l.add(" ");
		l.add("\u00A79" + "+20 " + StatCollector.translateToLocal("text.attackdamage"));
	}

	@Override
	public boolean hitEntity (ItemStack s, EntityLivingBase e, EntityLivingBase p)
	{
		e.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) p), 20);
		s.damageItem(2, p);
		return false;
	}

	@Override
	public float getDigSpeed (ItemStack s, Block b, int meta)
	{
		return getEfficiency(s);
	}

	@Override
	public boolean onBlockDestroyed (ItemStack s, World w, Block b, int x, int y, int z, EntityLivingBase e)
	{
		if (b.getBlockHardness(w, x, y, z) != 0.0D) s.damageItem(1, e);
		return true;
	}

	@Override
	public boolean onBlockStartBreak (ItemStack s, int x, int y, int z, EntityPlayer p)
	{
		super.onBlockStartBreak(s, x, y, z, p);

		if (!p.worldObj.isRemote)
		{
			Block block = p.worldObj.getBlock(x, y, z);
			int meta = p.worldObj.getBlockMetadata(x, y, z);

			if (block == Blocks.lit_redstone_ore) block = Blocks.redstone_ore;

			ItemStack stack = new ItemStack(block, 1, meta);
		}
		return false;
	}

	@Override
	public EnumRarity getRarity (ItemStack s)
	{
		return EnumRarity.uncommon;
	}

	@Override
	public boolean isFull3D ()
	{
		return true;
	}

	@Override
	public ItemStack onItemRightClick (ItemStack s, World w, EntityPlayer p)
	{
		if (!w.isRemote && p.isSneaking()) toggleMode(s);

		return s;
	}

	public static int getEfficiency (int mode)
	{
		switch (mode)
		{
		case 0 :
			return 20;
		case 1 :
			return 8;
		case 2 :
			return 128;
		case 3 :
			return 0;
		}
		return 0;
	}

	public static int getEfficiency (ItemStack s)
	{
		return getEfficiency(getMode(s));
	}

	public static int getMode (ItemStack s)
	{
		if (s.stackTagCompound == null) return 0;
		return s.stackTagCompound.getInteger(TAG_MODE);
	}

	public static String getModeName (int mode)
	{
		switch (mode)
		{
		case 0 :
			return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.normal");
		case 1 :
			return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.slow");
		case 2 :
			return "\u00A7a" + StatCollector.translateToLocal("text.disassembler.fast");
		case 3 :
			return "\u00A7c" + StatCollector.translateToLocal("text.disassembler.off");
		}
		return null;
	}

	public static String getModeName (ItemStack s)
	{
		return getModeName(getMode(s));
	}

	public void toggleMode (ItemStack s)
	{
		if (s.stackTagCompound == null) s.setTagCompound(new NBTTagCompound());
		s.stackTagCompound.setInteger(TAG_MODE, getMode(s) < 3 ? getMode(s) + 1 : 0);
	}

	public void onUpdate (ItemStack s, World w, Entity e, int i, boolean b)
	{
		super.onUpdate(s, w, e, i, b);
		if (e instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e;
			if (!w.isRemote && s.isItemDamaged() && e.ticksExisted % 20 == 0)
			{
				if (WandManager.consumeVisFromInventory(p, new AspectList().add(Aspect.ENTROPY, 5))) s.damageItem(-1, (EntityLivingBase) e);
			}
		}
	}

	@Override
	public void renderHUD (ScaledResolution res, EntityPlayer p, ItemStack s, float partialTicks, float fract)
	{
		if (data.isEmpty())
		{
			data.add(getMode(s));
		}

		if (!data.isEmpty() && getMode(s) != data.get(0))
		{
			data.clear();
			data.add(getMode(s));
		}

		int mode = data.get(0);
		String str = "\u00A78" + StatCollector.translateToLocal("text.disassembler.mode") + ": " + getModeName(mode) + (mode == 3 ? "\u00A7c" : "\u00A7a") + " (" + getEfficiency(mode) + ")";

		GL11.glPushMatrix();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glTranslated(40.0F, -15.0F, 0.0F);
		GL11.glTranslatef(-fract * 40.0F, 0.0F, 0.0F);

		GL11.glScalef(0.8F, 0.8F, 0.8F);

		FontRenderer f = Minecraft.getMinecraft().fontRenderer;
		f.drawStringWithShadow(str, res.getScaledWidth() / 2 + 100, res.getScaledHeight() / 2 + (p.capabilities.isCreativeMode ? 230 : 212), 0xFFFFFF);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);

		GL11.glPopMatrix();
	}
}
