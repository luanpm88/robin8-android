package com.robin8.rb.http.xutil;

import java.io.File;
import java.util.Map;

/**
 * @author DLJ
 * @Description 网络请求工具类接口
 * @date ${date} ${time}
 * ${tags}
 */
public interface IHttpUtils {


    void post(Map<String, Object> params, IHttpCallBack<String> callback);

    void get(Map<String, Object> params, IHttpCallBack<String> callback);

    /**
     * @param targetPath 下载到本地路径
     * @param params     参数
     * @param autoResume 是否自动恢复下载
     * @param autoRename 是否自动命名
     * @param callback   回调
     */
    void download(String targetPath, Map<String, Object> params, boolean autoResume, boolean autoRename,
                  IHttpCallBack<File> callback);

    /**
     * 提供由公共回调到自身框架特殊回调的转化
     *
     * @param callback 公共回调
     * @return 返回自身框架所需的特殊回调接口
     */
    <T> Object getCallBack(Map<String, Object> params, IHttpCallBack<T> callback);

}
