package com.robin8.rb.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.robin8.rb.util.UIUtils;

public class TagItemViewPager extends ViewPager {
    private float xPosition;// 手指触摸点的x轴坐标
    private final int MINIMUM_DISTANCE = 10;// 手指移动的最小距离
    private int distance;// 根据屏幕密度计算出来的，手指移动的最小距离

    public TagItemViewPager(Context context) {
        super(context);
        distance = (int)UIUtils.dip2px(MINIMUM_DISTANCE);
    }

    public TagItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        distance = (int)UIUtils.dip2px(MINIMUM_DISTANCE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 先保存手指按下的x轴的坐标
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            xPosition = ev.getX();
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            /*
             计算手指移动时的坐标跟按下的坐标之间的绝对值，如果超过给定的值，
             就认为viewpager需要滚动。通过调节distance的大小，可以改变滑动
             灵敏度
              */
            if (Math.abs(ev.getX() - xPosition) < distance)
                return false;
            else// 意思就是：touch事件已经被PeopleViewPager自己消费了，不会传递到子控件
                return true;
        }
        // 其他情况，依旧保持默认的处理方法
        return super.onInterceptTouchEvent(ev);
    }
}