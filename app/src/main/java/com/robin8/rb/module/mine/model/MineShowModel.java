package com.robin8.rb.module.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/8/4.
 */
public class MineShowModel implements Serializable {

    /**
     * error : 0
     * hide : 1
     * detail : 9
     * kol : {"id":109050,"name":"159****9098","kol_role":"public","role_apply_status":"pending","role_check_remark":null,"max_campaign_click":null,"max_campaign_earn_money":0,"campaign_total_income":"0.0","avg_campaign_credit":null,"avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/c1a7dcb670.jpg!avatar","tags":[{"name":"airline","label":"航空"},{"name":"ce","label":"消费电子"},{"name":"appliances","label":"家电"}]}
     * is_open_indiana : true
     */

    private int error;
    private int hide;
    private int detail;
    private KolBean kol;
    private boolean is_open_indiana;
    private boolean has_any_unread_message;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }
    public boolean isIs_open_indiana() {
        return is_open_indiana;
    }

    public void setIs_open_indiana(boolean is_open_indiana) {
        this.is_open_indiana = is_open_indiana;
    }

    public KolBean getKol() {
        return kol;
    }

    public void setKol(KolBean kol) {
        this.kol = kol;
    }

    public boolean isHas_any_unread_message() {
        return has_any_unread_message;
    }

    public void setHas_any_unread_message(boolean has_any_unread_message) {
        this.has_any_unread_message = has_any_unread_message;
    }

    public static class KolBean implements Serializable{
        private int id;
        private String name;
        private String kol_role;
        private String role_apply_status;
        private String role_check_remark;
        private int max_campaign_click = 0;
        private String max_campaign_earn_money = "0";
        private String campaign_total_income = "0";
        private String avg_campaign_credit = "0";
        private String avatar_url;
        /**
         * name : food
         * label : 美食
         */

        private List<TagsBean> tags;
       private List<String> admintag;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKol_role() {
            return kol_role;
        }

        public void setKol_role(String kol_role) {
            this.kol_role = kol_role;
        }

        public String getRole_apply_status() {
            return role_apply_status;
        }

        public void setRole_apply_status(String role_apply_status) {
            this.role_apply_status = role_apply_status;
        }

        public String getRole_check_remark() {
            return role_check_remark;
        }

        public void setRole_check_remark(String role_check_remark) {
            this.role_check_remark = role_check_remark;
        }

        public int getMax_campaign_click() {
            return max_campaign_click;
        }

        public void setMax_campaign_click(int max_campaign_click) {
            this.max_campaign_click = max_campaign_click;
        }

        public String getMax_campaign_earn_money() {
            return max_campaign_earn_money;
        }

        public void setMax_campaign_earn_money(String max_campaign_earn_money) {
            this.max_campaign_earn_money = max_campaign_earn_money;
        }

        public String getCampaign_total_income() {
            return campaign_total_income;
        }

        public void setCampaign_total_income(String campaign_total_income) {
            this.campaign_total_income = campaign_total_income;
        }

        public String getAvg_campaign_credit() {
            return avg_campaign_credit;
        }

        public void setAvg_campaign_credit(String avg_campaign_credit) {
            this.avg_campaign_credit = avg_campaign_credit;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public List<String> getAdmintag() {
            return admintag;
        }

        public void setAdmintag(List<String> admintag) {
            this.admintag = admintag;
        }

        public static class TagsBean {
            private String name;
            private String label;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }

    }
}
