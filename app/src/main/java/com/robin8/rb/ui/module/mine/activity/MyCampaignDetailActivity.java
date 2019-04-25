package com.robin8.rb.ui.module.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.IconFontHelper;
import com.robin8.rb.ui.model.CampaignListBean;
import com.robin8.rb.ui.model.LaunchRewordModel;
import com.robin8.rb.ui.module.reword.activity.DetailContentActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.CoordinateView;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

/**
 * 我发布的活动
 * 详情--进行中、已完成
 */
public class MyCampaignDetailActivity extends BaseActivity {

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
    private LaunchRewordModel mLaunchRewordModel;
    private LaunchRewordModel.Campaign mCampaign;
    private TextView mNumberConsumedTv;
    private TextView mNumberPersonTv;
    private TextView mTotalClickTv;
    private TextView mCountClickTv;
    private CoordinateView mCoordinateView;
    private String[] clickNum = {"0", "0", "0"};//总点击数
    private String[] clickNum2 = {"0", "0", "0"};//计费点击数
    private int mID;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.launch_reword));
    }

    @Override
    public void initView() {
        mLLTitleBar.setVisibility(View.GONE);
        mViewLine.setVisibility(View.GONE);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_my_campaign_detail, mLLContent, true);
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
        mCoordinateView = (CoordinateView) findViewById(R.id.coordinateView);
        mRightIv.setVisibility(View.GONE);

        mTitleTv.setText(this.getText(R.string.launch_reword));
        mBackIv.setOnClickListener(this);
        mRightIv.setOnClickListener(this);
        initStaticView();
        initData();
        load();
    }

    private void initStaticView() {
        TextView tv_arrow = (TextView) findViewById(R.id.tv_arrow);
        TextView tv_arrow2 = (TextView) findViewById(R.id.tv_arrow2);
        IconFontHelper.setTextIconFont(tv_arrow, R.string.arrow_right);
        IconFontHelper.setTextIconFont(tv_arrow2, R.string.arrow_right);
        View rl_check_campaign = findViewById(R.id.rl_check_campaign);
        View rl_check_person = findViewById(R.id.rl_check_person);
        rl_check_campaign.setOnClickListener(this);
        rl_check_person.setOnClickListener(this);

        View layout_consumed = findViewById(R.id.layout_consumed);
        TextView tv_item_name_consumed = (TextView) layout_consumed.findViewById(R.id.tv_item_name);
        mNumberConsumedTv = (TextView) layout_consumed.findViewById(R.id.tv_item_number);
        tv_item_name_consumed.setText(R.string.has_consumed);

        View layout_person_number = findViewById(R.id.layout_person_number);
        TextView tv_item_name_person_number = (TextView) layout_person_number.findViewById(R.id.tv_item_name);
        mNumberPersonTv = (TextView) layout_person_number.findViewById(R.id.tv_item_number);
        tv_item_name_person_number.setText(R.string.person_number);

        View layout_click_number = findViewById(R.id.layout_click_number);
        TextView tv_item_name_click_number = (TextView) layout_click_number.findViewById(R.id.tv_item_name);
        mTotalClickTv = (TextView) layout_click_number.findViewById(R.id.tv_item_number);
        tv_item_name_click_number.setText(R.string.total_clicks);

        View layout_count_click = findViewById(R.id.layout_count_click);
        TextView tv_item_name_count_click = (TextView) layout_count_click.findViewById(R.id.tv_item_name);
        mCountClickTv = (TextView) layout_count_click.findViewById(R.id.tv_item_number);
        tv_item_name_count_click.setText(R.string.count_click);
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
                LogUtil.LogShitou("发布过的活动详情"+HelpTools.getUrl(CommonConfig.CAMPAIGN_DETAIL_URL),response);
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

        float takeBudget = mCampaign.getTake_budget();
        int shareTimes = mCampaign.getShare_times();
        int totalClick = mCampaign.getTotal_click();
        int availClick = mCampaign.getAvail_click();

        mNumberConsumedTv.setText("¥ " + StringUtil.deleteZero(takeBudget));
        mNumberPersonTv.setText(String.valueOf(shareTimes));
        mTotalClickTv.setText(String.valueOf(totalClick));
        mCountClickTv.setText(String.valueOf(availClick));

        mActivityTitleTv.setText(mCampaign.getName());
        mActivityTimeTv.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", startTime) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", endTime));
        mBrandInfoTv.setText(mCampaign.getDescription());
        mTotalConsumeTv.setText("¥ " + totalConsume);

        switch (countWay) {
            case "click":
                mCountWayTv.setText(getString(R.string.click) + " | ¥ " + everyConsume);
                break;
            case "post":
                mCountWayTv.setText(getString(R.string.post) + " | ¥ " + everyConsume);
                break;
        }
        setImageView(path);
        String[][] statsData = mCampaign.getStats_data();
        String[] arrX = null;
        if (statsData != null) {
            switch (statsData.length) {
                case 1:
                    arrX = statsData[0];
                    if(statsData[1]!=null && statsData[1].length>0){
                        clickNum[0] = clickNum[1] = clickNum[2] = statsData[1][0];
                    }
                    break;
                case 2:
                    arrX = statsData[0];
                    if(statsData[1]!=null ){
                        if(statsData[1].length>0){
                            clickNum[0] = statsData[1][0];
                        }
                        if(statsData[1].length>1){
                            clickNum[1]= clickNum[2]  = statsData[1][1];
                        }
                    }
                    break;
                case 3:
                    arrX = statsData[0];
                    clickNum = statsData[1];
                    clickNum2 = statsData[2];
                    break;
            }
        }

        if(arrX!=null && arrX.length>0){
            mCoordinateView.setArrX(arrX);
        }
        if(clickNum!=null && clickNum.length>0){
            mCoordinateView.setClickNum(clickNum);
        }
        if(clickNum2!=null && clickNum2.length>0){
            mCoordinateView.setClickNum2(clickNum2);
        }
        mCoordinateView.notifyDateSetChanged();
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
            case R.id.rl_check_campaign:
                skipToDetail();
                break;
            case R.id.rl_check_person:
                skipToPaticipateList();
                break;
        }
    }

    private void skipToPaticipateList() {
        if (mCampaign == null) {
            return;
        }
        Intent intent = new Intent(this, BaseRecyclerViewActivity.class);//参与人员列表
        intent.putExtra("destination", SPConstants.PARTICIPATE_CREW_LIST);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.JOINED_KOLS_URL));
        intent.putExtra("title", getString(R.string.participate_crew_list));
        intent.putExtra("id", mCampaign.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void skipToDetail() {
        if (mCampaign == null) {
            return;
        }
        Intent intent = new Intent(this, DetailContentActivity.class);
        Bundle bundle = new Bundle();
        CampaignListBean.CampaignInviteEntity campaignInviteEntity = new CampaignListBean.CampaignInviteEntity();
        CampaignListBean.CampaignInviteEntity.CampaignEntity campaignEntity = new CampaignListBean.CampaignInviteEntity.CampaignEntity();
        campaignEntity.setPer_budget_type(mCampaign.getPer_budget_type());
        campaignEntity.setId(mCampaign.getId());
        campaignInviteEntity.setCampaign(campaignEntity);
        bundle.putSerializable("bean", campaignInviteEntity);
        startActivity(intent.putExtras(bundle));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
