package com.lalameow.guicommon.common;

import com.lalameow.guicommon.client.network.packet.GuiPacket;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public FMLEventChannel getGuiChannel() {
        return null;
    }

    public GuiPacket[] getGuiPackets() {
        return null;
    }

    public void setGuiPackets(GuiPacket[] guiPackets) {

    }
}
