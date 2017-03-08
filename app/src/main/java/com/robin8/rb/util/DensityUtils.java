package com.robin8.rb.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.robin8.rb.base.BaseApplication;


/**
 * 常用单位转换的辅助类
 *
 * @author zhy
 */
public class DensityUtils {
    private int mStatusBarHeights;

    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpVal) {
        if (BaseApplication.getContext() == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return dp2px(dpVal);
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        if (BaseApplication.getContext() == null) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        if (BaseApplication.getContext() == null) {
            return 0;
        }
        final float scale = BaseApplication.getContext().getResources()
                .getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        if (BaseApplication.getContext() == null) {
            return 0;
        }
        return (pxVal / BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity);
    }

    public  void getScreenSize(Activity activity) {
        // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        mStatusBarHeights = rect.top;
    }

    public static int getRectHeight(Activity activity) {
        View view = activity.getWindow().getDecorView();
        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static int getStatusHeight(Activity activity) {
       /* // 获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();
        view.buildDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;*/
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
