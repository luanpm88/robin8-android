package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.adapter.ViewPagerAdapter;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseFragment;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.first.fragment.AnalysisFragment;
import com.robin8.rb.module.first.fragment.LaunchFragment;
import com.robin8.rb.module.first.model.AnalysisResultModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.ui.widget.viewpagerindicator.TabPageIndicator;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import java.util.ArrayList;

/**
 * 发起悬赏活动分析 页面
 */
public class LaunchRewordActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabPageIndicator mTabPageIndicator;
    private ViewPagerAdapter mViewPagerAdapter;
    String nameArr[] = {"智能发布", "文章分析"};
    private ArrayList<ViewPagerAdapter.SelectItem> mTitleList = new ArrayList<>();
    private ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;

    @Override
    public void setTitleView() {
        mTVCenter.setText(R.string.launch_reword);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign, mLLContent, true);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabPageIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentList, this.getSupportFragmentManager());
        getData();
        initData();
        initFragment();
    }

    private void getData() {

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String expectEffect = intent.getStringExtra("expect_effect");

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mWProgressDialog = WProgressDialog.createDialog(this);
        mWProgressDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("url", url);
        requestParams.put("expect_effect", expectEffect);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.CONTENT_ANALYSIS_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                AnalysisResultModel analysisResultModel = GsonTools.jsonToBean(response, AnalysisResultModel.class);
                if (analysisResultModel != null && analysisResultModel.getError() == 0) {
                    updateData(analysisResultModel);
                } else if (analysisResultModel != null) {
                    CustomToast.showShort(LaunchRewordActivity.this, analysisResultModel.getDetail());
                }
            }
        });
    }

    private void updateData(AnalysisResultModel analysisResultModel) {
        if (mFragmentList == null || mFragmentList.size() <= 0) {
            return;
        }
        for (int i = 0; i < mFragmentList.size(); i++) {
            mFragmentList.get(i).setAnalysisResultModel(analysisResultModel);
        }
    }

    private void initFragment() {
        mTabPageIndicator.setUserChannelList(mTitleList);
        mFragmentList.clear();
        for (int i = 0; i < mTitleList.size(); i++) {
            BaseFragment fragment = null;
            ViewPagerAdapter.SelectItem selectItem = mTitleList.get(i);
            switch (i) {
                case 0:
                    fragment = new LaunchFragment();
                    break;
                case 1:
                    fragment = new AnalysisFragment();
                    break;
            }
            fragment.setData(selectItem, null, null);
            mFragmentList.add(fragment);
        }
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabPageIndicator.setViewPager(mViewPager);//关联
        mViewPagerAdapter.notifyDataSetChanged();
    }

    private void initData() {
        for (int i = 0; i < nameArr.length; i++) {
            ViewPagerAdapter.SelectItem selectItem = new ViewPagerAdapter.SelectItem();
            selectItem.name = nameArr[i];
            mTitleList.add(selectItem);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragmentList != null && mFragmentList.size() > 0) {
            mFragmentList.get(0).onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_submit:
                break;
        }
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
    }


}
