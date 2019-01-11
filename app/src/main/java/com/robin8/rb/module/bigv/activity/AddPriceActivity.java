package com.robin8.rb.module.bigv;

import android.view.LayoutInflater;
import android.view.View;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;

public class AddPriceActivity extends BaseActivity {


    @Override
    public void setTitleView() {
        mLLTitleBar.setVisibility(View.GONE);
        mCardTitle.setVisibility(View.VISIBLE);
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
