package com.robin8.rb.indiana.model;

import com.robin8.rb.model.BaseBean;

/**
 * Created by IBM on 2016/7/25.
 */
public class DetailImageModel extends BaseBean {
    private int code;

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    private String[] pictures;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
