package com.robin8.rb.ui.module.first.prenster;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.ui.activity.OrederPayActivity;
import com.robin8.rb.ui.activity.PaySuccessActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.LaunchRewordModel;
import com.robin8.rb.ui.module.first.activity.ModifyRewordActivity;
import com.robin8.rb.ui.module.mine.model.LaunchRewordPayModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.presenter.PresenterI;
import com.robin8.rb.task.CampaignTask;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DateUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.ILaunchRewordSecondView;

import static android.app.Activity.RESULT_CANCELED;

/**
 @author Figo */
public class LaunchRewordSecondPresenter extends BasePresenter implements PresenterI {

    private final ILaunchRewordSecondView mIUserView;
    private Activity mActivity;
    private String totalConsume;
    private boolean mUseKolAmountB;
    private LaunchRewordModel.Campaign mCampaign;
    private int mID;
    private WProgressDialog mWProgressDialog;
    private LaunchRewordModel mLaunchRewordModel;
    private boolean clickable;
    private CampaignTask mCampaignTask;
    private int from;

    public LaunchRewordSecondPresenter(Activity activity, ILaunchRewordSecondView userView) {
        mActivity = activity;
        mIUserView = userView;
    }

    public void init() {
        initData();
        load();
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        mID = intent.getIntExtra("id", 0);
        from = intent.getIntExtra("from", - 1);
        Object obj = intent.getSerializableExtra("launchrewordmodel");
        if (obj != null && obj instanceof LaunchRewordModel) {
            mLaunchRewordModel = (LaunchRewordModel) intent.getSerializableExtra("launchrewordmodel");
        } else {
            return;
        }
        //        updateView();
    }

