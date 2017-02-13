package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.callback.IFooterCallBack;

import com.robin8.rb.R;

public class RefreshFooterView extends LinearLayout implements IFooterCallBack {

    private static final String TAG = "RefreshFooterView";
    private View mFooter;
    private int mFooterHeight;
    private ImageView mProgressIv;

    public RefreshFooterView(Context context) {
        super(context);
        initFooterView(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView(context);
    }

    public RefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFooterView(context);
    }

    private  AnimationDrawable mAnimation;

    private void initAnimation(){
        mAnimation = new AnimationDrawable();
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_01),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_02),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_03),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_04),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_05),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_06),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_07),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_08),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_09),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_10),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_11),50);
        mAnimation.addFrame(getContext().getResources().getDrawable(R.mipmap.loading_12),50);
    }


    public void initFooterView(Context context) {
        initAnimation();
        mFooter = LayoutInflater.from(context).inflate(R.layout.refrech_footer, null);
        mProgressIv = (ImageView) mFooter.findViewById(R.id.iv_progress);
        mProgressIv.setBackgroundDrawable(mAnimation);
        if (mAnimation != null && !mAnimation.isRunning()) {
            mAnimation.start();
        }
        mFooter.measure(0, 0);
        mFooterHeight = mFooter.getMeasuredHeight();
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        addView(mFooter,lp);
    }

    @Override
    public void callWhenNotAutoLoadMore(XRefreshView.XRefreshViewListener xRefreshViewListener) {
    }

    @Override
    public void onStateReady() {
        Log.e(TAG, "onStateReady");
    }

    @Override
    public void onStateRefreshing() {
        Log.e(TAG, "onStateRefreshing");
    }

    @Override
    public void onStateFinish(boolean hidefooter) {
        Log.e(TAG, "onStateFinish");
    }

    @Override
    public void onStateComplete() {
        Log.e(TAG, "onStateComplete");
    }

    @Override
    public void show(boolean show) {
        Log.e(TAG, "show");
    }

    @Override
    public boolean isShowing() {
        Log.e(TAG, "isShowing");
        return false;
    }

    @Override
    public int getFooterHeight() {
        return mFooterHeight;
    }
}