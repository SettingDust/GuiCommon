package com.lalameow.guicommon.common.util;

import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.io.UnsupportedEncodingException;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
public class PacketUtils {
    public static String getPacket(FMLProxyPacket packet) {
        String json = "";
        if (!packet.payload().hasArray()) {
            int len = packet.payload().readableBytes();
            byte[] arr = new byte[len];
            packet.payload().getBytes(0, arr);
            try {
                json = new String(arr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else try {
            json = new String(packet.payload().array(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
