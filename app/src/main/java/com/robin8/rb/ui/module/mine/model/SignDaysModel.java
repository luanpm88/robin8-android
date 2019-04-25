package com.robin8.rb.ui.module.mine.model;

import java.io.Serializable;

/**
 Created by zc on 2018/4/24. */

public class SignDaysModel implements Serializable {
    private float rewards;
    private String week;
    private boolean isSign;

    public float getRewards() {
        return rewards;
    }

    public void setRewards(float rewards) {
        this.rewards = rewards;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }
}
