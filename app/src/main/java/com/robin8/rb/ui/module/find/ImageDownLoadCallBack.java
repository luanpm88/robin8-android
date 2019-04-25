package com.robin8.rb.ui.module.find;

import android.graphics.Bitmap;

import java.io.File;

/**
 Created by zc on 2018/4/27. */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
