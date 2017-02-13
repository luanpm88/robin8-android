package com.robin8.rb.module.create.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.create.fragment.CreateContentFragment;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;


/**
 * 我的创作列表
 */
public class FragmentsActivity extends BaseActivity {

    private BasePresenter mBasePresenter;
    private Handler mHandler;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleList = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();

    String nameArr[];
    String campaignTypeArr[];

    private int mCurrentPosition;
    private String tvCenterStr;
    private String mUrl;

    @Override
    public void setTitleView() {
        getData();
        mTVCenter.setText(tvCenterStr);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        nameArr = extras.getStringArray("name");
        campaignTypeArr = extras.getStringArray("type");
        tvCenterStr = extras.getString("title_name");
        mPageName = extras.getString("page_name");
        mUrl = extras.getString("url");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign, mLLContent, true);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentList, this.getSupportFragmentManager());
        initData();
        initFragment();
    }

    private void initData() {
        for (int i = 0; i < nameArr.length; i++) {
            ViewPagerAdapter.SelectItem selectItem = new ViewPagerAdapter.SelectItem();
            selectItem.name = nameArr[i];
            selectItem.campaignType = campaignTypeArr[i];
            mTitleList.add(selectItem);
        }
    }

    private void initFragment() {

        mTabPageIndicator.setUserChannelList(mTitleList);
        mFragmentList.clear();
        for (ViewPagerAdapter.SelectItem selectItem :  mTitleList) {
            BaseFragment fragment = null;
            switch (mPageName){
                case StatisticsAgency.MY_CREATE:
                    fragment = new CreateContentFragment();
                    fragment.setData(selectItem,mUrl,mPageName);
                    break;
                case StatisticsAgency.MY_TASK:
                    fragment = new CreateContentFragment();
                    fragment.setData(selectItem,mUrl,mPageName);
                    break;
            }
            mFragmentList.add(fragment);
        }
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabPageIndicator.setViewPager(mViewPager);//关联
        mTabPageIndicator.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPagerAdapter.notifyDataSetChanged();
    }



    @Override
    protected void executeOnclickLeftView() {
        finish();
    }


    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mBasePresenter == null) {
            mBasePresenter = null;
        }
    }




    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
