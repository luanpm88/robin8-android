/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.robin8.rb.module.share.thirdplatfom;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class AccessTokenKeeper {
	private static final String PREFERENCES_NAME_SINA = "com_weibo_sdk_android";
	private static final String PREFERENCES_NAME_QQ = "com_qq_sdk_android";
	private static final String PREFERENCES_NAME_WX = "com_wx_sdk_android";

	private static final String KEY_UID = "uid";
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_EXPIRES_IN = "expires_in";
	private static final String KEY_REFRESH_TOKEN = "refresh_token";

	/**
	 * 保存 Token 对象到 SharedPreferences。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * @param token
	 *            Token 对象
	 */
	public static void writeAccessToken(Context context, int platform, Oauth2AccessToken token) {
		if (null == context || null == token) {
			return;
		}
		String preferences_name = PREFERENCES_NAME_SINA;
		switch (platform) {
		case Constants.PLATFORM_SINA:
			preferences_name = PREFERENCES_NAME_SINA;
			break;
//		case Constants.PLATFORM_QQ:
//			preferences_name = PREFERENCES_NAME_QQ;
//			break;
//		case Constants.PLATFORM_WX:
//			preferences_name = PREFERENCES_NAME_WX;
//			break;
		}
		SharedPreferences pref = context.getSharedPreferences(preferences_name, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(KEY_UID, token.getUid());
		editor.putString(KEY_ACCESS_TOKEN, token.getToken());
		editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
		editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
		editor.commit();
	}

	/**
	 * 从 SharedPreferences 读取 Token 信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 * 
	 * @return 返回 Token 对象
	 */
	public static Oauth2AccessToken readAccessToken(Context context, int platform) {
		if (null == context) {
			return null;
		}
		String preferences_name = PREFERENCES_NAME_SINA;
		switch (platform) {
		case Constants.PLATFORM_SINA:
			preferences_name = PREFERENCES_NAME_SINA;
			break;
//		case Constants.PLATFORM_QQ:
//			preferences_name = PREFERENCES_NAME_QQ;
//			break;
//		case Constants.PLATFORM_WX:
//			preferences_name = PREFERENCES_NAME_WX;
//			break;
		}
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(preferences_name, Context.MODE_APPEND);
		token.setUid(pref.getString(KEY_UID, ""));
		Log.e("tag", "atoken====>" + pref.getString(KEY_ACCESS_TOKEN, ""));
		token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
		token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));
		token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));

		return token;
	}

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 * 
	 * @param context
	 *            应用程序上下文环境
	 */
	public static void clear(Context context, int platform) {
		if (null == context) {
			return;
		}
		String preferences_name = PREFERENCES_NAME_SINA;
		switch (platform) {
		case 1:
			preferences_name = PREFERENCES_NAME_SINA;
		}
		SharedPreferences pref = context.getSharedPreferences(preferences_name, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
}
