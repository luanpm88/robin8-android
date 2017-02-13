package com.robin8.rb.module.share;

import android.content.Context;

import com.robin8.rb.R;
import com.robin8.rb.util.CustomToast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * 第三方分享结果处理类: toast显示分享结果
 * 
 * @author weichao
 */
public class CustomPlatformActionListener implements PlatformActionListener {

	private Context mContext;

	public CustomPlatformActionListener(Context context) {
		this.mContext = context;
	}


	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		CustomToast.showShort(mContext,mContext.getString(R.string.share_success));
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
		CustomToast.showShort(mContext,mContext.getString(R.string.share_error));
	}

	@Override
	public void onCancel(Platform platform, int i) {
		CustomToast.showShort(mContext,mContext.getString(R.string.share_cancel));
	}
}
