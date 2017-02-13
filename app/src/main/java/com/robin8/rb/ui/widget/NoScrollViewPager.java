package com.robin8.rb.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	private int downX;
	private int downY;
	private int movex;
	private int movey;
	private int upx;
	private int upy;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	/**
	 * 覆盖ViewPager的onTouchEvent，去除默认的滑动行为
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

	// 是否拦截孩子的事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// return false 不拦截孩子的事件
		return false;
	}

}
