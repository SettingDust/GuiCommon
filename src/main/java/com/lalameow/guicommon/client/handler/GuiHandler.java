package com.lalameow.guicommon.client.handler;

import com.lalameow.guicommon.GuiCommon;
import com.lalameow.guicommon.client.gui.IGuiChest;
import com.lalameow.guicommon.client.network.packet.GuiPacket;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
            if (GuiCommon.proxy.getGuiPackets() != null && GuiCommon.proxy.getGuiPackets().length > 0) {
                for (GuiPacket guiPacket : GuiCommon.proxy.getGuiPackets()) {
                    try {
                        Field lowerChestInventory = gui.getClass().getDeclaredField("lowerChestInventory");
                        lowerChestInventory.setAccessible(true);
                        IInventory lowernventory = (IInventory) lowerChestInventory.get(gui);
                        Field upperChestInventory = gui.getClass().getDeclaredField("upperChestInventory");
                        upperChestInventory.setAccessible(true);
                        IInventory upperinventory = (IInventory) upperChestInventory.get(gui);
                        if (lowernventory.getDisplayName().getUnformattedText().equals(guiPacket.getTitle())) {
                            IGuiChest guiChest = new IGuiChest(upperinventory, lowernventory, guiPacket.getEnableIndex());
                            event.setGui(guiChest);
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
