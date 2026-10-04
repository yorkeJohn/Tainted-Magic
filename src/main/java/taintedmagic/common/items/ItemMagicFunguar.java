package taintedmagic.common.items;

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

public class ItemMagicFunguar extends ItemFood {

    public ItemMagicFunguar(final int healAmount, final float saturation, final boolean b) {
        super(healAmount, saturation, b);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemMagicFunguar");
        setAlwaysEdible();
        setUnlocalizedName("ItemMagicFunguar");
    }

    @Override
    protected void onFoodEaten(final ItemStack stack, final World world, final EntityPlayer player) {
        super.onFoodEaten(stack, world, player);

        // Potion effects
        player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 60, 1));
        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 2));

        final Aspect[] aspects = Aspect.getPrimalAspects().toArray(new Aspect[0]);
        final int i = world.rand.nextInt(aspects.length);

        if (!world.isRemote) {
            Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspects[i], (short) 1);
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(
                    new PacketAspectPool(
                            aspects[i].getTag(),
                            (short) 1,
                            Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(
                                    player.getCommandSenderName(), aspects[i])),
                    (EntityPlayerMP) player);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
