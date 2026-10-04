package taintedmagic.common.items.tools;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.registry.ItemRegistry;
import thaumcraft.api.IRepairable;

public class ItemShadowmetalHoe extends ItemHoe implements IRepairable {

    public ItemShadowmetalHoe(final ToolMaterial material) {
        super(material);
        setCreativeTab(TaintedMagic.tabTM);
        setTextureName("taintedmagic:ItemShadowmetalHoe");
        setUnlocalizedName("ItemShadowmetalHoe");

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public boolean getIsRepairable(final ItemStack stack, final ItemStack repairItem) {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0))
                ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public boolean onItemUse(
            final ItemStack stack,
            final EntityPlayer player,
            final World world,
            final int x,
            final int y,
            final int z,
            final int i,
            final float f,
            final float f1,
            final float f2) {
        super.onItemUse(stack, player, world, x, y, z, i, f, f1, f2);

        if (!player.worldObj.isRemote) {
            final Block dirt = Blocks.dirt;

            if (player.worldObj.getBlock(x, y, z) == Blocks.farmland) {
                player.worldObj.setBlock(x, y, z, Blocks.dirt);
                stack.damageItem(1, player);
                world.playSoundEffect(
                        x + 0.5F,
                        y + 0.5F,
                        z + 0.5F,
                        dirt.stepSound.getStepResourcePath(),
                        (dirt.stepSound.getVolume() + 1.0F) / 2.0F,
                        dirt.stepSound.getPitch() * 0.8F);
            } else if (player.worldObj.getBlock(x, y, z) == Blocks.dirt
                    || player.worldObj.getBlock(x, y, z) == Blocks.grass) {
                player.worldObj.setBlock(x, y, z, Blocks.farmland);
                stack.damageItem(1, player);
                world.playSoundEffect(
                        x + 0.5F,
                        y + 0.5F,
                        z + 0.5F,
                        dirt.stepSound.getStepResourcePath(),
                        (dirt.stepSound.getVolume() + 1.0F) / 2.0F,
                        dirt.stepSound.getPitch() * 0.8F);
            }
        }
        if (player.worldObj.getBlock(x, y, z) == Blocks.dirt
                || player.worldObj.getBlock(x, y, z) == Blocks.grass
                || player.worldObj.getBlock(x, y, z) == Blocks.farmland) {
            player.swingItem();
        }
        return false;
    }

    @SubscribeEvent
    public void useHoe(final UseHoeEvent event) {
        if (event.current.getItem() == ItemRegistry.ItemShadowmetalHoe) {
            event.setCanceled(true);
        }
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }
}
