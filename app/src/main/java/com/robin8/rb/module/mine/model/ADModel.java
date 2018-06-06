package com.robin8.rb.module.mine.model;

import java.io.Serializable;

/**
 Created by zc on 2018/5/30. */

public class ADModel implements Serializable {
    /**
     name : 烛黎
     avatar_url : null
     campany_name : null
     url : null
     description : null
     brand_amount : 11.0
     brand_credit : 1000
     */
    private int error;
    private String name;
    private String avatar_url;
    private String campany_name;
    private String url;
    private String description;
    private float brand_amount;
    private int brand_credit;
    private String brand_credit_expired_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getCampany_name() {
        return campany_name;
    }

    public void setCampany_name(String campany_name) {
        this.campany_name = campany_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBrand_amountX() {
        return brand_amount;
    }

    public void setBrand_amountX(float brand_amountX) {
        this.brand_amount = brand_amountX;
    }

    public int getBrand_credit() {
        return brand_credit;
    }

    public void setBrand_credit(int brand_credit) {
        this.brand_credit = brand_credit;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getBrand_credit_expired_at() {
        return brand_credit_expired_at;
    }

    public void setBrand_credit_expired_at(String brand_credit_expired_at) {
        this.brand_credit_expired_at = brand_credit_expired_at;
    }
}
