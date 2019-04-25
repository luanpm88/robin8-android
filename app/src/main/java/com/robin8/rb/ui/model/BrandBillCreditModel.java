package com.robin8.rb.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2018/5/30. */

public class BrandBillCreditModel extends BaseBean{
    private List<CreditsBean> credits;

    public List<CreditsBean> getCredits() {
        return credits;
    }

    public void setCredits(List<CreditsBean> credits) {
        this.credits = credits;
    }

    public static class CreditsBean implements Serializable{
        /**
         * trade_no : 201805301654304100068188
         * direct : 花费
         * show_time : 2018-05-30
         * score : -100
         * amount : 49500
         * remark : 营销活动（世界顶级）
         */

        private String trade_no;
        private String direct;
        private String show_time;
        private int score;
        private int amount;
        private String remark;

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getDirect() {
            return direct;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }

        public String getShow_time() {
            return show_time;
        }

        public void setShow_time(String show_time) {
            this.show_time = show_time;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
