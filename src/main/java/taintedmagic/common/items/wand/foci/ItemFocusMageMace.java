package taintedmagic.common.items.wand.foci;

import java.text.DecimalFormat;
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
    IIcon ornIcon = null;

    private static final AspectList COST = new AspectList().add(Aspect.ENTROPY, 10);

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

    public IIcon getOrnament (ItemStack stack)
    {
        return this.ornIcon;
    }

    @SideOnly (Side.CLIENT)
    public boolean requiresMultipleRenderPasses ()
    {
        return true;
    }

    @SideOnly (Side.CLIENT)
    public int getRenderPasses (int meta)
    {
        return 2;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass (int meta, int pass)
    {
        return (pass == 0) ? this.ornIcon : this.icon;
    }

    public String getSortingHelper (ItemStack stack)
    {
        return "MACE" + super.getSortingHelper(stack);
    }

    public int getFocusColor (ItemStack stack)
    {
        return 0xACA9B5;
    }

    public AspectList getVisCost (ItemStack stack)
    {
        return this.COST;
    }

    public int getActivationCooldown (ItemStack stack)
    {
        return -1;
    }

    public boolean isVisCostPerTick (ItemStack stack)
    {
        return false;
    }

    public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack stack)
    {
        return WandFocusAnimation.WAVE;
    }

    public ItemStack onFocusRightClick (ItemStack stack, World world, EntityPlayer player, MovingObjectPosition mop)
    {
        return stack;
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        super.addInformation(stack, player, list, b);
        list.add(" ");

        DecimalFormat df = new DecimalFormat("0.#");
        double dmg = ConfigHandler.MAGE_MACE_DMG_INC_BASE + (double) this.getUpgradeLevel(stack, FocusUpgradeType.potency);

        list.add(EnumChatFormatting.BLUE + "+" + df.format(dmg) + " "
                + StatCollector.translateToLocal("text.attackdamageequipped"));
    }

    public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack stack, int rank)
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
