package com.lalameow.guicommon.client.texture;

import com.google.common.base.Strings;

/**
 * Author: SettingDust.
 * Date: 2018/5/17.
 */
public class TextureEntity {
    private String path;
    private int width;
    private int height;
    private int u;
    private int v;

    public TextureEntity(String path, int width, int height, int u, int v) {
        this.path = path;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getU() {
        return u;
    }

    public void setU(int u) {
        this.u = u;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public boolean isEmpty() {
        return Strings.isNullOrEmpty(path);
    }
}
