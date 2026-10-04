package taintedmagic.common.items.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Color;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import taintedmagic.api.IHeldItemHUD;
import taintedmagic.api.IRenderInventoryItem;
import taintedmagic.client.model.ModelKatana;
import taintedmagic.client.model.ModelSaya;
import taintedmagic.common.TaintedMagic;
import taintedmagic.common.helper.TaintedMagicHelper;
import taintedmagic.common.items.wand.foci.ItemFocusShockwave;
import taintedmagic.common.network.PacketHandler;
import taintedmagic.common.network.PacketKatanaAttack;
import thaumcraft.api.IRepairable;
import thaumcraft.api.IWarpingGear;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.vec.Vector3;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;

public class ItemKatana extends Item implements IWarpingGear, IRepairable, IRenderInventoryItem, IHeldItemHUD {

    private static final int SUBTYPES = 3;

    // NBT tags
    public static final String TAG_INSCRIPTION = "inscription";
    public static final String TAG_COOLDOWN = "cooldown";

    // ticks to fully charge (1 second)
    private static final int CHARGE_TICKS = 20;
    // inscription attack cooldown (7 seconds)
    private static final int COOLDOWN_TICKS = 140;

    private static final ResourceLocation TEXTURE_THAUMIUM =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaThaumium.png");
    private static final ResourceLocation TEXTURE_VOIDMETAL =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaVoidmetal.png");
    private static final ResourceLocation TEXTURE_SHADOWMETAL =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaShadowmetal.png");

    public static final ModelKatana KATANA = new ModelKatana();
    public static final ModelSaya SAYA = new ModelSaya();

    private int ticksInUse = 0;

