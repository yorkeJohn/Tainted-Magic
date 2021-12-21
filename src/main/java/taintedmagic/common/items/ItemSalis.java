package taintedmagic.common.items;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import taintedmagic.common.TaintedMagic;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSparkle;

public class ItemSalis extends Item {

    private static final int SUBTYPES = 2;
    private final IIcon[] icons = new IIcon[SUBTYPES];

    public ItemSalis () {
        setCreativeTab(TaintedMagic.tabTM);
        hasSubtypes = true;
        setUnlocalizedName("ItemSalis");
    }

    @Override
    public EnumRarity getRarity (final ItemStack stack) {
        return TaintedMagic.rarityCreation;
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void registerIcons (final IIconRegister ir) {
        for (int i = 0; i < icons.length; i++) {
            icons[i] = ir.registerIcon("taintedmagic:ItemSalis" + i);
        }
    }

    @Override
    @SideOnly (Side.CLIENT)
    public IIcon getIconFromDamage (final int meta) {
        return icons[meta];
    }

    @Override
    @SideOnly (Side.CLIENT)
    public void getSubItems (final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < SUBTYPES; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName (final ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public boolean onEntityItemUpdate (final EntityItem entity) {
        super.onEntityItemUpdate(entity);
        final World world = entity.worldObj;
        final int meta = entity.getEntityItem().getItemDamage();

        if (entity.ticksExisted == 100) {
            switch (meta) {
            // Weather
            case 0 : {
                world.getWorldInfo().setRainTime(world.isRaining() ? 24000 : 0);
                world.getWorldInfo().setRaining(!world.isRaining());
                if (world.isRaining() && world.rand.nextInt(10) == 0) {
                    world.getWorldInfo().setThundering(true);
                }
                break;
            }
            // Time
            case 1 : {
                world.setWorldTime(world.isDaytime() ? 14000 : 24000);
                break;
            }
            }
            world.playSoundAtEntity(entity, "thaumcraft:ice", 0.3F, 1.0F + world.rand.nextFloat() * 0.25F);
            entity.setDead();
            if (world.isRemote) {
                for (int i = 0; i < 200; i++) {
                    spawnParticles(entity, meta, true);
                }
            }
            return false;
        }
        if (entity.ticksExisted % 30 == 1) {
            world.playSoundAtEntity(entity, "thaumcraft:wind", 0.1F, 1.0F + world.rand.nextFloat() * 0.5F);
        }
        if (world.isRemote) {
            spawnParticles(entity, meta, false);
        }
        return false;
    }

    @SideOnly (Side.CLIENT)
    private void spawnParticles (final EntityItem entity, final int meta, final boolean death) {
        final Random rand = entity.worldObj.rand;

        final double theta = Math.random() * Math.PI;
        final double phi = Math.random() * Math.PI * 2d;

        final double x = Math.cos(phi) * Math.sin(theta) * 0.25d;
        final double y = Math.sin(phi) * Math.sin(theta) * 0.25d;
        final double z = Math.cos(theta) * 0.25d;

        final FXSparkle fx = new FXSparkle(entity.worldObj, entity.posX + x, entity.boundingBox.maxY + y, entity.posZ + z,
                1.75f, meta == 0 ? 7 : 6, death ? 30 + rand.nextInt(5) : 3 + rand.nextInt(2));
        fx.setGravity(death ? 0f : -0.1f);
        fx.noClip = true;

        if (death) {
            fx.motionX = x * 3d;
            fx.motionY = y * 3d;
            fx.motionZ = z * 3d;
        }
        ParticleEngine.instance.addEffect(entity.worldObj, fx);
    }
}
