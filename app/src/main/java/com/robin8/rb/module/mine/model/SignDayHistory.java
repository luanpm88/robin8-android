package com.robin8.rb.module.mine.model;

import com.robin8.rb.model.BaseBean;

import java.util.List;

/**
 Created by zc on 2018/5/2. */

public class SignDayHistory extends BaseBean {


    /**
     * continuous_checkin_count : 1
     * today_had_check_in : true
     * checkin_history : [{"created_at":"2018-5-02","is_continuous":0}]
     * total_check_in_days : 4
     * total_check_in_amount : 0.4
     * today_already_amount : 0.1
     * today_can_amount : 0.1
     * tomorrow_can_amount : 0.2
     * check_in_7 : [true,false,false,false,false,false,false]
     * campaign_invites_count : 0
     * invite_friends : 0
     */

    private int continuous_checkin_count;
    private boolean today_had_check_in;
    private int total_check_in_days;
    private double total_check_in_amount;
    private double today_already_amount;
    private double today_can_amount;
    private double tomorrow_can_amount;
    private int campaign_invites_count;
    private int invite_friends;
    private List<CheckinHistoryBean> checkin_history;
    private List<Boolean> check_in_7;
    private boolean is_show_newbie;
    private int red_money_count;

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

    public int getCampaign_invites_count() {
        return campaign_invites_count;
    }

    public void setCampaign_invites_count(int campaign_invites_count) {
        this.campaign_invites_count = campaign_invites_count;
    }

    public int getInvite_friends() {
        return invite_friends;
    }

    public void setInvite_friends(int invite_friends) {
        this.invite_friends = invite_friends;
    }

    public List<CheckinHistoryBean> getCheckin_history() {
        return checkin_history;
    }

    public void setCheckin_history(List<CheckinHistoryBean> checkin_history) {
        this.checkin_history = checkin_history;
    }

    public List<Boolean> getCheck_in_7() {
        return check_in_7;
    }

    public void setCheck_in_7(List<Boolean> check_in_7) {
        this.check_in_7 = check_in_7;
    }

    public boolean is_show_newbie() {
        return is_show_newbie;
    }

    public void setIs_show_newbie(boolean is_show_newbie) {
        this.is_show_newbie = is_show_newbie;
    }

    public int getRed_money_count() {
        return red_money_count;
    }

    public void setRed_money_count(int red_money_count) {
        this.red_money_count = red_money_count;
    }

    public static class CheckinHistoryBean {
        /**
         * created_at : 2018-5-02
         * is_continuous : 0
         */

        private String created_at;
        private int is_continuous;

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
    }
}