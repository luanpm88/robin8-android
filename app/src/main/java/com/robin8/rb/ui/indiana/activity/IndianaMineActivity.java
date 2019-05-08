package com.robin8.rb.ui.indiana.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.ui.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.indiana.fragment.IndianaContentFragment;
import com.robin8.rb.ui.widget.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;


/**
 * 夺宝我的
 */
public class IndianaMineActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private ViewPagerAdapter mViewPagerAdapter;
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleList = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();

    String nameArr[];
    String campaignTypeArr[] = {"executing", "finished", "win"};

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.text_mine);
    }

    @Override
    public void initView() {
        mTVRight.setVisibility(View.VISIBLE);
        mTVRight.setText(getString(R.string.delivery_address));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign, mLLContent, true);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentList, this.getSupportFragmentManager());
        initData();
        initFragment();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INDIANA_ALL;
        super.onResume();
    }

    private void initData() {
        nameArr = getResources().getStringArray(R.array.indianaItems);
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
        for (ViewPagerAdapter.SelectItem selectItem : mTitleList) {
            IndianaContentFragment contentFragment = new IndianaContentFragment();
            contentFragment.setData(selectItem, null,null);
            mFragmentList.add(contentFragment);
        }
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabPageIndicator.setViewPager(mViewPager);//关联
        mViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
        Intent intent = new Intent(this, ReceiveAddressActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
