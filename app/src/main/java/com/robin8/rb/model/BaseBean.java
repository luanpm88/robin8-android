package com.robin8.rb.model;

import java.io.Serializable;

public class BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String detail;// 提示信息
    private int error = -1;// 状态
    private String alipay_url;
    private float brand_amount;

    private int total_pages = 0;
    private int current_page = 0;
    private int per_page = 0;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getAlipay_url() {
        return alipay_url;
    }

    public void setAlipay_url(String alipay_url) {
        this.alipay_url = alipay_url;
    }

    public float getBrand_amount() {
        return brand_amount;
    }

    public void setBrand_amount(float brand_amount) {
        this.brand_amount = brand_amount;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

}
