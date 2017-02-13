package com.robin8.rb.http.xutil;

import java.io.File;
import java.util.Map;

/**
 * @author DLJ
 * @Description 网络请求 参数转化帮助类
 * @date ${date} ${time}
 * ${tags}
 */
public interface IParamHelper<T> {
    public HttpParamBean<T> getParamBean(Map<String, Object> params);

    public void addHeader(T t, String key, String value);

    public void addStringParams(T t, String key, String value);

    public void addJSonParams(T t, String key, String value);

    public void addFileParams(T t, String key, File file, String mimeType);

    public void addStreamParams(T t, String key, File file);

    public void addArrayParams(T t, String key, String value);

    public T genetrateRequest();

    /**
     * 通用参数封装bean
     *
     * @param <T> 各个网络请求框架所需的特殊参数类
     */
    public static class HttpParamBean<T> {
        public String url;//请求地址
        //        public String tag;
        public T params;//主要存放参数
        public Object otherObject;//用于拓展 存放任意对象 自己进行强制转换
        public IHttpCallBack callback;//回调
        public String jsonBody;
    }
}
