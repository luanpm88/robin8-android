package com.robin8.rb.ui.module.create.model;

import java.io.Serializable;

public  class CpsArticleSharesBean implements Serializable{
    private int id;
    private String share_url;
    private String created_at;
    private String kol_id;
    private String kol_name;
    private String kol_avatar_url;
    private float share_forecast_commission;
    private float share_settled_commission;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getKol_id() {
        return kol_id;
    }

    public void setKol_id(String kol_id) {
        this.kol_id = kol_id;
    }

    public String getKol_name() {
        return kol_name;
    }

    public void setKol_name(String kol_name) {
        this.kol_name = kol_name;
    }

    public String  getKol_avatar_url() {
        return kol_avatar_url;
    }

    public void setKol_avatar_url(String kol_avatar_url) {
        this.kol_avatar_url = kol_avatar_url;
    }

    public float getShare_forecast_commission() {
        return share_forecast_commission;
    }

    public void setShare_forecast_commission(int share_forecast_commission) {
        this.share_forecast_commission = share_forecast_commission;
    }

    public float getShare_settled_commission() {
        return share_settled_commission;
    }

    public void setShare_settled_commission(int share_settled_commission) {
        this.share_settled_commission = share_settled_commission;
    }
}