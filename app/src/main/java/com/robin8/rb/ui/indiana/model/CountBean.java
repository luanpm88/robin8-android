package com.robin8.rb.ui.indiana.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;

/**
 * Created by IBM on 2016/7/25.
 */
public class CountBean extends BaseBean implements Serializable {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLucky_number() {
        return lucky_number;
    }

    public void setLucky_number(String lucky_number) {
        this.lucky_number = lucky_number;
    }

    public String getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(String order_sum) {
        this.order_sum = order_sum;
    }

    public String getLottery_number() {
        return lottery_number;
    }

    public void setLottery_number(String lottery_number) {
        this.lottery_number = lottery_number;
    }

    public String getLottery_issue() {
        return lottery_issue;
    }

    public void setLottery_issue(String lottery_issue) {
        this.lottery_issue = lottery_issue;
    }

    private String code;
    private String lucky_number;
    private String order_sum;
    private String lottery_number;
    private String lottery_issue;
}
