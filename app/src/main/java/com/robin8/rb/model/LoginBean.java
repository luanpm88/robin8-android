package com.robin8.rb.model;

import java.io.Serializable;
import java.util.List;

/**
 @author DLJ
 @Description ${TODO}
 @date 2016/1/25 11:29 */
public class LoginBean extends BaseBean {
    /**
     kol : {"id":60196,"email":null,"mobile_number":"15910439098","gender":0,"date_of_birthday":null,"alipay_account":null,"alipay_name":null,"desc":null,"age":null,"weixin_friend_count":null,"id_card":null,"kol_role":"public","role_apply_status":"applying","role_check_remark":null,"name":"159****9098","country":null,"app_city":"shanghai","app_city_label":"上海市","avatar_url":null,"tags":[],"influence_score":-1,"selected_like_articles":false,"issue_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcml2YXRlX3Rva2VuIjoiNGFjNmJmMzRmMDRjODkxMWMzOWI5OTkwNDM3YTY3NWIifQ.noJP0nmK3ps97nTDlAthL3eFSv3jylnF10xZXy4cWUk","kol_uuid":"9ff0411f889462c944c161b128564473","rongcloud_token":null}
     kol_identities : [{"provider":"weibo","uid":"3978206437","token":"2.00xNJO2EJ34yGD19b6593263M3XC1C","name":"爱吃草莓的子木","url":"","avatar_url":"http://tvax3.sinaimg.cn/crop.0.0.750.750.1024/ed1e9ce5ly8ffh90kb8yvj20ku0kvabf.jpg","desc":"一个原创自言自语的帅气的微博主🍭"}]
     */

    private KolEntity kol;
    private List<KolIdentitiesBean> kol_identities;
    private String id;//身份证号码
    public KolEntity getKol() {
        return kol;
    }

    public void setKol(KolEntity kol) {
        this.kol = kol;
    }

    public List<KolIdentitiesBean> getKol_identities() {
        return kol_identities;
    }

