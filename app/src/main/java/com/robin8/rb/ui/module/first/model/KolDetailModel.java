package com.robin8.rb.ui.module.first.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IBM on 2016/8/4.
 */
public class KolDetailModel extends BaseBean {

    private BigVBean big_v;
    private int is_follow;
    private List<KolShowsBean> kol_shows;
    private List<KolKeywordsBean> kol_keywords;
    private List<SocialAccountsBean> social_accounts;

    public BigVBean getBig_v() {
        return big_v;
    }

    public void setBig_v(BigVBean big_v) {
        this.big_v = big_v;
    }

    public int getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(int is_follow) {
        this.is_follow = is_follow;
    }

    public List<KolShowsBean> getKol_shows() {
        return kol_shows;
    }

    public void setKol_shows(List<KolShowsBean> kol_shows) {
        this.kol_shows = kol_shows;
    }

    public List<KolKeywordsBean> getKol_keywords() {
        return kol_keywords;
    }

    public void setKol_keywords(List<KolKeywordsBean> kol_keywords) {
        this.kol_keywords = kol_keywords;
    }

    public List<SocialAccountsBean> getSocial_accounts() {
        return social_accounts;
    }

    public void setSocial_accounts(List<SocialAccountsBean> social_accounts) {
        this.social_accounts = social_accounts;
    }

    public static class BigVBean {
        private int id;
        private int age;
        private String name;
        private String job_info;
        private String desc;
        private String app_city_label;
        private String kol_role;
        private String role_apply_status;
        private String role_check_remark;
        private String email;
        private int gender;
        private String avatar_url;
        /**
         * name : overall
         * label : 综合
         */

        private List<TagsBean> tags;

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

        public String getJob_info() {
            return job_info;
        }

        public void setJob_info(String job_info) {
            this.job_info = job_info;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getApp_city_label() {
            return app_city_label;
        }

        public void setApp_city_label(String app_city_label) {
            this.app_city_label = app_city_label;
        }

        public String getKol_role() {
            return kol_role;
        }

        public void setKol_role(String kol_role) {
            this.kol_role = kol_role;
        }

        public Object getRole_apply_status() {
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

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

    public static class KolShowsBean implements Serializable {
        private String title;
        private String desc;
        private String link;
        private String provider;
        private String cover_url;

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

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getCover_url() {
            return cover_url;
        }

        public void setCover_url(String cover_url) {
            this.cover_url = cover_url;
        }
    }

    public static class KolKeywordsBean {
        private String keyword;
        private Object weight;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public Object getWeight() {
            return weight;
        }

        public void setWeight(Object weight) {
            this.weight = weight;
        }
    }
}
