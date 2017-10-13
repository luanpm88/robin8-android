package com.robin8.rb.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.extras.alipay.PayResult;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LaunchRewordModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.io.Serializable;

/**
 发布活动支付页面
 */
public class OrederPayActivity extends BaseActivity {

    private static final int BRAND_AMMOUNT = 1;
    private static final int ALIPAY = 2;
    private static final String BALANCE = "balance";
    private TextView mAccountIncomeTv;//¥ 9.4
    private ImageView mIVBtn1;
    private ImageView mIVBtn2;
    private TextView mRechargeInstantlyTv;
    private TextView mPayNumberTv;
    private TextView mPayConfirmTv;
    private LaunchRewordModel.Campaign mCampaign;
    private BasePresenter mBasePresenter;
    private int mPayWay = BRAND_AMMOUNT;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            Log.e("xxfigo", "resultStatus=" + resultStatus);
            if (TextUtils.equals(resultStatus, "9000")) {
                checkAlipay();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    CustomToast.showShort(OrederPayActivity.this, "支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    CustomToast.showShort(OrederPayActivity.this, "支付失败");
                }
            }
        }
    };
    private WProgressDialog mWProgressDialog;
    private float mBrandAmountF;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.activity_order_pay));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_order_pay, mLLContent, true);

        mAccountIncomeTv = (TextView) view.findViewById(R.id.tv_account_income);
        mRechargeInstantlyTv = (TextView) view.findViewById(R.id.tv_recharge_instantly);
        mIVBtn1 = (ImageView) view.findViewById(R.id.iv_btn1);
        mIVBtn2 = (ImageView) view.findViewById(R.id.iv_btn2);
        mPayNumberTv = (TextView) view.findViewById(R.id.tv_pay_number);
        mPayConfirmTv = (TextView) view.findViewById(R.id.tv_pay_confirm);

        mRechargeInstantlyTv.setOnClickListener(this);
        mPayConfirmTv.setOnClickListener(this);
        mIVBtn1.setOnClickListener(this);
        mIVBtn2.setOnClickListener(this);
        mIVBtn1.setSelected(true);
        mIVBtn2.setSelected(false);
        initData();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.ADVERTISER_ADD_PAY;
        super.onResume();
    }

    private void initData() {
        Intent intent = getIntent();
        Serializable serializable = intent.getSerializableExtra("campaign");
        if (!(serializable instanceof LaunchRewordModel.Campaign)) {
            return;
        }
        mCampaign = (LaunchRewordModel.Campaign) serializable;
         mBrandAmountF = mCampaign.getBrand_amount();
        mPayNumberTv.setText("支付金额：¥ " + StringUtil.deleteZero(mCampaign.getNeed_pay_amount()));
        mPayConfirmTv.setText("确认支付 ¥ " + StringUtil.deleteZero(mCampaign.getNeed_pay_amount()));
        mAccountIncomeTv.setText("¥ " + StringUtil.deleteZero(mBrandAmountF));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_recharge_instantly:
                skipToRecharge();
                break;
            case R.id.tv_pay_confirm:
                confirmPay();
                break;
            case R.id.iv_btn1:
                if (!mIVBtn1.isSelected()) {
                    mIVBtn1.setSelected(true);
                    mIVBtn2.setSelected(false);
                    mPayWay = BRAND_AMMOUNT;
                }
                break;
            case R.id.iv_btn2:
                if (!mIVBtn2.isSelected()) {
                    mIVBtn1.setSelected(false);
                    mIVBtn2.setSelected(true);
                    mPayWay = ALIPAY;
                }
                break;
        }
    }

    /**
     * 确认支付
     */
    private void confirmPay() {
        if (mPayWay == ALIPAY) {
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(OrederPayActivity.this);
                    String result = alipay.pay(mCampaign.getAlipay_url(), true);
                    Message msg = new Message();
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else {
            mWProgressDialog = WProgressDialog.createDialog(this);
            if (mBasePresenter == null) {
                mBasePresenter = new BasePresenter();
            }
            RequestParams requestParams = new RequestParams();
            requestParams.put("id", mCampaign.getId());
            requestParams.put("pay_way", BALANCE);
            mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BRAND_AMMOUNT_PAY_URL), requestParams, new RequestCallback() {
                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                    LogUtil.LogShitou("账户余额支付===>",HelpTools.getUrl(CommonConfig.BRAND_AMMOUNT_PAY_URL)+"///"+response);
                   // Log.e("xxfigo", "response=" + response);
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);

                    if(baseBean == null){
                        CustomToast.showShort(OrederPayActivity.this, getString(R.string.please_data_wrong));
                        return;
                    }

                    if (baseBean.getError() == 0) {
                        CustomToast.showShort(OrederPayActivity.this, "支付成功");
                        OrederPayActivity.this.startActivityForResult(new Intent(OrederPayActivity.this, PaySuccessActivity.class),0);
                        OrederPayActivity.this.finish();
                    } else {
                        CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
                    }
                }
            });
        }
    }

    /**
     * 立即充值
     */
    private void skipToRecharge() {
        if (mCampaign == null) {
            return;
        }
        Intent intent = new Intent(OrederPayActivity.this, RechargeActivity.class);
        intent.putExtra("brand_amount",mBrandAmountF);
        startActivityForResult(intent, SPConstants.OREDERPAYACTIVITY);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPConstants.OREDERPAYACTIVITY){
            mBrandAmountF = data.getFloatExtra("brand_amount", 0);
            mAccountIncomeTv.setText("¥ " + StringUtil.deleteZero(mBrandAmountF));
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    public void checkAlipay() {
        if (mCampaign == null) {
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", mCampaign.getId());
        requestParams.put("check_pay", 1);
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CAMPAIGN_CHECK_SUCCESS_BY_ALIPAY_URL), requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
               // Log.e("xxfigo", "CAMPAIGN_CHECK_SUCCESS_BY_ALIPAY_URL =" + response);
                LogUtil.LogShitou("支付成功","==>"+response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if(baseBean == null){
                    CustomToast.showShort(OrederPayActivity.this, getString(R.string.please_data_wrong));
                    return;
                }
                if (baseBean.getError() == 0) {
                    CustomToast.showShort(OrederPayActivity.this, "支付成功");
                    OrederPayActivity.this.startActivityForResult(new Intent(OrederPayActivity.this, PaySuccessActivity.class),0);
                    OrederPayActivity.this.finish();
                } else {
                    CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
                }
            }
        });
    }

}
