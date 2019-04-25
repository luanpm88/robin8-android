package com.robin8.rb.ui.module.social.bean;

/**
 Created by zc on 2017/8/16. */

public class SocialDataBean {
    private String name;
    private String badge;
    private int pro;

    public SocialDataBean(String name, String badge, int pro) {
        this.name = name;
        this.badge = badge;
        this.pro = pro;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }
}
