package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 * Created by IBM on 2016/7/25.
 */
public class SignHistoryModel extends BaseBean {
    public int getContinuous_checkin_count() {
        return continuous_checkin_count;
    }

    public void setContinuous_checkin_count(int continuous_checkin_count) {
        this.continuous_checkin_count = continuous_checkin_count;
    }

    public boolean isToday_had_check_in() {
        return today_had_check_in;
    }

    public void setToday_had_check_in(boolean today_had_check_in) {
        this.today_had_check_in = today_had_check_in;
    }

    public List<String> getCheckin_history() {
        return checkin_history;
    }

    public void setCheckin_history(List<String> checkin_history) {
        this.checkin_history = checkin_history;
    }

    private int continuous_checkin_count;
    private boolean today_had_check_in;
    private List<String> checkin_history;
}
