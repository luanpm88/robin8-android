package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

/**
 Created by zc on 2018/5/24. */

public class RechargeModel extends BaseBean {

    /**
     * promotion : {"id":2,"title":"app","min_credit":1000,"rate":0.5,"start_at":"2018-05-24T00:00:00.000+08:00","end_at":"2018-12-25T00:00:00.000+08:00","state":true,"description":"ffffffff","created_at":"2018-05-24T15:09:06.000+08:00","updated_at":"2018-05-24T15:09:06.000+08:00","valid_days_count":0,"expired_at":"2018-05-24 15:21:24"}
     */

    private PromotionBean promotion;

    public PromotionBean getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionBean promotion) {
        this.promotion = promotion;
    }

    public static class PromotionBean {
        /**
         * id : 2
         * title : app
         * min_credit : 1000
         * rate : 0.5
         * start_at : 2018-05-24T00:00:00.000+08:00
         * end_at : 2018-12-25T00:00:00.000+08:00
         * state : true
         * description : ffffffff
         * created_at : 2018-05-24T15:09:06.000+08:00
         * updated_at : 2018-05-24T15:09:06.000+08:00
         * valid_days_count : 0
         * expired_at : 2018-05-24 15:21:24
         */

        private int id;
        private String title;
        private int min_credit;
        private double rate;
        private String start_at;
        private String end_at;
        private boolean state;
        private String description;
        private String created_at;
        private String updated_at;
        private int valid_days_count;
        private String expired_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getMin_credit() {
            return min_credit;
        }

        public void setMin_credit(int min_credit) {
            this.min_credit = min_credit;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getValid_days_count() {
            return valid_days_count;
        }

        public void setValid_days_count(int valid_days_count) {
            this.valid_days_count = valid_days_count;
        }

        public String getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(String expired_at) {
            this.expired_at = expired_at;
        }
    }
}
