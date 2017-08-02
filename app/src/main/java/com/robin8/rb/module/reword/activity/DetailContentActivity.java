package com.robin8.rb.module.reword.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseDataActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.CampaignInviteBean;
import com.robin8.rb.model.CampaignListBean;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.mine.model.MessageModel;
import com.robin8.rb.module.reword.CorrectionRunnable;
import com.robin8.rb.module.reword.DetailCampaignDownAdapter;
import com.robin8.rb.module.reword.bean.CampaignMaterialsModel;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.SlideDetailsLayout;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.FileUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.util.TimerUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * 活动详情页面
 */
public class DetailContentActivity extends BaseDataActivity implements View.OnClickListener, Observer {

    private static final String CAMPAIGN_TYPE_INVITE = "invite";//邀请
    private static final String CAMPAIGN_TYPE_RECRUIT = "recruit";//招募
    private static final String CAMPAIGN_TYPE_CLICK = "click";//点击
    private static final String CAMPAIGN_TYPE_POST = "post";//转发
    private static final String CAMPAIGN_TYPE_CPA = "cpa";//CPA活动
    private static final String CAMPAIGN_TYPE_CPI = "simple_cpi";
    private static final String CAMPAIGN_TYPE_CPT = "cpt";

    private WebView mWebView;
    private WebSettings mWebSettings;
    private CampaignListBean.CampaignInviteEntity mCampaignInviteEntity;
    private ImageView mImageView;
    private ImageView mBackIv;
    private TextView mTVTitleUp;
    private TextView mTVActivityName;
    private TextView mTVBrandName;
    private TextView mTVActivityTime;
    private TextView mTVBrandInfo;
    private TextView mTVClick;
    private TextView mTVMoney;
    private TextView mTVCountTime;
    private TextView mTVNextPage;
    private TextView mTVShareInfo;
    private SlideDetailsLayout mSlideDetailsLayout;
    private LinearLayout mLLContent;
    private ImageView mIvBackDown;
    private TextView mTVBottomRight;
    private boolean mWithWebViewB;//判断招募活动，是否显示webview
    private TextView mTVBottomLeft;
    private View mViewLine;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private int mFrom;
    private Bundle mBundle;
    private String title;
    private String startTime;
    private String endTime;
    private String introduce;
    private String countWay;
    private String everyConsume;
    private String address;
    private String path;
    private TextView mTVTitleDown;
    private View mContentFl;
    private ListView mListView;
    private DetailCampaignDownAdapter mDetailCampaignDownAdapter;
    private List<CampaignMaterialsModel.CampaignMaterialsBean> mDataList = new ArrayList<>();
    private String url;
    private int mCampaignInviteId;
    private int mCampaignId;
    private String mWebUrl;
    private TextView mTVJoinNumber;
    private LinearLayout mLinearLayout;
    private List<CampaignInviteBean.InviteesBean> mInviteBean;
    private LinearLayout mLLListInvite;
    private DetailContentHelper mDetailContentHelper;
    private TextView mTVRemark;

    private RequestParams mRequestParams;
    private final String BACKSLASH = "/";
    private ImageView mImgCampaignsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_content);
        getData();
        initView();
        initData();

    }

    public void getData() {
        Intent intent = getIntent();
        mBundle = intent.getExtras();
        mFrom = mBundle.getInt("from");
        switch (mFrom) {
            case SPConstants.LAUNCHREWORDACTIVIRY://发起悬赏活动
                LogUtil.LogShitou("发起悬赏活动", "step1");
                initDataWhenFromLauncher();
                break;
            case SPConstants.MESSAGE_ACTIVITY://消息列表
                LogUtil.LogShitou("发起悬赏活动", "step2");
                initDataWhenFromMessage();
                break;
            case SPConstants.PUSH_TO_DETAIL:
                //从推送进入

                break;
            default://默认为悬赏活动列表
                LogUtil.LogShitou("发起悬赏活动", "step3");
                initDataWhenFromDefault();
                break;
        }
    }
    private void shareCtaAndCti(final Activity activity,String info,boolean is) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_bg);
        //TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        confirmTV.setText(R.string.known);
