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
	Aspect[] aspects = new Aspect[]{
			Aspect.AIR,
			Aspect.EARTH,
			Aspect.FIRE,
			Aspect.WATER,
			Aspect.ORDER,
			Aspect.ENTROPY };

	public ItemMagicFunguar (int i, float j, boolean k)
	{
		super(i, j, k);
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setTextureName("taintedmagic:ItemMagicFunguar");
		this.setAlwaysEdible();
		this.setUnlocalizedName("ItemMagicFunguar");
	}

	@Override
	protected void onFoodEaten (ItemStack s, World w, EntityPlayer p)
	{
		super.onFoodEaten(s, w, p);

		Random r = new Random();
		try
		{
			p.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 2));
			p.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 100, 2));
			p.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 100, 2));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		int i = r.nextInt(aspects.length);

		if (!w.isRemote)
		{
			Thaumcraft.proxy.playerKnowledge.addAspectPool(p.getCommandSenderName(), aspects[i], Short.valueOf((short) (1)));
			ResearchManager.scheduleSave(p);
			PacketHandler.INSTANCE.sendTo(new PacketAspectPool(aspects[i].getTag(), Short.valueOf((short) 1), Short.valueOf(Thaumcraft.proxy.playerKnowledge.getAspectPoolFor(p.getCommandSenderName(), aspects[i]))), (EntityPlayerMP) p);
		}
	}

	@SideOnly (Side.CLIENT)
	public EnumRarity getRarity (ItemStack itemstack)
	{
		return EnumRarity.uncommon;
	}
}
