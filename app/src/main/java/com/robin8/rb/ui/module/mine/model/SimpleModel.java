package com.robin8.rb.ui.module.mine.model;

/**
 Created by zc on 2018/4/27. */

public class SimpleModel {
    public int icon;
    public String title;
    public String detail;
    public String btn;
    public String url;

    public SimpleModel(int icon, String title, String detail, String btn) {
        this.icon = icon;
        this.title = title;
        this.detail = detail;
        this.btn = btn;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getBtn() {
        return btn;
    }

    public void setBtn(String btn) {
        this.btn = btn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
