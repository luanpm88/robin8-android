package com.robin8.rb.model;

import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/25 11:29
 */
public class LoginBean extends BaseBean {

    /**
     * email : null
     * mobile_number : 13817164611
     * name : null
     * gender : 0
     * date_of_birthday : null
     * avatar_url : null
     * country : null
     * province : null
     * city : null
     * alipay_account : null
     * desc :
     * tags : []
     * issue_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
     * .IntcInByaXZhdGVfdG9rZW5cIjpcImI4ZTE1OWI4YzEwZjg2Y2FhOTY4YjI3Mzk0Mzg3ZjQ1XCJ9Ig
     * .CN8C9yrNGcJa3mzzRsRG5W0oNhb-ONXiw1FgcI7htE8
     */

    private KolEntity kol;
    private String id;//身份证号码

    public void setKol(KolEntity kol) {
        this.kol = kol;
    }

    public KolEntity getKol() {
        return kol;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class KolEntity {
        private String email;
        private String mobile_number;
        private String name;
        private int gender;
        private String date_of_birthday;
        private String avatar_url;
        private String app_city;
        private String app_city_label;
        private String alipay_account;
        private String alipay_name;
        private String desc;
        private int influence_score;
        private boolean selected_like_articles;
        private String issue_token;
        private String kol_uuid;
        private String role_apply_status;
        private int id;
        private String role_check_remark;

        public String getKol_uuid() {
            return kol_uuid;
        }

        public void setKol_uuid(String kol_uuid) {
            this.kol_uuid = kol_uuid;
        }

        public boolean isSelected_like_articles() {
            return selected_like_articles;
        }

        public void setSelected_like_articles(boolean selected_like_articles) {
            this.selected_like_articles = selected_like_articles;
        }

        public int getInfluence_score() {
            return influence_score;
        }

        public void setInfluence_score(int influence_score) {
            this.influence_score = influence_score;
        }

        private List<TagListBean.TagsEntity> tags;

        public List<TagListBean.TagsEntity> getTags() {
            return tags;
        }

        public void setTags(List<TagListBean.TagsEntity> tags) {
            this.tags = tags;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile_number() {
            return mobile_number;
        }

        public void setMobile_number(String mobile_number) {
            this.mobile_number = mobile_number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getDate_of_birthday() {
            return date_of_birthday;
        }

        public void setDate_of_birthday(String date_of_birthday) {
            this.date_of_birthday = date_of_birthday;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getApp_city() {
            return app_city;
        }

        public void setApp_city(String app_city) {
            this.app_city = app_city;
        }

        public String getApp_city_label() {
            return app_city_label;
        }

        public void setApp_city_label(String app_city_label) {
            this.app_city_label = app_city_label;
        }

        public String getAlipay_account() {
            return alipay_account;
        }

        public void setAlipay_account(String alipay_account) {
            this.alipay_account = alipay_account;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIssue_token() {
            return issue_token;
        }

        public void setIssue_token(String issue_token) {
            this.issue_token = issue_token;
        }


        public String getAlipay_name() {
            return alipay_name;
        }

        public void setAlipay_name(String alipay_name) {
            this.alipay_name = alipay_name;
        }

        public String getRole_apply_status() {
            return role_apply_status;
        }

        public void setRole_apply_status(String role_apply_status) {
            this.role_apply_status = role_apply_status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRole_check_remark(String role_check_remark) {
            this.role_check_remark = role_check_remark;
        }

        public String getRole_check_remark() {
            return role_check_remark;
        }
    }

}
