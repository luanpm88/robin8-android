package com.robin8.rb.ui.module.find.FloatAction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.robin8.rb.R;
import com.robin8.rb.ui.module.find.FloatAction.permission.FloatPermissionManager;
import com.robin8.rb.ui.module.find.FloatAction.util.DensityUtil;
import com.robin8.rb.ui.module.find.FloatAction.view.FloatLayout;

import java.util.ArrayList;
import java.util.List;


/**
 Des:悬浮窗统一管理，与悬浮窗交互的真正实现 */
public class FloatWindowManager {
    /**
     悬浮窗
     */
    private static FloatLayout mFloatLayout;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;
    private static ImageView imageView;

    /**
     创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     @param context 必须为应用程序的Context.
     */
    public static void createFloatWindow(final Context context) {
        FloatPermissionManager.getInstance().applyFloatWindow(context);
        wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);
        mFloatLayout = new FloatLayout(context);
        imageView = (ImageView) mFloatLayout.findViewById(R.id.img_red);
        imageView.setOnClickListener(new MyClick(context));
        if (Build.VERSION.SDK_INT>=26){
            wmParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        }else {
            if (Build.VERSION.SDK_INT >= 24) { /*android7.0不能用TYPE_TOAST*/
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else { /*以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限*/
                String packname = context.getPackageName();
                PackageManager pm = context.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
                if (permission) {
                    wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                } else {
                    wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                }
            }
        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.windowAnimations = R.style.AnimLeft;
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth;
        wmParams.y = screenHeight - DensityUtil.dip2px(context, (138));
        // wmParams.y = screenHeight;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mFloatLayout.setParams(wmParams);
        animation(imageView);
        // moves();
        runnable = new Runnable(){

            @Override
            public void run() {
                animation(imageView);
                handler.postDelayed(this,7000);
            }
        };
        handler.postDelayed(runnable,4000);
        try {
            windowManager.addView(mFloatLayout, wmParams);
        }catch (Exception e){
            e.printStackTrace();
        }


        mHasShown = true;
    }

    private static Handler handler = new Handler();
    private static Runnable runnable;

    private static void animation(ImageView imageView) {
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(imageView, "rotation", 2, 2, 2, - 10, 2);
        translationXAnim.setDuration(100);
        translationXAnim.setRepeatCount(10);
        translationXAnim.setRepeatMode(ValueAnimator.RESTART);
        translationXAnim.start();
        animators.add(translationXAnim);

        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(imageView, "rotation", - 2, - 2, - 2, 10, - 2);
        translationYAnim.setDuration(100);
        translationYAnim.setRepeatCount(10);
        translationYAnim.setRepeatMode(ValueAnimator.RESTART);
        translationYAnim.start();
        animators.add(translationYAnim);
        AnimatorSet btnSexAnimatorSet = new AnimatorSet();//
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(1);
        btnSexAnimatorSet.start();
    }

    /**
     移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mFloatLayout.isAttachedToWindow();
        }
        if (mHasShown && isAttach && mWindowManager != null)
            handler.removeCallbacks(runnable);
            mWindowManager.removeView(mFloatLayout);
    }

    /**
     返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }


    public static void hide() {
        if (mHasShown)
            handler.removeCallbacks(runnable);
            mWindowManager.removeViewImmediate(mFloatLayout);
        mHasShown = false;
    }


    public static void show() {
        if (! mHasShown)
            mWindowManager.addView(mFloatLayout, wmParams);
        mHasShown = true;
    }
}
