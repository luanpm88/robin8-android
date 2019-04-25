package com.robin8.rb.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robin8.rb.ui.adapter.ViewPagerAdapter;

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    private ViewPagerAdapter.SelectItem data;
    protected long lastTime;

    /**
     * 获取Activity的引用
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();// 得到的就是MainUI
    }

    public boolean isDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime < 800) {
            return true;
        }
        lastTime = currentTimeMillis;
        return false;
    }

    /**
     * 返回具体的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 子类初始化数据
     */
    public void initData() {

    }

    /**
     * @return 让子类实现 创建自己的view
     */
    public abstract View initView();

    public String getName() {
        return null;
    }

    public abstract void setData(ViewPagerAdapter.SelectItem data, String url, String pageName);

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void setAnalysisResultModel(Object obj) {
    }
}
