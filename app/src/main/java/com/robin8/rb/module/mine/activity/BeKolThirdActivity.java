package com.robin8.rb.module.mine.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.util.DensityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 成为KOL
 */
public class BeKolThirdActivity extends BaseActivity {

    @Bind(R.id.view_header)
    View viewHeader;
    @Bind(R.id.tv_hook)
    TextView tvHook;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.be_kol));
    }

    @Override
    public void initView() {
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setText(getString(R.string.confirm));
        View view = LayoutInflater.from(this).inflate(R.layout.activity_be_kol_third, mLLContent, true);
        ButterKnife.bind(this);

        viewHeader.post(new Runnable() {
            @Override
            public void run() {
                viewHeader.getLayoutParams().height = DensityUtils.getScreenWidth(viewHeader.getContext()) * 58 / 700;
            }
        });
        viewHeader.setBackgroundResource(R.mipmap.pic_kol_step_2);
        IconFontHelper.setTextIconFont(tvHook, R.string.hook_sign);
        mBottomTv.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.KOL_APPLY_SUBMIT;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
