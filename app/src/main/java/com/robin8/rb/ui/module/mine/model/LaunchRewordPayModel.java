package com.robin8.rb.ui.module.mine.model;

import com.robin8.rb.ui.model.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 Created by zc on 2018/5/31. */

public class LaunchRewordPayModel extends BaseBean {
    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public float getKol_amount() {
        return kol_amount;
    }

    public void setKol_amount(float kol_amount) {
        this.kol_amount = kol_amount;
    }

    private Campaign campaign;
    private float kol_amount;
    private int kol_credit;

    public int getKol_credit() {
        return kol_credit;
    }

    public void setKol_credit(int kol_credit) {
        this.kol_credit = kol_credit;
    }

    public class Campaign implements Serializable {
        private int id;
        private String name;
        private String description;
        private String url;
        private String img_url;
        private String per_budget_type;
        private float per_action_budget;
        private float budget;
        private float need_pay_amount;
        private int avail_click;
        private int total_click;
        private String region;
        private String gender;
        private String age;
        private String[][] stats_data;
        private String tag_labels;
        private String sub_type;
        private String evaluation_status;
        private int effect_score;
        private String review_content;



        public String getEvaluation_status() {
            return evaluation_status;
        }

        public void setEvaluation_status(String evaluation_status) {
            this.evaluation_status = evaluation_status;
        }

        public int getEffect_score() {
            return effect_score;
        }

        public void setEffect_score(int effect_score) {
            this.effect_score = effect_score;
        }

        public String getReview_content() {
            return review_content;
        }

        public void setReview_content(String review_content) {
            this.review_content = review_content;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String[][] getStats_data() {
            return stats_data;
        }

        public void setStats_data(String[][] stats_data) {
            this.stats_data = stats_data;
        }


        public float getTake_budget() {
            return take_budget;
        }

        public void setTake_budget(float take_budget) {
            this.take_budget = take_budget;
        }

        public int getShare_times() {
            return share_times;
        }

        public void setShare_times(int share_times) {
            this.share_times = share_times;
        }

        public int getAvail_click() {
            return avail_click;
        }

        public void setAvail_click(int avail_click) {
            this.avail_click = avail_click;
        }

        public int getTotal_click() {
            return total_click;
        }

        public void setTotal_click(int total_click) {
            this.total_click = total_click;
        }

        private float take_budget;
        private int share_times;

        public void setBudget(float budget) {
            this.budget = budget;
        }

        public void setNeed_pay_amount(float need_pay_amount) {
            this.need_pay_amount = need_pay_amount;
        }

        public float getBrand_amount() {
            return brand_amount;
        }

        public void setBrand_amount(float brand_amount) {
            this.brand_amount = brand_amount;
        }

        private float brand_amount;
        private String status;
        private String alipay_url;
        private String voucher_amount;
        private boolean used_voucher;
        private boolean budget_editable;
        private String deadline;
        private String start_time;
        private List<String> invalid_reasons;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getPer_budget_type() {
            return per_budget_type;
        }

        public void setPer_budget_type(String per_budget_type) {
            this.per_budget_type = per_budget_type;
        }

        public float getPer_action_budget() {
            return per_action_budget;
        }

        public void setPer_action_budget(float per_action_budget) {
            this.per_action_budget = per_action_budget;
        }

        public float getBudget() {
            return budget;
        }

        public void setBudget(int budget) {
            this.budget = budget;
        }

        public float getNeed_pay_amount() {
            return need_pay_amount;
        }

        public void setNeed_pay_amount(int need_pay_amount) {
            this.need_pay_amount = need_pay_amount;
        }

        public String getVoucher_amount() {
            return voucher_amount;
        }

        public void setVoucher_amount(String voucher_amount) {
            this.voucher_amount = voucher_amount;
        }

        public boolean isUsed_voucher() {
            return used_voucher;
        }

        public void setUsed_voucher(boolean used_voucher) {
            this.used_voucher = used_voucher;
        }

        public boolean isBudget_editable() {
            return budget_editable;
        }

        public void setBudget_editable(boolean budget_editable) {
            this.budget_editable = budget_editable;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public List<String> getInvalid_reasons() {
            return invalid_reasons;
        }

        public void setInvalid_reasons(List<String> invalid_reasons) {
            this.invalid_reasons = invalid_reasons;
        }

        public String getAlipay_url() {
            return alipay_url;
        }

        public void setAlipay_url(String alipay_url) {
            this.alipay_url = alipay_url;
        }

        public String getTag_labels() {
            return tag_labels;
        }

        public void setTag_labels(String tag_labels) {
            this.tag_labels = tag_labels;
        }

        public String getSub_type() {
            return sub_type;
        }

        public void setSub_type(String sub_type) {
            this.sub_type = sub_type;
        }
    }

}