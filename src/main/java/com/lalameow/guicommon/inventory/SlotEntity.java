package com.lalameow.guicommon.inventory;

import com.lalameow.guicommon.client.gui.common.GuiLocation;
import com.lalameow.guicommon.client.texture.TextureEntity;

/**
 * Author: SettingDust.
 * Date: 2018/5/17.
 */
public class SlotEntity {
    private GuiLocation location;
    private boolean canFire = true;
    private TextureEntity texture = new TextureEntity(null, 0, 0, 0, 0);

    public SlotEntity(GuiLocation location, boolean canFire) {
        this.location = location;
        this.canFire = canFire;
    }

    public SlotEntity(GuiLocation location, boolean canFire, TextureEntity texture) {
        this.location = location;
        this.canFire = canFire;
        this.texture = texture;
    }

    public GuiLocation getLocation() {
        return location;
    }

    public TextureEntity getTexture() {
        return texture;
    }

    public void setLocation(GuiLocation location) {
        this.location = location;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }
}
