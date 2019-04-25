package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.fragment.ContentFragment;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 我发布的活动
 */
public class MyCampaignActivity extends BaseActivity {

    private BasePresenter mBasePresenter;
    private Handler mHandler;
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleList = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();

    String nameArr[] = {"待付款", "审核中", "进行中", "已完成"};
    String campaignTypeArr[] = {"unpay", "checking", "running", "completed"};
    private int mCurrentPosition;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.my_launch_activity));
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

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_TASK;
        super.onResume();
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
            ContentFragment contentFragment = new ContentFragment();
            contentFragment.setData(selectItem, null,null);
            mFragmentList.add(contentFragment);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == SPConstants.LAUNCH_REWORD_SEOND_ACTIVIRY){
            if(mFragmentList!=null && mCurrentPosition>=0 && mCurrentPosition<mFragmentList.size()){
                ContentFragment contentFragment = (ContentFragment) mFragmentList.get(mCurrentPosition);
                contentFragment.setCurrentState(ContentFragment.DRAG_REFRESH);
                contentFragment.getDataFromNet(ContentFragment.DRAG_REFRESH);
            }
        }else if(requestCode == ContentFragment.EVALUATE_ACTIVITY){
            ContentFragment contentFragment = (ContentFragment) mFragmentList.get(mCurrentPosition);
            contentFragment.changeEvaluateData(requestCode,resultCode,data);
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
