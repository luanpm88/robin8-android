package com.robin8.rb.module.bigv;

import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;

public class BigvCampaignDetailActivity extends BaseActivity {

    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
        mCardTitle.setVisibility(View.VISIBLE);
        mTitle.setText("详情");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_bigv_campaign_detail, mLLContent, true);

    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
