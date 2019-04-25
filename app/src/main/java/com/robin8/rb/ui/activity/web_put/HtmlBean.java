package com.robin8.rb.ui.activity.web_put;

/**
 Created by zc on 2018/8/10. */

public class HtmlBean {

    /**
     * phone :
     * public_key : 04fa36b7dd2786dbcaabc255b336e2be44f5acd225eeacf0084e57eb4b8d4986cdd61c96a28da0c86cc3efffa11921565e3234642ffdbd4396769c97d483a869ee
     * count : 5
     * level : 2
     * news_count : 0
     * email : null
     * id : 69
     * href : /api/accounts//04fa36b7dd2786dbcaabc255b336e2be44f5acd225eeacf0084e57eb4b8d4986cdd61c96a28da0c86cc3efffa11921565e3234642ffdbd4396769c97d483a869ee
     * wallets : [{"coinid": "BTCTEST", "uid": 69, "address": "mtwANVNDbQn15sWLR1kWvgGM12PBwxTQBy", "amount_active": 0, "amount_frozen": 0}, {"coinid": "LTCTEST", "uid": 69, "address": "mtwANVNDbQn15sWLR1kWvgGM12PBwxTQBy", "amount_active": 0, "amount_frozen": 0}, {"coinid": "ETH", "uid": 69, "address": "0x1a93148277a212c82d01e55a00387a96fd7cf148", "amount_active": 0, "amount_frozen": 0}, {"coinid": "QTUMTEST", "uid": 69, "address": "qWybEU3oz2wtSmm7dnkPHoiRfZhgPLEzkw", "amount_active": 0, "amount_frozen": 0}, {"coinid": "PUTTEST", "uid": 69, "address": "qWybEU3oz2wtSmm7dnkPHoiRfZhgPLEzkw", "amount_active": 0, "amount_frozen": 0}]
     */

    private String phone;
    private String public_key;
    private int count;
    private int level;
    private int news_count;
    private Object email;
    private int id;
    private String href;
    private String wallets;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getNews_count() {
        return news_count;
    }

    public void setNews_count(int news_count) {
        this.news_count = news_count;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getWallets() {
        return wallets;
    }

    public void setWallets(String wallets) {
        this.wallets = wallets;
    }
}