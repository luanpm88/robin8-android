package com.robin8.rb.ui.module.mine.model;

import java.util.List;

/**
 Created by zc on 2017/9/1. */

public class MyInviteModel {

    private List<UserBean> user;

    public List<UserBean> getUser() {
        return user;
    }

    public void setUser(List<UserBean> user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * mobile : 13311362355
         * name : 王总
         */

        private String mobile;
        private String name;

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
}
