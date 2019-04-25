package com.robin8.rb.ui.model;

import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/25 11:29
 */
public class BrandBillModel extends BaseBean {

    private List<BrandBill> transactions;

    public List<BrandBill> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BrandBill> transactions) {
        this.transactions = transactions;
    }

    public static class BrandBill {

//        "id": 13073,
//                "created_at": "07-27",
//                "title": "账户充值",
//                "credits": "+500",
//                "pay_way": "",
//                "direct_text": "充值"
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCredits() {
            return credits;
        }

        public void setCredits(String credits) {
            this.credits = credits;
        }

        public String getPay_way() {
            return pay_way;
        }

        public void setPay_way(String pay_way) {
            this.pay_way = pay_way;
        }

        public String getDirect_text() {
            return direct_text;
        }

        public void setDirect_text(String direct_text) {
            this.direct_text = direct_text;
        }

        private String created_at;
        private String title;
        private String credits;
        private String pay_way;
        private String direct_text;

    }
}
