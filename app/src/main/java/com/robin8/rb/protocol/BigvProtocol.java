package com.robin8.rb.protocol;

import android.text.TextUtils;

import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;

/**
 * Created by Figo on 2016/6/30.
 */
public class BigvProtocol {
    /**
     * 获取活动列表
     */
    public void getBigvTasks(String url,String status,int currentPage, RequestCallback requestCallback) {

//        status: all(全部)
//        passed(进行中)
//        ended(已结束)
//        finished(已完成)
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(status)){
            requestParams.put("status", status);
        }
        requestParams.put("page", currentPage);
        HttpRequest.getInstance().get(true, url, requestParams, requestCallback);
    }
}
