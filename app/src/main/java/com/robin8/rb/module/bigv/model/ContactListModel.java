package com.robin8.rb.module.bigv.model;

import com.robin8.rb.model.BaseBean;

/**
 Created by zc on 2019/1/9. */

public class ContactListModel extends BaseBean{
    private String name;

    private String time;

    private String detail;

    private int num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
