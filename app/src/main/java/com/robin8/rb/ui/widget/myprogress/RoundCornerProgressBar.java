/*

Copyright 2015 Akexorcist

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package com.robin8.rb.ui.widget.myprogress;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.robin8.rb.R;


/**
 * Created by Akexorcist on 9/14/15 AD.
 */
public class RoundCornerProgressBar extends BaseRoundCornerProgressBar {


    private boolean begin = false;


    public RoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView() {

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth, int radius, int padding, int colorProgress, boolean isReverse) {

            GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
            int newRadius = radius - (padding / 2);
            backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }
            float ratio = max / progress;
            int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
            ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
            progressParams.width = progressWidth;
            layoutProgress.setLayoutParams(progressParams);
        startAnimation(totalWidth, progressWidth, layoutProgress, isReverse,isBegin());

    }

    @Override
    protected void drawProgressSecond(LinearLayout layoutProgress, float max, float progress, float totalWidth, int radiusBg, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radiusBg - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {

    }

    private void startAnimation(final float totalWidth, final float baifenbi, final View v, boolean is,boolean begin) {
        TranslateAnimation mAnimation;
        if (is) {
            mAnimation = new TranslateAnimation(baifenbi, 0, 0, 0);
        } else {
            mAnimation = new TranslateAnimation(- baifenbi, 0, 0, 0);
        }
        mAnimation.setDuration(1200);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // animation.cancel();

                if (mOnAnimationEndListener != null) {
                    mOnAnimationEndListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mAnimation.setFillAfter(true);
        if (begin){
            v.startAnimation(mAnimation);
        }

    }

    public boolean isBegin() {
        return this.begin;
    }

    public void setBegin(boolean begin) {
        this.begin = begin;
        invalidateView();
    }

    private OnFinishedListener mOnFinishedListener;
    private OnAnimationEndListener mOnAnimationEndListener;

    /**
     set finish listener
     @param onFinishedListener
     */
    public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
        mOnFinishedListener = onFinishedListener;
    }

    /**
     set animation end listener
     @param onAnimationEndListener
     */
    public void setOnAnimationEndListener(OnAnimationEndListener onAnimationEndListener) {
        mOnAnimationEndListener = onAnimationEndListener;
    }

    public interface OnFinishedListener {
        void onFinish();
    }

    public interface OnAnimationEndListener {
        void onAnimationEnd();
    }
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }
}