//        switch (type){
//            case CAMPAIGN_TYPE_POST:
//                infoTv.setText(info);
//                break;
//            case
//        }
        // rightTv.setText(R.string.known)
        if (is){
            new Thread(new TimerUtil(4, null, confirmTV,layout, activity, "知道了","s",activity.getResources().getColor(R.color.black_686868),activity.getResources().getColor(R.color.white_custom))).start();
        }
        infoTv.setText(info);
        infoTv.setGravity(Gravity.LEFT);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // showSnapDialog(activity, entity.getCampaign().getCpi_example_screenshot());
                cdm.dismiss();
            }
        });
        //        rightTv.setOnClickListener(new View.OnClickListener() {
        //
        //            @Override
        //            public void onClick(View v) {
        //
        //                cdm.dismiss();
        //            }
        //        });
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }
    private void initDataWhenFromDefault() {

        mCampaignInviteEntity = (CampaignListBean.CampaignInviteEntity) mBundle.getSerializable("bean");
        String perBudgetType = mCampaignInviteEntity.getCampaign().getPer_budget_type();
        String perActionType = mCampaignInviteEntity.getCampaign().getPer_action_type();
        String remark = mCampaignInviteEntity.getCampaign().getRemark();

        if (CAMPAIGN_TYPE_RECRUIT.equals(perBudgetType) || CAMPAIGN_TYPE_INVITE.equals(perBudgetType)) {
            if (TextUtils.isEmpty(perActionType)) {
                mWithWebViewB = false;
            } else {
                mWithWebViewB = true;
            }

            if (CAMPAIGN_TYPE_RECRUIT.equals(perBudgetType)) {
                mPageName = StatisticsAgency.CAMPAIGN_RECRUIT;//招募活动详情
            } else {
                mPageName = StatisticsAgency.CAMPAIGN_INVITE;
            }
        }
//        else if (CAMPAIGN_TYPE_CPI.equals(perBudgetType)|| CAMPAIGN_TYPE_CPA.equals(perBudgetType)){
//            if (!TextUtils.isEmpty(remark)){
//                LogUtil.LogShitou("?????","---22---->"+remark);
//                shareCtaAndCti(DetailContentActivity.this,remark);
//            }
//        }
        else {
            mWithWebViewB = true;
            mPageName = StatisticsAgency.CAMPAIGN_DETAIL;
        }
        mCampaignId = mCampaignInviteEntity.getCampaign().getId();
        mCampaignInviteId = mCampaignInviteEntity.getId();
        url = CommonConfig.CAMPAIGNS_DETAIL_URL + mCampaignId;
        LogUtil.LogShitou("通过活动列表进入",url);
        mWebUrl = mCampaignInviteEntity.getCampaign().getUrl();
    }

