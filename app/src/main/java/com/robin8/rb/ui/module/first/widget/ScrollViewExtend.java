package com.robin8.rb.ui.module.first.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 能够兼容ViewPager的ScrollView
 @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
 @File: ScrollViewExtend.java
 @Package com.image.indicator.control
 @Author Hanyonglu
 @Date 2012-6-18 下午01:34:50
 @Version V1.0 */
public class ScrollViewExtend extends ScrollView {
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    public OnScrollChangeListener onScrollChangeListener;

    public View contentView;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    public ScrollViewExtend(Context context) {
        super(context);
    }

    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewExtend(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance > yDistance) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    public interface OnScrollChangeListener {
        void onScrollChange(ScrollViewExtend view, int x, int y, int oldx, int oldy);

        void onScrollBottomListener();

        void onScrollTopListener();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
    }
    /**
     l当前水平滚动的开始位置
     t当前的垂直滚动的开始位置
     oldl上一次水平滚动的位置。
     oldt上一次垂直滚动的位置。
     **/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
        }
//      {
//            onScrollChangeListener.onScrollBottomListener();
//        }
//        if (t == 0 || t + getHeight() > contentView.getHeight() && onScrollChangeListener != null) {
//            onScrollChangeListener.onScrollTopListener();
//        }
    }
}