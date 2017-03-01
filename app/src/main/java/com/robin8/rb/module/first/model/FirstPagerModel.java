package com.robin8.rb.module.first.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seven on 28/02/2017.
 */

public class FirstPagerModel {
    @SerializedName("error")
    private String error;

    @SerializedName("total_income")
    private String totalIncome;

    @SerializedName("unread_messages_count")
    private int unReadMessages;

    @SerializedName("continuous_checkin_count")
    private String continuousCheckInCount;

    @SerializedName("today_had_check_in")
    private boolean hadCheckedInToday;


    @SerializedName("campaigns")
    private Campaigns campaigns;

    @SerializedName("cps_share")
    private Product product;

    @SerializedName("kol_invitation")

    private Invite invite;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getContinuousCheckInCount() {
        return continuousCheckInCount;
    }

    public void setContinuousCheckInCount(String continuousCheckInCount) {
        this.continuousCheckInCount = continuousCheckInCount;
    }

    public boolean isHadCheckedInToday() {
        return hadCheckedInToday;
    }

    public void setHadCheckedInToday(boolean hadCheckedInToday) {
        this.hadCheckedInToday = hadCheckedInToday;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public int getUnReadMessages() {
        return unReadMessages;
    }

    public void setUnReadMessages(int unReadMessages) {
        this.unReadMessages = unReadMessages;
    }

    public Campaigns getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(Campaigns campaigns) {
        this.campaigns = campaigns;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Invite getInvite() {
        return invite;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public class Campaigns {
        @SerializedName("running_count")
        private String runningCount;

        @SerializedName("completed_count")
        private String completedCount;

        @SerializedName("income_via_share")
        private String income;

        @SerializedName("had_shared_today")
        private boolean hadSharedToday;

        public String getRunningCount() {
            return runningCount;
        }

        public void setRunningCount(String runningCount) {
            this.runningCount = runningCount;
        }

        public String getCompletedCount() {
            return completedCount;
        }

        public void setCompletedCount(String completedCount) {
            this.completedCount = completedCount;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public boolean isHadSharedToday() {
            return hadSharedToday;
        }

        public void setHadSharedToday(boolean hadSharedToday) {
            this.hadSharedToday = hadSharedToday;
        }
    }

    public class Product {
        @SerializedName("running_count")
        private String runningCount;

        @SerializedName("sold_count")
        private String soldCount;

        @SerializedName("income_via_share")
        private String income;

        @SerializedName("had_shared_today")
        private boolean hadSharedToday;

        public String getRunningCount() {
            return runningCount;
        }

        public void setRunningCount(String runningCount) {
            this.runningCount = runningCount;
        }

        public String getSoldCount() {
            return soldCount;
        }

        public void setSoldCount(String soldCount) {
            this.soldCount = soldCount;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public boolean isHadSharedToday() {
            return hadSharedToday;
        }

        public void setHadSharedToday(boolean hadSharedToday) {
            this.hadSharedToday = hadSharedToday;
        }
    }

    public class Invite {

        @SerializedName("running_count")
        private String runningCount;

        @SerializedName("completed_count")
        private String completedCount;

        @SerializedName("income_via_invitation")
        private String income;

        public String getRunningCount() {
            return runningCount;
        }

        public void setRunningCount(String runningCount) {
            this.runningCount = runningCount;
        }

        public String getCompletedCount() {
            return completedCount;
        }

        public void setCompletedCount(String completedCount) {
            this.completedCount = completedCount;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }
    }

}
