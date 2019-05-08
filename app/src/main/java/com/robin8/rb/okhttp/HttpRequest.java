package com.robin8.rb.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.HttpPostKeyConstants;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
 @author Figo
 @description http请求类
 @date 2016-6-24 */
public class HttpRequest {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final MediaType MEDIA_TYPE_MY = MediaType.parse("image/jpeg");
    public static final int GET = 0;
    public static final int POST = 1;
    public static final int PUT = 2;
    public static final long mTimeout = 15000;
    public static HttpRequest mInstance;
    public static OkHttpClient mOkHttpClient;
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    private HttpRequest() {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(mTimeout, TimeUnit.MILLISECONDS).writeTimeout(mTimeout, TimeUnit.MILLISECONDS).readTimeout(mTimeout, TimeUnit.MILLISECONDS).build();
    }

    public static HttpRequest getInstance() {
        if (mInstance == null) {
            synchronized(HttpRequest.class) {
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

        if (! NetworkUtil.isNetworkAvailable(BaseApplication.getContext())) {
            CustomToast.showShort(BaseApplication.getContext(), R.string.robin391);
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
                    } else {
                        if (url.equals(CommonConfig.RONG_CLOUD_URL)) {
                            List<String> yun = BaseApplication.getHeaderRongYun();
                            builder.addHeader("App-Key", yun.get(0));
                            builder.addHeader("Nonce", yun.get(1));
                            builder.addHeader("Signature", yun.get(2));
                            builder.addHeader("Content-Type", yun.get(3));
                            builder.addHeader("Timestamp", yun.get(4));
                        }
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
                        //  Log.e("result",result);
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
                            Log.e("result", exception + "");
                            callback.onError(exception);
                        }
                    }
                });
            }
        });
    }

    /**
     多图上传的请求
     @param needHeader
     @param method
     @param url
     @param paramsName 参数名字
     @param mapImages 图片顺序+路径
     @param callback
     */
    private void executeRequestImg(boolean needHeader, Method method, String url, String paramsName, Map<Integer, String> mapImages, final RequestCallback callback) {
        if (! NetworkUtil.isNetworkAvailable(BaseApplication.getContext())) {
            CustomToast.showShort(BaseApplication.getContext(), R.string.robin391);
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mapImages == null) {
            return;
        }
        MediaType mediaType = null;
        if (mapImages.size() != 0) {
            for (int i = 0; i < mapImages.size(); i++) {
                String fileName = mapImages.get(i).substring(mapImages.get(i).lastIndexOf("/") + 1);
                if (! TextUtils.isEmpty(fileName) && fileName.endsWith("png")) {
                    mediaType = MEDIA_TYPE_PNG;
                } else {
                    mediaType = MEDIA_TYPE_JPG;
                }
            }
        }
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < mapImages.size(); i++) {
            if (mapImages.get(i).startsWith("http") || mapImages.get(i).startsWith("https")) {
                multipartBodyBuilder.addFormDataPart(String.valueOf(paramsName + i), mapImages.get(i));
            } else {
                File f = new File(mapImages.get(i));
                if (f != null) {
                    multipartBodyBuilder.addFormDataPart(String.valueOf(paramsName + i), f.getName(), RequestBody.create(mediaType, f));
                }
            }
        }
        //        for (int i = 0; i < mapImages.size(); i++) {
        //            File f = new File(mapImages.get(i));
        //            if (f != null) {
        //                multipartBodyBuilder.addFormDataPart(String.valueOf(paramsName + i), f.getName(), RequestBody.create(mediaType, f));
        //            }
        //        }
        MultipartBody body = multipartBodyBuilder.build();
        Request.Builder builder = new Request.Builder();
        if (needHeader) {
            builder.addHeader(HttpPostKeyConstants.AUTHORIZATION, BaseApplication.getHeader());
        }
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
                LogUtil.LogShitou("result_more_images", result);
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

    private void executeRequestPostImage(boolean needHeader, Method method, String url, String imageKey, String fileName, File fileValue, HashMap hashMap, final RequestCallback callback) {

        if (! NetworkUtil.isNetworkAvailable(BaseApplication.getContext())) {
            CustomToast.showShort(BaseApplication.getContext(), R.string.robin391);
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        MediaType mediaType;
        if (! TextUtils.isEmpty(fileName) && fileName.endsWith("png")) {
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

        if (url.contains("http://www.tba-kol.com/")) {
            Log.e("gson", "gson");
            Log.e("gson", GsonTools.mapToJson(hashMap));
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
                Log.e("result", result);
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
     同步的Get请求
     @param url
     @return Response
     */

    public Response getSynchrony(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }


    /**
     @param url
     @param mUrlParams
     @return 返回完整的get请求url
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

    /**
     多图上传
     @param needHeader
     @param url
     @param paramsName
     @param mapImages
     @param callback
     */
    public void put(boolean needHeader, String url, String paramsName, Map<Integer, String> mapImages, RequestCallback callback) {
        executeRequestImg(needHeader, Method.PUT, url, paramsName, mapImages, callback);
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
                    while ((len = is.read(buf)) != - 1) {
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
                    if (file != null) {
                        callback.onResponse(file.getAbsolutePath());
                    } else {
                        callback.onResponse("");
                    }
                }
            }
        });
    }

    private static String sha1(String data) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(data.getBytes());
            byte[] bits = md.digest();
            for (int i = 0; i < bits.length; i++) {
                int a = bits[i];
                if (a < 0)
                    a += 256;
                if (a < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        } catch (Exception e) {

        }
        return buf.toString();
    }
}
