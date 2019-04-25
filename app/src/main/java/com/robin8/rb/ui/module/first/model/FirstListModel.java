package com.robin8.rb.ui.module.first.model;

import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/8/4.
 */
public class FirstListModel extends BaseBean {

    private List<KolAnnouncementsBean> kol_announcements;

    private List<BigVsBean> big_vs;

    public List<KolAnnouncementsBean> getKol_announcements() {
        return kol_announcements;
    }

    public void setKol_announcements(List<KolAnnouncementsBean> kol_announcements) {
        this.kol_announcements = kol_announcements;
    }

    public List<BigVsBean> getBig_vs() {
        return big_vs;
    }

    public void setBig_vs(List<BigVsBean> big_vs) {
        this.big_vs = big_vs;
    }

}
