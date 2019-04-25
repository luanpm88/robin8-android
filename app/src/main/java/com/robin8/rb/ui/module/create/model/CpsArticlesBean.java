package com.robin8.rb.ui.module.create.model;

public  class CpsArticlesBean {
    private int id;
    private String title;
    private String cover;
    private String content;
    private String show_url;
    private String status;
    private String check_remark;
    private float material_total_price;
    private float writing_forecast_commission;
    private float writing_settled_commission;
    private float share_forecast_commission;
    private float share_settled_commission;


    /**
     * id : 48587
     * name : 孙月清
     * avatar_url : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/48587/27a4a929f8.jpg!200
     */

    private AuthorBean author;
    private int cps_article_share_count;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheck_remark() {
        return check_remark;
    }

    public void setCheck_remark(String check_remark) {
        this.check_remark = check_remark;
    }

    public double getMaterial_total_price() {
        return material_total_price;
    }

    public void setMaterial_total_price(float material_total_price) {
        this.material_total_price = material_total_price;
    }

    public float getWriting_forecast_commission() {
        return writing_forecast_commission;
    }

    public void setWriting_forecast_commission(int writing_forecast_commission) {
        this.writing_forecast_commission = writing_forecast_commission;
    }

    public float getWriting_settled_commission() {
        return writing_settled_commission;
    }

    public void setWriting_settled_commission(int writing_settled_commission) {
        this.writing_settled_commission = writing_settled_commission;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public int getCps_article_share_count() {
        return cps_article_share_count;
    }

    public void setCps_article_share_count(int cps_article_share_count) {
        this.cps_article_share_count = cps_article_share_count;
    }

    public float getShare_forecast_commission() {
        return share_forecast_commission;
    }

    public void setShare_forecast_commission(float share_forecast_commission) {
        this.share_forecast_commission = share_forecast_commission;
    }

    public float getShare_settled_commission() {
        return share_settled_commission;
    }

    public void setShare_settled_commission(float share_settled_commission) {
        this.share_settled_commission = share_settled_commission;
    }

    public static class AuthorBean {
        private int id;
        private String name;
        private String avatar_url;

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

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}