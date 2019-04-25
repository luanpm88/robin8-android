package com.robin8.rb.ui.module.create.model;

import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/9/13.
 */
public class CreateFirstDetailModel extends BaseBean {

    /**
     * id : 28
     * title : 快付通支付
     * cover : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/7cc5d6f53b4f075b1c384a5338923497.jpg
     * content : <img>http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/7cc5d6f53b4f075b1c384a5338923497.jpg<text>支付<product>8<text>支付222
     * show_url : http://robin8-staging.cn/cps_articles/28
     * status : passed
     * check_remark :
     * material_total_price : 11.14
     * writing_forecast_commission : 1.59
     * writing_settled_commission : 0
     * author : {"id":54640,"name":"小小","avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/54640/6e0fce55eb.jpg!200"}
     * cps_article_share_count : 2
     * cps_article_shares : [{"id":30,"share_url":"http://robin8-staging.cn/cps_article_shares/30","created_at":"2016-09-13T14:36:56+08:00","kol_id":54640,"kol_name":"小小","kol_avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/54640/6e0fce55eb.jpg!avatar","share_forecast_commission":11.14,"share_settled_commission":0},{"id":31,"share_url":"http://robin8-staging.cn/cps_article_shares/31","created_at":"2016-09-13T14:38:34+08:00","kol_id":59831,"kol_name":"TEST","kol_avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/59831/0c500643d3.jpg!avatar","share_forecast_commission":0,"share_settled_commission":0}]
     */

    private CpsArticleBean cps_article;

    public CpsArticleBean getCps_article() {
        return cps_article;
    }

    public void setCps_article(CpsArticleBean cps_article) {
        this.cps_article = cps_article;
    }

    public static class CpsArticleBean {
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
         * id : 54640
         * name : 小小
         * avatar_url : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/54640/6e0fce55eb.jpg!200
         */

        private AuthorBean author;
        private int cps_article_share_count;
        /**
         * id : 30
         * share_url : http://robin8-staging.cn/cps_article_shares/30
         * created_at : 2016-09-13T14:36:56+08:00
         * kol_id : 54640
         * kol_name : 小小
         * kol_avatar_url : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/54640/6e0fce55eb.jpg!avatar
         * share_forecast_commission : 11.14
         * share_settled_commission : 0
         */

        private List<CpsArticleSharesBean> cps_article_shares;

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

        public double getWriting_forecast_commission() {
            return writing_forecast_commission;
        }

        public void setWriting_forecast_commission(float writing_forecast_commission) {
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

        public List<CpsArticleSharesBean> getCps_article_shares() {
            return cps_article_shares;
        }

        public void setCps_article_shares(List<CpsArticleSharesBean> cps_article_shares) {
            this.cps_article_shares = cps_article_shares;
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
}
