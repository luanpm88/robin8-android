package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.module.mine.fragmennt.CampaignContentFragment;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.viewpagerindicator.TabPageIndicator;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import java.util.ArrayList;

public class MyCampaignHistoryActivity extends BaseActivity {
    private ImageView ivBack;
    private TextView tvLeftChose;
    private TextView tvRightChose;
    private TabPageIndicator indicatorBigV;
    private TabPageIndicator indicator;
    private ViewPager pager;
    private ViewPager pagerBigV;
    private BasePresenter mBasePresenter;
    private Handler mHandler;
    private ViewPagerAdapter mViewPagerAdapterBigV;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleList = new ArrayList<>();
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleListBigV = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentListBigV = new ArrayList<>();

    private String nameArr[] = {"审核中", "审核通过", "审核拒绝"};
    private String nameArrBigV[] = {"待合作", "合作中", "已完成","被拒绝"};
    private String campaignTypeArr[] = {"pending", "passed", "rejected"};
    private String campaignTypeArrTender[] = {"pending", "valid","finished", "rejected"};

    private int mCurrentPosition;
    private String mUrl = HelpTools.getUrl(CommonConfig.MY_CAMPAIGNS);
    private String mUrlBigV;

    private int kolId;
    protected LinearLayout llOld;
    protected LinearLayout llNew;

    @Override
    public void setTitleView() {
        getData();
        mLLTitleBar.setVisibility(View.GONE);
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        mPageName = extras.getString("page_name");
        // mUrl = extras.getString("url");
        kolId = extras.getInt("kolId", 0);
        mUrlBigV = HelpTools.getUrl(CommonConfig.BIGV_LIST_CAMPAIGN_HISTORY_URL + kolId + "/tenders");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign_history, mLLContent, true);
        ivBack = (ImageView) view.findViewById(R.id.img_back);
        tvLeftChose = (TextView) view.findViewById(R.id.tv_left_chose);
        tvRightChose = (TextView) view.findViewById(R.id.tv_right_chose);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pagerBigV = (ViewPager) view.findViewById(R.id.pager_bigv);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        indicatorBigV = (TabPageIndicator) view.findViewById(R.id.indicator_bigv);
        llOld = ((LinearLayout) view.findViewById(R.id.ll_old));
        llNew = ((LinearLayout) view.findViewById(R.id.ll_new));

        ivBack.setOnClickListener(this);
        tvLeftChose.setOnClickListener(this);
        tvRightChose.setOnClickListener(this);
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentList, this.getSupportFragmentManager());
        mViewPagerAdapterBigV = new ViewPagerAdapter(mFragmentListBigV, this.getSupportFragmentManager());
        initData(campaignTypeArr);
        initDataBigV(campaignTypeArrTender);
        initFragment();
        initFragmentBigV();
    }

    private void initData(String[] arr) {
        for (int i = 0; i < nameArr.length; i++) {
            ViewPagerAdapter.SelectItem selectItem = new ViewPagerAdapter.SelectItem();
            selectItem.name = nameArr[i];
            selectItem.campaignType = arr[i];
            mTitleList.add(selectItem);
        }
    }

    private void initDataBigV(String[] arr) {
        for (int i = 0; i < nameArrBigV.length; i++) {
            ViewPagerAdapter.SelectItem selectItem = new ViewPagerAdapter.SelectItem();
            selectItem.name = nameArrBigV[i];
            selectItem.campaignType = arr[i];
            mTitleListBigV.add(selectItem);
        }
    }

    private void initFragment() {
        indicator.setUserChannelList(mTitleList);
        mFragmentList.clear();
        for (ViewPagerAdapter.SelectItem selectItem : mTitleList) {
            BaseFragment fragment = new CampaignContentFragment();
            fragment.setData(selectItem, mUrl, StatisticsAgency.MY_TASK);
            mFragmentList.add(fragment);
        }
        pager.setAdapter(mViewPagerAdapter);
        pager.setOffscreenPageLimit(3);
        indicator.setViewPager(pager);//关联
        indicator.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPagerAdapter.notifyDataSetChanged();
    }

    private void initFragmentBigV() {
        indicatorBigV.setUserChannelList(mTitleListBigV);
        mFragmentListBigV.clear();
        for (ViewPagerAdapter.SelectItem selectItem : mTitleListBigV) {
            BaseFragment fragment = new CampaignContentFragment();
            fragment.setData(selectItem, mUrlBigV, StatisticsAgency.MY_TASK);
            mFragmentListBigV.add(fragment);
        }
        pagerBigV.setAdapter(mViewPagerAdapterBigV);
        pagerBigV.setOffscreenPageLimit(4);
        indicatorBigV.setViewPager(pagerBigV);//关联
        indicatorBigV.setOnPageChangeListener(new MyOnPageChangeListener());
        mViewPagerAdapterBigV.notifyDataSetChanged();
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


    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_left_chose:
                LogUtil.LogShitou("为什么", "点击时间");
                tvLeftChose.setTextColor(getColor(R.color.white_custom));
                tvLeftChose.setBackgroundResource(R.drawable.shape_bg_blue_left);
                tvRightChose.setBackgroundResource(R.drawable.shape_bg_white_right);
                tvRightChose.setTextColor(getColor(R.color.blue_custom));
                llOld.setVisibility(View.VISIBLE);
                llNew.setVisibility(View.GONE);
                break;
            case R.id.tv_right_chose:
                tvRightChose.setBackgroundResource(R.drawable.shape_bg_blue_right);
                tvRightChose.setTextColor(getColor(R.color.white_custom));
                tvLeftChose.setBackgroundResource(R.drawable.shape_bg_white_left);
                tvLeftChose.setTextColor(getColor(R.color.blue_custom));
                llOld.setVisibility(View.GONE);
                llNew.setVisibility(View.VISIBLE);
                break;
        }
    }
}
