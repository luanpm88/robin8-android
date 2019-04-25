package com.robin8.rb.ui.module.create.model;

import com.robin8.rb.ui.model.BaseBean;

/**
 * Created by IBM on 2016/9/13.
 */
public class ShareResultModel extends BaseBean {


    private CpsArticleSharesBean cps_article_share;

    public CpsArticleSharesBean getCps_article_share() {
        return cps_article_share;
    }

    public void setCps_article_share(CpsArticleSharesBean cps_article_share) {
        this.cps_article_share = cps_article_share;
    }
}
