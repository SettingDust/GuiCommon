package com.lalameow.guicommon.client.network.packet;

import com.lalameow.guicommon.client.texture.TextureEntity;
import com.lalameow.guicommon.inventory.SlotEntity;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
public class GuiPacket {
    private String title;
    private TextureEntity texture;
    private SlotEntity[] slotEntities;

    public GuiPacket(String title, TextureEntity texture, SlotEntity[] slotEntities) {
        this.title = title;
        this.texture = texture;
        this.slotEntities = slotEntities;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TextureEntity getTexture() {
        return texture;
    }

    public void setTexture(TextureEntity texture) {
        this.texture = texture;
    }

    public SlotEntity[] getSlotEntities() {
        return slotEntities;
    }

    public void setSlotEntities(SlotEntity[] slotEntities) {
        this.slotEntities = slotEntities;
    }
}

