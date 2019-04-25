package com.robin8.rb.ui.indiana.model;

import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class IndianaMineModel extends BaseBean {
    private List<IndianaDetailBean.IndianaActivity> activities;

    public List<IndianaDetailBean.IndianaActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<IndianaDetailBean.IndianaActivity> activities) {
        this.activities = activities;
    }
}
