package com.robin8.rb.presenter;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.http.xutil.IHttpCallBack;
import com.robin8.rb.http.xutil.XUtilsHttpUtils;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Activity事件处理接口 PresenterI
 *
 * @author Figo
 */
public class BasePresenter implements PresenterI {

    @Override
    public void init() {
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {

        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    public void getDataFromServer(boolean needHeader, int method, String url, String imageKey, String fileName, File file, RequestCallback callback) {

        switch (method) {
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, imageKey, fileName, file, null, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, imageKey, fileName, file, null, callback);
                break;
        }
    }




    public void postImage(boolean addHeader, int method,Map<String, Object> requestMap, IHttpCallBack callBack) {
        if (checkParam(requestMap)) {
            if (addHeader){
                requestMap.put("[header]Authorization", BaseApplication.getHeader());
            }
            switch (method){
                case HttpRequest.POST:
                    XUtilsHttpUtils.getInstance().post(requestMap, callBack);
                    break;
                case HttpRequest.PUT:
                    XUtilsHttpUtils.getInstance().put(requestMap, callBack);
                    break;
            }
        }
    }

    /**
     * 检查参数
     *
     * @param requestMap
     * @return
     */
    private boolean checkParam(Map<String, Object> requestMap) {
        if (requestMap != null && requestMap.containsKey("[url]")) {
            return true;
        } else {
            try {
                throw new RuntimeException("the targetUrl of httpRequest is null!!!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void getDataFromServer(boolean needHeader, int method, String url, String img, String imageName, File file, LinkedHashMap<String, Object> requestMap, RequestCallback callback) {

        switch (method) {
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, img, imageName, file, requestMap, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, img, imageName, file, requestMap, callback);
                break;
        }
    }
}
