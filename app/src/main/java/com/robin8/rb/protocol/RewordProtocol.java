package com.robin8.rb.protocol;

import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.LogUtil;

/**
 * Created by Figo on 2016/6/30.
 */
public class RewordProtocol {
    /**
     * 获取活动列表
     */
    public void getRewordTasks(String url,String filterStr,int currentPage,String withAnnouncements, RequestCallback requestCallback) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("status", filterStr);
        requestParams.put("page", currentPage);
        requestParams.put("title", "");
        requestParams.put("with_message_stat", null);
        requestParams.put("with_announcements", "y");
        LogUtil.LogShitou("活动参数","status"+filterStr+"\n"+"page"+currentPage+"\n"+"with_message_stat"+"y"+"\n"+"with_announcements"+withAnnouncements);
        HttpRequest.getInstance().get(true, url, requestParams, requestCallback);
    }
}
