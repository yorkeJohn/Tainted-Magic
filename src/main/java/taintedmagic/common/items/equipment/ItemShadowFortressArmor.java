package taintedmagic.common.items.equipment;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.ISpecialArmor;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IVisDiscountGear;
import thaumcraft.api.IWarpingGear;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.armor.ItemFortressArmor;

public class ItemShadowFortressArmor extends ItemFortressArmor implements IWarpingGear, IVisDiscountGear, ISpecialArmor
{
    public ItemShadowFortressArmor (ArmorMaterial material, int j, int k)
    {
        super(material, j, k);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
    }

    @SideOnly (Side.CLIENT)
    public void registerIcons (IIconRegister ir)
    {
        this.iconHelm = ir.registerIcon("taintedmagic:ItemShadowFortressHelmet");
        this.iconChest = ir.registerIcon("taintedmagic:ItemShadowFortressChestplate");
        this.iconLegs = ir.registerIcon("taintedmagic:ItemShadowFortressLeggings");
    }

    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("tc.visdiscount") + ": "
                + getVisDiscount(stack, player, null) + "%");
        super.addInformation(stack, player, list, b);
    }

    public String getArmorTexture (ItemStack stack, Entity entity, int slot, String type)
    {
        return "taintedmagic:textures/models/ModelShadowFortressArmor.png";
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.epic;
    }

    public boolean getIsRepairable (ItemStack stack, ItemStack repairItem)
    {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public int getVisDiscount (ItemStack stack, EntityPlayer player, Aspect aspect)
    {
        return 5;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 5;
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties (EntityLivingBase entity, ItemStack stack, DamageSource source,
            double dmg, int slot)
    {
        int priority = 0;
        double ratio = this.damageReduceAmount / 25.0D;

        if (source.isMagicDamage())
        {
            priority = 1;
            ratio = this.damageReduceAmount / 35.0D;
        }
        else if (source.isFireDamage() || source.isExplosion())
        {
            priority = 1;
            ratio = this.damageReduceAmount / 20.0D;
        }
        else if (source.isUnblockable())
        {
            priority = 0;
            ratio = 0.0D;
        }

        if (entity instanceof EntityPlayer)
        {
            double set = 0.875D;
            for (int a = 1; a < 4; a++)
            {
                ItemStack piece = ((EntityPlayer) entity).inventory.armorInventory[a];
                if (piece != null && piece.getItem() instanceof ItemFortressArmor)
                {
                    set += 0.250D;
                    if (piece.hasTagCompound() && piece.stackTagCompound.hasKey("mask")) set += 0.05D;
                }
            }
            ratio *= set;
        }

        return new ISpecialArmor.ArmorProperties(priority, ratio, stack.getMaxDamage() + 1 - stack.getItemDamage());
    }

    public int getArmorDisplay (EntityPlayer player, ItemStack stack, int slot)
    {
        return this.damageReduceAmount;
    }

    public void damageArmor (EntityLivingBase entity, ItemStack stack, DamageSource source, int dmg, int slot)
    {
        if (source != DamageSource.fall) stack.damageItem(dmg, entity);
    }
}