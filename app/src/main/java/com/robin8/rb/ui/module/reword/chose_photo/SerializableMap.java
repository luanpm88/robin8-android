package com.robin8.rb.ui.module.reword.chose_photo;

import java.io.Serializable;
import java.util.Map;

/**
 Created by zc on 2018/1/17. */

public class SerializableMap implements Serializable {

    private Map<Integer,String> map;

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        this.map = map;
    }
}
