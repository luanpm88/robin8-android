package com.robin8.rb.http.xutil;


import com.robin8.rb.model.Pair;

/**
 * @author DLJ
 * @Description 网络请求的通用回调
 * @date ${date} ${time}
 * ${tags}
 */
public abstract class IHttpCallBack<T> {
    /**
     * 回调的标志
     */
    public String tag;

    public IHttpCallBack(String tag) {
        this.tag = tag;
    }

    public abstract void onLoading(ResponceBean responceBean);

    public abstract void onComplate(ResponceBean responceBean);

    public abstract void onFailure(ResponceBean responceBean);

    /**
     * 封装了网络返回信息
     */
    public static class ResponceBean {
        public Pair<String, String> pair;//key为tag,value为返回的字符串
        public int errorCode;//错误码
    }
}
