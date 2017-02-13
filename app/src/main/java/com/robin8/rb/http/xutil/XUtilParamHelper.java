package com.robin8.rb.http.xutil;

import com.lidroid.xutils.http.RequestParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author DLJ
 * @Description xutil 网络请求 参数转化帮助类
 * @date ${date} ${time}
 * ${tags}
 */
public class XUtilParamHelper extends DefaultParamHelper<RequestParams> {
    @Override
    public void addHeader(RequestParams requestParams, String key, String value) {
        requestParams.addHeader(key, value);

    }

    @Override
    public void addStringParams(RequestParams requestParams, String key, String value) {
        requestParams.addBodyParameter(key, value);
    }

    @Override
    public void addJSonParams(RequestParams requestParams, String key, String value) {
        requestParams.addBodyParameter(key, value);
    }

    @Override
    public void addFileParams(RequestParams requestParams, String key, File file, String
            mimeType) {
        requestParams.addBodyParameter(key, file, mimeType);
    }

    @Override
    public void addStreamParams(RequestParams requestParams, String key, File file
    ) {
        try {
            requestParams.addBodyParameter(key, new FileInputStream(file), file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addArrayParams(RequestParams requestParams, String key, String value) {
        String[] split = value.split(",");
        for (int i = 0; i < split.length; i++) {
//            requestParams.addQueryStringParameter(key, split[i]);
            requestParams.addBodyParameter(key, split[i]);
        }
    }

    @Override
    public RequestParams genetrateRequest() {
        return new RequestParams();
    }


}
