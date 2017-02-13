package com.robin8.rb.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.HttpPostKeyConstants;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Figo
 * @description http请求类
 * @date 2016-6-24
 */
public class HttpRequest {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final long mTimeout = 15000;
    public static HttpRequest mInstance;
    public static OkHttpClient mOkHttpClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    private HttpRequest() {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(mTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(mTimeout, TimeUnit.MILLISECONDS).readTimeout(mTimeout, TimeUnit.MILLISECONDS).build();
    }

    public static HttpRequest getInstance() {
        if (mInstance == null) {
            synchronized (HttpRequest.class) {
                if (mInstance == null) {
                    mInstance = new HttpRequest();
                }
            }
        }
        return mInstance;
    }

    public void get(boolean needHeader, String url) {
        get(needHeader, url, null, null);
    }

    public void get(boolean needHeader, String url, RequestParams params) {
        get(needHeader, url, params, null);
    }

    public void get(boolean needHeader, String url, RequestCallback callback) {
        get(needHeader, url, new RequestParams(), callback);
    }

    public void get(boolean needHeader, String url, RequestParams params, RequestCallback callback) {
        executeRequest(needHeader, Method.GET, url, params, callback);
    }

    public void post(boolean needHeader, String url) {
        post(needHeader, url, null, null);
    }

    public void post(boolean needHeader, String url, RequestParams params) {
        post(needHeader, url, params, null);
    }

    public void post(boolean needHeader, String url, RequestCallback callback) {
        post(needHeader, url, new RequestParams(), callback);
    }

    public void post(boolean needHeader, String url, RequestParams params, RequestCallback callback) {
        executeRequest(needHeader, Method.POST, url, params, callback);
    }

    private void executeRequest(boolean needHeader, Method method, String url, RequestParams params, final RequestCallback callback) {

        if (!NetworkUtil.isNetworkAvailable(BaseApplication.getContext())) {
            CustomToast.showShort(BaseApplication.getContext(), "网络加载失败");
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (params == null) {
            params = new RequestParams();
        }

        Request.Builder builder = new Request.Builder();
        switch (method) {
            case GET:
                url = getFullUrl(url, params.getParams());
                if (needHeader) {
                    builder.header(HttpPostKeyConstants.AUTHORIZATION, BaseApplication.getHeader());
                }
                builder.get();
                break;

            case POST:
                RequestBody postBody = params.getRequestBody();
                if (postBody != null) {
                    if (needHeader) {
                        builder.header(HttpPostKeyConstants.AUTHORIZATION, BaseApplication.getHeader());
                    }
                    builder.post(postBody);
                }
                break;

            case PUT:
                RequestBody putBody = params.getRequestBody();
                if (putBody != null) {
                    if (needHeader) {
                        builder.header(HttpPostKeyConstants.AUTHORIZATION, BaseApplication.getHeader());
                    }
                    builder.put(putBody);
                }
                break;

        }
        Request request = builder.url(url).build();
        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("result",result);
                        if (callback != null) {
                            callback.onResponse(result);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, final IOException exception) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            Log.e("result",exception+"");
                            callback.onError(exception);
                        }
                    }
                });
            }
        });
    }

    private void executeRequestPostImage(boolean needHeader, Method method, String url, String imageKey, String fileName, File fileValue, HashMap hashMap, final RequestCallback callback) {

        if (!NetworkUtil.isNetworkAvailable(BaseApplication.getContext())) {
            CustomToast.showShort(BaseApplication.getContext(), "网络加载失败");
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        MediaType mediaType;
        if (!TextUtils.isEmpty(fileName) && fileName.endsWith("png")) {
            mediaType = MEDIA_TYPE_PNG;
        } else {
            mediaType = MEDIA_TYPE_JPG;
        }

        Request.Builder builder = new Request.Builder();
        if (needHeader) {
            builder.addHeader(HttpPostKeyConstants.AUTHORIZATION, BaseApplication.getHeader());
        }

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (fileValue != null) {
            multipartBodyBuilder.addFormDataPart(imageKey, fileName, RequestBody.create(mediaType, fileValue));
        }

        if (url.contains("http://www.tba-kol.com/")){
            Log.e("gson","gson");
            Log.e("gson",GsonTools.mapToJson(hashMap));
        }
        if (hashMap != null && hashMap.size() > 0) {
            Set set = hashMap.keySet();
            for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                String key = (String) iter.next();
                String value = (String) hashMap.get(key);
                multipartBodyBuilder.addFormDataPart(key, value);
            }
        }
        MultipartBody body = multipartBodyBuilder.build();
        switch (method) {
            case PUT:
                builder.put(body);
                break;
            case POST:
                builder.post(body);
                break;
        }
        Request request = builder.url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.e("result",result);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onResponse(result);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, final IOException exception) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(exception);
                        }
                    }
                });
            }
        });
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */

    public Response getSynchrony(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }


    /**
     * @param url
     * @param mUrlParams
     * @return 返回完整的get请求url
     */
    public String getFullUrl(String url, ConcurrentHashMap<String, String> mUrlParams) {


        if (mUrlParams == null || mUrlParams.size() <= 0) {
            return url;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(url);
        if (sb.indexOf("?", 0) < 0 && mUrlParams.size() > 0) {
            sb.append("?");
        }
        for (Map.Entry<String, String> entry : mUrlParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(value);
            sb.append('&');
        }
        if (mUrlParams.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public void put(boolean needHeader, String url, RequestParams params, RequestCallback callback) {
        executeRequest(needHeader, Method.PUT, url, params, callback);
    }

    public void put(boolean needHeader, String url, String imageKey, String fileName, File fileValue, HashMap hashMap, RequestCallback callback) {
        executeRequestPostImage(needHeader, Method.PUT, url, imageKey, fileName, fileValue, hashMap, callback);
    }

    public void post(boolean needHeader, String url, String imageKey, String fileName, File fileValue, HashMap requestMap, RequestCallback callback) {
        executeRequestPostImage(needHeader, Method.POST, url, imageKey, fileName, fileValue, requestMap, callback);
    }

    enum Method {
        GET, POST, PUT
    }

    File file = null;
    public void downLoad(String url, final String name, final RequestCallback callback) {
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.logXXfigo("文件下载失败");
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                try {
                    is = response.body().byteStream();
                     file = new File(SDPath, name);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    LogUtil.logXXfigo("文件下载成功");
                } catch (Exception e) {
                    LogUtil.logXXfigo("文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }

                if (callback != null) {
                    if(file!=null){
                        callback.onResponse(file.getAbsolutePath());
                    }else {
                        callback.onResponse("");
                    }
                }
            }
        });
    }
}