    public void setKol_identities(List<KolIdentitiesBean> kol_identities) {
        this.kol_identities = kol_identities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class KolEntity implements Serializable{
        /**
         id : 60196
         email : null
         mobile_number : 15910439098
         gender : 0
         date_of_birthday : null
         alipay_account : null
         alipay_name : null
         desc : null
         age : null
         weixin_friend_count : null
         id_card : null
         kol_role : public
         role_apply_status : applying
         role_check_remark : null
         name : 159****9098
         country : null
         app_city : shanghai
         app_city_label : 上海市
         avatar_url : null
         tags : []
         influence_score : -1
         selected_like_articles : false
         issue_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwcml2YXRlX3Rva2VuIjoiNGFjNmJmMzRmMDRjODkxMWMzOWI5OTkwNDM3YTY3NWIifQ.noJP0nmK3ps97nTDlAthL3eFSv3jylnF10xZXy4cWUk
         kol_uuid : 9ff0411f889462c944c161b128564473
         rongcloud_token : null
         */
        private int id;
        private String email;
        private String mobile_number;
        private int gender;
        private String date_of_birthday;
        private String alipay_account;
        private String alipay_name;
        private String desc;
        private String kol_role;
        private String role_apply_status;
        private String role_check_remark;
        private String name;
        private Object country;
        private String app_city;
        private String app_city_label;
        private String avatar_url;
        private int influence_score;
        private boolean selected_like_articles;
        private String issue_token;
        private String kol_uuid;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getAlipay_account() {
            return alipay_account;
        }

        public void setAlipay_account(String alipay_account) {
            this.alipay_account = alipay_account;
        }

        public String getAlipay_name() {
            return alipay_name;
        }

        public void setAlipay_name(String alipay_name) {
            this.alipay_name = alipay_name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getCountry() {
            return country;
        }

        public void setCountry(Object country) {
            this.country = country;
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

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getInfluence_score() {
            return influence_score;
        }

        public void setInfluence_score(int influence_score) {
            this.influence_score = influence_score;
        }

        public boolean isSelected_like_articles() {
            return selected_like_articles;
        }

        public void setSelected_like_articles(boolean selected_like_articles) {
            this.selected_like_articles = selected_like_articles;
        }

        public String getIssue_token() {
            return issue_token;
        }

        public void setIssue_token(String issue_token) {
            this.issue_token = issue_token;
        }

        public String getKol_uuid() {
            return kol_uuid;
        }

        public void setKol_uuid(String kol_uuid) {
            this.kol_uuid = kol_uuid;
        }

        private List<TagListBean.TagsEntity> tags;

        public List<TagListBean.TagsEntity> getTags() {
            return tags;
        }

        public void setTags(List<TagListBean.TagsEntity> tags) {
            this.tags = tags;
        }
    }

    public static class KolIdentitiesBean {
        /**
         provider : weibo
         uid : 3978206437
         token : 2.00xNJO2EJ34yGD19b6593263M3XC1C
         name : 爱吃草莓的子木
         url :
         avatar_url : http://tvax3.sinaimg.cn/crop.0.0.750.750.1024/ed1e9ce5ly8ffh90kb8yvj20ku0kvabf.jpg
         desc : 一个原创自言自语的帅气的微博主🍭
         */

        private String provider;
        private String uid;
        private String token;
        private String name;
        private String url;
        private String avatar_url;
        private String desc;

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    //    /**
    //     * email : null
    //     * mobile_number : 13817164611
    //     * name : null
    //     * gender : 0
    //     * date_of_birthday : null
    //     * avatar_url : null
    //     * country : null
    //     * province : null
    //     * city : null
    //     * alipay_account : null
    //     * desc :
    //     * tags : []
    //     * issue_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9
    //     * .IntcInByaXZhdGVfdG9rZW5cIjpcImI4ZTE1OWI4YzEwZjg2Y2FhOTY4YjI3Mzk0Mzg3ZjQ1XCJ9Ig
    //     * .CN8C9yrNGcJa3mzzRsRG5W0oNhb-ONXiw1FgcI7htE8
    //     */

    //    private KolEntity kol;
    //    private String id;//身份证号码
    //
    //    public void setKol(KolEntity kol) {
    //        this.kol = kol;
    //    }
    //
    //    public KolEntity getKol() {
    //        return kol;
    //    }
    //    public String getId() {
    //        return id;
    //    }
    //
    //    public void setId(String id) {
    //        this.id = id;
    //    }
    //
    //    public static class KolEntity {
    //        private String email;
    //        private String mobile_number;
    //        private String name;
    //        private int gender;
    //        private String date_of_birthday;
    //        private String avatar_url;
    //        private String app_city;
    //        private String app_city_label;
    //        private String alipay_account;
    //        private String alipay_name;
    //        private String desc;
    //        private int influence_score;
    //        private boolean selected_like_articles;
    //        private String issue_token;
    //        private String kol_uuid;
    //        private String role_apply_status;
    //        private int id;
    //        private String role_check_remark;
    //
    //        public String getKol_uuid() {
    //            return kol_uuid;
    //        }
    //
    //        public void setKol_uuid(String kol_uuid) {
    //            this.kol_uuid = kol_uuid;
    //        }
    //
    //        public boolean isSelected_like_articles() {
    //            return selected_like_articles;
    //        }
    //
    //        public void setSelected_like_articles(boolean selected_like_articles) {
    //            this.selected_like_articles = selected_like_articles;
    //        }
    //
    //        public int getInfluence_score() {
    //            return influence_score;
    //        }
    //
    //        public void setInfluence_score(int influence_score) {
    //            this.influence_score = influence_score;
    //        }
    //
    //        private List<TagListBean.TagsEntity> tags;
    //
    //        public List<TagListBean.TagsEntity> getTags() {
    //            return tags;
    //        }
    //
    //        public void setTags(List<TagListBean.TagsEntity> tags) {
    //            this.tags = tags;
    //        }
    //
    //        public String getEmail() {
    //            return email;
    //        }
    //
    //        public void setEmail(String email) {
    //            this.email = email;
    //        }
    //
    //        public String getMobile_number() {
    //            return mobile_number;
    //        }
    //
    //        public void setMobile_number(String mobile_number) {
    //            this.mobile_number = mobile_number;
    //        }
    //
    //        public String getName() {
    //            return name;
    //        }
    //
    //        public void setName(String name) {
    //            this.name = name;
    //        }
    //
    //        public int getGender() {
    //            return gender;
    //        }
    //
    //        public void setGender(int gender) {
    //            this.gender = gender;
    //        }
    //
    //        public String getDate_of_birthday() {
    //            return date_of_birthday;
    //        }
    //
    //        public void setDate_of_birthday(String date_of_birthday) {
    //            this.date_of_birthday = date_of_birthday;
    //        }
    //
    //        public String getAvatar_url() {
    //            return avatar_url;
    //        }
    //
    //        public void setAvatar_url(String avatar_url) {
    //            this.avatar_url = avatar_url;
    //        }
    //
    //        public String getApp_city() {
    //            return app_city;
    //        }
    //
    //        public void setApp_city(String app_city) {
    //            this.app_city = app_city;
    //        }
    //
    //        public String getApp_city_label() {
    //            return app_city_label;
    //        }
    //
    //        public void setApp_city_label(String app_city_label) {
    //            this.app_city_label = app_city_label;
    //        }
    //
    //        public String getAlipay_account() {
    //            return alipay_account;
    //        }
    //
    //        public void setAlipay_account(String alipay_account) {
    //            this.alipay_account = alipay_account;
    //        }
    //
    //        public String getDesc() {
    //            return desc;
    //        }
    //
    //        public void setDesc(String desc) {
    //            this.desc = desc;
    //        }
    //
    //        public String getIssue_token() {
    //            return issue_token;
    //        }
    //
    //        public void setIssue_token(String issue_token) {
    //            this.issue_token = issue_token;
    //        }
    //
    //
    //        public String getAlipay_name() {
    //            return alipay_name;
    //        }
    //
    //        public void setAlipay_name(String alipay_name) {
    //            this.alipay_name = alipay_name;
    //        }
    //
    //        public String getRole_apply_status() {
    //            return role_apply_status;
    //        }
    //
    //        public void setRole_apply_status(String role_apply_status) {
    //            this.role_apply_status = role_apply_status;
    //        }
    //
    //        public int getId() {
    //            return id;
    //        }
    //
    //        public void setId(int id) {
    //            this.id = id;
    //        }
    //
    //        public void setRole_check_remark(String role_check_remark) {
    //            this.role_check_remark = role_check_remark;
    //        }
    //
    //        public String getRole_check_remark() {
    //            return role_check_remark;
    //        }
    //    }

}