//    @Override
//    protected void onResume() {
//        if (DetailContentHelper.SUCCESS.equals("success")){
//            initData();
//        }
//        super.onResume();
//    }

    private void initDataWhenFromMessage() {

        MessageModel.MessagesBean message = (MessageModel.MessagesBean) mBundle.getSerializable("bean");
        String subMessageType = message.getSub_message_type();
        String subSubMessageType = message.getSub_sub_message_type();

        if (CAMPAIGN_TYPE_RECRUIT.equals(subMessageType) || CAMPAIGN_TYPE_INVITE.equals(subMessageType)) {
            if (TextUtils.isEmpty(subSubMessageType)) {
                mWithWebViewB = false;
            } else {
                mWithWebViewB = true;
            }
            if (CAMPAIGN_TYPE_RECRUIT.equals(message.getSub_message_type())) {
                mPageName = StatisticsAgency.CAMPAIGN_RECRUIT;
            } else {
                mPageName = StatisticsAgency.CAMPAIGN_INVITE;
            }
        } else {
            mWithWebViewB = true;
            mPageName = StatisticsAgency.CAMPAIGN_DETAIL;
        }

        mCampaignId = message.getItem_id();
        url = CommonConfig.CAMPAIGNS_DETAIL_URL + mCampaignId;
    }

    private void initDataWhenFromLauncher() {

        path = mBundle.getString("path");
        title = mBundle.getString("title");
        startTime = mBundle.getString("start_time");
        endTime = mBundle.getString("end_time");
        introduce = mBundle.getString("introduce");
        countWay = mBundle.getString("count_way");
        everyConsume = mBundle.getString("every_consume");
        address = mBundle.getString("address");
        mWithWebViewB = true;
        mPageName = StatisticsAgency.ADVERTISER_ADD_PREVIEW;
    }

    private void initView() {

        mSlideDetailsLayout = (SlideDetailsLayout) findViewById(R.id.slide_detail_layout);
        mLLContent = (LinearLayout) findViewById(R.id.ll_content2);
        mTVBottomLeft = (TextView) findViewById(R.id.tv_bottom_left);
        mTVBottomRight = (TextView) findViewById(R.id.tv_bottom_right);
        mViewLine = findViewById(R.id.view_line);
        mTVJoinNumber = (TextView) findViewById(R.id.tv_join_number);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_content_invite);
        mLLListInvite = (LinearLayout) findViewById(R.id.ll_list_invite);
        mImgCampaignsRequest = ((ImageView) findViewById(R.id.img_campaigns_request));//活动要求的按钮

        if (mWithWebViewB) {
            mLLListInvite.setVisibility(View.VISIBLE);
            LayoutInflater.from(this).inflate(R.layout.detail_content_view_down_webv, mLLContent);
            View layoutTitleBar = findViewById(R.id.layout_titlebar);
            mIvBackDown = (ImageView) layoutTitleBar.findViewById(R.id.iv_back);
            mTVTitleDown = (TextView) layoutTitleBar.findViewById(R.id.tv_title);
            mIvBackDown.setImageResource(R.mipmap.icon_btn_back_black);
            mTVTitleDown.setTextColor(UIUtils.getColor(R.color.sub_black_custom));
            mWebView = (WebView) findViewById(R.id.wb_content);
            initWebViewSettings();
            initWebView();
        } else {//招募、特邀活动
            mLLListInvite.setVisibility(View.GONE);
            View view = LayoutInflater.from(this).inflate(R.layout.detail_content_view_down, mLLContent);
            mIvBackDown = (ImageView) view.findViewById(R.id.iv_back_down);
            mListView = (ListView) view.findViewById(R.id.lv_list);
            initListView();
        }

        mImageView = (ImageView) findViewById(R.id.iv);
        mContentFl = findViewById(R.id.rl_content);
        mTVRemark = (TextView) findViewById(R.id.tv_remark);
        mBackIv = (ImageView) findViewById(R.id.iv_back);
        mTVTitleUp = (TextView) findViewById(R.id.tv_title);
        mTVActivityName = (TextView) findViewById(R.id.tv_activity_name);
        mTVBrandName = (TextView) findViewById(R.id.lstv_brand_name);
        mTVActivityTime = (TextView) findViewById(R.id.tv_activity_time);
        mTVBrandInfo = (TextView) findViewById(R.id.tv_brand_info);
        mTVClick = (TextView) findViewById(R.id.tv_click);
        mTVMoney = (TextView) findViewById(R.id.tv_money);
        mTVShareInfo = (TextView) findViewById(R.id.tv_share_info);
        mTVCountTime = (TextView) findViewById(R.id.tv_count_time);
        mTVNextPage = (TextView) findViewById(R.id.tv_next_page);

        mBackIv.setOnClickListener(this);
        mTVNextPage.setOnClickListener(this);
        mIvBackDown.setOnClickListener(this);
        mLinearLayout.setOnClickListener(this);

        if (mFrom == SPConstants.LAUNCHREWORDACTIVIRY) {
            updateUpFromLaunchView();
            return;
        } else {
            mTVBrandName.setVisibility(View.VISIBLE);
            mTVBottomLeft.setOnClickListener(this);
            mTVBottomRight.setOnClickListener(this);
        }
    }

    /**
     * 活动素材（招募、特邀）
     */
    private void initListView() {

        mDetailCampaignDownAdapter = new DetailCampaignDownAdapter();
        mDataList.add(null);
        mDetailCampaignDownAdapter.setData(mDataList);
        mListView.setAdapter(mDetailCampaignDownAdapter);
    }

    /**
     * 发布悬赏活动--预览
     */
    private void updateUpFromLaunchView() {

        mLLListInvite.setVisibility(View.GONE);
        mTVCountTime.setVisibility(View.GONE);
        mTVTitleUp.setVisibility(View.GONE);
        mTVBrandName.setVisibility(View.GONE);
        mTVActivityName.setText(title);
        mTVActivityTime.setText(startTime + " - " + endTime);
        mTVBrandInfo.setText(introduce);
        if (TextUtils.isEmpty(countWay)) {
            return;
        }
        switch (countWay) {
            case CAMPAIGN_TYPE_CLICK:
                mTVClick.setText(getString(R.string.click));
                mTVShareInfo.setText(getString(R.string.shared_clicked_by_friends));
                break;
            case CAMPAIGN_TYPE_POST:
                mTVClick.setText(getString(R.string.post));
                mTVShareInfo.setText(getString(R.string.post_this_page_get_reword));
                break;
        }
        mTVMoney.setText("¥ " + everyConsume);
        if (! TextUtils.isEmpty(path) && ! path.startsWith("http://")) {
            BitmapUtil.loadLocalImage(this, path, mImageView);
        } else {
            BitmapUtil.loadImage(this, path, mImageView);
        }
    }

    private void initData() {

        NotifyManager.getNotifyManager().addObserver(this);
        if (mFrom == SPConstants.LAUNCHREWORDACTIVIRY) {
            return;
        }
        RequestParams requestParams = new RequestParams();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(DetailContentActivity.this);
        }
        mWProgressDialog.show();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(url), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(DetailContentActivity.this, "网络连接错误");
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("活动详情数据", response);
                CampaignInviteBean campaignInviteEntity = GsonTools.jsonToBean(response, CampaignInviteBean.class);
                if (campaignInviteEntity != null && campaignInviteEntity.getError() == 0) {
                    mCampaignInviteEntity = campaignInviteEntity.getCampaign_invite();
                    mInviteBean = campaignInviteEntity.getInvitees();

                    if (mWebView != null) {
                        mWebUrl = mCampaignInviteEntity.getCampaign().getUrl();
                        mWebView.loadUrl(mWebUrl);
                    }
                    updateUpView();
                    if (mDetailContentHelper == null) {
                        mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                    }
                    mDetailContentHelper.updateBottomShareView(mCampaignInviteEntity);
                    //mDetailContentHelper.setUpCenterView(mCampaignInviteEntity, mInviteBean, mTVClick, mTVMoney, mTVShareInfo, mTVCountTime, mTVJoinNumber, mLinearLayout);
                }
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });

        requestParams.put("campaign_id", mCampaignId);
        requestParams.put("campaign_invite_id", mCampaignInviteId);
        //获取素材
        if (! mWithWebViewB) {
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CAMPAIGNS_MATERIALS_URL), requestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {

                    CustomToast.showShort(DetailContentActivity.this, "网络连接错误");
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    CampaignMaterialsModel campaignMaterialsModel = GsonTools.jsonToBean(response, CampaignMaterialsModel.class);
                    if (campaignMaterialsModel != null && campaignMaterialsModel.getError() == 0) {
                        List<CampaignMaterialsModel.CampaignMaterialsBean> tempList = campaignMaterialsModel.getCampaign_materials();
                        if (tempList != null) {
                            mDataList.clear();
                            mDataList.addAll(tempList);
                            mDataList.add(null);
                            mDetailCampaignDownAdapter.setData(mDataList);
                            mDetailCampaignDownAdapter.notifyDataSetChanged();
                        }
                    }
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void initWebView() {

        if (mFrom == SPConstants.LAUNCHREWORDACTIVIRY) {
            if (! TextUtils.isEmpty(address)) {
                if (address.startsWith("www.")) {
                    address = "http://" + address;
                }
                mWebView.loadUrl(address);
            }
        } else {
            mWebView.loadUrl(mWebUrl);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

        });
    }

    /**
     * 更新上页
     */
    private void updateUpView() {

        if (mImageView == null || mCampaignInviteEntity == null) {
            return;
        }

        if (mWithWebViewB) {
            mTVNextPage.setText(getString(R.string.promote_the_content_page_for_details));
        } else {
            mTVNextPage.setText(getString(R.string.campaign_data_download));
        }
//走马灯
        if (CAMPAIGN_TYPE_CPI.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type()) || CAMPAIGN_TYPE_CPT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type()) || CAMPAIGN_TYPE_CPA.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
            mTVRemark.setVisibility(View.GONE);
            String remark = mCampaignInviteEntity.getCampaign().getRemark();
            if (TextUtils.isEmpty(remark)) {
                return;
            }
            mImgCampaignsRequest.setVisibility(View.VISIBLE);
            mImgCampaignsRequest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    shareCtaAndCti(DetailContentActivity.this,mCampaignInviteEntity.getCampaign().getRemark(),false);
                }
            });
            if (mCampaignInviteEntity.getStatus().equals("running")){
                shareCtaAndCti(DetailContentActivity.this,mCampaignInviteEntity.getCampaign().getRemark(),true);
            }
         //   mTVRemark.setText(remark + getString(R.string.white_space) + getString(R.string.white_space) + remark + getString(R.string.white_space));
           // mTVRemark.requestFocus();
        } else if (CAMPAIGN_TYPE_RECRUIT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())){
            mTVRemark.setVisibility(View.VISIBLE);
            String remark = mCampaignInviteEntity.getCampaign().getRemark();
            if (TextUtils.isEmpty(remark)) {
                return;
            }
            mTVRemark.setText(remark + getString(R.string.white_space) + getString(R.string.white_space) + remark + getString(R.string.white_space));
            mTVRemark.requestFocus();
        }else if (CAMPAIGN_TYPE_POST.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())){
            mTVRemark.setVisibility(View.GONE);
            mImgCampaignsRequest.setVisibility(View.VISIBLE);
            mImgCampaignsRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareCtaAndCti(DetailContentActivity.this,"此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过",false);
                }
            });
            if (mCampaignInviteEntity.getStatus().equals("running")){
                shareCtaAndCti(DetailContentActivity.this,"此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过",true);
            }
        }else {
            mImgCampaignsRequest.setVisibility(View.GONE);
            mTVRemark.setVisibility(View.GONE);
        }
        mImageView.post(new CorrectionRunnable(mImageView.getContext(), mContentFl, mImageView));
        BitmapUtil.loadImage(this.getApplicationContext(), mCampaignInviteEntity.getCampaign().getImg_url(), mImageView);
        mTVTitleUp.setText(mCampaignInviteEntity.getCampaign().getBrand_name());

        mTVActivityName.setText(mCampaignInviteEntity.getCampaign().getName());
        mTVBrandInfo.setText(mCampaignInviteEntity.getCampaign().getDescription());
        mTVBrandName.setText(mCampaignInviteEntity.getCampaign().getBrand_name());

        mTVActivityTime.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mCampaignInviteEntity.getCampaign().getStart_time()) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", mCampaignInviteEntity.getCampaign().getDeadline()));

        if (mDetailContentHelper == null) {
            mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
        }
        mDetailContentHelper.setUpCenterView(mCampaignInviteEntity, mInviteBean, mTVClick, mTVMoney, mTVShareInfo, mTVCountTime, mTVJoinNumber, mLinearLayout);
    }

    public void initWebViewSettings() {

        mWebSettings = mWebView.getSettings();
        if (NetworkUtil.isNetworkAvailable(DetailContentActivity.this)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        String cacheDirPath = DetailContentActivity.this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        mWebSettings.setAppCachePath(cacheDirPath);
        mWebSettings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= 11) {
            mWebSettings.setEnableSmoothTransition(true);
        }
        mWebSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        mWebSettings.setUserAgentString("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; zh-tw) AppleWebKit/533.16 (KHTML, like Gecko) Version/5.0 Safari/533.16");
    }

    @Override
    protected void onDestroy() {

        NotifyManager.getNotifyManager().deleteObserver(this);
        if (mDetailContentHelper != null) {
            mDetailContentHelper.onDestroy();
        }

        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }
        if (mBasePresenter != null) {
            mBasePresenter = null;
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_back_down:
                mSlideDetailsLayout.smoothClose(true);
                break;
            case R.id.iv_back:
                if (mFrom==SPConstants.PUSH_TO_DETAIL){
                    //从推送进入
                    startActivity(new Intent(DetailContentActivity.this, MainActivity.class));
                    DetailContentActivity.this.finish();
                }else {
                    finish();
                }
               // finish();
                break;
            case R.id.tv_next_page:
                mSlideDetailsLayout.smoothOpen(true);
                break;
            case R.id.tv_bottom_right:
                if (BaseApplication.getInstance().hasLogined()) {
                    if (mDetailContentHelper == null) {
                        mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                    }
                    mDetailContentHelper.clickBottomRight(DetailContentActivity.this);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("kol_uuid", HelpTools.getKolUUid());
                    bundle.putInt("from", SPConstants.DETAILCONTENTACTIVITY);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, SPConstants.DETAILCONTENTACTIVITY);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
            case R.id.tv_bottom_left:
                if (BaseApplication.getInstance().hasLogined()) {
                    if (mDetailContentHelper == null) {
                        mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                    }
                    mDetailContentHelper.clickBottomLeft(this);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("kol_uuid", HelpTools.getKolUUid());
                    bundle.putInt("from", SPConstants.DETAILCONTENTACTIVITY);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, SPConstants.DETAILCONTENTACTIVITY);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                break;
            case R.id.ll_content_invite:
                skipToInvitees();
                break;
        }
    }



    private void skipToInvitees() {
        Intent intent = new Intent(this, InviteesActivity.class);
        intent.putExtra("campaign_id", String.valueOf(mCampaignId));
        intent.putExtra("invitees", (Serializable) mInviteBean);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SPConstants.REQUEST_DETAILCONTENTACTIVITY) {
            initData();
            return;
        }

        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case DetailContentHelper.IMAGE_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    final String finalPicturePath = FileUtils.getAbsoluteImagePath(this, selectedImage);
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            final String uploadPicturePath = BitmapUtil.getCompressImagePath(finalPicturePath);
                            UIUtils.runInMainThread(new Runnable() {

                                @Override
                                public void run() {

                                    if (uploadPicturePath == null)
                                        CustomToast.showShort(DetailContentActivity.this, "图片不存在,请检查本地路径");
                                    else {
                                        if (mDetailContentHelper == null) {
                                            mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                                        }
                                        mDetailContentHelper.uploadTurnImage(DetailContentActivity.this, uploadPicturePath.substring(uploadPicturePath.lastIndexOf("/") + 1), new File(uploadPicturePath));
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
                case DetailContentHelper.IMAGE_REQUEST_LOOK_CODE:
                    final String backPicturePath = data.getStringExtra("look_back_img");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            final String uploadPicturePath = BitmapUtil.getCompressImagePath(backPicturePath);
                            UIUtils.runInMainThread(new Runnable() {

                                @Override
                                public void run() {

                                    if (uploadPicturePath == null)
                                        CustomToast.showShort(DetailContentActivity.this, "图片不存在,请检查本地路径,重新上传截图");
                                    else {
                                        if (mDetailContentHelper == null) {
                                            mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                                        }
                                        mDetailContentHelper.uploadTurnImage(DetailContentActivity.this, uploadPicturePath.substring(uploadPicturePath.lastIndexOf("/") + 1), new File(uploadPicturePath));
                                    }
                                }
                            });
                        }
                    }).start();
                    break;
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof NotifyMsgEntity) {
            NotifyMsgEntity msgEntity = (NotifyMsgEntity) data;
            if (msgEntity.getCode() == NotifyManager.TYPE_SHARE_SUCCESS) {
                CampaignListBean.CampaignInviteEntity entity = (CampaignListBean.CampaignInviteEntity) msgEntity.getData();
                if (mDetailContentHelper == null) {
                    mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                }
                if (entity != null) {
                    if (CAMPAIGN_TYPE_RECRUIT.equals(entity.getCampaign().getPer_budget_type())) {
                        mDetailContentHelper.showSignUpSuccessDialog(this, getString(R.string.campaign_sign_up_success_wait_for_auditing));
                        mDetailContentHelper.updateBottomShareView(entity);
                    }else {
                        mDetailContentHelper.setUpCenterView(entity, mInviteBean, mTVClick, mTVMoney, mTVShareInfo, mTVCountTime, mTVJoinNumber, mLinearLayout);
                    }
                }else{
                    //如果为空就刷新界面
                    initData();
                }
            }
        }
    }
