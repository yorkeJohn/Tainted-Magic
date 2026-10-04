package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.text.DecimalFormat;
import java.util.List;
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

public class ItemFocusMageMace extends ItemFocusBasic {

    private IIcon ornIcon;

    private static final AspectList COST = new AspectList().add(Aspect.ENTROPY, 10);

    public ItemFocusMageMace() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemFocusMageMace");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister ir) {
        icon = ir.registerIcon("taintedmagic:ItemFocusMageMace");
        ornIcon = ir.registerIcon("taintedmagic:ItemFocusMageMace_orn");
    }

    @Override
    public IIcon getOrnament(final ItemStack stack) {
        return ornIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(final int meta) {
        return 2;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int meta, final int pass) {
        return pass == 0 ? ornIcon : icon;
    }

    @Override
    public String getSortingHelper(final ItemStack stack) {
        return "MACE" + super.getSortingHelper(stack);
    }

    @Override
    public int getFocusColor(final ItemStack stack) {
        return 0xACA9B5;
    }

    @Override
    public AspectList getVisCost(final ItemStack stack) {
        return COST;
    }

    @Override
    public int getActivationCooldown(final ItemStack stack) {
        return -1;
    }

    @Override
    public boolean isVisCostPerTick(final ItemStack stack) {
        return false;
    }

    @Override
    public ItemFocusBasic.WandFocusAnimation getAnimation(final ItemStack stack) {
        return WandFocusAnimation.WAVE;
    }

    @Override
    public ItemStack onFocusRightClick(
            final ItemStack stack, final World world, final EntityPlayer player, final MovingObjectPosition mop) {
        return stack;
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        super.addInformation(stack, player, list, b);
        list.add(" ");

        final DecimalFormat df = new DecimalFormat("0.#");
        final double dmg = ConfigHandler.MAGE_MACE_DMG_INC_BASE + getUpgradeLevel(stack, FocusUpgradeType.potency);

        list.add(EnumChatFormatting.BLUE + "+" + df.format(dmg) + " "
                + StatCollector.translateToLocal("text.attackdamageequipped"));
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(final ItemStack stack, final int rank) {
        switch (rank) {
            case 1:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 2:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 3:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 4:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
            case 5:
                return new FocusUpgradeType[] {FocusUpgradeType.frugal, FocusUpgradeType.potency};
        }
        return null;
    }
}
