package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 Created by zc on 2017/8/23. */

public class InviteModel extends BaseBean{


    private List<KolUsersBean> kol_users;

    public List<KolUsersBean> getKol_users() {
        return kol_users;
    }

    public void setKol_users(List<KolUsersBean> kol_users) {
        this.kol_users = kol_users;
    }

    public static class KolUsersBean {
        /**
         * mobile_number : 13311362355
         * status : already_invited
         */

        private String mobile_number;
        private String status;

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
