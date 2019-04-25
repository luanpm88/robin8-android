package com.robin8.rb.ui.module.first.model;

import java.util.List;

public class BigVsBean {
    private int id;
    private String name;
    private String job_info;
    private String avatar_url;
    /**
     * name : overall
     * label : 综合
     */

    private List<TagsBean> tags;

    private List<SocialAccountsBean> social_accounts;

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

    public List<SocialAccountsBean> getSocial_accounts() {
        return social_accounts;
    }

    public void setSocial_accounts(List<SocialAccountsBean> social_accounts) {
        this.social_accounts = social_accounts;
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