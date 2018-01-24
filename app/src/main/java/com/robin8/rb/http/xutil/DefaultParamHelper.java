package com.robin8.rb.http.xutil;

import android.text.TextUtils;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

/**
 * @author DLJ
 * @Description 网络请求参数解析帮助类
 * @date 2016/1/27 10:06
 */
public class DefaultParamHelper<T> implements IParamHelper<T> {
    protected DefaultParamHelper fatherHelper;

    public DefaultParamHelper() {
        fatherHelper = this;
    }

    @Override
    public IParamHelper.HttpParamBean<T> getParamBean(Map<String, Object> params) {
        if (!checkParams(params))
            return null;
        HttpParamBean<T> httpParamBean = new HttpParamBean<T>();
        T requestParams = genetrateRequest();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String && TextUtils.isEmpty((CharSequence) value))
                continue;

            if (value != null) {
                if (key.equals("[url]")) {
                    httpParamBean.url = value.toString();
                } else {
                    //请求头
                    if (key.startsWith("[header]")) {
                        String substring = key.substring(8, key.length());
                        addHeader(requestParams, substring, value.toString());
                    } else if (key.startsWith("[file/")) {//传文件
                        int i = key.indexOf("]");
                        if (i > 0) {
                            String fileHeader = key.substring(0, i);
                            if (fileHeader.contains("/")) {
                                String[] split = fileHeader.split("/");
                                addFileParams(requestParams, key.substring(i + 1, key.length()), (File) value, split[1] + "/" + split[2]);
                            }
                        }
                    } else if (key.startsWith("[array]")) {//传普通字符串参数
                        String substring = key.substring(7, key.length());
                        addArrayParams(requestParams, substring, value.toString());
                    } else if (key.startsWith("[stream]")) {
                        String substring = key.substring(8, key.length());
                        addStreamParams(requestParams, substring, (File) value);
                    } else {
                        addStringParams(requestParams, key, value.toString());
                    }
                }
            }
        }
        httpParamBean.params = requestParams;
        return httpParamBean;
    }

    @Override
    public void addHeader(T t, String key, String value) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public void addStringParams(T t, String key, String value) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public void addJSonParams(T t, String key, String value) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public void addFileParams(T t, String key, File file, String mimeType) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public void addStreamParams(T t, String key, File file) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public void addArrayParams(T t, String key, String value) {
        throw new RuntimeException("please override this method");
    }

    @Override
    public T genetrateRequest() {
        throw new RuntimeException("please override this method");
    }

    /**
     * 检查参数
     *
     * @param params
     * @return
     */
    protected boolean checkParams(Map<String, Object> params) {
        return params.containsKey("[url]") || params.containsKey("url");
    }
}
