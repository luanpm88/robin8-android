package com.robin8.rb.ui.module.reword.bean;

import java.io.Serializable;

public class ShareBean implements Serializable {
    private String name;
    private int icon;

    public ShareBean(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}