package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.ui.model.LaunchRewordModel;
import com.robin8.rb.ui.module.first.activity.ModifyRewordActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.task.CampaignTask;
import com.robin8.rb.ui.widget.AlignTextView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.StringUtil;

import java.util.List;

/**
 * 我发布的活动
 * 详情--拒绝
 */
public class MyLaunchCampaignRejectActivity extends BaseActivity {

    private ImageView mImageView;
    private TextView mActivityTitleTv;//活动标题
    private TextView mActivityTimeTv;//活动时间
    private TextView mBrandInfoTv;//活动简介
    private TextView mCountWayTv;//点击 | ¥0.5
    private TextView mTotalConsumeTv;//活动总预算
    private TextView mTitleTv;
    private ImageView mBackIv;
    private TextView mRightIv;
    private WProgressDialog mWProgressDialog;
    private int mID;
    private LaunchRewordModel mLaunchRewordModel;
    private LaunchRewordModel.Campaign mCampaign;
    private AlignTextView mAlignTextView;
    private CampaignTask mCampaignTask;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.launch_reword));
    }

    @Override
    public void initView() {
        mLLTitleBar.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign_detail_reject, mLLContent, true);
        mImageView = (ImageView) view.findViewById(R.id.iv);
        mTitleTv = (TextView) view.findViewById(R.id.tv_title);
        mActivityTitleTv = (TextView) view.findViewById(R.id.tv_activity_title);
        mActivityTimeTv = (TextView) view.findViewById(R.id.tv_activity_time);
        mBrandInfoTv = (TextView) view.findViewById(R.id.tv_brand_info);
        mCountWayTv = (TextView) view.findViewById(R.id.tv_count_way);
        mTotalConsumeTv = (TextView) view.findViewById(R.id.tv_total_consume);

        View titlebarDetailContent = findViewById(R.id.titlebar_detail_content);
        mBackIv = (ImageView) titlebarDetailContent.findViewById(R.id.iv_back);
        mRightIv = (TextView) titlebarDetailContent.findViewById(R.id.tv_right);
        mRightIv.setVisibility(View.VISIBLE);

        TextView tv_arrow = (TextView) view.findViewById(R.id.tv_arrow);
        IconFontHelper.setTextIconFont(tv_arrow, R.string.arrow_right);
        View rl_modify_campaign = view.findViewById(R.id.rl_modify_campaign);
        mAlignTextView = (AlignTextView) view.findViewById(R.id.tv_reason);
        rl_modify_campaign.setOnClickListener(this);
        mTitleTv.setText(this.getText(R.string.launch_reword));
        mBackIv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);
        initData();
        load();
    }


    private void initData() {
        Intent intent = getIntent();
        mID = intent.getIntExtra("id", 0);
    }

    /**
     * 加载网络
     */
    private void load() {
        mWProgressDialog = WProgressDialog.createDialog(this);
        mWProgressDialog.show();
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", mID);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CAMPAIGN_DETAIL_URL), requestParams, new RequestCallback() {

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
                mLaunchRewordModel = GsonTools.jsonToBean(response, LaunchRewordModel.class);
                if (mLaunchRewordModel != null && mLaunchRewordModel.getError() == 0) {
                    updateView();
                }
            }
        });
    }

    /**
     * 更新界面
     */
    private void updateView() {

        if (mLaunchRewordModel == null) {
            return;
        }
        mCampaign = mLaunchRewordModel.getCampaign();
        if (mCampaign == null || TextUtils.isEmpty(mCampaign.getStatus())) {
            return;
        }

        String path = mCampaign.getImg_url();
        String startTime = mCampaign.getStart_time();
        String endTime = mCampaign.getDeadline();
        String countWay = mCampaign.getPer_budget_type();
        String everyConsume = StringUtil.deleteZero(String.valueOf(mCampaign.getPer_action_budget()));
        String totalConsume = StringUtil.deleteZero(mCampaign.getBudget());

        mActivityTitleTv.setText(mCampaign.getName());
        mActivityTimeTv.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", startTime) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", endTime));
        mBrandInfoTv.setText(mCampaign.getDescription());
        mTotalConsumeTv.setText("¥ " + totalConsume);
        List<String> invalidReasons = mCampaign.getInvalid_reasons();

        if (invalidReasons != null && invalidReasons.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < invalidReasons.size(); i++) {
                if (i != invalidReasons.size() - 1) {
                    sb.append(invalidReasons.get(i)).append("\n");
                } else {
                    sb.append(invalidReasons.get(i));
                }
            }
            mAlignTextView.setText(sb.toString());
        }

        switch (countWay) {
            case "click":
                mCountWayTv.setText(getString(R.string.click) + " | ¥ " + everyConsume);
                break;
            case "post":
                mCountWayTv.setText(getString(R.string.post) + " | ¥ " + everyConsume);
                break;
            case "simple_cpi":
                mCountWayTv.setText(getString(R.string.download) + " | ¥ " + everyConsume);
                break;

            case "cpt":
                mCountWayTv.setText(getString(R.string.task) + " | ¥ " + everyConsume);
                break;
        }
        setImageView(path);
        mBottomTv.setVisibility(View.VISIBLE);
        mBottomTv.setBackgroundResource(R.color.red_custom);
        mBottomTv.setText(R.string.checked_rejected_repair);
        mBottomTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (BaseApplication.isDoubleClick()) {
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_modify_campaign:
            case R.id.tv_bottom:
                skipToLaunchReword();
                break;
            case R.id.tv_right:
                mCampaignTask = new CampaignTask(this);
                mCampaignTask.cancelCampaign(mCampaign, SPConstants.LAUNCH_REWORD_SEOND_ACTIVIRY, true);
                break;
        }
    }

    /**
     * 修改活动
     */
    private void skipToLaunchReword() {
        if (mCampaign == null) {
            return;
        }
        Intent intent = new Intent(this, ModifyRewordActivity.class);
        intent.putExtra("campaign", mCampaign);
        startActivityForResult(intent, SPConstants.MyLaunchCampaignRejectActivity);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SPConstants.LAUNCHREWORDACTIVIRY) {
            setResult(SPConstants.LAUNCH_REWORD_SEOND_ACTIVIRY);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

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

}
