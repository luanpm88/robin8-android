package com.robin8.rb.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.extras.alipay.PayResult;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;


/**
    立即充值
 * 支付宝充值页面
 */
public class RechargeActivity extends BaseActivity {

    private TextView mRechargeInstantlyTv;
    private EditText mNumberEt;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj instanceof H5PayResultModel) {
                H5PayResultModel model = (H5PayResultModel) msg.obj;
                String returnUrl = model.getReturnUrl();
                String resultCode = model.getResultCode();
                return;
            }

            PayResult payResult = new PayResult((String) msg.obj);
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                try {
                    float number = Float.parseFloat(mNumberEt.getText().toString());
                    mBrandAmountF += number;
                } catch (Exception e) {

                }
               startActivity(new Intent(RechargeActivity.this, PaySuccessActivity.class));
               finish();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    CustomToast.showShort(RechargeActivity.this, "支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    CustomToast.showShort(RechargeActivity.this, "支付失败,请确保已安装支付宝");
                }
            }
        }
    };
    private WProgressDialog mWProgressDialog;
    private BasePresenter mBasePresenter;
    private float mBrandAmountF;
    private int SDK_CHECK_FLAG;
    private EditText mInviteCodeEt;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.recharge));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_recharge, mLLContent, true);

        mRechargeInstantlyTv = (TextView) view.findViewById(R.id.tv_recharge_instantly);
        mNumberEt = (EditText) view.findViewById(R.id.et_num);
        mInviteCodeEt = (EditText) view.findViewById(R.id.et_invite_code);
        mRechargeInstantlyTv.setOnClickListener(this);

        Intent intent = getIntent();
        mBrandAmountF = intent.getFloatExtra("brand_amount", 0);
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.ADVERTISER_RECHARGE;
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        if(isDoubleClick()){
            return;
        }
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_recharge_instantly:
                skipToRecharge();
                break;
        }
    }

    private void skipToRecharge() {
        String rechargeNumber = mNumberEt.getText().toString();
        if (TextUtils.isEmpty(rechargeNumber)) {
            CustomToast.showShort(this, "请输入充值金额");
            return;
        }
        float rechargeNumberI = 0;
        try {
            rechargeNumberI = Float.parseFloat(rechargeNumber);
        } catch (Exception e) {
            CustomToast.showShort(this, "请检查充值金额格式");
            return;
        }

//        if (rechargeNumberI < 100) {
//            CustomToast.showShort(this, "最低充值金额100元");
//            return;
//        }

        String inviteCode = mInviteCodeEt.getText().toString();
        requestAlipayurl(rechargeNumberI, inviteCode);
    }

    private void requestAlipayurl(float rechargeNumberI, String inviteCode) {
        mWProgressDialog = WProgressDialog.createDialog(this);
        mWProgressDialog.show();
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("credits", rechargeNumberI);
        requestParams.put("invite_code", inviteCode);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.BRAND_RECHARGE_URL), requestParams, new RequestCallback() {
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
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);

                if (baseBean == null) {
                    CustomToast.showShort(RechargeActivity.this, getString(R.string.please_data_wrong));
                    return;
                }

                if (baseBean.getError() == 0) {
                    String alipayUrl = baseBean.getAlipay_url();
                    pay(alipayUrl);
                } else {
                    CustomToast.showShort(BaseApplication.getContext(), baseBean.getDetail());
                }
            }
        });
    }

    private void pay(final String url) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                String result = alipay.pay(url, true);
                Message msg = new Message();
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void h5Pay(final String url) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                H5PayResultModel h5PayResultModel = alipay.h5Pay(url, true);
                Message msg = new Message();
                msg.obj = h5PayResultModel;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
//    public void check(View v) {
//        Runnable checkRunnable = new Runnable() {
//
//            @Override
//            public void run() {
//                // 构造PayTask 对象
//                PayTask payTask = new PayTask(RechargeActivity.this);
//                // 调用查询接口，获取查询结果
//                boolean isExist = payTask.
//
//                Message msg = new Message();
//                msg.what = SDK_CHECK_FLAG;
//                msg.obj = isExist;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        Thread checkThread = new Thread(checkRunnable);
//        checkThread.start();
//
//    }
    @Override
    public void finish() {
        Intent intent = getIntent();
        intent.putExtra("brand_amount", mBrandAmountF);
        setResult(SPConstants.OREDERPAYACTIVITY, intent);
        super.finish();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

}
