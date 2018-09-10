package com.robin8.rb.model;

import java.io.Serializable;

/**
 Created by zc on 2018/8/23. */

public class WebDataBean implements Serializable{
    private String token;
    private String public_key;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }
}
