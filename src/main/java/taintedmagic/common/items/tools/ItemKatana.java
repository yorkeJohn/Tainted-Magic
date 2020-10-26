package taintedmagic.common.items.tools;

import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.projectile.EntityExplosiveOrb;

public class ItemKatana extends Item implements IWarpingGear, IRepairable, IRenderInventoryItem, IHeldItemHUD
{
    public static final int SUBTYPES = 3;

    public static final ResourceLocation TEXTURE_THAUMIUM =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaThaumium.png");
    public static final ResourceLocation TEXTURE_VOIDMETAL =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaVoidmetal.png");
    public static final ResourceLocation TEXTURE_SHADOWMETAL =
            new ResourceLocation("taintedmagic:textures/models/ModelKatanaShadowmetal.png");

    public static final ModelKatana KATANA = new ModelKatana();
    public static final ModelSaya SAYA = new ModelSaya();

    int ticksInUse = 0;

    public ItemKatana ()
    {
        this.setCreativeTab(TaintedMagic.tabTaintedMagic);
        this.setUnlocalizedName("ItemKatana");
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hitEntity (ItemStack stack, EntityLivingBase entity, EntityLivingBase player)
    {
        if (!entity.worldObj.isRemote && (! (entity instanceof EntityPlayer) || ! (player instanceof EntityPlayer)
                || MinecraftServer.getServer().isPVPEnabled()))
        {
            try
            {
                if (stack.getItemDamage() == 1)
                {
                    entity.addPotionEffect(new PotionEffect(Potion.weakness.getId(), 60));
                    entity.addPotionEffect(new PotionEffect(Potion.hunger.getId(), 120));
                }
                if (stack.getItemDamage() == 2)
                {
                    entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 120, 2));
                    entity.addPotionEffect(new PotionEffect(Config.potionBlurredID, 120));
                }
            }
            catch (Exception e)
            {
            }

            if (hasAnyInscription(stack))
            {
                switch (getInscription(stack))
                {
                case 0 :
                {
                    entity.attackEntityFrom(
                            DamageSource.causePlayerDamage((EntityPlayer) player).setFireDamage().setDamageBypassesArmor(),
                            getAttackDamage(stack));
                    entity.setFire(5);
                    break;
                }
                case 1 :
                {
                    entity.attackEntityFrom(
                            DamageSource.causePlayerDamage((EntityPlayer) player).setMagicDamage().setDamageBypassesArmor(),
                            getAttackDamage(stack));

                    try
                    {
                        entity.addPotionEffect(new PotionEffect(Potion.weakness.id, 160));
                    }
                    catch (Exception e)
                    {
                    }
                    break;
                }
                case 2 :
                {
                    entity.attackEntityFrom(DamageSource.wither.setMagicDamage().setDamageBypassesArmor(),
                            getAttackDamage(stack));

                    try
                    {
                        entity.addPotionEffect(new PotionEffect(Potion.wither.id, 60));
                    }
                    catch (Exception ex)
                    {
                    }
                    break;
                }
                }
            }
            else entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) player), getAttackDamage(stack));
        }
        player.worldObj.playSoundAtEntity(player, "thaumcraft:swing", 0.5F + (float) Math.random(),
                0.5F + (float) Math.random());
        return super.hitEntity(stack, entity, player);
    }

    @Override
    public boolean isFull3D ()
    {
        return true;
    }

    @Override
    public boolean canHarvestBlock (Block block, ItemStack stack)
    {
        return false;
    }

    @Override
    public EnumAction getItemUseAction (ItemStack stack)
    {
        return EnumAction.bow;
    }

    @SideOnly (Side.CLIENT)
    public void getSubItems (Item item, CreativeTabs tab, List list)
    {
        for (int a = 0; a < SUBTYPES; a++)
            list.add(new ItemStack(this, 1, a));
    }

    public String getUnlocalizedName (ItemStack stack)
    {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        if (stack.getItemDamage() == 1)
            list.add(EnumChatFormatting.GOLD + StatCollector.translateToLocal("enchantment.special.sapgreat"));
        list.add(" ");
        list.add("\u00A79+" + getAttackDamage(stack) + " " + StatCollector.translateToLocal("text.attackdamage"));

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("inscription"))
            list.add(EnumChatFormatting.GOLD + StatCollector
                    .translateToLocal("text.katana.inscription." + stack.stackTagCompound.getInteger("inscription")));
    }

    @Override
    public EnumRarity getRarity (ItemStack stack)
    {
        return EnumRarity.uncommon;
    }

    public float getAttackDamage (ItemStack stack)
    {
        switch (stack.getItemDamage())
        {
        case 0 :
            return 14.25F;
        case 1 :
            return 17.5F;
        case 2 :
            return 20.75F;
        }
        return 0;
    }

    @Override
    public int getMaxItemUseDuration (ItemStack stack)
    {
        return 72000;
    }

    @Override
    public ItemStack onItemRightClick (ItemStack stack, World world, EntityPlayer player)
    {
        player.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public void onUsingTick (ItemStack stack, EntityPlayer player, int i)
    {
        super.onUsingTick(stack, player, i);

        this.ticksInUse = getMaxItemUseDuration(stack) - i;

        float j = 1.0F + ((float) Math.random() * 0.25F);
        if (player.ticksExisted % 5 == 0) player.worldObj.playSoundAtEntity(player, "thaumcraft:wind", j * 0.1F, j);
    }

    @Override
    public void onPlayerStoppedUsing (ItemStack stack, World world, EntityPlayer player, int i)
    {
        super.onPlayerStoppedUsing(stack, world, player, i);

        Random random = new Random();

        if (isFullyCharged(player) && (!hasAnyInscription(stack) || player.isSneaking() || getInscription(stack) == 2))
        {
            boolean leech = (getInscription(stack) == 2 && isFullyCharged(player));

            if (world.isRemote)
            {
                MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
                float mul = 2.0F;

                if (mop.entityHit != null) PacketHandler.INSTANCE
                        .sendToServer(new PacketKatanaAttack(mop.entityHit, player, this.getAttackDamage(stack) * mul, leech));
                player.swingItem();
            }
            player.worldObj.playSoundAtEntity(player, "thaumcraft:swing", 0.5F + (float) Math.random(),
                    0.5F + (float) Math.random());
        }
        else if (hasAnyInscription(stack) && isFullyCharged(player) && !player.isSneaking())
        {
            switch (getInscription(stack))
            {
            case 0 :
            {
                EntityExplosiveOrb proj = new EntityExplosiveOrb(world, player);
                proj.strength = getAttackDamage(stack) * .25F;
                proj.posX += proj.motionX;
                proj.posY += proj.motionY;
                proj.posZ += proj.motionZ;

                if (!world.isRemote) world.spawnEntityInWorld(proj);
                player.swingItem();

                break;
            }
            case 1 :
            {
                List<EntityLivingBase> ents = world.getEntitiesWithinAABB(EntityLivingBase.class,
                        AxisAlignedBB.getBoundingBox(player.posX, player.posY, player.posZ, player.posX + 1, player.posY + 1,
                                player.posZ + 1).expand(10.0D, 10.0D, 10.0D));
                if (ents != null && ents.size() > 0)
                {
                    for (int a = 0; a < ents.size(); a++)
                    {
                        EntityLivingBase entity = ents.get(a);

                        if (entity != player && entity.isEntityAlive() && !entity.isEntityInvulnerable())
                        {
                            entity.attackEntityFrom(DamageSource.magic, getAttackDamage(stack) * 0.25F);

                            Vector3 movement = TaintedMagicHelper.getVectorBetweenEntities(entity, player);
                            entity.addVelocity(movement.x * 5.0D, 1.5D, movement.z * 5.0D);

                            if (world.isRemote) ItemFocusShockwave.spawnParticles(world, player, entity);
                        }
                    }
                }
                world.playSoundAtEntity(player, "taintedmagic:shockwave", 5.0F, 1.5F * (float) Math.random());
                player.swingItem();
                break;
            }
            default :
                break;
            }
        }
    }

    private boolean isFullyCharged (EntityPlayer player)
    {
        float f = Math.min((float) this.ticksInUse / 10.0F, 1.0F);
        if (f == 1.0F) return true;
        else return false;
    }

    @Override
    public void renderHUD (ScaledResolution res, EntityPlayer player, ItemStack stack, float partialTicks, float fract)
    {
        Tessellator t = Tessellator.instance;

        float tickFract = Math.min((float) player.getItemInUseDuration() / 10.0F, 1.0F);

        int x = res.getScaledWidth() / 2 + 725;
        int x2 = x + 16;
        int y = res.getScaledHeight() / 2 + (player.capabilities.isCreativeMode ? 805 : 755);
        int y2 = y + 16;

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        float scale = 0.315F;
        GL11.glScalef(scale, scale, scale);

        UtilsFX.bindTexture(new ResourceLocation("thaumcraft:textures/misc/script.png"));

        for (int rune = 0; rune < 16; rune++)
        {
            float red = MathHelper.sin( (player.ticksExisted + rune * 5) / 5.0F) * 0.1F + 0.8F;
            float green = MathHelper.sin( (player.ticksExisted + rune * 5) / 7.0F) * 0.1F + 0.7F;
            float alpha = MathHelper.sin( (player.ticksExisted + rune * 5) / 10.0F) * 0.3F;

            float f = 0.0625F * rune;
            float f1 = f + 0.0625F;
            float f2 = 0.0F;
            float f3 = 1.0F;

            t.startDrawingQuads();

            t.setBrightness(240);
            t.setColorRGBA_F(red, green, 0.4F, (alpha + 0.7F) * fract);
            t.addVertexWithUV(x + (rune * 16) - alpha, y2 + alpha, 0, f, f3);
            t.addVertexWithUV(x2 + (rune * 16) + alpha, y2 + alpha, 0, f1, f3);
            t.addVertexWithUV(x2 + (rune * 16) + alpha, y - alpha, 0, f1, f2);
            t.addVertexWithUV(x + (rune * 16) - alpha, y - alpha, 0, f, f2);

            t.draw();

            if ((int) (16 * tickFract) > rune)
            {
                t.startDrawingQuads();

                t.setBrightness(240);
                t.setColorRGBA_F(1.0F - (0.0625F * rune), 0.0F + (0.0625F * rune), 0.0F, 0.9F + alpha);
                t.addVertexWithUV(x + (rune * 16) - alpha, y2 + alpha, 0, f, f3);
                t.addVertexWithUV(x2 + (rune * 16) + alpha, y2 + alpha, 0, f1, f3);
                t.addVertexWithUV(x2 + (rune * 16) + alpha, y - alpha, 0, f1, f2);
                t.addVertexWithUV(x + (rune * 16) - alpha, y - alpha, 0, f, f2);

                t.draw();
            }
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public static boolean hasAnyInscription (ItemStack stack)
    {
        return stack.hasTagCompound() && stack.stackTagCompound.hasKey("inscription");
    }

    public static int getInscription (ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("inscription"))
            return stack.stackTagCompound.getInteger("inscription");
        else return 0;
    }

    public static ResourceLocation getTexture (ItemStack stack)
    {
        switch (stack.getItemDamage())
        {
        case 0 :
            return TEXTURE_THAUMIUM;
        case 1 :
            return TEXTURE_VOIDMETAL;
        case 2 :
            return TEXTURE_SHADOWMETAL;
        }
        return null;
    }

    @Override
    public int getWarp (ItemStack stack, EntityPlayer player)
    {
        return stack.getItemDamage() == 0 ? 0 : stack.getItemDamage() == 1 ? 3 : 7;
    }

    @Override
    public void render (EntityPlayer player, ItemStack stack, float partialTicks)
    {
        GL11.glPushMatrix();

        int light = player.getBrightnessForRender(0);
        int lightmapX = light % 65536;
        int lightmapY = light / 65536;
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

        if (player.getHeldItem() == null || player.getHeldItem() != stack)
        {
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
