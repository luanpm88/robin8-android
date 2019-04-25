package com.robin8.rb.ui.widget;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.robin8.rb.ui.dialog.CustomDialog;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * @author DLJ
 * @Description 滑动返回的dialog
 * @date 2016/2/27 13:53
 */
public class SwipeBackDialog extends CustomDialog {


    public SwipeBackDialog(Context mContext, int layoutId) {
        super(mContext, layoutId);
        init();
    }

    public SwipeBackDialog(Context mContext, View view) {
        super(mContext, view);
        init();
    }

    private void init() {
        WindowManager.LayoutParams attributes = dg.getWindow().getAttributes();
        attributes.dimAmount = 0.25f;
        dg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
    }

    @Override
    protected View getContentView(View view) {
        SwipeBackLayout swipeBackLayout = new SwipeBackLayout(mContext, null);
        swipeBackLayout.addView(view);
        swipeBackLayout.setContentView(view);
        swipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                if(scrollPercent>1){
                    dg.dismiss();
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
            }

            @Override
            public void onScrollOverThreshold() {
                Log.e("xxfigo","onScrollOverThreshold");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dg.dismiss();
                            }
                        });
                    }
                }).start();

            }
        });
        return swipeBackLayout;
    }
}
