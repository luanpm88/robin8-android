package com.robin8.rb.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * @description 网络工具辅助类
 * @author Figo
 * @date 2016-6-24
 */
public class NetworkUtil {
	public final static int NONE = 0;// 无网络
	public final static int WIFI = 1;// Wi-Fi
	public final static int MOBILE = 2;// 3G,GPRS
	
	/**
	 * 获取当前网络状态(wifi,3G)
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetworkState(Context context) {
		if(context==null){
    		return NONE;
    	}
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connManager == null||
				connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null||
				connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)==null){
			return NONE;
		}
		
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return MOBILE;
		}

		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return WIFI;
		}
		return NONE;

	}
	
	
	/**
     * 检测是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
    	if(context==null){
    		return false;
    	}
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo network : info) {
                    if (network.getState() == State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
