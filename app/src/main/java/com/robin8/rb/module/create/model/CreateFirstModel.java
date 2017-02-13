package com.robin8.rb.module.create.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/9/12.
 */
public class CreateFirstModel extends BaseBean {

    /**
     * id : 16
     * title : 教师
     * cover : http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/e9943eaf480331a5a434304d02690066.jpg
     * content : <img>http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/avatars/e9943eaf480331a5a434304d02690066.jpg<product>4
     * show_url : http://robin8-staging.cn/cps_articles/16
     * status : passed
     * check_remark : 审核通过
     * material_total_price : 21.28
     * writing_forecast_commission : 0
     * writing_settled_commission : 0
     * author : {"id":48587,"name":"孙月清","avatar_url":"http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/48587/27a4a929f8.jpg!200"}
     * cps_article_share_count : 1
     */

    private List<CpsArticlesBean> cps_articles;
    /**
     * id : null
     * share_url : http://robin8-staging.cn/cps_article_shares/
     * created_at : null
     * kol_id : null
     * kol_name : null
     * kol_avatar_url : null
     * share_forecast_commission : 0
     * share_settled_commission : 0
     */

    private List<CpsArticleSharesBean> cps_article_shares;

    public List<CpsArticlesBean> getCps_articles() {
        return cps_articles;
    }

    public void setCps_articles(List<CpsArticlesBean> cps_articles) {
        this.cps_articles = cps_articles;
    }

    public List<CpsArticleSharesBean> getCps_article_shares() {
        return cps_article_shares;
    }

    public void setCps_article_shares(List<CpsArticleSharesBean> cps_article_shares) {
        this.cps_article_shares = cps_article_shares;
    }


    public static class CpsArticleSharesBean {
        private Object id;
        private String share_url;
        private Object created_at;
        private Object kol_id;
        private Object kol_name;
        private Object kol_avatar_url;
        private int share_forecast_commission;
        private int share_settled_commission;

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public Object getCreated_at() {
            return created_at;
        }

        public void setCreated_at(Object created_at) {
            this.created_at = created_at;
        }

        public Object getKol_id() {
            return kol_id;
        }

        public void setKol_id(Object kol_id) {
            this.kol_id = kol_id;
        }

        public Object getKol_name() {
            return kol_name;
        }

        public void setKol_name(Object kol_name) {
            this.kol_name = kol_name;
        }

        public Object getKol_avatar_url() {
            return kol_avatar_url;
        }

        public void setKol_avatar_url(Object kol_avatar_url) {
            this.kol_avatar_url = kol_avatar_url;
        }

        public int getShare_forecast_commission() {
            return share_forecast_commission;
        }

        public void setShare_forecast_commission(int share_forecast_commission) {
            this.share_forecast_commission = share_forecast_commission;
        }

        public int getShare_settled_commission() {
            return share_settled_commission;
        }

        public void setShare_settled_commission(int share_settled_commission) {
            this.share_settled_commission = share_settled_commission;
        }
    }
}
