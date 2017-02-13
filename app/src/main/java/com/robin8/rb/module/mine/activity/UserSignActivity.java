package com.robin8.rb.module.mine.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.mine.presenter.UserSignPresenter;
import com.robin8.rb.module.mine.view.IUserSignView;
import com.robin8.rb.ui.widget.OtherGridView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.view.widget.CircleImageView;


/**
 * 签到页面
 */
public class UserSignActivity extends BaseActivity implements IUserSignView {

    private UserSignPresenter mUserSignPresenter;
    private TextView mBottomTv;
    private OtherGridView mMonthGv;
    private TextView mHasSignedDaysTv;
    private CircleImageView mCircleImageView;
    private TextView mMonthTv;
    private TextView mUserNameTv;


    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.sign));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_sign, mLLContent, true);

        mCircleImageView = (CircleImageView) view.findViewById(R.id.civ_image);
        mUserNameTv = (TextView) view.findViewById(R.id.tv_user_name);
        mHasSignedDaysTv = (TextView) view.findViewById(R.id.tv_has_signed_days);
        mMonthTv = (TextView) view.findViewById(R.id.tv_month);
        mMonthGv = (OtherGridView) view.findViewById(R.id.gv_month);
        mBottomTv = (TextView) view.findViewById(R.id.tv_bottom);

        mBottomTv.setOnClickListener(this);
        mUserSignPresenter = new UserSignPresenter(this, this);
        mUserSignPresenter.init();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_SIGN;
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_bottom:
                mUserSignPresenter.sign();
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
    public void setCircleImageView(String url) {
        BitmapUtil.loadImage(this, url, mCircleImageView);
    }

    @Override
    public void setHasSignedDaysTv(String text) {
        mHasSignedDaysTv.setText(getString(R.string.you_have_sign) + text + getString(R.string.day));
    }

    @Override
    public void setUserNameTv(String text) {
        mUserNameTv.setText(text);
        mUserNameTv.setVisibility(View.GONE);
    }

    @Override
    public void setMonthTv(String text) {
        mMonthTv.setText(text + getString(R.string.month_sign_chart));
    }

    @Override
    public OtherGridView getMonthGv() {
        return mMonthGv;
    }

    @Override
    public TextView getBottomTv() {
        return mBottomTv;
    }
}
