package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 Created by zc on 2017/11/7. */

public class MyCampaignModel extends BaseBean {

    private List<MyCampaignsBean> my_campaigns;

    public List<MyCampaignsBean> getMy_campaigns() {
        return my_campaigns;
    }

    public void setMy_campaigns(List<MyCampaignsBean> my_campaigns) {
        this.my_campaigns = my_campaigns;
    }

    public static class MyCampaignsBean {
        /**
         * id : 1355276
         * campaign_id : 4297
         * status : rejected
         * img_status : rejected
         * reject_reason : null
         * earn_money : 0.0
         * campaign_name : 【测试】测试推送是否正常101705
         * per_action_type : wechat
         */

        private int id;
        private int campaign_id;
        private String status;
        private String img_status;
        private String reject_reason;
        private double earn_money;
        private String campaign_name;
        private String per_action_type;
        private String screenshot;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCampaign_id() {
            return campaign_id;
        }

        public void setCampaign_id(int campaign_id) {
            this.campaign_id = campaign_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImg_status() {
            return img_status;
        }

        public void setImg_status(String img_status) {
            this.img_status = img_status;
        }

        public String getReject_reason() {
            return reject_reason;
        }

        public void setReject_reason(String reject_reason) {
            this.reject_reason = reject_reason;
        }

        public double getEarn_money() {
            return earn_money;
        }

        public void setEarn_money(double earn_money) {
            this.earn_money = earn_money;
        }

        public String getCampaign_name() {
            return campaign_name;
        }

        public void setCampaign_name(String campaign_name) {
            this.campaign_name = campaign_name;
        }

        public String getPer_action_type() {
            return per_action_type;
        }

        public void setPer_action_type(String per_action_type) {
            this.per_action_type = per_action_type;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public void setScreenshot(String screenshot) {
            this.screenshot = screenshot;
        }
    }
}
