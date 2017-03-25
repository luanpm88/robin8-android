package com.robin8.rb.pager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.robin8.rb.base.BasePager;

/**
 * Created by seven on 25/03/2017.
 */

public class NotificationPager extends BasePager {

    public NotificationPager(FragmentActivity activity, ViewPager viewPager) {
        super(activity);
        this.mActivity = activity;
    }

    @Override
    public View initView() {

        return null;
    }
}
