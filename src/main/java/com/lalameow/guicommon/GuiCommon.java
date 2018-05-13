package com.lalameow.guicommon;


import com.lalameow.guicommon.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class GuiCommon {
    @SidedProxy(clientSide = "com.lalameow.guicommon.client.ClientProxy",
            serverSide = "com.lalameow.guicommon.common.CommonProxy")
    public static CommonProxy proxy;
    @Mod.Instance(ModInfo.MODID)
    public static GuiCommon instance;

    private static final String guiChannel = "GuiCommon";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    public static String getGuiChannel() {
        return guiChannel;
    }
}
