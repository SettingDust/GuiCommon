package com.lalameow.guicommon.client.gui.common;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
public class GuiLocation {
    private int x;
    private int y;

    public GuiLocation(int x, int y) {
        this.x = x > 0 ? x : 1;
        this.y = y > 0 ? y : 1;
    }

    public GuiLocation(int slot) {
        this.x = slot % 9 + 1;
        this.y = slot / 9 + 1;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int toSlot() {
        int slot = 0;
        slot += x - 1;
        slot += (y - 1) * 9;
        return slot;
    }
}
