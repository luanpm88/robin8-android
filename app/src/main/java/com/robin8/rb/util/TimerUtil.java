package com.robin8.rb.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

/**
 * 计时器 用于验证码时间显示
 */
public class TimerUtil implements Runnable {
    private Handler mHandler = new Handler();
    private int i = 6;
    private Button view;
    private Context context;
    private String s = "获取验证码";
    private TextView v2;

    public TimerUtil(int i, Button v, TextView v2, Context context, String s) {
        this.i = i;
        this.view = v;
        this.context = context;
        this.s = s;
        this.v2 = v2;
    }

    @Override
    public void run() {
        while (i > 0) {
            i--;
            mHandler.post(new Runnable() {//通过它在UI主线程中修改显示的剩余时间
                @Override
                public void run() {
                    if (view != null) {
                        view.setText(i + "s后重新获取");
                        view.setClickable(false);
                    }

                    if (v2 != null) {
                        v2.setText(i + "s后重新获取");
                        v2.setClickable(false);
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
            @Override
            public void run() {
                try {
                    if (view != null) {
                        view.setText(s);
                        view.setClickable(true);
                    }
                    if (v2 != null) {
                        v2.setText(s);
                        v2.setClickable(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}