package taintedmagic.common.items.wand.foci;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.handler.ConfigHandler;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

public class ItemFocusMageMace extends ItemFocusBasic
{
	public IIcon ornIcon = null;

	public static final AspectList cost = new AspectList().add(Aspect.ENTROPY, 50);

	public ItemFocusMageMace ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusMageMace");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusMageMace");
		this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_orn");
	}

	public IIcon getOrnament (ItemStack s)
	{
		return this.ornIcon;
	}

	@SideOnly (Side.CLIENT)
	public boolean requiresMultipleRenderPasses ()
	{
		return true;
	}

	@SideOnly (Side.CLIENT)
	public int getRenderPasses (int m)
	{
		return 2;
	}

	@Override
	@SideOnly (Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass (int meta, int pass)
	{
		return (pass == 0) ? this.ornIcon : this.icon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "MACE" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 0xACA9B5;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return this.cost;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return -1;
	}

	public boolean isVisCostPerTick (ItemStack s)
	{
		return false;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return WandFocusAnimation.WAVE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		return s;
	}

	@Override
	public void addInformation (ItemStack s, EntityPlayer p, List l, boolean b)
	{
		super.addInformation(s, p, l, b);
		l.add(" ");
		l.add(EnumChatFormatting.BLUE + "+" + String.valueOf(ConfigHandler.MAGE_MACE_DMG_INC_BASE + (double) this.getUpgradeLevel(s, FocusUpgradeType.potency)) + " "
				+ StatCollector.translateToLocal("text.attackdamageequipped"));
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 4 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{ FocusUpgradeType.frugal, FocusUpgradeType.potency };
		}
		return null;
	}
}
