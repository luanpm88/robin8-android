package com.robin8.rb.module.first.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.first.prenster.LaunchRewordSecondPresenter;
import com.robin8.rb.ui.widget.SwitchView;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.view.ILaunchRewordSecondView;

import java.util.Observable;

import static com.robin8.rb.R.id.tv_right;

/**
 * 发布活动
 * 待付款／审核中
 * 等待支付页面
 */
public class LaunchRewordSecondActivity extends BaseActivity implements ILaunchRewordSecondView {

    private LaunchRewordSecondPresenter mLaunchRewordSecondPresenter;
    private ImageView mImageView;
    private TextView mActivityTitleTv;//活动标题
    private TextView mActivityTimeTv;//活动时间
    private TextView mBrandInfoTv;//活动简介
    private TextView mCountWayTv;//点击 | ¥0.5
    private TextView mTotalConsumeTv;//活动总预算
    private TextView mAccountIncomeTv;//¥1232（余额：¥1232）
    private TextView mCountTv;//总计
    private TextView mPayInstantlyTv;//立即支付
    private TextView mTitleTv;
    private SwitchView mSwitchView;
    private SwitchView mSwitchCredit;
    private TextView mCreditIncomeTv;

    private ImageView mBackIv;
    private TextView mRightIv;
    private int from;

    @Override
    public void setTitleView() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", -1);
        mTVCenter.setText(this.getText(R.string.launch_reword));
        mLLTitleBar.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_launch_reword_second, mLLContent, true);
        mImageView = (ImageView) view.findViewById(R.id.iv);
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
        mActivityTitleTv = (TextView) view.findViewById(R.id.tv_activity_title);
        mActivityTimeTv = (TextView) view.findViewById(R.id.tv_activity_time);
        mBrandInfoTv = (TextView) view.findViewById(R.id.tv_brand_info);
        mCountWayTv = (TextView) view.findViewById(R.id.tv_count_way);
        mTotalConsumeTv = (TextView) view.findViewById(R.id.tv_total_consume);
        mAccountIncomeTv = (TextView) view.findViewById(R.id.tv_account_income);
        mCreditIncomeTv = (TextView) view.findViewById(R.id.tv_credit_income);
        mCountTv = (TextView) view.findViewById(R.id.tv_count);
        mPayInstantlyTv = (TextView) view.findViewById(R.id.tv_pay_instantly);
        mTitleTv.setText(this.getText(R.string.launch_reword));
        ((LinearLayout) view.findViewById(R.id.ll_share)).setVisibility(View.GONE);
        mSwitchView = (SwitchView) findViewById(R.id.view_switch);
        mSwitchCredit = (SwitchView) findViewById(R.id.switch_credit);
        View titlebarDetailContent = findViewById(R.id.titlebar_detail_content);
        mBackIv = (ImageView) titlebarDetailContent.findViewById(R.id.iv_back);
        mRightIv = (TextView) titlebarDetailContent.findViewById(tv_right);

        if(from == SPConstants.MY_LAUNCH_REWORD_ACTIVITY){
            View rlModifyCampaign = view.findViewById(R.id.rl_modify_campaign);
            TextView mArrowIv = (TextView) view.findViewById(R.id.tv_arrow);
            IconFontHelper.setTextIconFont(mArrowIv, R.string.arrow_right);
            View lineTop = view.findViewById(R.id.line_top);
            rlModifyCampaign.setVisibility(View.VISIBLE);
            lineTop.setVisibility(View.VISIBLE);
            rlModifyCampaign.setOnClickListener(this);
        }

        mPayInstantlyTv.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);

        NotifyManager.getNotifyManager().addObserver(this);
        mLaunchRewordSecondPresenter = new LaunchRewordSecondPresenter(this, this);
        mLaunchRewordSecondPresenter.init();

        mSwitchCredit.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {

            @Override
            public void toggleToOn(View view) {
                // 执行一些耗时的业务逻辑操作 implement some time-consuming logic operation
                mLaunchRewordSecondPresenter.setUseKolAmount(true);
            }

            @Override
            public void toggleToOff(View view) {
                // 原本为打开的状态，被点击后 originally present the status of open after clicking
                mLaunchRewordSecondPresenter.setUseKolAmount(false);
            }
        });

    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);

        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (NotifyManager.TYPE_PAY_SUCCESSFUL == msgEntity.getCode()) {
                this.finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLaunchRewordSecondPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_pay_instantly:
              //  mLaunchRewordSecondPresenter.skipToOrder();
                mLaunchRewordSecondPresenter.newSkipToOrder();
                break;
            case R.id.iv_back:
                finish();
                break;
            case tv_right:
                mLaunchRewordSecondPresenter.cancelCampaign();
                break;
            case R.id.rl_modify_campaign:
                //去修改活动
                mLaunchRewordSecondPresenter.skipToLaunchReword();
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
    public void setImageView(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if (path.startsWith("http://")) {
            BitmapUtil.loadImage(this.getApplicationContext(), path, mImageView);
        } else {
            BitmapUtil.loadLocalImage(this.getApplicationContext(), path, mImageView);
        }

    }

    @Override
    protected void onDestroy() {
        NotifyManager.getNotifyManager().deleteObserver(this);
        if (mLaunchRewordSecondPresenter != null) {
            mLaunchRewordSecondPresenter.onDestroy();
            mLaunchRewordSecondPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void setActivityTitleTv(String title) {
        mActivityTitleTv.setText(title);
    }

    @Override
    public void setActivityTimeTv(String text) {
        mActivityTimeTv.setText(text);
    }

    @Override
    public void setBrandInfoTv(String text) {
        mBrandInfoTv.setText(text);
    }

    @Override
    public void setCountWayTv(String text) {
        mCountWayTv.setText(text);
    }

    @Override
    public void setTotalConsumeTv(String text) {
        mTotalConsumeTv.setText(text);
    }

    @Override
    public void setAccountIncomeTv(String text) {
        mAccountIncomeTv.setText(text);
    }

    @Override
    public void setCreditIncomeTv(String text) {
        mCreditIncomeTv.setText(text);
    }

    @Override
    public void setCountTv(String title) {
        mCountTv.setText("合计: ¥ " + title);
    }

    @Override
    public TextView getPayInstantlyTv() {
        return mPayInstantlyTv;
    }

    @Override
    public SwitchView getViewSwitch() {
        return mSwitchCredit;
    }

    @Override
    public void setViewSwitch(boolean flag) {
        mSwitchCredit.toggleSwitch(flag);
    }

    @Override
    public TextView getRightTv() {
        return mRightIv;
    }
}
