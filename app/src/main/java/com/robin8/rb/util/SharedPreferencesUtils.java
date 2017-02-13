package com.robin8.rb.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.SPConstants;

public class SharedPreferencesUtils {

	private SharedPreferences sp;
	private static SharedPreferencesUtils mInstance;

	private SharedPreferencesUtils() {
		sp = BaseApplication.getContext().getSharedPreferences(SPConstants.SP_SIGN, Context.MODE_PRIVATE);
	}

	public static SharedPreferencesUtils getInstance(){
		if(mInstance == null){
			mInstance = new SharedPreferencesUtils();
		}
		return mInstance;
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public boolean getBoolean(String key, Boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	public void putFloat(String key, float value) {
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public void putBoolean(String key, Boolean value) {
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void putInt(String key, int value) {
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void putString(String key, String value) {
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
