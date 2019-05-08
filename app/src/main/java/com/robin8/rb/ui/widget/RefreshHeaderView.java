package com.robin8.rb.ui.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.robin8.rb.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RefreshHeaderView extends LinearLayout implements IHeaderCallBack {

    private final String TAG = "RefreshHeaderView";
    private View mHeader;
    private int mHeaderHeight;
    private ImageView mRefreshArr;//箭头
    private ImageView mIVProgress;//刷新进度条
    private TextView mRefreshState;//刷新状态
    private TextView mRefreshUpdatetime;//刷新时间

    private static final int STATE_IDLE = 0;
    private static final int PULL_DOWN_REFRESH = 1;// 下拉刷新状态
    private static final int RELEASE_REFRESH = 2;// 松开刷新状态
    private static final int REFRESHING = 3;// 正在刷新状态
    private static final int REFREFINISH = 4;// 正在刷新状态
    private static int CURRENT_STATE = STATE_IDLE;// 当前下拉刷新的状态
    private RotateAnimation up;
    private RotateAnimation down;
    private long mRefreshTime;
    private AnimationDrawable mAnimation;

    public RefreshHeaderView(Context context) {
        super(context);
        initHeaderView(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView(context);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
    }


    public void initHeaderView(Context context) {
        initAnimation();
        mHeader = LayoutInflater.from(context).inflate(R.layout.refresh_header, null);
        mRefreshArr = (ImageView) mHeader.findViewById(R.id.iv_refresh_arr);
        mIVProgress = (ImageView) mHeader.findViewById(R.id.iv_progress);
        mRefreshState = (TextView) mHeader.findViewById(R.id.tv_refresh_state);
        mRefreshUpdatetime = (TextView) mHeader.findViewById(R.id.tv_refresh_updatetime);
        mHeader.measure(0, 0);
        mHeaderHeight = mHeader.getMeasuredHeight();
        addView(mHeader);
    }

    @Override
    public void onStateNormal() {
        Log.e(TAG, "onStateNormal");
    }

    @Override
    public void onStateReady() {
        Log.e(TAG, "onStateReady");
        updateRefreshState(RELEASE_REFRESH);
    }

    @Override
    public void onStateRefreshing() {
        Log.e(TAG, "onStateRefreshing");
        updateRefreshState(REFRESHING);
    }

    @Override
    public void onStateFinish() {
        Log.e(TAG, "onStateFinish");
        updateRefreshState(REFREFINISH);
    }

    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {
        Log.e(TAG, "setRefreshTime");
        mRefreshTime = lastRefreshTime;
        updateRefreshState(PULL_DOWN_REFRESH);
    }

    @Override
    public void hide() {
        Log.e(TAG, "hide");
    }

    @Override
    public void show() {
        Log.e(TAG, "show");
        updateRefreshState(PULL_DOWN_REFRESH);
    }

    @Override
    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    private void initAnimation() {
        up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        up.setDuration(500);
        up.setFillAfter(true);

        down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        down.setDuration(500);
        down.setFillAfter(true);

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
        mAnimation.setOneShot(false);
    }

    private void updateRefreshState(int state) {
        switch (state) {
            case PULL_DOWN_REFRESH:
                mRefreshArr.setVisibility(View.VISIBLE);
                mIVProgress.setVisibility(View.INVISIBLE);
                mRefreshState.setText(R.string.robin296);
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
                mRefreshUpdatetime.setText(getContext().getString(R.string.robin298,dateFormat.format(new Date(mRefreshTime))));
                mRefreshArr.startAnimation(down);
                break;
            case RELEASE_REFRESH:
                mRefreshState.setText(R.string.robin297);
                mRefreshArr.startAnimation(up);
                break;
            case REFRESHING:
                mRefreshArr.clearAnimation();
                mRefreshState.setText(R.string.robin296);
                mRefreshArr.setVisibility(View.INVISIBLE);
                mIVProgress.setVisibility(View.VISIBLE);
                mIVProgress.setBackgroundDrawable(mAnimation);
                if (mAnimation != null && !mAnimation.isRunning()) {
                    mAnimation.start();
                }
                break;

            case REFREFINISH:
                mRefreshArr.clearAnimation();
                mRefreshArr.setVisibility(View.INVISIBLE);
                mIVProgress.setVisibility(View.INVISIBLE);
                break;
        }
    }
}