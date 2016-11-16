package taintedmagic.common.items.wand.foci;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.entities.EntityHomingShard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.utils.EntityUtils;

/**
 * this class is based off of ItemFocusShard.class created by <Azanor> as part
 * of Thaumcraft 5
 */
public class ItemFocusVisShard extends ItemFocusBasic
{
	private static final AspectList cost = new AspectList().add(Aspect.FIRE, 1).add(Aspect.ENTROPY, 1).add(Aspect.AIR, 1);
	private static final AspectList costPersistant = new AspectList().add(Aspect.FIRE, 1).add(Aspect.ENTROPY, 1).add(Aspect.WATER, 1).add(Aspect.AIR, 1);

	public static FocusUpgradeType persistant = new FocusUpgradeType(69, new ResourceLocation("taintedmagic:textures/foci/IconPersistant.png"), "focus.upgrade.persistant.name", "focus.upgrade.persistant.text", new AspectList().add(Aspect.ARMOR, 1).add(Aspect.MOTION, 1).add(Aspect.ENERGY, 1));

	public ItemFocusVisShard ()
	{
		this.setCreativeTab(TaintedMagic.tabTaintedMagic);
		this.setUnlocalizedName("ItemFocusVisShard");
	}

	@SideOnly (Side.CLIENT)
	public void registerIcons (IIconRegister ir)
	{
		this.icon = ir.registerIcon("taintedmagic:ItemFocusVisShard");
	}

	public String getSortingHelper (ItemStack s)
	{
		return "SHARD" + super.getSortingHelper(s);
	}

	public int getFocusColor (ItemStack s)
	{
		return 10037693;
	}

	public int getActivationCooldown (ItemStack s)
	{
		return 300;
	}

	public ItemFocusBasic.WandFocusAnimation getAnimation (ItemStack s)
	{
		return ItemFocusBasic.WandFocusAnimation.WAVE;
	}

	public ItemStack onFocusRightClick (ItemStack s, World w, EntityPlayer p, MovingObjectPosition mop)
	{
		ItemWandCasting wand = (ItemWandCasting) s.getItem();

		Entity look = EntityUtils.getPointedEntity(p.worldObj, p, 0.0D, 32.0D, 1.1F);
		if (look != null && look instanceof EntityLivingBase)
		{
			if (!w.isRemote)
			{
				EntityHomingShard blast = new EntityHomingShard(w, p, (EntityLivingBase) look, wand.getFocusPotency(s), isUpgradedWith(wand.getFocusItem(s), persistant));
				w.spawnEntityInWorld(blast);
				w.playSoundAtEntity(blast, "taintedmagic:shard", 0.3F, 1.1F + w.rand.nextFloat() * 0.1F);
			}
			p.swingItem();
		}
		return s;
	}

	public AspectList getVisCost (ItemStack s)
	{
		return isUpgradedWith(s, persistant) ? costPersistant : cost;
	}

	public FocusUpgradeType[] getPossibleUpgradesByRank (ItemStack s, int r)
	{
		switch (r)
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
					persistant };
		}
		return null;
	}
}
