package com.robin8.rb.ui.module.reword;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.robin8.rb.util.DensityUtils;

/**
 * Created by IBM on 2016/8/18.
 */
public class CorrectionRunnable implements Runnable {

    private final Context context;
    private View[] views;

    public CorrectionRunnable(Context context,View... views) {
        this.context = context;
        this.views = views;
    }

    @Override
    public void run() {
        if (views == null || views.length <= 0) {
            return;
        }
        for (View view : views) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = DensityUtils.getScreenWidth(context) * 9/16;
            view.setLayoutParams(lp);
        }
    }
}