    public ItemKatana() {
        setCreativeTab(TaintedMagic.tabTM);
        setUnlocalizedName("ItemKatana");
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean canHarvestBlock(final Block block, final ItemStack stack) {
        return false;
    }

    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.bow;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int a = 0; a < SUBTYPES; a++) {
            list.add(new ItemStack(this, 1, a));
        }
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List list, final boolean b) {
        if (stack.getItemDamage() == 1) {
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("enchantment.special.sapless"));
        }

        if (stack.getItemDamage() == 2) {
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("enchantment.special.sapgreat"));
        }

        list.add(" ");
        list.add("\u00A79+" + getAttackDamage(stack) + " " + StatCollector.translateToLocal("text.attackdamage"));

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(TAG_INSCRIPTION)) {
            list.add(EnumChatFormatting.GOLD
                    + StatCollector.translateToLocal(
                            "text.katana.inscription." + stack.stackTagCompound.getInteger(TAG_INSCRIPTION)));
        }
    }

    @Override
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.uncommon;
    }

    @Override
    public int getWarp(final ItemStack stack, final EntityPlayer player) {
        return stack.getItemDamage() == 0 ? 0 : stack.getItemDamage() == 1 ? 3 : 7;
    }

    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase entity, final EntityLivingBase player) {
        if (!entity.worldObj.isRemote
                && (!(entity instanceof EntityPlayer)
                        || !(player instanceof EntityPlayer)
                        || MinecraftServer.getServer().isPVPEnabled())) {
            // Sapping effects
            if (stack.getItemDamage() == 1) {
                entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 60)); // lesser sapping
            }
            if (stack.getItemDamage() == 2) {
                entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 60)); // greater sapping
                entity.addPotionEffect(new PotionEffect(Potion.hunger.id, 120));
            }

            // Inscription effects
            if (hasAnyInscription(stack)) {
                switch (getInscription(stack)) {
                    case 0: {
                        entity.attackEntityFrom(
                                DamageSource.causePlayerDamage((EntityPlayer) player)
                                        .setFireDamage(),
                                getAttackDamage(stack));
                        entity.setFire(3);
                        break;
                    }
                    case 1: {
                        entity.attackEntityFrom(
                                DamageSource.causePlayerDamage((EntityPlayer) player)
                                        .setMagicDamage(),
                                getAttackDamage(stack));
                        entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 140));
                        break;
                    }
                    case 2: {
                        entity.attackEntityFrom(
                                DamageSource.causePlayerDamage((EntityPlayer) player)
                                        .setMagicDamage(),
                                getAttackDamage(stack));
                        entity.addPotionEffect(new PotionEffect(Potion.wither.id, 60));
                        break;
                    }
                    default:
                        break;
                }
            } else {
                entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), getAttackDamage(stack));
            }
        }
        // Sound effect
        player.worldObj.playSoundAtEntity(
                player, "thaumcraft:swing", 0.5F + (float) Math.random(), 0.5F + (float) Math.random());

        return super.hitEntity(stack, entity, player);
    }

    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
        if (!hasCooldown(stack)) {
            player.setItemInUse(stack, getMaxItemUseDuration(stack));
        }
        return stack;
    }

    @Override
    public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int i) {
        super.onUsingTick(stack, player, i);

        ticksInUse = getMaxItemUseDuration(stack) - i;

        final float j = 0.75F + (float) Math.random() * 0.25F;
        if (player.ticksExisted % 5 == 0) {
            player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", j * 0.1F, j);
        }
    }

    @Override
    public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityPlayer player, final int i) {
        super.onPlayerStoppedUsing(stack, world, player, i);

        if (isFullyCharged(player)) {
            if (!hasAnyInscription(stack) || player.isSneaking()) {
                if (world.isRemote) {
                    final MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;

                    // charged strike damage multiplier
                    float mul = 1.5F;
                    if (world.rand.nextInt(10) == 0) {
                        mul += 1.0F; // crit
                    }

                    if (mop.entityHit != null) {
                        PacketHandler.INSTANCE.sendToServer(
                                new PacketKatanaAttack(mop.entityHit, player, getAttackDamage(stack) * mul));
                    }
                }
                player.worldObj.playSoundAtEntity(
                        player, "thaumcraft:swing", 0.5F + (float) Math.random(), 0.5F + (float) Math.random());
                player.swingItem();
            } else {
                switch (getInscription(stack)) {
                    case 0: // fireball
                    {
                        final EntityExplosiveOrb proj = new EntityExplosiveOrb(world, player);
                        proj.strength = getAttackDamage(stack) * .25F;
                        proj.posX += proj.motionX;
                        proj.posY += proj.motionY;
                        proj.posZ += proj.motionZ;

                        if (!world.isRemote) {
                            world.spawnEntityInWorld(proj);
                        }
                        player.swingItem();

                        break;
                    }
                    case 1: // thunder
                    {
                        // get entities within 10 block radius
                        final List<EntityLivingBase> ents = world.getEntitiesWithinAABB(
                                EntityLivingBase.class,
                                AxisAlignedBB.getBoundingBox(
                                                player.posX,
                                                player.posY,
                                                player.posZ,
                                                player.posX + 1,
                                                player.posY + 1,
                                                player.posZ + 1)
                                        .expand(10.0D, 10.0D, 10.0D));

                        if (ents != null && ents.size() > 0) {
                            for (final EntityLivingBase entity : ents) {
                                if (entity != player && entity.isEntityAlive() && !entity.isEntityInvulnerable()) {
                                    entity.attackEntityFrom(DamageSource.magic, getAttackDamage(stack) * 0.25F);

                                    final Vector3 movement =
                                            TaintedMagicHelper.getVectorBetweenEntities(entity, player);
                                    entity.addVelocity(movement.x * 5.0D, 1.5D, movement.z * 5.0D);

                                    if (world.isRemote) {
                                        ItemFocusShockwave.spawnParticles(world, player, entity);
                                    }
                                }
                            }
                        }

                        world.playSoundAtEntity(player, "taintedmagic:shockwave", 5.0F, 1.5F * (float) Math.random());
                        player.swingItem();

                        break;
                    }
                    case 2: // heal
                    {
                        // heal 50% of missing health
                        player.heal((player.getMaxHealth() - player.getHealth()) * 0.5F);

                        // get entities within 5 blocks
                        final List<EntityLivingBase> ents = world.getEntitiesWithinAABB(
                                EntityLivingBase.class,
                                AxisAlignedBB.getBoundingBox(
                                                player.posX,
                                                player.posY,
                                                player.posZ,
                                                player.posX + 1,
                                                player.posY + 1,
                                                player.posZ + 1)
                                        .expand(5.0D, 5.0D, 5.0D));

                        // add wither to all entities in range
                        if (ents != null && ents.size() > 0) {
                            for (final EntityLivingBase entity : ents)
                                if (entity != player && entity.isEntityAlive() && !entity.isEntityInvulnerable()) {
                                    entity.addPotionEffect(new PotionEffect(Potion.wither.id, 100, 1));
                                }
                        }

                        // beneficial effects on the player
                        player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 60, 1));
                        player.addPotionEffect(new PotionEffect(Potion.resistance.id, 120, 1));

                        // particles similar to pech focus
                        if (world.isRemote) {
                            for (int a = 0; a < 18; a++) {
                                Thaumcraft.proxy.wispFX2(
                                        world,
                                        player.posX + world.rand.nextGaussian() * 0.25F,
                                        player.boundingBox.minY + 1.0F + world.rand.nextGaussian() * 0.5F,
                                        player.posZ + world.rand.nextGaussian() * 0.25F,
                                        0.25F + (float) Math.random() * 0.25F,
                                        3,
                                        true,
                                        false,
                                        0.02F);

                                Thaumcraft.proxy.sparkle(
                                        (float) player.posX + (float) world.rand.nextGaussian() * 0.25F,
                                        (float) player.boundingBox.minY
                                                + 1.0F
                                                + (float) world.rand.nextGaussian() * 0.5F,
                                        (float) player.posZ + (float) world.rand.nextGaussian() * 0.25F,
                                        5);
                            }
                        }

                        world.playSoundAtEntity(player, "thaumcraft:wand", 1.0F, 0.9F + (float) Math.random() * 0.1F);
                        player.swingItem();

                        break;
                    }
                    default:
                        break;
                }
                setCooldown(stack, COOLDOWN_TICKS);
            }
        }
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int i, final boolean b) {
        // update the cooldown every tick
        if (hasCooldown(stack)) {
            setCooldown(stack, getCooldown(stack) - 1);
        }
    }

    private boolean isFullyCharged(final EntityPlayer player) {
        final float f = Math.min((float) ticksInUse / (float) CHARGE_TICKS, 1.0F);
        return f == 1.0F;
    }

    public static boolean hasAnyInscription(final ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.hasKey(TAG_INSCRIPTION);
    }

    private int getInscription(final ItemStack stack) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(TAG_INSCRIPTION))
            return stack.stackTagCompound.getInteger(TAG_INSCRIPTION);
        return 0;
    }

    private boolean hasCooldown(final ItemStack stack) {
        return stack.hasTagCompound() && stack.stackTagCompound.getInteger(TAG_COOLDOWN) > 0;
    }

    private void setCooldown(final ItemStack stack, final int cooldown) {
        if (!stack.hasTagCompound()) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        stack.stackTagCompound.setInteger(TAG_COOLDOWN, cooldown);
    }

    private int getCooldown(final ItemStack stack) {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey(TAG_COOLDOWN))
            return stack.stackTagCompound.getInteger(TAG_COOLDOWN);
        return 0;
    }

    private float getAttackDamage(final ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                return 14.25F;
            case 1:
                return 17.5F;
            case 2:
                return 20.75F;
        }
        return 0;
    }

    public static ResourceLocation getTexture(final ItemStack stack) {
        switch (stack.getItemDamage()) {
            case 0:
                return TEXTURE_THAUMIUM;
            case 1:
                return TEXTURE_VOIDMETAL;
            case 2:
                return TEXTURE_SHADOWMETAL;
        }
        return null;
    }

    @Override
    public void renderHUD(
            final ScaledResolution res,
            final EntityPlayer player,
            final ItemStack stack,
            final float partialTicks,
            final float fract) {
        final Tessellator t = Tessellator.instance;

        final float overlay = Math.min(
                hasCooldown(stack)
                        ? (float) getCooldown(stack) / (float) COOLDOWN_TICKS
                        : (float) player.getItemInUseDuration() / (float) CHARGE_TICKS,
                1.0F);

        final float scale = 0.315F;

        final double x = res.getScaledWidth() / 2d / scale + 30d;
        final double x2 = x + 16;
        final double y = (double) res.getScaledHeight() / scale - (player.capabilities.isCreativeMode ? 100d : 150d);
        final double y2 = y + 16;

        GL11.glPushMatrix();

        GL11.glScalef(scale, scale, scale);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        UtilsFX.bindTexture(new ResourceLocation("thaumcraft:textures/misc/script.png"));

        for (int rune = 0; rune < 16; rune++) {
            float red = MathHelper.sin((player.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.8F;
            float green = MathHelper.sin((player.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.7F;
            float blue = 0.4F;
            final float alpha = MathHelper.sin((player.ticksExisted + rune * 5) / 10.0F) * 0.3F;

            final float f = 0.0625F * rune;
            final float f1 = f + 0.0625F;
            final float f2 = 0.0F;
            final float f3 = 1.0F;

            t.startDrawingQuads();

            t.setBrightness(240);
            t.setColorRGBA_F(red, green, blue, (alpha + 0.7F) * fract);
            t.addVertexWithUV(x + rune * 16 - alpha, y2 + alpha, 0, f, f3);
            t.addVertexWithUV(x2 + rune * 16 + alpha, y2 + alpha, 0, f1, f3);
            t.addVertexWithUV(x2 + rune * 16 + alpha, y - alpha, 0, f1, f2);
            t.addVertexWithUV(x + rune * 16 - alpha, y - alpha, 0, f, f2);

            t.draw();

            if (Math.ceil(16 * overlay) > rune) {
                // red to green gradient
                Color color = new Color(Color.HSBtoRGB(rune / 16F * 0.3F, 1.0F, 1.0F));
                // flip the gradient when cooling down
                if (hasCooldown(stack)) {
                    color = new Color(Color.HSBtoRGB((16F - rune) / 16F * 0.3F, 1.0F, 1.0F));
                }
                // convert to float rgb
                red = color.getRed() / 255F;
                green = color.getGreen() / 255F;
                blue = color.getBlue() / 255F;

                t.startDrawingQuads();

                t.setBrightness(240);
                t.setColorRGBA_F(red, green, blue, 0.9F + alpha);
                t.addVertexWithUV(x + rune * 16 - alpha, y2 + alpha, 0, f, f3);
                t.addVertexWithUV(x2 + rune * 16 + alpha, y2 + alpha, 0, f1, f3);
                t.addVertexWithUV(x2 + rune * 16 + alpha, y - alpha, 0, f1, f2);
                t.addVertexWithUV(x + rune * 16 - alpha, y - alpha, 0, f, f2);

                t.draw();
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    // render katana on hip
    @Override
    public void render(final EntityPlayer player, final ItemStack stack, final float partialTicks) {
        GL11.glPushMatrix();

        final int light = player.getBrightnessForRender(0);
        final int lightmapX = light % 65536;
        final int lightmapY = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPushMatrix();

        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(55, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

        GL11.glTranslatef(-0.6F, 2.25F, 1.25F);

        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(stack));
        SAYA.render(0.0625F);

        GL11.glPopMatrix();

        if (player.getHeldItem() == null || player.getHeldItem() != stack) {
            GL11.glPushMatrix();

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glRotatef(55, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);

            GL11.glTranslatef(-0.6F, 2.25F, 1.25F);

            Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(stack));
            KATANA.render(0.0625F);

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }
}
