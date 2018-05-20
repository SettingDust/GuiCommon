package com.lalameow.guicommon.client.gui;

import com.google.common.collect.Ordering;
import com.lalameow.guicommon.client.network.packet.GuiPacket;
import com.lalameow.guicommon.inventory.IContainerChest;
import com.lalameow.guicommon.inventory.ISlot;
import com.lalameow.guicommon.inventory.SlotEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * Author: SettingDust.
 * Date: 2018/5/13.
 */
@SideOnly(Side.CLIENT)
public class IGuiChest extends GuiContainer {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static ResourceLocation ICHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    /**
     * The chest's inventory. Number of slots will vary based off of the type of chest.
     */
    private final IInventory lowerChestInventory;
    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    private boolean[] enableSlots;
    private final int inventoryRows;

    /**
     * holds the slot currently hovered
     */
    private Slot hoveredSlot;
    /**
     * Used when touchscreen is enabled.
     */
    private Slot clickedSlot;
    /**
     * Used when touchscreen is enabled.
     */
    private boolean isRightMouseClick;
    /**
     * Used when touchscreen is enabled
     */
    private ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    /**
     * Used when touchscreen is enabled
     */
    private ItemStack returningStack = ItemStack.EMPTY;
    private GuiPacket guiPacket;
    private boolean[] canFire;
    private int dragSplittingLimit;
    private int dragSplittingRemnant;

    public IGuiChest(IInventory upperInv, IInventory lowerInv, GuiPacket guiPacket) {
        super(new IContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().player, guiPacket));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = 114 + this.inventoryRows * 18;
        this.guiPacket = guiPacket;
        if (!guiPacket.getTexture().isEmpty()) {
            ICHEST_GUI_TEXTURE = new ResourceLocation("guicommon", "textures/gui/container/" + guiPacket.getTexture().getPath());
        }
        enableSlots = new boolean[inventorySlots.inventorySlots.size()];
        for (SlotEntity slotEntity : guiPacket.getSlotEntities()) {
            enableSlots[slotEntity.getLocation().toSlot()] = true;
        }
        canFire = new boolean[lowerChestInventory.getSizeInventory()];
        for (SlotEntity slotEntity : guiPacket.getSlotEntities()) {
            if (slotEntity.getLocation().toSlot() < lowerChestInventory.getSizeInventory()
                    && slotEntity.isCanFire())
                canFire[slotEntity.getLocation().toSlot()] = true;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawContainer(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.drawActivePotionEffects();
    }

    private void drawContainer(int mouseX, int mouseY, float partialTicks) {
        int i = this.guiLeft;
        int j = this.guiTop;
        this.hoveredSlot = null;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        for (GuiButton button : this.buttonList) {
            button.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }

        for (GuiLabel label : this.labelList) {
            label.drawLabel(this.mc, mouseX, mouseY);
        }

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) i, (float) j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            ISlot slot = convertSlot(i1);
            if (slot.isEnabled()) {
                this.drawSlot(slot);
            }
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                this.hoveredSlot = slot;
                int j1 = slot.xPos;
                int k1 = slot.yPos;
                if (i1 >= lowerChestInventory.getSizeInventory() || canFire[i1]) {
                    GlStateManager.colorMask(true, true, true, false);
                    this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
                    GlStateManager.colorMask(true, true, true, true);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                }
            }
        }
        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;
        if (!itemstack.isEmpty()) {
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int) ((float) l2 * f);
            int i2 = this.touchUpY + (int) ((float) i3 * f);
            this.drawItemStack(this.returningStack, l1, i2, null);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    private ISlot convertSlot(int index) {
        ISlot iSlot;
        if (index < this.lowerChestInventory.getSizeInventory() && index > -1) {
            iSlot = new ISlot(this.inventorySlots.inventorySlots.get(index), enableSlots[index]);
        } else {
            iSlot = new ISlot(this.inventorySlots.inventorySlots.get(index), true);
        }
        return iSlot;
    }

    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     */
    private void drawSlot(ISlot slotIn) {
        int i = slotIn.xPos;
        int j = slotIn.yPos;
//        for (SlotEntity slotEntity : guiPacket.getSlotEntities()) {
//            if (slotEntity.getLocation().toSlot() == i1
//                    && !slotEntity.getTexture().isEmpty()) {
//                TextureEntity textureEntity = slotEntity.getTexture();
//                mc.getTextureManager().bindTexture(new ResourceLocation("guicommon", "textures/gui/container/" + textureEntity.getPath()));
//                GlStateManager.enableBlend();
//                GlStateManager.enableAlpha();
//                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//                drawScaledCustomSizeModalRect(i, j,
//                        textureEntity.getU(),
//                        textureEntity.getV(),
//                        textureEntity.getWidth() == 0 ? 16 : textureEntity.getWidth(),
//                        textureEntity.getHeight() == 0 ? 16 : textureEntity.getHeight(),
//                        16, 16, 16, 16
//                );
//            }
//        }
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled()) {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private void updateDragSplitting() {
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting) {
            if (this.dragSplittingLimit == 2) {
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            } else {
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots) {
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j) {
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRenderer.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRenderer.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(ICHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }

    /**
     * Returns whether the mouse is over the given slot.
     */
    private boolean isMouseOverSlot(ISlot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    /**
     * Draws an ItemStack.
     * <p>
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
            this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }

    /**
     * Display the potion effects list
     */
    private void drawActivePotionEffects() {
        int i = this.guiLeft - 124;
        int j = this.guiTop;
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();

        if (!collection.isEmpty()) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableLighting();
            int l = 33;

            if (collection.size() > 5) {
                l = 132 / (collection.size() - 1);
            }

            for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection)) {
                Potion potion = potioneffect.getPotion();
                if (!potion.shouldRender(potioneffect)) continue;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.getTextureManager().bindTexture(INVENTORY_BACKGROUND);
                this.drawTexturedModalRect(i, j, 0, 166, 140, 32);

                if (potion.hasStatusIcon()) {
                    int i1 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(
                            i + 6, j + 7,
                            i1 % 8 * 18,
                            198 + i1 / 8 * 18,
                            18, 18);
                }

                potion.renderInventoryEffect(i, j, potioneffect, mc);
                if (!potion.shouldRenderInvText(potioneffect)) {
                    j += l;
                    continue;
                }
                String s1 = I18n.format(potion.getName());

                if (potioneffect.getAmplifier() == 1) {
                    s1 = s1 + " " + I18n.format("enchantment.level.2");
                } else if (potioneffect.getAmplifier() == 2) {
                    s1 = s1 + " " + I18n.format("enchantment.level.3");
                } else if (potioneffect.getAmplifier() == 3) {
                    s1 = s1 + " " + I18n.format("enchantment.level.4");
                }

                this.fontRenderer.drawStringWithShadow(s1, (float) (i + 10 + 18), (float) (j + 6), 16777215);
                String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                this.fontRenderer.drawStringWithShadow(s, (float) (i + 10 + 18), (float) (j + 6 + 10), 8355711);
                j += l;
            }
        }
    }
}
