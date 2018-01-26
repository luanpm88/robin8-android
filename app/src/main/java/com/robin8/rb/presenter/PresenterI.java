package com.robin8.rb.presenter;

import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;

/**
 * Activity事件处理接口 PresenterI
 *
 * @author Figo
 */
public interface PresenterI {

    void init();

    /**
     * 网络请求
     */
    void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback);
}
