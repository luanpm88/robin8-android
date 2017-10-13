package com.robin8.rb.module.social.bean;

/**
 Created by zc on 2017/8/15. */

public class UserPhotoBean {
    private String img_url;
    private String jump_url;
    private String id;
    private int show;

    public UserPhotoBean(int show) {
        this.show = show;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }
}
