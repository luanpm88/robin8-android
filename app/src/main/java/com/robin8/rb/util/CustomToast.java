package com.robin8.rb.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 */
public class CustomToast {

    public static boolean isShow = true;
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    @SuppressLint("ShowToast")
    public static void showShort(Context context, String message) {
        if(toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else {
            toast.setText(message);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (isShow){
            Toast.makeText(context, message, duration).show();
        }
    }

}
