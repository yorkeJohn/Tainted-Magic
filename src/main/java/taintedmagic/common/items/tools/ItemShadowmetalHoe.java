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

public class ItemShadowmetalHoe extends ItemHoe implements IRepairable
{
    public ItemShadowmetalHoe (ToolMaterial material)
    {
        super(material);
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setTextureName("taintedmagic:ItemShadowmetalHoe");
        this.setUnlocalizedName("ItemShadowmetalHoe");

        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public boolean getIsRepairable (ItemStack stack, ItemStack repairItem)
    {
        return repairItem.isItemEqual(new ItemStack(ItemRegistry.ItemMaterial, 1, 0)) ? true
                : super.getIsRepairable(stack, repairItem);
    }

    @Override
    public boolean onItemUse (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int i, float f, float f1,
            float f2)
    {
        super.onItemUse(stack, player, world, x, y, z, i, f, f1, f2);

        if (!player.worldObj.isRemote)
        {
            Block dirt = Blocks.dirt;

            if (player.worldObj.getBlock(x, y, z) == Blocks.farmland)
            {
                player.worldObj.setBlock(x, y, z, Blocks.dirt);
                stack.damageItem(1, player);
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
                        dirt.stepSound.getStepResourcePath(), (dirt.stepSound.getVolume() + 1.0F) / 2.0F,
                        dirt.stepSound.getPitch() * 0.8F);
            }
            else if (player.worldObj.getBlock(x, y, z) == Blocks.dirt || player.worldObj.getBlock(x, y, z) == Blocks.grass)
            {
                player.worldObj.setBlock(x, y, z, Blocks.farmland);
                stack.damageItem(1, player);
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
                        dirt.stepSound.getStepResourcePath(), (dirt.stepSound.getVolume() + 1.0F) / 2.0F,
                        dirt.stepSound.getPitch() * 0.8F);
            }
        }
        if (player.worldObj.getBlock(x, y, z) == Blocks.dirt || player.worldObj.getBlock(x, y, z) == Blocks.grass
                || player.worldObj.getBlock(x, y, z) == Blocks.farmland)
            player.swingItem();
        return false;
    }

    @SubscribeEvent
    public void useHoe (UseHoeEvent event)
    {
        if (event.current.getItem() == ItemRegistry.ItemShadowmetalHoe) event.setCanceled(true);
    }

    @Override
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }
}
