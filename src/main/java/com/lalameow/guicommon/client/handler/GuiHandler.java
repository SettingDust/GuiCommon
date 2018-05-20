package com.lalameow.guicommon.client.handler;

import com.google.gson.Gson;
import com.lalameow.guicommon.GuiCommon;
import com.lalameow.guicommon.client.gui.IGuiChest;
import com.lalameow.guicommon.client.network.packet.GuiPacket;
import com.lalameow.guicommon.client.texture.TextureEntity;
import com.lalameow.guicommon.inventory.SlotEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Author: SettingDust.
 * Date: 2018/5/13.
 */
@SideOnly(Side.CLIENT)
public class GuiHandler {
    @SubscribeEvent
    public void onOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiChest) {
            GuiChest gui = (GuiChest) event.getGui();
            if (GuiCommon.proxy.getGuiPackets() != null && GuiCommon.proxy.getGuiPackets().size() > 0) {
                for (String title : GuiCommon.proxy.getGuiPackets().keySet()) {
                    try {
                        Field lowerChestInventory = gui.getClass().getDeclaredField("lowerChestInventory");
                        lowerChestInventory.setAccessible(true);
                        IInventory chestInventory = (IInventory) lowerChestInventory.get(gui);
                        Field upperChestInventory = gui.getClass().getDeclaredField("upperChestInventory");
                        upperChestInventory.setAccessible(true);
                        IInventory playerInventory = (IInventory) upperChestInventory.get(gui);
                        if (chestInventory.getDisplayName().getUnformattedText().equals(title)) {
                            GuiPacket guiPacket = new Gson().fromJson(new Gson().toJson(GuiCommon.proxy.getGuiPackets().get(title)), GuiPacket.class);
                            IGuiChest guiChest = new IGuiChest(playerInventory, chestInventory, guiPacket);
                            event.setGui(guiChest);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //直接打开RPG
        if (event.getGui() instanceof GuiInventory
                && !Minecraft.getMinecraft().playerController.isInCreativeMode()) {
            ByteBuf buf = Unpooled.wrappedBuffer("OpenRPGInventory".getBytes());
            FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(buf), GuiCommon.getGuiChannel()); // 数据包
            GuiCommon.proxy.getGuiChannel().sendToServer(packet);
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void onRender(GuiContainerEvent.DrawForeground event) {
        if (!Minecraft.getMinecraft().playerController.isInCreativeMode()) {
            GuiContainer gui = event.getGuiContainer();
            List<Slot> slots = gui.inventorySlots.inventorySlots;
            GuiPacket guiPacket = new Gson().fromJson(new Gson().toJson(GuiCommon.proxy.getGuiPackets().get("PlayerInventory")), GuiPacket.class);
            SlotEntity[] slotEntities = guiPacket.getSlotEntities();

            for (Slot slot : slots) {
                if (!(slot.inventory instanceof InventoryPlayer)) continue;
                for (SlotEntity slotEntity : slotEntities) {
                    if (slotEntity.getLocation().toSlot() == slot.getSlotIndex()) {
                        TextureEntity textureEntity = slotEntity.getTexture();
                        if (!textureEntity.isEmpty()) {
                            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("guicommon", "textures/gui/container/" + textureEntity.getPath()));
                            GlStateManager.enableBlend();
                            GlStateManager.enableAlpha();
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                            Gui.drawScaledCustomSizeModalRect(
                                    slot.xPos,
                                    slot.yPos,
                                    textureEntity.getU(),
                                    textureEntity.getV(),
                                    textureEntity.getWidth() == 0 ? 16 : textureEntity.getWidth(),
                                    textureEntity.getHeight() == 0 ? 16 : textureEntity.getHeight(),
                                    16, 16, 16, 16
                            );
                        }
                        break;
                    }
                }
            }
        }
    }
}
