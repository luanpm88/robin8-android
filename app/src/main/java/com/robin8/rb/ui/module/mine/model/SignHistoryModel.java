package com.robin8.rb.ui.module.mine.model;

import com.google.gson.annotations.SerializedName;
import com.robin8.rb.ui.model.BaseBean;

import java.util.List;

/**
 Created by IBM on 2016/7/25.
 Fixed by zc on 2017/12/5. */
public class SignHistoryModel extends BaseBean {

      /*continuous_checkin_count 连续签到天数
    today_had_check_in 今天是否已经签到
    checkin_history 之前所有签到过的日期的集合，现在是本月内，这个可以根据需要调整
    total_check_in_days 累计签到天数（包括连续和不连续）
    total_check_in_amount 累计获得的钱数
    today_already_amount今天获得的钱数
    today_can_amount 今日签到可领钱数
    tomorrow_can_amount明日签到可领钱数*/
    /**
     continuous_checkin_count : 1
     today_had_check_in : true
     checkin_history : [{"created_at":"2017-12-08","is_continuous":0},{"created_at":"2017-12-07","is_continuous":0},{"created_at":"2017-12-07","is_continuous":0},{"created_at":"2017-12-06","is_continuous":0},{"created_at":"2017-12-06","is_continuous":0},{"created_at":"2017-12-05","is_continuous":0},{"created_at":"2017-12-05","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-04","is_continuous":0},{"created_at":"2017-12-03","is_continuous":0},{"created_at":"2017-12-03","is_continuous":0},{"created_at":"2017-12-02","is_continuous":0},{"created_at":"2017-12-01","is_continuous":0},{"created_at":"2017-12-01","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","i s_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-30","is_continuous":0},{"created_at":"2017-11-29","is_continuous":0},{"created_at":"2017-11-29","is_continuous":0},{"created_at":"2017-11-28","is_continuous":0},{"created_at":"2017-11-28","is_continuous":0},{"created_at":"2017-11-27","is_continuous":0},{"created_at":"2017-11-08","is_continuous":0},{"created_at":"2017-10-31","is_continuous":0}]
     total_check_in_days : 167
     total_check_in_amount : 24.9
     today_already_amount : 0
     today_can_amount : 0.2
     tomorrow_can_amount : 0.25
     */

    private int continuous_checkin_count;
    private boolean today_had_check_in;
    private int total_check_in_days;
    private double total_check_in_amount;
    private double today_already_amount;
    private double today_can_amount;
    private double tomorrow_can_amount;
    private List<CheckinHistoryBean> checkin_history;

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

    public int getTotal_check_in_days() {
        return total_check_in_days;
    }

    public void setTotal_check_in_days(int total_check_in_days) {
        this.total_check_in_days = total_check_in_days;
    }

    public double getTotal_check_in_amount() {
        return total_check_in_amount;
    }

    public void setTotal_check_in_amount(double total_check_in_amount) {
        this.total_check_in_amount = total_check_in_amount;
    }

    public double getToday_already_amount() {
        return today_already_amount;
    }

    public void setToday_already_amount(double today_already_amount) {
        this.today_already_amount = today_already_amount;
    }

    public double getToday_can_amount() {
        return today_can_amount;
    }

    public void setToday_can_amount(double today_can_amount) {
        this.today_can_amount = today_can_amount;
    }

    public double getTomorrow_can_amount() {
        return tomorrow_can_amount;
    }

    public void setTomorrow_can_amount(double tomorrow_can_amount) {
        this.tomorrow_can_amount = tomorrow_can_amount;
    }

    public List<CheckinHistoryBean> getCheckin_history() {
        return checkin_history;
    }

    public void setCheckin_history(List<CheckinHistoryBean> checkin_history) {
        this.checkin_history = checkin_history;
    }

    public static class CheckinHistoryBean {
        /**
         created_at : 2017-12-08
         is_continuous : 0
         i s_continuous : 0
         */

        private String created_at;
        private int is_continuous;
        @SerializedName("i s_continuous")
        private int _$IS_continuous247; // FIXME check this code

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getIs_continuous() {
            return is_continuous;
        }

        public void setIs_continuous(int is_continuous) {
            this.is_continuous = is_continuous;
        }

        public int get_$IS_continuous247() {
            return _$IS_continuous247;
        }

        public void set_$IS_continuous247(int _$IS_continuous247) {
            this._$IS_continuous247 = _$IS_continuous247;
        }
    }


    //    private int continuous_checkin_count;
    //    private boolean today_had_check_in;
    //    private int total_check_in_days;
    //    private double total_check_in_amount;
    //    private int today_already_amount;
    //    private double today_can_amount;
    //    private double tomorrow_can_amount;
    //    private List<String> checkin_history;
    //
    //    public int getContinuous_checkin_count() {
    //        return continuous_checkin_count;
    //    }
    //
    //    public void setContinuous_checkin_count(int continuous_checkin_count) {
    //        this.continuous_checkin_count = continuous_checkin_count;
    //    }
    //
    //    public boolean isToday_had_check_in() {
    //        return today_had_check_in;
    //    }
    //
    //    public void setToday_had_check_in(boolean today_had_check_in) {
    //        this.today_had_check_in = today_had_check_in;
    //    }
    //
    //    public int getTotal_check_in_days() {
    //        return total_check_in_days;
    //    }
    //
    //    public void setTotal_check_in_days(int total_check_in_days) {
    //        this.total_check_in_days = total_check_in_days;
    //    }
    //
    //    public double getTotal_check_in_amount() {
    //        return total_check_in_amount;
    //    }
    //
    //    public void setTotal_check_in_amount(double total_check_in_amount) {
    //        this.total_check_in_amount = total_check_in_amount;
    //    }
    //
    //    public int getToday_already_amount() {
    //        return today_already_amount;
    //    }
    //
    //    public void setToday_already_amount(int today_already_amount) {
    //        this.today_already_amount = today_already_amount;
    //    }
    //
    //    public double getToday_can_amount() {
    //        return today_can_amount;
    //    }
    //
    //    public void setToday_can_amount(double today_can_amount) {
    //        this.today_can_amount = today_can_amount;
    //    }
    //
    //    public double getTomorrow_can_amount() {
    //        return tomorrow_can_amount;
    //    }
    //
    //    public void setTomorrow_can_amount(double tomorrow_can_amount) {
    //        this.tomorrow_can_amount = tomorrow_can_amount;
    //    }
    //
    //    public List<String> getCheckin_history() {
    //        return checkin_history;
    //    }
    //
    //    public void setCheckin_history(List<String> checkin_history) {
    //        this.checkin_history = checkin_history;
    //    }
}
