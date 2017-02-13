package com.robin8.rb.model;

import java.util.List;

/**
 * @author DLJ
 * @Description ${TODO}
 * @date 2016/1/25 11:29
 */
public class AccountMenuModel extends BaseBean {

//    private int total_pages;
//    private int current_page;
//    private int per_page;
    private List<TransactionEntity> transactions;

    public List<TransactionEntity> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionEntity> transactions) {
        this.transactions = transactions;
    }

//    public int getTotal_pages() {
//        return total_pages;
//    }
//
//    public void setTotal_pages(int total_pages) {
//        this.total_pages = total_pages;
//    }
//
//    public int getPer_page() {
//        return per_page;
//    }
//
//    public void setPer_page(int per_page) {
//        this.per_page = per_page;
//    }
//
//    public int getCurrent_page() {
//        return current_page;
//    }
//
//    public void setCurrent_page(int current_page) {
//        this.current_page = current_page;
//    }

    public static class TransactionEntity {
        private String credits;
        private String amount;
        private String avail_amount;
        private String frozen_amount;
        private String subject;
        private String direct;
        private String created_at;

        public String getCredits() {
            return credits;
        }

        public void setCredits(String credits) {
            this.credits = credits;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAvail_amount() {
            return avail_amount;
        }

        public void setAvail_amount(String avail_amount) {
            this.avail_amount = avail_amount;
        }

        public String getFrozen_amount() {
            return frozen_amount;
        }

        public void setFrozen_amount(String frozen_amount) {
            this.frozen_amount = frozen_amount;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getDirect() {
            return direct;
        }

        public void setDirect(String direct) {
            this.direct = direct;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
