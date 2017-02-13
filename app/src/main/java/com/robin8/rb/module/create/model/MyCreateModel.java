package com.robin8.rb.module.create.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/9/13.
 */
public class MyCreateModel extends BaseBean {


    private List<CpsArticlesBean> cps_articles;


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
}
