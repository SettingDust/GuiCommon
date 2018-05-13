package com.lalameow.guicommon.client.network.packet;

/**
 * Author: SettingDust.
 * Date: 2018/5/12.
 */
public class GuiPacket {
    private String title;
    private String texture;
    private int row;
    private int[] enableIndex;

    public GuiPacket(String title, String texture, int row, int[] enableIndex) {
        this.title = title;
        this.texture = texture;
        this.row = row;
        this.enableIndex = enableIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int[] getEnableIndex() {
        return enableIndex;
    }

    public void setEnableIndex(int[] enableIndex) {
        this.enableIndex = enableIndex;
    }
}

