package com.robin8.rb.ui.module.mine.model;

import java.io.Serializable;
import java.util.List;

/**
 Created by IBM on 2016/8/4. */
public class MineShowModel implements Serializable {

    /**
     error : 0
     hide : 1
     detail : 9
     kol : {"id":109050,"name":"159****9098","kol_role":"public","role_apply_status":"pending","role_check_remark":null,"max_campaign_click":null,"max_campaign_earn_money":0,"campaign_total_income":"0.0","avg_campaign_credit":null,"avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/c1a7dcb670.jpg!avatar","tags":[{"name":"airline","label":"航空"},{"name":"ce","label":"消费电子"},{"name":"appliances","label":"家电"}]}
     is_open_indiana : true
     */

    private int error;
    private int hide;
    private int detail;
    private KolBean kol;
    private boolean is_open_indiana;
    private boolean has_any_unread_message;
    private String logo;
    private String is_show_invite_code;
    private float completed_rate;
    private int creator_is_read;
    private int public_wechat_account_is_read;
    private int weibo_account_is_read;
    private List<ReadListBean> read_list;
    private List<VoteInfosBean> vote_infos;

    public List<VoteInfosBean> getVote_infos() {
        return vote_infos;
    }

    public void setVote_infos(List<VoteInfosBean> vote_infos) {
        this.vote_infos = vote_infos;
    }

    public int getCreator_is_read() {
        return creator_is_read;
    }

    public void setCreator_is_read(int creator_is_read) {
        this.creator_is_read = creator_is_read;
    }

    public int getPublic_wechat_account_is_read() {
        return public_wechat_account_is_read;
    }

    public void setPublic_wechat_account_is_read(int public_wechat_account_is_read) {
        this.public_wechat_account_is_read = public_wechat_account_is_read;
    }

    public int getWeibo_account_is_read() {
        return weibo_account_is_read;
    }

    public void setWeibo_account_is_read(int weibo_account_is_read) {
        this.weibo_account_is_read = weibo_account_is_read;
    }

    public List<ReadListBean> getRead_list() {
        return read_list;
    }

    public void setRead_list(List<ReadListBean> read_list) {
        this.read_list = read_list;
    }

    public static class ReadListBean implements Serializable {
        /**
         state : 1
         dsp : 内容创作者身份认证已通过
         */

        private int state;
        private String dsp;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getDsp() {
            return dsp;
        }

        public void setDsp(String dsp) {
            this.dsp = dsp;
        }
    }

    public float getCompleted_rate() {
        return completed_rate;
    }

    public void setCompleted_rate(float completed_rate) {
        this.completed_rate = completed_rate;
    }

    public String getPut_switch() {
        return put_switch;
    }

    public void setPut_switch(String put_switch) {
        this.put_switch = put_switch;
    }

    private String put_switch;//1显示


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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIs_show_invite_code() {
        return is_show_invite_code;
    }

    public void setIs_show_invite_code(String is_show_invite_code) {
        this.is_show_invite_code = is_show_invite_code;
    }

    public static class VoteInfosBean {
        /**
         is_show : 2018-11-24
         banner_url : http://img.robin8.net/kol_banner.png
         url : https://qa.robin8.netvote?access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcml2YXRlX3Rva2VuIjoiNTRjODVkYjgwZTRjMTQzNmNmYzVkNTg2NzdmNmUyZjgifQ.AhC4m6vuRF2aB3cWUgVgn8e8XTrZzerLUGolpqi4hrs
         */

        private String is_show;
        private String banner_url;
        private String url;
        private String icon_url;
        private String title;
        private String desc;

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(String banner_url) {
            this.banner_url = banner_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class KolBean implements Serializable {
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
         name : food
         label : 美食
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
