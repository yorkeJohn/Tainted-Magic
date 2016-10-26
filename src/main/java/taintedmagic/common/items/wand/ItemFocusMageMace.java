package taintedmagic.common.items.wand;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFocusMageMace extends ItemFocusBasic
{
	public IIcon depthIcon = null;
	public IIcon ornIcon = null;

	public int dmg;

	public static FocusUpgradeType bloodlust = new FocusUpgradeType(58, new ResourceLocation("taintedmagic", "textures/misc/IconBloodlust.png"), "focus.upgrade.bloodlust.name", "focus.upgrade.bloodlust.text", new AspectList().add(Aspect.WEAPON, 1).add(Aspect.HEAL, 1));
	public static final AspectList costBase = new AspectList().add(Aspect.ENTROPY, 50);
	public static final AspectList costBloodlust = new AspectList().add(Aspect.ENTROPY, 50).add(Aspect.ORDER, 20).add(Aspect.FIRE, 50);

	public ItemFocusMageMace ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusMageMace");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusMageMace");
		this.depthIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_depth");
		this.ornIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_orn");
	}

	public IIcon getFocusDepthLayerIcon (ItemStack s)
	{
		return this.depthIcon;
	}

	public IIcon getOrnament (ItemStack s)
	{
		return this.ornIcon;
	}

	public String getSortingHelper (ItemStack s)
	{
		return "MM" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 3289650;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return this.isUpgradedWith(s, bloodlust) ? this.costBloodlust : this.costBase;
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
		l.add(EnumChatFormatting.BLUE + "+" + new String(this.isUpgradedWith(s, FocusUpgradeType.potency) ? Integer.toString(15 + this.getUpgradeLevel(s, FocusUpgradeType.potency)) : "15") + " " + StatCollector.translateToLocal("text.attackdamageequipped"));
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int rank)
	{
		switch (rank)
		{
		case 1 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 2 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 3 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 4 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency };
		case 5 :
			return new FocusUpgradeType[]{
					FocusUpgradeType.frugal,
					FocusUpgradeType.potency,
					this.bloodlust };
		}
		return null;
	}

	public boolean canApplyUpgrade (ItemStack s, EntityPlayer p, FocusUpgradeType t, int rank)
	{
		return (!t.equals(this.bloodlust)) || (ThaumcraftApiHelper.isResearchComplete(p.getCommandSenderName(), "BLOODLUSTUPGRADE"));
	}
}
