package taintedmagic.common.items.tools;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;

public class ItemPrimordialEdge extends ItemSword implements IWarpingGear, IRepairable
{
    public ItemPrimordialEdge (ToolMaterial material)
    {
        super(material);
        this.setTextureName("taintedmagic:ItemPrimordialEdge");
        this.setUnlocalizedName("ItemPrimordialEdge");
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
    }

    public EnumAction getItemUseAction (ItemStack stack)
    {
        return EnumAction.block;
    }

    public EnumRarity getRarity (ItemStack stack)
    {
        return TaintedMagic.rarityCreation;
    }

    public boolean hitEntity (ItemStack stack, EntityLivingBase entity, EntityLivingBase player)
    {
        super.hitEntity(stack, entity, player);

        try
        {
            entity.addPotionEffect(new PotionEffect(Potion.wither.id, 5, 1));
            entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 5, 1));
        }
        catch (Exception e)
        {
        }

        player.worldObj.playSoundAtEntity(player, "thaumcraft:swing", 1.0F, 1.0F);

        return true;
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onUsingTick (ItemStack stack, EntityPlayer player, int i)
    {
        // Based off of hungry node
        Iterator i$;

        List ents = player.worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB
                .getBoundingBox(player.posX, player.posY, player.posZ, player.posX + 1, player.posY + 1, player.posZ + 1)
                .expand(15.0D, 15.0D, 15.0D));

        player.worldObj.playSoundAtEntity(player, "thaumcraft:brain", 0.05F, 0.5F);

        if (ents != null && ents.size() > 0) for (i$ = ents.iterator(); i$.hasNext();)
        {
            Object next = i$.next();
            Entity entity = (Entity) next;

            if (entity != player)
            {
                if (entity.isEntityAlive() && !entity.isEntityInvulnerable())
                {
                    double dist = TaintedMagicHelper.getDistanceTo(player, entity.posX, entity.posY, entity.posZ);
                    if (dist < 2.0D) entity.attackEntityFrom(DamageSource.magic, 5.0F);
                }

                double x = (player.posX + 0.5D - entity.posX) / 20.0D;
                double y = (player.posY + 0.5D - entity.posY) / 20.0D;
                double z = (player.posZ + 0.5D - entity.posZ) / 20.0D;
                double vec = Math.sqrt(x * x + y * y + z * z);
                double vec2 = 1.0D - vec;

                if (vec2 > 0.0D)
                {
                    vec2 *= vec2;
                    entity.motionX += x / vec * vec2 * 0.20D;
                    entity.motionY += y / vec * vec2 * 0.30D;
                    entity.motionZ += z / vec * vec2 * 0.20D;
                }
            }
        }
    }

    @Override
    public void onUpdate (ItemStack stack, World world, Entity entity, int i, boolean b)
    {
        if (!world.isRemote && stack.isItemDamaged() && entity.ticksExisted % 20 == 0)
            stack.damageItem(-1, (EntityLivingBase) entity);
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("text.eldritchsapping"));
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return 5;
    }
}
