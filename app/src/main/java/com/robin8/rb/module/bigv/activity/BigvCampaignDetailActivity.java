package com.robin8.rb.module.bigv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseDataActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.module.bigv.adapter.PreKolsAdapter;
import com.robin8.rb.module.bigv.model.BigvDetailModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 大V活动详情页 */
public class BigvCampaignDetailActivity extends BaseDataActivity implements View.OnClickListener {

    @Bind(R.id.iv)
    ImageView iv;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_campaign_title)
    TextView tvCampaignTitle;
    @Bind(R.id.tv_brand_info)
    TextView tvBrandInfo;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_pre_kols_count)
    TextView tvPreKolsCount;
    @Bind(R.id.tv_plat)
    TextView tvPlat;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_brand_name)
    TextView tvBrandName;
    @Bind(R.id.tv_brand_dec)
    TextView tvBrandDec;
    @Bind(R.id.card_bottom)
    CardView cardBottom;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;
    @Bind(R.id.iv_author)
    ImageView ivAuthor;
    @Bind(R.id.tv_author_name)
    TextView tvAuthorName;

    private List<BigvDetailModel.CreationBean.SelectedKolsBean> mKolList = new ArrayList<>();
    private PreKolsAdapter preKolsAdapter;
    private static final String STATE_UNPASSED = "unpassed";
    private static final String STATE_PASSED = "passed";
    private static final String STATE_EDNED = "ended";//结束
    private static final String STATE_CLOSED = "closed";//关闭

    private static final String STATE_PENDING = "pending";//待审核
    private static final String STATE_REJECTED = "rejected";//不合作
    private static final String STATE_UNPAY = "unpay";//确认合作，待付款
    private static final String STATE_PAID = "paid";//确认合作，已付款
    private static final String STATE_UPLOADED = "uploaded";//上传作品，待验收
    private static final String STATE_APPROVED = "approved";//验收满意，待结款
    private static final String STATE_FINISHED = "finished";//完成，结款成功，合作完成


    public static final String CAMPAIGN_ID = "id";
    private int campaignId;
    private String campaignStatus;
    private String kolStatus;
    private String notice;
    private boolean isRead=false;
    private List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean> terraceInfo = new ArrayList<>();
    private List<BigvDetailModel.CreationBean.MyTendersBean> myTenders = new ArrayList<>();
    private WProgressDialog mWProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigv_campaign_detail);
        ButterKnife.bind(this);
        mPageName = StatisticsAgency.BIGV_DETAIL;
        Intent intent = getIntent();
        campaignId = intent.getIntExtra(CAMPAIGN_ID, 0);
        initView();
        initData(campaignId);
    }

    public void initView() {
        ivBack.setOnClickListener(this);
        cardBottom.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        preKolsAdapter = new PreKolsAdapter(this, mKolList);
        recyclerView.setAdapter(preKolsAdapter);
    }


    private void initData(final int id) {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        final BasePresenter mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.BIGV_LIST_DETAIL_URL) + id, null, new RequestCallback() {

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
                LogUtil.LogShitou("大v活动详情", HelpTools.getUrl(CommonConfig.BIGV_LIST_DETAIL_URL) + id + "//////" + response);
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        if (! TextUtils.isEmpty(response)) {
            BigvDetailModel bigvDetailModel = GsonTools.jsonToBean(response, BigvDetailModel.class);
            if (bigvDetailModel.getError() == 0) {
                BigvDetailModel.CreationBean.BaseInfoBean base_info = bigvDetailModel.getCreation().getBase_info();
                List<BigvDetailModel.CreationBean.SelectedKolsBean> selected_kols = bigvDetailModel.getCreation().getSelected_kols();
                List<BigvDetailModel.CreationBean.MyTendersBean> my_tenders = bigvDetailModel.getCreation().getMy_tenders();

                if (null != base_info) {
                    tvCampaignTitle.setText(StringUtil.checkString(base_info.getName()));
                    tvBrandInfo.setText(StringUtil.checkString(base_info.getDescription()));
                    if (! TextUtils.isEmpty(base_info.getImg_url())) {
                        BitmapUtil.loadImage(this, base_info.getImg_url(), iv);
                    }
                    tvAuthorName.setText(StringUtil.checkString(base_info.getBrand_info().getName()));
                    if (!TextUtils.isEmpty(base_info.getNotice())){
                        notice = base_info.getNotice();
                    }
                    if (!TextUtils.isEmpty(base_info.getBrand_info().getAvatar_url())){
                        BitmapUtil.loadImage(this, base_info.getBrand_info().getAvatar_url(), ivAuthor);
                    }
                    tvTime.setText(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", StringUtil.checkString(base_info.getStart_at())) + " - \n" + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", StringUtil.checkString(base_info.getEnd_at())));
                    tvPrice.setText(StringUtil.checkString(base_info.getPrice_range()));
                    tvPreKolsCount.setText(String.valueOf(base_info.getPre_kols_count()));
                    tvPlat.setText(StringUtil.checkString(base_info.getTerrace_names()));
                    if (null != base_info.getStatus()) {
                        campaignStatus = base_info.getStatus();
                        // setBottom(base_info.getStatus());
                    }
                    BigvDetailModel.CreationBean.BaseInfoBean.TrademarkBean trademark = base_info.getTrademark();
                    if (null != trademark) {
                        tvBrandName.setText(StringUtil.checkString(trademark.getName()));
                        tvBrandDec.setText(StringUtil.checkString(trademark.getDescription()));
                    }
                    List<BigvDetailModel.CreationBean.BaseInfoBean.TerraceInfosBean> terrace_infos = base_info.getTerrace_infos();
                    if (null != terrace_infos) {
                        if (mKolList != null) {
                            terraceInfo.clear();
                        }
                        terraceInfo.addAll(terrace_infos);
                    }
                }
                if (! TextUtils.isEmpty(bigvDetailModel.getCreation().getMy_tender_status())) {
                    kolStatus = bigvDetailModel.getCreation().getMy_tender_status();
                    setBottom(kolStatus);
                } else {
                    setBottom(campaignStatus);
                }
                if (null != selected_kols) {
                    if (selected_kols.size() != 0) {
                        if (mKolList != null) {
                            mKolList.clear();
                        }
                        mKolList.addAll(selected_kols);
                        preKolsAdapter.notifyDataSetChanged();
                    }
                }
                if (null != my_tenders) {
                    if (myTenders != null) {
                        myTenders.clear();
                    }
                    myTenders.addAll(my_tenders);
                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.card_bottom:
                if (BaseApplication.getInstance().hasLogined()) {
                    String applyStatus = BaseApplication.getInstance().getLoginBean().getKol().getRole_apply_status();
                    if (! TextUtils.isEmpty(applyStatus)) {
                        if (applyStatus.equals(STATE_PASSED)) {
//                            if (!TextUtils.isEmpty(notice) && isRead==false){
//                                showNotice(BigvCampaignDetailActivity.this,notice);
//                            }else {
//                                if (!TextUtils.isEmpty(kolStatus)) {
//                                    setBottomJump(kolStatus);
//                                } else {
//                                    setBottomJump(STATE_PASSED);
//                                }
//                            }
                            if (!TextUtils.isEmpty(kolStatus)) {
                                setBottomJump(kolStatus);
                            } else {
                                setBottomJump(STATE_PASSED);
                            }
                        } else {
                            CustomToast.showShort(this, "请先认证");
                        }
                    }
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("from", SPConstants.MAINACTIVITY);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, SPConstants.MAIN_TO_LOGIN);
                }

                break;
        }
    }

    private void setBottomJump(String status) {
        switch (status) {
            case STATE_PASSED:
                //  tvBottom.setText("参加活动");
                Intent intent = new Intent(this, AddPriceActivity.class);
                intent.putExtra(CAMPAIGN_ID, campaignId);
                intent.putExtra(AddPriceActivity.PLATFORM_LIST, (Serializable) terraceInfo);
                startActivityForResult(intent, SPConstants.ADD_PRICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case STATE_PENDING:
                // tvBottom.setText("查看报价");
                Intent intentFix = new Intent(this, AddPriceActivity.class);
                intentFix.putExtra(CAMPAIGN_ID, campaignId);
                intentFix.putExtra(AddPriceActivity.PLATFORM_LIST, (Serializable) terraceInfo);
                intentFix.putExtra(AddPriceActivity.FIX_LIST, (Serializable) myTenders);
                startActivityForResult(intentFix, SPConstants.ADD_PRICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case STATE_PAID://上传作品
                // tvBottom.setText("上传作品");
                Intent intentUp = new Intent(this, UploadActivity.class);
                intentUp.putExtra(CAMPAIGN_ID, campaignId);
                intentUp.putExtra(AddPriceActivity.PLATFORM_LIST, (Serializable) terraceInfo);
                intentUp.putExtra(UploadActivity.STATUS, kolStatus);
                startActivityForResult(intentUp, SPConstants.ADD_PRICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case STATE_FINISHED:
                // tvBottom.setText("结款成功，合作完成");
                Intent intentOver = new Intent(this, UploadActivity.class);
                intentOver.putExtra(CAMPAIGN_ID, campaignId);
                intentOver.putExtra(UploadActivity.STATUS, kolStatus);
                startActivityForResult(intentOver, SPConstants.ADD_PRICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case STATE_UPLOADED:
                //  tvBottom.setText("作品上传，待验收");
                Intent intentFixUp = new Intent(this, UploadActivity.class);
                intentFixUp.putExtra(CAMPAIGN_ID, campaignId);
                intentFixUp.putExtra(UploadActivity.STATUS, kolStatus);
                intentFixUp.putExtra(AddPriceActivity.PLATFORM_LIST, (Serializable) terraceInfo);
                intentFixUp.putExtra(AddPriceActivity.FIX_LIST, (Serializable) myTenders);
                startActivityForResult(intentFixUp, SPConstants.ADD_PRICE);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case STATE_REJECTED:
                tvBottom.setText("不合作");

                break;
            case STATE_UNPAY:
                tvBottom.setText("确认合作，待付款");

                break;
            case STATE_APPROVED:
                tvBottom.setText("验收满意，待结款");

                break;


        }
    }

    private void setBottom(String status) {
        switch (status) {
            case STATE_PENDING:
                tvBottom.setText("查看报价");
                break;
            case STATE_PASSED:
                tvBottom.setText("参加活动");
                break;
            case STATE_UNPASSED:
                tvBottom.setText("报价被拒绝");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_PAID://上传作品
                tvBottom.setText("上传作品");
                break;
            case STATE_EDNED:
                //根据报价情况
                tvBottom.setText("活动已结束");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_FINISHED:

                tvBottom.setText("结款成功，合作完成");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_CLOSED:
                tvBottom.setText("活动已关闭");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_UPLOADED:
                tvBottom.setText("作品上传，待验收");
                break;
            case STATE_REJECTED:
                tvBottom.setText("不合作");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_UNPAY:
                tvBottom.setText("确认合作，待付款");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;
            case STATE_APPROVED:
                tvBottom.setText("验收满意，待结款");
                cardBottom.setCardBackgroundColor(getResources().getColor(R.color.gray_second));
                cardBottom.setClickable(false);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SPConstants.ADD_PRICE:
                    initData(campaignId);
                    break;
                case SPConstants.MAIN_TO_LOGIN:
                    initData(campaignId);
                    break;
            }
        }
    }

    private void showNotice(final Activity activity, String info) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_promit_cpa, null);
        TextView confirmTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_bg);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        confirmTV.setText(R.string.known);
        infoTv.setText(info);
        topTv.setText("请注意");
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        confirmTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isRead=true;
                cdm.dismiss();
                if (!TextUtils.isEmpty(kolStatus)) {
                    setBottomJump(kolStatus);
                } else {
                    setBottomJump(STATE_PASSED);
                }
            }
        });
        cdm.dg.setCanceledOnTouchOutside(false);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

}
