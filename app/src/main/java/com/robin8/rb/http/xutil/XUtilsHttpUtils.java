package com.robin8.rb.http.xutil;


import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.model.Pair;
import com.robin8.rb.util.CommonUtil;
import com.robin8.rb.util.LogUtil;

import java.io.File;
import java.util.Iterator;
import java.util.Map;


/**
 * @author DLJ
 * @Description xutil 网络请求工具类
 * @date ${date} ${time}
 * ${tags}
 */
public class XUtilsHttpUtils implements IHttpUtils {
    private static XUtilsHttpUtils instance;
    private HttpUtils httpUtils;

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    private XUtilParamHelper defaultParamHelper;
    public boolean isDebug;//当前是否是调试模式


    private XUtilsHttpUtils() {
        init();
    }

    public static XUtilsHttpUtils getInstance() {
        if (instance == null) {
            synchronized (XUtilsHttpUtils.class) {
                if (instance == null)
                    instance = new XUtilsHttpUtils();
            }
        }
        return instance;
    }

    public void init() {
        httpUtils = new HttpUtils();
        //设置默认请求的缓存时间
        httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
        httpUtils.configRequestThreadPoolSize(10);//线程池数量
        httpUtils.configTimeout(10 * 1000);//连接服务器超时时间
        httpUtils.configSoTimeout(5 * 1000);//从服务器拿取数据超时时间
        httpUtils.configHttpCacheSize(3);
        httpUtils.configResponseTextCharset("utf-8");
        defaultParamHelper = new XUtilParamHelper();
        isDebug = CommonUtil.isDebug(BaseApplication.getContext());
        System.out.println("================================" + String.valueOf(isDebug));
    }

    @Override
    public void post(Map<String, Object> params, IHttpCallBack<String> callBack) {
        IParamHelper.HttpParamBean paramBean = defaultParamHelper.getParamBean(params);
        if (paramBean != null) {
            paramBean.callback = callBack;
            httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, paramBean
                    .url, (RequestParams) paramBean
                    .params, getCallBack
                    (params, callBack));
        }
    }

    @Override
    public void get(Map<String, Object> params, IHttpCallBack<String> callBack) {
        IParamHelper.HttpParamBean paramBean = defaultParamHelper.getParamBean(params);
        if (paramBean != null) {
            paramBean.callback = callBack;
            httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, paramBean
                    .url, (RequestParams) paramBean
                    .params, getCallBack
                    (params, callBack));
        }
    }

    public void put(Map<String, Object> params, IHttpCallBack<String> callBack) {
        IParamHelper.HttpParamBean paramBean = defaultParamHelper.getParamBean(params);
        if (paramBean != null) {
            paramBean.callback = callBack;
            httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.PUT, paramBean
                    .url, (RequestParams) paramBean.params, getCallBack(params, callBack));
        }
    }

    @Override
    public void download(String targetPath, Map<String, Object> params, boolean autoResume, boolean
            autoRename, IHttpCallBack<File> callBack) {
        IParamHelper.HttpParamBean paramBean = defaultParamHelper.getParamBean(params);
        if (paramBean != null) {
            httpUtils.download(paramBean.url, targetPath, autoResume, autoRename, getCallBack
                    (params,callBack));
        }
    }


    @Override
    public <T> RequestCallBack<T> getCallBack(final Map<String, Object> params, final
    IHttpCallBack<T>
            callback) {
        RequestCallBack requestCallBack = new RequestCallBack<T>() {
            @Override
            public void onSuccess(ResponseInfo<T> responseInfo) {
                String result = null;
                if (responseInfo.result instanceof String) {
                    result = (String) responseInfo.result;

                } else if (responseInfo.result instanceof File) {
                    result = ((File) (responseInfo.result)).getAbsolutePath();
                }
                printLog(params, result);
                IHttpCallBack.ResponceBean responceBean = new IHttpCallBack.ResponceBean();
                responceBean.pair = Pair.create(callback.tag, result);
                try {
                    callback.onComplate(responceBean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String msg) {
                printLog(params, e.getExceptionCode() + ":" + msg);
                IHttpCallBack.ResponceBean responceBean = new IHttpCallBack.ResponceBean();
                responceBean.errorCode = e.getExceptionCode();
                responceBean.pair = Pair.create(callback.tag, msg);
                try {
                    callback.onFailure(responceBean);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

        };
        return requestCallBack;
    }

    public void printLog(Map<String, Object> params, String result) {
        if (isDebug) {
            StringBuffer sb = new StringBuffer("");
            sb.append("请求网络时候所传递的参数++++++++++++++++++++++++++++++++++开始\n");
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String && TextUtils.isEmpty((CharSequence) value))
                    continue;
                if (value != null) {
                    if (key.equals("url")) {
                        sb.append(value).append("\n");
                    } else {
                        if (value instanceof File)
                            sb.append("参数 " + key + ":" + ((File) ((File) value)
                                    .getAbsoluteFile())).append("\n");
                        else
                            sb.append("参数 " + key + ":" + value).append("\n");
                    }
                }
            }
            sb.append("网络返回的结果++++++++++++++++++++++++++++++++++开始\n").append(result != null ?
                    result : "null").append
                    ("\n").append("网络返回的结果++++++++++++++++++++++++++++++++++++++结束\n");
            LogUtil.logXXfigo(sb.toString());
        }
    }


}
