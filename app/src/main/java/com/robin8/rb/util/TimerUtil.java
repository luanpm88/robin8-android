package com.robin8.rb.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;

/**
 计时器 用于验证码时间显示 */
public class TimerUtil implements Runnable {
    private Handler mHandler = new Handler();
    private int i = 6;
    private Button view;
    private Context context;
    private String s = "获取验证码";
    private String add = "s后重新获取";//改变状态后的后缀
    private TextView v2;
    private LinearLayout layout;
    private int colorbefor;
    private int colorafter;


    public TimerUtil(int i, Button v, TextView v2, Context context, String s, String add) {
        this.i = i;
        this.view = v;
        this.context = context;
        this.s = s;
        this.v2 = v2;
        this.add = add;
    }

    public TimerUtil(int i, Button v, TextView v2, LinearLayout layout, Context context, String s, String add, int colorbefor,int colorafter) {
        this.i = i;
        this.view = v;
        this.layout = layout;
        this.context = context;
        this.s = s;
        this.v2 = v2;
        this.add = add;
        this.colorbefor = colorbefor;
        this.colorafter = colorafter;
    }

    public TimerUtil(int i, Button v, TextView v2, LinearLayout layout, Context context, String s, String add) {
        this.i = i;
        this.view = v;
        this.layout = layout;
        this.context = context;
        this.s = s;
        this.v2 = v2;
        this.add = add;
    }

    @Override
    public void run() {
        while (i > 0) {
            i--;
            mHandler.post(new Runnable() {//通过它在UI主线程中修改显示的剩余时间

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    if (view != null) {
                        if (add == null) {
                            view.setText(i + "s后重新获取");
                        } else {
                            view.setText(i + add);
                        }
                        view.setClickable(false);
                        if (!(colorbefor+"").equals("")){
                            view.setTextColor(colorbefor);
                        }
                    }

                    if (v2 != null) {
                        if (add == null) {
                            v2.setText(i + "s后重新获取");
                        } else {
                            v2.setText(i + add);
                        }
                        v2.setClickable(false);
                        if (!(colorbefor+"").equals("")){
                            v2.setTextColor(colorbefor);
                        }
                    }
                    if (layout != null) {
                        layout.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_bg_bottom_black));
                    }

                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mHandler.post(new Runnable() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                try {
                    if (view != null) {
                        view.setText(s);
                        view.setClickable(true);
                        if (!(colorafter+"").equals("")){
                            view.setTextColor(colorafter);
                        }
                    }
                    if (v2 != null) {
                        v2.setText(s);
                        v2.setClickable(true);
                        if (!(colorafter+"").equals("")){
                            v2.setTextColor(colorafter);
                        }
                    }
                    if (layout != null) {
                        layout.setBackground(context.getResources().getDrawable(R.drawable.shape_corner_bg_bottom));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}