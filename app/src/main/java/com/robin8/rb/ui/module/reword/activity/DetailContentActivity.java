package com.robin8.rb.ui.module.reword.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseDataActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.activity.LoginActivity;
import com.robin8.rb.ui.activity.MainActivity;
import com.robin8.rb.ui.activity.web.PutWebActivity;
import com.robin8.rb.ui.dialog.CustomDialogManager;
import com.robin8.rb.ui.model.CampaignInviteBean;
import com.robin8.rb.ui.model.CampaignListBean;
import com.robin8.rb.ui.model.NotifyMsgEntity;
import com.robin8.rb.ui.module.mine.model.MessageModel;
import com.robin8.rb.ui.module.reword.CorrectionRunnable;
import com.robin8.rb.ui.module.reword.DetailCampaignDownAdapter;
import com.robin8.rb.ui.module.reword.bean.CampaignMaterialsModel;
import com.robin8.rb.ui.module.reword.bean.ShareBean;
import com.robin8.rb.ui.module.reword.chose_photo.SerializableMap;
import com.robin8.rb.ui.module.reword.helper.DetailContentHelper;
import com.robin8.rb.ui.module.share.thirdplatfom.Constants;
import com.robin8.rb.ui.widget.SlideDetailsLayout;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.util.TimerUtil;
import com.robin8.rb.util.UIUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;


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
    private static final String FACEBOOK_SHARE_TYPE = "facebook";
    private static final String MESSENGE_SHARE_TYPE = "facebook_messenger";

    private WebView mWebView;
    private WebSettings mWebSettings;
    private CampaignListBean.CampaignInviteEntity mCampaignInviteEntity;
    private CampaignInviteBean mCampaignEntity;
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
    private TextView mTvCampaignsRequest;
    private LinearLayout llShare;//分享按钮
    private int mInviteesCount = -1;
    private RelativeLayout llRemark;
    private LinearLayout mLayoutPut;
    private TextView mTvPutEnter;
    private TextView mTvPutResult;

    private CallbackManager mCallbackManager;
    private ShareDialog mShareDialog;
    private MessageDialog mMessageDialog;

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
                //从预览进入
                // LogUtil.LogShitou("发起悬赏活动", "step1");
                initDataWhenFromLauncher();
                break;
            case SPConstants.MESSAGE_ACTIVITY://消息列表
                //LogUtil.LogShitou("发起悬赏活动", "step2");
                initDataWhenFromMessage();
                break;
            case SPConstants.PUSH_TO_DETAIL:
                //从推送进入

                break;
            case SPConstants.MY_CAMPAIGN_ACTIVITY:
                initDataMyCampaign();
                break;
            default://默认为悬赏活动列表
                // LogUtil.LogShitou("发起悬赏活动", "step3");
                initDataWhenFromDefault();
                break;
        }
    }


    private void shareCtaAndCti(final Activity activity, String info, boolean is) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_bg);
        //TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        confirmTV.setText(R.string.known);
        if (is) {
            new Thread(new TimerUtil(4, null, confirmTV, layout, activity, "知道了", "s", activity.getResources().getColor(R.color.black_686868), activity.getResources().getColor(R.color.white_custom))).start();
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

    /**
     * 从我的活动进入
     */
    private void initDataMyCampaign() {
        mCampaignId = mBundle.getInt("campaign_id");
        url = CommonConfig.CAMPAIGNS_DETAIL_URL + mCampaignId;
    }

    /**
     *
     */
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
        // LogUtil.LogShitou("通过活动列表进入", url);
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

    /**
     * 从预览进入
     */
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
        LinearLayout mTitleLayout = (LinearLayout) findViewById(R.id.ll_content);
        mTitleLayout.setBackgroundColor(R.color.black_000000);
        mTitleLayout.setAlpha(0.78f);//活动详情开头半透明
        mTVBottomLeft = (TextView) findViewById(R.id.tv_bottom_left);
        mTVBottomRight = (TextView) findViewById(R.id.tv_bottom_right);
        mViewLine = findViewById(R.id.view_line);
        mTVJoinNumber = (TextView) findViewById(R.id.tv_join_number);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_content_invite);
        mLLListInvite = (LinearLayout) findViewById(R.id.ll_list_invite);
        mTvCampaignsRequest = ((TextView) findViewById(R.id.tv_campaigns_request));//活动要求的按钮
        llShare = (LinearLayout) findViewById(R.id.ll_share);
        llShare.setVisibility(View.VISIBLE);
        llShare.setOnClickListener(this);
        mLayoutPut = ((LinearLayout) findViewById(R.id.ll_put));
        mTvPutEnter = ((TextView) findViewById(R.id.tv_put_entry));
        mTvPutResult = ((TextView) findViewById(R.id.tv_put_result));

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
        llRemark = (RelativeLayout) findViewById(R.id.ll_remark);
        llRemark.setAlpha(0.4f);
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

        mTvPutEnter.setOnClickListener(this);
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
        mTVTitleUp.setVisibility(View.VISIBLE);
        mTVBrandName.setVisibility(View.GONE);
        llShare.setVisibility(View.INVISIBLE);
        mTVTitleUp.setText(title);
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
        if (!TextUtils.isEmpty(path) && !path.startsWith("http://")) {
            BitmapUtil.loadLocalImage(this, path, mImageView);
        } else {
            BitmapUtil.loadImage(this, path, mImageView);
        }
    }

    private void initData() {
        setupFacebookSharing();
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
        requestParams.put("invitee_page", "1");
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(url), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

                CustomToast.showShort(DetailContentActivity.this, R.string.robin345);
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
                    mCampaignEntity = campaignInviteEntity;
                    mInviteBean = campaignInviteEntity.getInvitees();
                    if (TextUtils.isEmpty(campaignInviteEntity.getPut_switch())) {
                        HelpTools.insertLoginInfo(HelpTools.ISOPENPUT, "");
                    } else {
                        HelpTools.insertLoginInfo(HelpTools.ISOPENPUT, campaignInviteEntity.getPut_switch());
                    }
                    if (TextUtils.isEmpty(campaignInviteEntity.getLeader_club())) {
                        HelpTools.insertCommonXml(HelpTools.isLeader, "");
                        forLeader(false);
                    } else {
                        HelpTools.insertCommonXml(HelpTools.isLeader, campaignInviteEntity.getLeader_club());
                        forLeader(true);
                    }
                    mInviteesCount = campaignInviteEntity.getInvitees_count();
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
        if (!mWithWebViewB) {
            mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CAMPAIGNS_MATERIALS_URL), requestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {

                    CustomToast.showShort(DetailContentActivity.this, R.string.robin345);
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

    /**
     * The method is used to setup FacebookSDK for sharing
     */
    private void setupFacebookSharing() {
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog = new ShareDialog(this);
        mMessageDialog = new MessageDialog(this);
        mShareDialog.registerCallback(mCallbackManager, shareCallback);
        mMessageDialog.registerCallback(mCallbackManager, shareCallback);
    }

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
        }

        @Override
        public void onSuccess(Sharer.Result result) {
        }
    };

    private void initWebView() {

        if (mFrom == SPConstants.LAUNCHREWORDACTIVIRY) {
            if (!TextUtils.isEmpty(address)) {
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
        if (TextUtils.isEmpty(mCampaignInviteEntity.getStatus())) {
            return;
        }
        if (mWithWebViewB) {
            mTVNextPage.setText(getString(R.string.promote_the_content_page_for_details));
        } else {
            mTVNextPage.setText(getString(R.string.campaign_data_download));
        }
        //走马灯
        if (CAMPAIGN_TYPE_CPI.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type()) || CAMPAIGN_TYPE_CPT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type()) || CAMPAIGN_TYPE_CPA.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
            // forLeader(false);
            String remark = mCampaignInviteEntity.getCampaign().getRemark();
            if (TextUtils.isEmpty(remark)) {
                return;
            }
            mTvCampaignsRequest.setVisibility(View.VISIBLE);
            mTvCampaignsRequest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    shareCtaAndCti(DetailContentActivity.this, mCampaignInviteEntity.getCampaign().getRemark(), false);
                }
            });
            if (mCampaignInviteEntity.getStatus().equals("running")) {
                if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ShadowCampaign))) {
                    showShadowDialog(DetailContentActivity.this, true, mCampaignInviteEntity.getCampaign().getRemark());
                } else {
                    if (HelpTools.getCommonXml(HelpTools.ShadowCampaign).equals(getString(R.string.submit))) {
                        shareCtaAndCti(DetailContentActivity.this, mCampaignInviteEntity.getCampaign().getRemark(), true);
                    } else {
                        showShadowDialog(DetailContentActivity.this, true, mCampaignInviteEntity.getCampaign().getRemark());
                    }
                }
            }
            //   mTVRemark.setText(remark + getString(R.string.white_space) + getString(R.string.white_space) + remark + getString(R.string.white_space));
            // mTVRemark.requestFocus();
        } else if (CAMPAIGN_TYPE_RECRUIT.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
            // forLeader(false);
            if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ShadowCampaign))) {
                showShadowDialog(DetailContentActivity.this, false, null);
            } else {
                if (!HelpTools.getCommonXml(HelpTools.ShadowCampaign).equals(getString(R.string.submit))) {
                    showShadowDialog(DetailContentActivity.this, false, null);
                }
            }

            //            String remark = mCampaignInviteEntity.getCampaign().getRemark();
            //            if (TextUtils.isEmpty(remark)) {
            //                return;
            //            }
            //   mTVRemark.setText(remark + getString(R.string.white_space) + getString(R.string.white_space) + remark + getString(R.string.white_space));
            //  mTVRemark.requestFocus();
        } else if (CAMPAIGN_TYPE_POST.equals(mCampaignInviteEntity.getCampaign().getPer_budget_type())) {
            // forLeader(false);
            mTvCampaignsRequest.setVisibility(View.VISIBLE);
            mTvCampaignsRequest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    shareCtaAndCti(DetailContentActivity.this, "此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过", false);
                }
            });
            if (mCampaignInviteEntity.getStatus().equals("running")) {
                //  shareCtaAndCti(DetailContentActivity.this, "此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过", true);
                if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ShadowCampaign))) {
                    showShadowDialog(DetailContentActivity.this, true, "此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过");
                } else {
                    if (HelpTools.getCommonXml(HelpTools.ShadowCampaign).equals(getString(R.string.submit))) {
                        shareCtaAndCti(DetailContentActivity.this, "此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过", true);
                    } else {
                        showShadowDialog(DetailContentActivity.this, true, "此任务要求分享后保留至少30分钟后截图并上传，否则不予以通过");
                    }
                }
            }
        } else {
            if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ShadowCampaign))) {
                showShadowDialog(DetailContentActivity.this, false, null);
            } else {
                if (!HelpTools.getCommonXml(HelpTools.ShadowCampaign).equals(getString(R.string.submit))) {
                    showShadowDialog(DetailContentActivity.this, false, null);
                }
            }
            mTvCampaignsRequest.setVisibility(View.GONE);
            //forLeader(false);
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
//        String isOpen = HelpTools.getLoginInfo(HelpTools.ISOPENPUT);
//        String isPutUser = HelpTools.getLoginInfo(HelpTools.WEBADDRESS);
        //put钱包
        if (mCampaignInviteEntity.getCampaign().getPer_budget_type().equals(CAMPAIGN_TYPE_RECRUIT)) {
            mLayoutPut.setVisibility(View.GONE);
        } else {
            mLayoutPut.setVisibility(View.VISIBLE);
            mTvPutResult.setVisibility(View.VISIBLE);
            mTvPutEnter.setVisibility(View.GONE);
        }
        mDetailContentHelper.setUpCenterView(mCampaignEntity, mCampaignInviteEntity, mInviteBean, mTVClick, mTVMoney, mTVShareInfo, mTVCountTime, mTVJoinNumber, mLinearLayout, mInviteesCount, mTvPutResult, mTvPutEnter);

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

    /**
     * leader用户需要显示走马灯
     *
     * @param is
     */
    private void forLeader(boolean is) {
        String mText = "点击右上角分享按钮，将活动分享给你的成员";
        if (is) {
            if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.isLeader))) {
                llRemark.setVisibility(View.GONE);
                mTVRemark.setVisibility(View.GONE);
            } else {
                llRemark.setVisibility(View.VISIBLE);
                mTVRemark.setVisibility(View.VISIBLE);
                mTVRemark.setText(getString(R.string.white_space_ban) + mText + getString(R.string.white_space) + getString(R.string.white_space) + mText + getString(R.string.white_space));
                mTVRemark.requestFocus();
            }
        } else {
            if (!TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.isLeader))) {
                llRemark.setVisibility(View.VISIBLE);
                mTVRemark.setVisibility(View.VISIBLE);
                mTVRemark.setText(getString(R.string.white_space_ban) + mText + getString(R.string.white_space) + getString(R.string.white_space) + mText + getString(R.string.white_space));
                mTVRemark.requestFocus();
            }
            llRemark.setVisibility(View.GONE);
            mTVRemark.setVisibility(View.GONE);
        }
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
        //        if (mTVRemark.getVisibility()==View.VISIBLE){
        //            mTVRemark.stopScroll();
        //        }
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
                if (mFrom == SPConstants.PUSH_TO_DETAIL) {
                    //从推送进入
                    startActivity(new Intent(DetailContentActivity.this, MainActivity.class));
                    DetailContentActivity.this.finish();
                } else {
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
            case R.id.ll_share:
                //分享H5
                showShareDialog(DetailContentActivity.this);
                //  checklsShowTipsDialog(DetailContentActivity.this);
                break;
            case R.id.tv_put_entry:
                if (BaseApplication.getInstance().hasLogined()) {
                    Intent intent = new Intent(this, PutWebActivity.class);
                    intent.putExtra(PutWebActivity.PUT_TYPE, "2");
                    startActivityForResult(intent, DetailContentHelper.IMAGE_REQUEST_PUT_RESULT);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SPConstants.REQUEST_DETAILCONTENTACTIVITY) {
            initData();
            return;
        }
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case DetailContentHelper.IMAGE_REQUEST_MORE_IMG_CODE:
                    final SerializableMap mapImages = (SerializableMap) (data.getExtras().get(ScreenImgActivity.EXTRA_SCREEN_MAP));
                    if (mapImages == null) {
                        CustomToast.showShort(DetailContentActivity.this, getString(R.string.img_empty));
                        return;
                    }
                    if (mapImages.getMap() == null) {
                        CustomToast.showShort(DetailContentActivity.this, getString(R.string.img_empty));
                        return;
                    }
                    if (mapImages.getMap().size() == 0) {
                        CustomToast.showShort(DetailContentActivity.this, getString(R.string.img_empty));
                        return;
                    }
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            //2019年03月19日11:03:32添加压缩逻辑
                            final SerializableMap serializableMap = new SerializableMap();
                            Map<Integer, String> map = new HashMap<>();
                            for (int index : mapImages.getMap().keySet()
                            ) {
                                map.put(index, BitmapUtil.getCompressImagePath(mapImages.getMap().get(index)));
                            }
                            serializableMap.setMap(map);

                            UIUtils.runInMainThread(new Runnable() {

                                @Override
                                public void run() {

                                    if (mDetailContentHelper == null) {
                                        mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                                    }

                                    mDetailContentHelper.uploadTurnImages(DetailContentActivity.this, serializableMap);
                                }


                            });
                        }
                    }).start();
                    break;
                case DetailContentHelper.IMAGE_REQUEST_PUT_RESULT:
                    updateUpView();
                    break;
            }
        } else {
            if (requestCode == DetailContentHelper.IMAGE_REQUEST_PUT_RESULT) {
                updateUpView();
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
                    // LogUtil.LogShitou("底部状态栏是否改变", "DetailContentActivity" + "855");
                    mDetailContentHelper = new DetailContentHelper(mViewLine, mTVBottomRight, mTVBottomLeft);
                }
                if (entity != null) {
                    if (CAMPAIGN_TYPE_RECRUIT.equals(entity.getCampaign().getPer_budget_type())) {
                        // LogUtil.LogShitou("底部状态栏是否改变", "DetailContentActivity" + "860");

                        mDetailContentHelper.showSignUpSuccessDialog(this, getString(R.string.campaign_sign_up_success_wait_for_auditing));
                        mDetailContentHelper.updateBottomShareView(entity);
                    } else {
                        //  LogUtil.LogShitou("底部状态栏是否改变", "DetailContentActivity" + "863");
                        if (mCampaignEntity == null) {
                            initData();
                        } else {
                            mDetailContentHelper.setUpCenterView(mCampaignEntity, entity, mInviteBean, mTVClick, mTVMoney, mTVShareInfo, mTVCountTime, mTVJoinNumber, mLinearLayout, mInviteesCount, mTvPutResult, mTvPutEnter);
                        }
                    }
                } else {
                    //  LogUtil.LogShitou("底部状态栏是否改变", "DetailContentActivity" + "866");
                    //如果为空就刷新界面
                    initData();
                }
            }
        }
    }

    //===============分享===============
    private CustomDialogManager mCustomDialogManager;
    private List<ShareBean> myShareList;
    private List<ShareBean> myShareListCampaign;


    /**
     * title右上角分享的弹窗
     *
     * @param activity
     */
    private void showShareDialog(final Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.campaign_share_bottom_layout, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView recyclerViewCampaign = (RecyclerView) view.findViewById(R.id.recycler_view_campaign);
        LinearLayout llTop = (LinearLayout) view.findViewById(R.id.ll_top_share);
        LinearLayout llBittom = ((LinearLayout) view.findViewById(R.id.ll_bottom_share));
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        //邀请好友一起赚钱
        llTop.setVisibility(View.VISIBLE);
        llBittom.setVisibility(View.GONE);
        myShareList = new ArrayList<>();
        myShareList.add(new ShareBean("FaceBook", R.mipmap.ic_facebook));
        ShareAdapter shareAdapter = new ShareAdapter(myShareList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shareAdapter);

        mCustomDialogManager = new CustomDialogManager(activity, view);
        shareAdapter.setOnRecyclerViewListener(new ShareAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    case 0:
                        if (AppUtils.isAppInstalled(getApplicationContext(), Constants.FACEBOOK_PACKAGE)) {
                            share(FACEBOOK_SHARE_TYPE);
                        } else {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + Constants.FACEBOOK_PACKAGE)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Constants.FACEBOOK_PACKAGE)));
                            }
                        }
                        break;
                }

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }



    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    private static final String TITLE_URL = CommonConfig.SERVICE + "invite?inviter_id=";
    private static final String TITLE_URL_CAMPAIGN = CommonConfig.SERVICE + "wechat_campaign/campaign_page?campaign_id=";
    private static final String TITLE_URL_LEADER = CommonConfig.SERVICE + "club_campaign/campaign_page?campaign_id=";

    /**
     * 弹出分享面板：微信朋友圈H5  其他文章内容
     *
     * @param platName
     */
    private void share(String platName) {

        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }
        CustomToast.showShort(DetailContentActivity.this, "前往分享...");
        //ShareSDK.initSDK(DetailContentActivity.this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        if (mCampaignInviteEntity == null) {
            int id = BaseApplication.getInstance().getLoginBean().getKol().getId();
            if (FACEBOOK_SHARE_TYPE.equals(platName)) {
                shareFacebook(TITLE_URL + String.valueOf(id), getString(R.string.share_invite_friends_title), IMAGE_URL);
                return;
            } else if (MESSENGE_SHARE_TYPE.equals(platName)) {
                shareFacebookMessenger(TITLE_URL + String.valueOf(id), getString(R.string.share_invite_friends_title), IMAGE_URL);
                return;
            } else {
                oks.setText(getResources().getString(R.string.share_invite_friends_text));
            }
            oks.setTitle(getResources().getString(R.string.share_invite_friends_title));
            oks.setTitleUrl(TITLE_URL + String.valueOf(id));
            oks.setUrl(TITLE_URL + String.valueOf(id));
        } else {
            if (FACEBOOK_SHARE_TYPE.equals(platName)) {
                shareFacebook(mCampaignInviteEntity.getCampaign().getUrl(), mCampaignInviteEntity.getCampaign().getName(), IMAGE_URL);
                return;
            }
            if (MESSENGE_SHARE_TYPE.equals(platName)) {
                shareFacebookMessenger(mCampaignInviteEntity.getCampaign().getUrl(), mCampaignInviteEntity.getCampaign().getName(), IMAGE_URL);
                return;
            }
        }

        oks.setImageUrl(IMAGE_URL);
        oks.setSite(getResources().getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        // oks.setSiteUrl(("http://robin8.net/wechat_campaign/campaign_page?campaign_id=3904"));
        oks.show(DetailContentActivity.this);
    }

    /**
     * The method is used to share campaign to facebook
     *
     * @param shareUrl     campaign url
     * @param title        campaign title
     * @param thumbnailUrl campaign thumbnail url
     */
    private void shareFacebook(final String shareUrl, final String title, final String thumbnailUrl) {
        if (mShareDialog != null && ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(shareUrl))
                    .setImageUrl(Uri.parse(thumbnailUrl))
                    .setContentTitle(title)
                    .build();
            mShareDialog.show(linkContent);
        }
    }

    /**
     * The method is used to share campaign to facebook messenger
     *
     * @param shareUrl     campaign url
     * @param title        campaign title
     * @param thumbnailUrl campaign thumbnail url
     */
    private void shareFacebookMessenger(final String shareUrl, final String title, final String thumbnailUrl) {
        if (mMessageDialog != null && ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(shareUrl))
                    .setImageUrl(Uri.parse(thumbnailUrl))
                    .setContentTitle(title)
                    .build();
            mMessageDialog.show(linkContent);
        }
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(DetailContentActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(DetailContentActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(DetailContentActivity.this, "分享取消");
        }
    }

    public static class ShareAdapter extends BaseRecyclerAdapter {
        private OnRecyclerViewListener onRecyclerViewListener;
        private ViewHolder mViewHolder;
        private List<ShareBean> myList;

        public ShareAdapter(List<ShareBean> list) {
            this.myList = list;
        }

        public interface OnRecyclerViewListener {
            void onItemClick(View v, int position);
        }

        public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
            this.onRecyclerViewListener = onRecyclerViewListener;
        }


        @Override
        public RecyclerView.ViewHolder getViewHolder(View view) {
            return mViewHolder;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_share_item, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            mViewHolder = new ViewHolder(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position, boolean isItem) {
            if (holder instanceof ViewHolder) {
                final ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.position = position;
                viewHolder.itemIcon.setImageResource(myList.get(position).getIcon());
                viewHolder.itemName.setText(myList.get(position).getName());
                viewHolder.itemLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (onRecyclerViewListener != null) {
                            onRecyclerViewListener.onItemClick(viewHolder.itemLayout, position);
                        }
                    }
                });
            }
        }

        @Override
        public int getAdapterItemCount() {
            return myList == null ? 0 : myList.size();
        }

        //        @Override
        //        public int getAdapterItemViewType(int position) {
        //            return 1;
        //        }
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

            private final TextView itemName;
            private final ImageView itemIcon;
            private final RelativeLayout itemLayout;
            public int position;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (onRecyclerViewListener != null) {
                            onRecyclerViewListener.onItemClick(v, getPosition());
                        }
                    }
                });
                itemName = ((TextView) itemView.findViewById(R.id.tv_share_item_name));
                itemIcon = ((ImageView) itemView.findViewById(R.id.img_share_item_icon));
                itemLayout = ((RelativeLayout) itemView.findViewById(R.id.ll_layout));
            }

            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        }
    }


    //===================蒙版======================
    public void showShadowDialog(final Activity activity, final boolean isShowRequest, final String content) {
        // String rejectReason = campaignInviteEntity.getReject_reason();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_shadow_layout, null);
        RelativeLayout llShadow = (RelativeLayout) view.findViewById(R.id.ll_shadow);
        final ImageView imgBg = (ImageView) view.findViewById(R.id.img_shadow_invite);
        final LinearLayout imgBgTwo = (LinearLayout) view.findViewById(R.id.ll_shadow_share);
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        imgBg.setVisibility(View.VISIBLE);
        llShadow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HelpTools.insertCommonXml(HelpTools.ShadowCampaign, getString(R.string.submit));
                if (imgBg.getVisibility() == View.VISIBLE) {
                    imgBg.setVisibility(View.GONE);
                    imgBgTwo.setVisibility(View.VISIBLE);
                } else {
                    cdm.dismiss();
                    if (isShowRequest) {
                        shareCtaAndCti(DetailContentActivity.this, content, true);
                    }
                }
            }
        });

        Window win = cdm.dg.getWindow();

        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        lp.dimAmount = 0.2f;

        win.setAttributes(lp);
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

}

