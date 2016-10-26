package taintedmagic.common.items.tools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.items.wands.WandManager;

public class ItemThaumicDisassembler extends Item
{
	public static final String TAG_MODE = "mode";

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

		l.add(StatCollector.translateToLocal("text.mode") + ": " + EnumChatFormatting.BLUE + getModeName(s));
		l.add(StatCollector.translateToLocal("text.efficiency") + ": " + EnumChatFormatting.BLUE + getEfficiency(s));
		l.add(" ");
		l.add(EnumChatFormatting.BLUE + "+20 " + StatCollector.translateToLocal("text.attackdamage"));
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

			if (block == Blocks.lit_redstone_ore)
			{
				block = Blocks.redstone_ore;
			}

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

	public int getEfficiency (ItemStack s)
	{
		switch (getMode(s))
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

	public int getMode (ItemStack s)
	{
		if (s.stackTagCompound == null)
		{
			return 0;
		}
		return s.stackTagCompound.getInteger(TAG_MODE);
	}

	public String getModeName (ItemStack s)
	{
		int mode = getMode(s);

		switch (mode)
		{
		case 0 :
			return StatCollector.translateToLocal("text.disassembler.normal");
		case 1 :
			return StatCollector.translateToLocal("text.disassembler.slow");
		case 2 :
			return StatCollector.translateToLocal("text.disassembler.fast");
		case 3 :
			return StatCollector.translateToLocal("text.disassembler.off");
		}
		return null;
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
			if ( (!w.isRemote) && (s.isItemDamaged()) && (e.ticksExisted % 20 == 0))
			{
				if (WandManager.consumeVisFromInventory(p, new AspectList().add(Aspect.ENTROPY, 5)))
				{
					s.damageItem(-1, (EntityLivingBase) e);
				}
			}
		}
	}
}
