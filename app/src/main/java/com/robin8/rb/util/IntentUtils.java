package com.robin8.rb.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.robin8.rb.view.SerializableMapS;

import java.util.ArrayList;
import java.util.Map;

/**
 Created by zc on 2018/10/30. */

public class IntentUtils extends Intent {
    public IntentUtils(Context packageContext,Class<?> mClass) {
        super(packageContext, mClass);
    }

    public void setMapPaths(String tag, Map<String, String> paths) {
        SerializableMapS map = new SerializableMapS();
        map.setMap(paths);
        Bundle bundle = new Bundle();
        bundle.putSerializable(tag, map);
        this.putExtras(bundle);
    }

    public void setStrinng(String tag, String path) {
        this.putExtra(tag, path);
    }

    public void setSomeList(String tag, ArrayList<String> title) {
        this.putStringArrayListExtra(tag, title);
    }

}
