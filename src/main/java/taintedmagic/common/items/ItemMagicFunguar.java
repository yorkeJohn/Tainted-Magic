package taintedmagic.common.items;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketAspectPool;
import thaumcraft.common.lib.research.ResearchManager;

public class ItemMagicFunguar extends ItemFood
{
    Aspect aspects[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

    public ItemMagicFunguar (int healAmount, float saturation, boolean b)
    {
        super(healAmount, saturation, b);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemMagicFunguar");
        this.setAlwaysEdible();
        this.setUnlocalizedName("ItemMagicFunguar");
    }

    @Override
    protected void onFoodEaten (ItemStack stack, World world, EntityPlayer player)
    {
        super.onFoodEaten(stack, world, player);

        Random random = new Random();
        try
        {
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 2));
            player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 100, 2));
            player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 2));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        int i = random.nextInt(aspects.length);

        if (!world.isRemote)
        {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspects[i],
                    Short.valueOf((short) (1)));
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(
                    new PacketAspectPool(aspects[i].getTag(), Short.valueOf((short) 1), Short.valueOf(
                            Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(player.getCommandSenderName(), aspects[i]))),
                    (EntityPlayerMP) player);
        }
    }

    @SideOnly (Side.CLIENT)
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
