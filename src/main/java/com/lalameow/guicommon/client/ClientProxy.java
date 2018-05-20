package com.lalameow.guicommon.client;

import com.lalameow.guicommon.GuiCommon;
import com.lalameow.guicommon.client.handler.GuiHandler;
import com.lalameow.guicommon.client.handler.GuiPacketHandler;
import com.lalameow.guicommon.common.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    private FMLEventChannel guiChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(GuiCommon.getGuiChannel());

    private HashMap<String, String> guiPackets;

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        GuiPacketHandler packetHandler = new GuiPacketHandler();
        guiChannel.register(packetHandler);
        MinecraftForge.EVENT_BUS.register(new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public FMLEventChannel getGuiChannel() {
        return guiChannel;
    }

    public HashMap<String, String> getGuiPackets() {
        return guiPackets;
    }

    public void setGuiPackets(HashMap<String, String> guiPackets) {
        this.guiPackets = guiPackets;
    }
}
