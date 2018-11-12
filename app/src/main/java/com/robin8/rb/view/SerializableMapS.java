package com.robin8.rb.view;

import java.io.Serializable;
import java.util.Map;

/**
 Created by zc on 2018/10/30. */

public class SerializableMapS implements Serializable {

    private Map<String,String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
