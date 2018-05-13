package com.lalameow.guicommon.client.handler;

import com.google.gson.Gson;
import com.lalameow.guicommon.GuiCommon;
import com.lalameow.guicommon.client.network.packet.GuiPacket;
import com.lalameow.guicommon.common.util.PacketUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
@SideOnly(Side.CLIENT)
public class GuiPacketHandler {
    @SubscribeEvent
    public void guiPacket(FMLNetworkEvent.ClientCustomPacketEvent event) throws IOException {
        if (event.getPacket().channel().equals(GuiCommon.getGuiChannel())) {
            try {
                String json = PacketUtils.getPacket(event.getPacket());
                Gson gson = new Gson();
                GuiPacket[] guiPackets = gson.fromJson(json, GuiPacket[].class);
                GuiCommon.proxy.setGuiPackets(guiPackets);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
