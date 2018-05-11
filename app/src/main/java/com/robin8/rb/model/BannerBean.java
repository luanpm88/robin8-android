package com.robin8.rb.model;


public class BannerBean {

    public static final int BE_KOL = 1;
    public static final int LAUNCH_CAMPAIGN = 2;
    public static final int INVITE_FRIEND = 3;
    public static final int INDIANA = 4;
    public static final int CHECK_IN = 5;

    public static final int CREATE_EARN_MONEY = 6;
    public static final int HOW_CREATE = 7;
    public int resId;
    public int type;

    public BannerBean() {

    }

    public BannerBean(int resId, int type) {
        this.resId = resId;
        this.type = type;
    }
}