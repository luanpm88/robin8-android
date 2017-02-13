package com.robin8.rb.okhttp;


/**
 * @description 网络接口回调接口
 * @author Figo
 * @date 2016-6-24
 */
public interface RequestCallback {
	
	   public abstract void onError(Exception e);
	   
	   public abstract void onResponse(String response);
}
