package com.robin8.rb.ui.module.social.bean;

/**
 Created by zc on 2017/8/14. */

public class UserContacts {

    /**
     * mobile : 13311362355
     * name : 王总
     */

    private String mobile;
    private String name;

    public UserContacts(String mobile, String name) {
        this.mobile = mobile;
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
