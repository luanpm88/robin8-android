package com.robin8.rb.ui.activity.uesr_msg.choose_city;

/**
 Created by User on 2017/7/21. */

public class CityBean {
    private boolean select;

    private String city;

    public String getStr() {
        return city == null ? "" : city;
    }

    public void setStr(String str) {
        this.city = str;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
