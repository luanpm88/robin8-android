package com.robin8.rb.module.mine.model;

/**
 Created by zc on 2018/11/9. */

public class UserCheckBean {
    private boolean isPass;
    private String text;
    private boolean isShow;

    /**
     @param isPass 是否通过
     @param text 文字
     @param isShow 是否显示左边的进度轴
     */
    public UserCheckBean(boolean isPass, String text, boolean isShow) {
        this.isPass = isPass;
        this.text = text;
        this.isShow = isShow;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }


}