    /**
     加载网络
     */
    private void load() {
        mWProgressDialog = WProgressDialog.createDialog(mActivity);
        mWProgressDialog.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", mID);
        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CAMPAIGN_DETAIL_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("修改活动详情", "===>" + response);
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
        totalConsume = StringUtil.deleteZero(mCampaign.getBudget());
        setBottomView(mCampaign);
        // setSwitchView(mCampaign);
        //setSwitchCredit(mLaunchRewordModel);
        // setUseKolAmount(true);
        float ammount = mCampaign.getBudget() - mLaunchRewordModel.getKol_amount();
        if (ammount <= 0) {
            mIUserView.setCountTv(String.valueOf(0));
        } else {
            mIUserView.setCountTv(StringUtil.deleteZero(String.valueOf(ammount)));
        }

        mIUserView.setActivityTitleTv(mCampaign.getName());
        mIUserView.setActivityTimeTv(DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", startTime) + " - " + DateUtil.formatTime("yyyy-MM-dd'T'HH:mm:ssZ", endTime));
        mIUserView.setBrandInfoTv(mCampaign.getDescription());
        mIUserView.setTotalConsumeTv("¥ " + totalConsume);

        switch (countWay) {
            case "click":
                mIUserView.setCountWayTv(mActivity.getString(R.string.click) + " | ¥ " + everyConsume);
                break;
            case "post":
                mIUserView.setCountWayTv(mActivity.getString(R.string.post) + " | ¥ " + everyConsume);
                break;

            case "simple_cpi":
                mIUserView.setCountWayTv(mActivity.getString(R.string.download) + " | ¥ " + everyConsume);
                break;

            case "cpt":
                mIUserView.setCountWayTv(mActivity.getString(R.string.task) + " | ¥ " + everyConsume);
                break;
        }

        mIUserView.setImageView(path);
        mUseKolAmountB = true;
        mIUserView.setViewSwitch(mUseKolAmountB);
        float credit = (mLaunchRewordModel.getKol_credit());
        float amount = mCampaign.getBudget() - credit/10;
        if (mUseKolAmountB) {
            if (amount <= 0) {
                mIUserView.setCountTv(String.valueOf(0));
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mCampaign.getBudget()));
            } else {
                mIUserView.setCountTv(StringUtil.deleteZero(amount));
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mLaunchRewordModel.getKol_credit()/10));
            }
        }
    }


    private void setBottomView(LaunchRewordModel.Campaign campaign) {
        TextView payInstantlyTv = mIUserView.getPayInstantlyTv();
        TextView rightTv = mIUserView.getRightTv();
        clickable = true;
        switch (campaign.getStatus()) {
            case "unpay"://未付款或部分付款(campaign 的need_pay_amount > 0 的时候 都是 未付款的)	string
                rightTv.setVisibility(View.VISIBLE);
                payInstantlyTv.setText(mActivity.getString(R.string.pay_instantly));
                clickable = true;
                break;
            case "unexecute"://付款后 运营人员审核中	string
                rightTv.setVisibility(View.VISIBLE);
                payInstantlyTv.setText(mActivity.getString(R.string.checking));
                clickable = false;
                break;
            case "rejected"://审核拒绝	string
                payInstantlyTv.setText(mActivity.getString(R.string.checked_rejected));
                clickable = false;
                break;
            case "agreed"://审核通过， 活动未开始前 的状态(即将执行)	string
                payInstantlyTv.setText(mActivity.getString(R.string.checked_passed));
                clickable = false;
                break;
            case "executing"://已开始 执行中	string
                payInstantlyTv.setText(mActivity.getString(R.string.executing));
                clickable = false;
                break;
            case "executed"://活动结束 未结算	string
                payInstantlyTv.setText(mActivity.getString(R.string.executed));
                clickable = false;
                break;
            case "settled"://已结算
                payInstantlyTv.setText(mActivity.getString(R.string.settled));
                clickable = false;
                break;
        }

        if (clickable) {
            payInstantlyTv.setBackgroundResource(R.color.blue_custom);
        } else {
            payInstantlyTv.setBackgroundResource(R.color.sub_gray_custom);
        }
        payInstantlyTv.setClickable(clickable);
        payInstantlyTv.setEnabled(clickable);
    }


    public void setUseKolAmount(boolean flag) {
        mUseKolAmountB = flag;
        Log.e("xxfigo", "setUseKolAmount" + flag);
        mIUserView.setViewSwitch(flag);
        if (mLaunchRewordModel == null || mCampaign == null) {
            return;
        }
        float credit = (mLaunchRewordModel.getKol_credit());
        float amount = mCampaign.getBudget()  - credit/10;
        if (mUseKolAmountB) {
            if (amount <= 0) {
                mIUserView.setCountTv(String.valueOf(0));
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mCampaign.getBudget()));
            } else {
                mIUserView.setCountTv(StringUtil.deleteZero(amount));
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mLaunchRewordModel.getKol_credit()/10));
            }
        } else {
            mIUserView.setCountTv(StringUtil.deleteZero(String.valueOf(mCampaign.getBudget())));
            if (amount <= 0) {
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mCampaign.getBudget()));
            } else {
                mIUserView.setCreditIncomeTv(" ¥"+StringUtil.deleteZero(mLaunchRewordModel.getKol_credit()/10));
            }
        }
        //        float ammount = mCampaign.getBudget() - mLaunchRewordModel.getKol_amount();
        //        if (mUseKolAmountB) {
        //            if (ammount <= 0) {
        //                mIUserView.setCountTv(String.valueOf(0));
        //            } else {
        //                mIUserView.setCountTv(StringUtil.deleteZero(String.valueOf(ammount)));
        //            }
        //        } else {
        //            mIUserView.setCountTv(StringUtil.deleteZero(String.valueOf(mCampaign.getBudget())));
        //        }
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    public void newSkipToOrder() {
        RequestParams params = new RequestParams();
        params.put("id", mCampaign.getId());
        params.put("use_credit", mUseKolAmountB ? 1 : 0);
        LogUtil.LogShitou("提交参数", "===>" + mUseKolAmountB);
        getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.PAY_BY_CREDIT_URL), params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("积分／去支付", HelpTools.getUrl(CommonConfig.PAY_BY_CREDIT_URL) + response);
                LaunchRewordPayModel model = GsonTools.jsonToBean(response, LaunchRewordPayModel.class);
                if (model == null) {
                    CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                    return;
                }
                if (model.getError() == 0) {
                    LaunchRewordModel.Campaign campaign = mLaunchRewordModel.getCampaign();
                    if (model.getCampaign() != null && model.getCampaign().getNeed_pay_amount() == 0) {
                        CustomToast.showShort(mActivity, R.string.robin405);
                        mActivity.startActivityForResult(new Intent(mActivity, PaySuccessActivity.class), 0);
                    } else {
                        campaign.setAlipay_url(model.getCampaign().getAlipay_url());
                        campaign.setStatus(model.getCampaign().getStatus());
                        campaign.setBrand_amount(model.getCampaign().getBrand_amount());
                        campaign.setNeed_pay_amount(model.getCampaign().getNeed_pay_amount());
                        Intent intent = new Intent(mActivity, OrederPayActivity.class);
                        intent.putExtra("campaign", campaign);
                        mActivity.startActivityForResult(intent, 0);
                    }
                } else {
                    CustomToast.showShort(mActivity, model.getDetail());
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SPConstants.PAY_SUCCESS_ACTIVITY) {
            mActivity.setResult(SPConstants.PAY_SUCCESS_ACTIVITY);
            mActivity.finish();
        } else if (resultCode == SPConstants.LAUNCHREWORDACTIVIRY) {
            mActivity.setResult(SPConstants.LAUNCH_REWORD_SEOND_ACTIVIRY);
            mActivity.finish();
        } else if (requestCode == RESULT_CANCELED) {
            // updateView();
        }
    }

    /**
     撤销活动
     */
    public void cancelCampaign() {
        mCampaignTask = new CampaignTask(mActivity);
        mCampaignTask.cancelCampaign(mCampaign, SPConstants.LAUNCH_REWORD_SEOND_ACTIVIRY, true);
    }

    /**
     修改活动
     */
    public void skipToLaunchReword() {
        if (mCampaign == null) {
            return;
        }
        Intent intent = new Intent(mActivity, ModifyRewordActivity.class);
        intent.putExtra("campaign", mCampaign);
        mActivity.startActivityForResult(intent, SPConstants.MyLaunchCampaignRejectActivity);
    }


    public void onDestroy() {
        if (mCampaignTask != null) {
            mCampaignTask.onDestroy();
            mCampaignTask = null;
        }
    }
}
