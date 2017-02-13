package com.robin8.rb.common;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author DLJ
 * @Description 公共adapter 继承baseadapter 使用方法 继承 该类 重写convert
 * @date 2015年6月17日 下午12:36:38
 */
public abstract class CommonViewpagerAdapter extends PagerAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<View> mDatas;

    // protected final int mItemLayoutId;

    public CommonViewpagerAdapter(Context context, List<View> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        // this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mDatas.get(position));// 删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
        container.addView(mDatas.get(position), 0);// 添加页卡
        return mDatas.get(position);

    }

    @Override
    public int getCount() {
        return mDatas.size();// 返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void setPrimaryItem(View container, int position, Object object) {

        return;

    }

}
