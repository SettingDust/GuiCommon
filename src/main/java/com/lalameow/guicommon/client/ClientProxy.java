package com.lalameow.guicommon.client;

import com.lalameow.guicommon.GuiCommon;
import com.lalameow.guicommon.client.handler.GuiHandler;
import com.lalameow.guicommon.client.handler.GuiPacketHandler;
import com.lalameow.guicommon.client.network.packet.GuiPacket;
import com.lalameow.guicommon.common.CommonProxy;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    private FMLEventChannel guiChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(GuiCommon.getGuiChannel());

    private GuiPacket[] guiPackets;

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        GuiPacketHandler packetHandler=new GuiPacketHandler();
        guiChannel.register(packetHandler);
        MinecraftForge.EVENT_BUS.register(packetHandler);
        MinecraftForge.EVENT_BUS.register(new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public FMLEventChannel getGuiChannel() {
        return guiChannel;
    }

    public GuiPacket[] getGuiPackets() {
        return guiPackets;
    }

    public void setGuiPackets(GuiPacket[] guiPackets) {
        this.guiPackets = guiPackets;
    }
}
