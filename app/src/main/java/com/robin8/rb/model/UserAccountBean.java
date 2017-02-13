package com.robin8.rb.model;

import java.util.List;

/**
 * @author DLJ
 * @Description 用户账户
 * @date 2016/2/17 10:55
 */
public class UserAccountBean extends BaseBean {


    /**
     * amount : 138.9
     * frozen_amount : 0.0
     * avail_amount : 138.9
     * total_income : 18.1
     * verifying_income : 18.1
     * total_withdraw : 202.0
     * today_income : 0
     * total_income
     */

    private KolEntity kol;
    /**
     * date : 2016-02-12
     * total_amount : 0
     * count : 0
     */

    private List<StatsEntity> stats;

    public KolEntity getKol() {
        return kol;
    }

    public void setKol(KolEntity kol) {
        this.kol = kol;
    }

    public List<StatsEntity> getStats() {
        return stats;
    }

    public void setStats(List<StatsEntity> stats) {
        this.stats = stats;
    }

    public static class KolEntity {
        private String amount;
        private String frozen_amount="";//可兑现
        private String avail_amount;//
        private String total_income;//总收入
        private String verifying_income;
        private String total_withdraw ="";
        private float today_income;
        private String total_expense;//已花费

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getFrozen_amount() {
            return frozen_amount;
        }

        public void setFrozen_amount(String frozen_amount) {
            this.frozen_amount = frozen_amount;
        }

        public String getAvail_amount() {
            return avail_amount;
        }

        public void setAvail_amount(String avail_amount) {
            this.avail_amount = avail_amount;
        }

        public String getTotal_income() {
            return total_income;
        }

        public void setTotal_income(String total_income) {
            this.total_income = total_income;
        }

        public String getVerifying_income() {
            return verifying_income;
        }

        public void setVerifying_income(String verifying_income) {
            this.verifying_income = verifying_income;
        }

        public String getTotal_withdraw() {
            return total_withdraw;
        }

        public void setTotal_withdraw(String total_withdraw) {
            this.total_withdraw = total_withdraw;
        }

        public float getToday_income() {
            return today_income;
        }

        public void setToday_income(float today_income) {
            this.today_income = today_income;
        }

        public String getTotal_expense() {
            return total_expense;
        }

        public void setTotal_expense(String total_expense) {
            this.total_expense = total_expense;
        }
    }

    public static class StatsEntity {
        private String date;
        private float total_amount;
        private int count;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public float getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(float total_amount) {
            this.total_amount = total_amount;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