//    @Override
//        public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                if (mFrom==SPConstants.PUSH_TO_DETAIL){
//                    //从推送进入
//                    LogUtil.LogShitou("推送","back");
//                    startActivity(new Intent(DetailContentActivity.this, MainActivity.class));
//                    DetailContentActivity.this.finish();
//                }else {
//                    LogUtil.LogShitou("正常","back");
//                    finish();
//                }
//            }
//            return super.onKeyDown(keyCode, event);
//        }
}

//    private void bindWechat() {
//        String string = CacheUtils.getString(DetailContentActivity.this, SPConstants.MINE_DATA, null);
//        MineShowModel mineShowModel = GsonTools.jsonToBean(string, MineShowModel.class);
//        if (mineShowModel!=null){
//            int id = mineShowModel.getKol().getId();
//            getDataFromNet(id);
//        }
//    }
//    private void getDataFromNet(int id) {
//        if (mBasePresenter == null) {
//            mBasePresenter = new BasePresenter();
//        }
//
//        if (mRequestParams == null) {
//            mRequestParams = new RequestParams();
//        }
//
//        if (mWProgressDialog == null) {
//            mWProgressDialog = WProgressDialog.createDialog(this);
//        }
//        mWProgressDialog.show();
//
//        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.FIRST_KOL_LIST_URL + BACKSLASH + String.valueOf(id) + BACKSLASH + "detail"), mRequestParams, new RequestCallback() {
//            @Override
//            public void onError(Exception e) {
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onResponse(String response) {
//                if (mWProgressDialog != null) {
//                    mWProgressDialog.dismiss();
//                }
//                //LogUtil.LogShitou("初始信息",response);
//                KolDetailModel kolDetailModel = GsonTools.jsonToBean(response, KolDetailModel.class);
//                if (kolDetailModel != null && kolDetailModel.getError() == 0) {
//                    List<SocialAccountsBean> mSocialAccounts = kolDetailModel.getSocial_accounts();
//                    if (mSocialAccounts!=null){
//                        if (mSocialAccounts.size()!=0){
//                            for (int i = 0; i <mSocialAccounts.size() ; i++) {
//                                if (mSocialAccounts.get(i).getProvider_name().equals("微信")
//                                        ||mSocialAccounts.get(i).getProvider().equals("wechat")){
//
//                                }else {
//                                    bind(getString(R.string.weixin));
//                                    CustomToast.showShort(DetailContentActivity.this,"去绑定微信吧");
//                                }
//                            }
//                        }else {
//                            bind(getString(R.string.weixin));
//                            CustomToast.showShort(DetailContentActivity.this,"不空的");
//                        }
//                    }else {
//                        bind(getString(R.string.weixin));
//                        CustomToast.showShort(DetailContentActivity.this,"就是空的");
//                    }
//                }
//            }
//        });
//    }
//    private void bind(final String names) {
//        BindSocialPresenter presenter = new BindSocialPresenter(this.getApplicationContext(), null, names);
//        presenter.setOnBindListener(new BindSocialPresenter.OnBindListener() {
//            @Override
//            public void onResponse(String name) {
//                if (null!=name){
//                    CustomToast.showShort(DetailContentActivity.this,"已成功绑定"+name);
//                    bindPostData(names,name);
//                }else {
//                    CustomToast.showLong(DetailContentActivity.this,"绑定失败，请重试");
//                }
//            }
//        });
//        if (getString(R.string.weixin).equals(names)) {
//            presenter.authorize(new Wechat(this));
//        }
//
//    }
//    private void bindPostData(String name,String userName) {
//        String url;
//        BasePresenter mBasePresenter = new BasePresenter();
//        RequestParams params = new RequestParams();
//        params.put("provider_name", name);
//        params.put("price","1");
//        params.put("followers_count","1");
//        params.put("username", userName);
//        //LogUtil.LogShitou("绑定微信的报价之类的",name+"//"+userName);
//        url = HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL);
//
//        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, params, new RequestCallback() {
//            @Override
//            public void onError(Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                //LogUtil.LogShitou("分享页面提交微信绑定","OK"+response);
//                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
//
//                if (bean == null) {
//                    CustomToast.showShort(DetailContentActivity.this, getString(R.string.please_data_wrong));
//                    return;
//                }
//
//                if (bean.getError() == 0) {
//                    LogUtil.LogShitou("走到这里没有","直接绑定微信");
//                    // setResult(SPConstants.BE_KOL_SECOND_PERSONAL_SHOW, intent);
//                    //finish();
//                } else {
//                    CustomToast.showShort(DetailContentActivity.this, bean.getDetail());
//                }
//            }
//        });
//    }
