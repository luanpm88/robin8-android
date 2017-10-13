package com.robin8.rb.module.social.bean;

import com.robin8.rb.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2017/8/17. */

public class InfluenceScoreBean extends BaseBean{


    /**
     * calculated : true
     * provider : weibo
     * avatar_url : http://tvax3.sinaimg.cn/crop.0.0.750.750.1024/ed1e9ce5ly8ffh90kb8yvj20ku0kvabf.jpg
     * name : 爱吃草莓的子木
     * description : 一个原创自言自语的帅气的微博主🍭
     * influence_score : 0.216206
     * influence_level : 影响力中等
     * influence_score_percentile : 超过59%的用户
     * calculated_date : 2017-08-23
     * avg_posts : 5.22761
     * avg_comments : 3.97761
     * avg_likes : 5.8806
     * industries : [{"industry_name":"books","industry_score":0.00983333,"avg_posts":1,"avg_comments":0,"avg_likes":4},{"industry_name":"food","industry_score":0.0179762,"avg_posts":0,"avg_comments":2,"avg_likes":10},{"industry_name":"babies","industry_score":0.030119,"avg_posts":3,"avg_comments":4,"avg_likes":0},{"industry_name":"education","industry_score":0.00440476,"avg_posts":0,"avg_comments":1,"avg_likes":0},{"industry_name":"other","industry_score":0.348016,"avg_posts":35.8205,"avg_comments":27.1538,"avg_likes":39.4359},{"industry_name":"airline","industry_score":0.00983333,"avg_posts":0,"avg_comments":0,"avg_likes":9},{"industry_name":"appliances","industry_score":0.0158333,"avg_posts":0,"avg_comments":0,"avg_likes":15}]
     * similar_kols : [{"id":60235,"avatar_url":""}]
     */

    private boolean calculated;
    private boolean influence_score_visibility;
    private String provider;
    private String avatar_url;
    private String name;
    private String description;
    private double influence_score;
    private String influence_level;
    private String influence_score_percentile;
    private String calculated_date;
    private double avg_posts;
    private double avg_comments;
    private double avg_likes;
    private List<IndustriesBean> industries;
    private List<SimilarKolsBean> similar_kols;

    public boolean isCalculated() {
        return calculated;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getInfluence_score() {
        return influence_score;
    }

    public void setInfluence_score(double influence_score) {
        this.influence_score = influence_score;
    }

    public String getInfluence_level() {
        return influence_level;
    }

    public void setInfluence_level(String influence_level) {
        this.influence_level = influence_level;
    }

    public String getInfluence_score_percentile() {
        return influence_score_percentile;
    }

    public void setInfluence_score_percentile(String influence_score_percentile) {
        this.influence_score_percentile = influence_score_percentile;
    }

    public String getCalculated_date() {
        return calculated_date;
    }

    public void setCalculated_date(String calculated_date) {
        this.calculated_date = calculated_date;
    }

    public double getAvg_posts() {
        return avg_posts;
    }

    public void setAvg_posts(double avg_posts) {
        this.avg_posts = avg_posts;
    }

    public double getAvg_comments() {
        return avg_comments;
    }

    public void setAvg_comments(double avg_comments) {
        this.avg_comments = avg_comments;
    }

    public double getAvg_likes() {
        return avg_likes;
    }

    public void setAvg_likes(double avg_likes) {
        this.avg_likes = avg_likes;
    }

    public List<IndustriesBean> getIndustries() {
        return industries;
    }

    public void setIndustries(List<IndustriesBean> industries) {
        this.industries = industries;
    }

    public List<SimilarKolsBean> getSimilar_kols() {
        return similar_kols;
    }

    public void setSimilar_kols(List<SimilarKolsBean> similar_kols) {
        this.similar_kols = similar_kols;
    }

    public boolean isInfluence_score_visibility() {
        return influence_score_visibility;
    }

    public void setInfluence_score_visibility(boolean influence_score_visibility) {
        this.influence_score_visibility = influence_score_visibility;
    }

    public static class IndustriesBean implements Serializable{
        /**
         * industry_name : books
         * industry_score : 0.00983333
         * avg_posts : 1.0
         * avg_comments : 0.0
         * avg_likes : 4.0
         */

        private String industry_name;
        private double industry_score;
        private double avg_posts;
        private double avg_comments;
        private double avg_likes;

        public String getIndustry_name() {
            return industry_name;
        }

        public void setIndustry_name(String industry_name) {
            this.industry_name = industry_name;
        }

        public double getIndustry_score() {
            return industry_score;
        }

        public void setIndustry_score(double industry_score) {
            this.industry_score = industry_score;
        }

        public double getAvg_posts() {
            return avg_posts;
        }

        public void setAvg_posts(double avg_posts) {
            this.avg_posts = avg_posts;
        }

        public double getAvg_comments() {
            return avg_comments;
        }

        public void setAvg_comments(double avg_comments) {
            this.avg_comments = avg_comments;
        }

        public double getAvg_likes() {
            return avg_likes;
        }

        public void setAvg_likes(double avg_likes) {
            this.avg_likes = avg_likes;
        }
    }

    public static class SimilarKolsBean implements Serializable{
        /**
         * id : 60235
         * avatar_url :
         */

        private int id;
        private String avatar_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
