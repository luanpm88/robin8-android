package com.robin8.rb.okhttp;

import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * @description 请求参数包装类
 * @author Figo
 * @date 2016-6-24
 */
public class RequestParams {
	
        protected final ConcurrentHashMap<String, String> mUrlParams = new ConcurrentHashMap<String, String>();

	    public RequestParams() {
	    	 
	    }
	    
	    public void put(String key, String value) {
	        if (key != null && value != null) {
                if("is_vip".equals(key) || "is_yellow_vip".equals(key) || "verified".equals(key)){
                   value = "0";
                }
	        	mUrlParams.put(key, value);
	        }
	    }

	public void put(String key, Object object) {
		if (key != null) {
			mUrlParams.put(key, String.valueOf(object));
		}
	}

	    public void put(String key, int value) {
	        if (key != null) {
	        	mUrlParams.put(key, String.valueOf(value));
	        }
	    }

	    public void put(String key, long value) {
	        if (key != null) {
	        	mUrlParams.put(key, String.valueOf(value));
	        }
	    }
	    
	    public ConcurrentHashMap<String, String> getParams(){
	    	return mUrlParams;
	    }
	    
	    public RequestBody getRequestBody() {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> entry : mUrlParams.entrySet()) {
            	  String key = entry.getKey();
                  String value = entry.getValue();
                  builder.add(key, value);
            }
            RequestBody body = builder.build();
	        return body;
	    }    
}
