package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.net.ParseException;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.ui.model.LoginBean;
import com.robin8.rb.ui.model.WithdrawBean;
import com.robin8.rb.ui.module.mine.model.AlipayModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.tendcloud.appcpa.TalkingDataAppCpa;


/**
 * 提现页面
 */
public class WithdrawCashActivity extends BaseActivity {

    private TextView mBindInfoTv;
    private View mBindLl;
    private EditText mNumberEt;
    private TextView mSubmitTv;
    private boolean mIsBindedB;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private String mAlipayName;
    private String mAlipayAccount;
    private TextView mBindNameTv;
    private boolean mWithCashedB;
    private LoginBean mLoginBean;
    private float mAvailAmountI;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.withdraw));
    }

    @Override
    public void initView() {

        Intent intent = getIntent();
        String availAmountStr = intent.getStringExtra("avail_amount");
        if(!TextUtils.isEmpty(availAmountStr)){
            try{
                mAvailAmountI = Float.parseFloat(availAmountStr);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        View view = LayoutInflater.from(this).inflate(R.layout.activity_withdraw, mLLContent, true);
        mBindInfoTv = (TextView) view.findViewById(R.id.tv_bind_info);
        mBindNameTv = (TextView) view.findViewById(R.id.tv_bind_name);

        mBindLl = view.findViewById(R.id.ll_bind);
        mNumberEt = (EditText) view.findViewById(R.id.et_num);
        mSubmitTv = (TextView) view.findViewById(R.id.tv_submit);
        mSubmitTv.setOnClickListener(this);
        mBindLl.setOnClickListener(this);
        mLoginBean = BaseApplication.getInstance().getLoginBean();
        LoginBean.KolEntity kol = mLoginBean.getKol();
        mAlipayAccount = kol.getAlipay_account();
        mAlipayName = kol.getAlipay_name();
        initData();
        getDataFromNet();
    }

    @Override
    protected void onResume() {
        mPageName = StatisticsAgency.MY_INCOME_CASH;
        super.onResume();
    }

    private void initData() {
        if (TextUtils.isEmpty(mAlipayAccount)) {
            mBindInfoTv.setText(getString(R.string.un_binded));
            mBindNameTv.setText(getString(R.string.click_bind));
            mIsBindedB = false;
        } else {
            mBindInfoTv.setText(mAlipayAccount);
            mBindNameTv.setText(mAlipayName);
            mIsBindedB = true;
        }
    }

    private void getDataFromNet() {

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.KOLS_ALIPAY_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                AlipayModel alipayModel = GsonTools.jsonToBean(response, AlipayModel.class);
                if (alipayModel.getError() == 0) {
                    String idCard = alipayModel.getId_card();
                    if(mLoginBean != null){
                        mLoginBean.setId(idCard);
                        BaseApplication.getInstance().setLoginBean(mLoginBean);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_submit:
                submit();
                break;
            case R.id.ll_bind:
                bindAlipay();
                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = getIntent();
        intent.putExtra("withcashed", mWithCashedB);
        setResult(SPConstants.WALLETACTIVIRY, intent);
        super.finish();
    }

    private void submit() {
        String numberETstr = mNumberEt.getText().toString();

        if (TextUtils.isEmpty(numberETstr)) {
            CustomToast.showShort(this, R.string.robin406);
            return;
        }

        float number = 0;
        try {
            number = Float.parseFloat(numberETstr);
        } catch (ParseException e) {
            CustomToast.showShort(this, R.string.robin407);
            return;
        }

        if (number < 50) {
            CustomToast.showShort(this, R.string.robin408);
            return;
        }

        if (number > mAvailAmountI) {
            CustomToast.showShort(this, R.string.robin409);
            return;
        }


        if (!mIsBindedB) {
            CustomToast.showShort(this, R.string.robin410);
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mWProgressDialog = WProgressDialog.createDialog(this);
        mWProgressDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("credits", numberETstr);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.WITHDRAW_APPLY_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                WithdrawBean withdrawBean = GsonTools.jsonToBean(response, WithdrawBean.class);
                if (withdrawBean.getError() == 0) {
                    //提现成功埋点
                    TalkingDataAppCpa.onCustEvent3();
                    mWithCashedB = true;
                    finish();
                    CustomToast.showShort(WithdrawCashActivity.this, R.string.robin411);
                }

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }
        });
    }

    private void bindAlipay() {
        Intent intent = new Intent(WithdrawCashActivity.this, BindAlipayActivity.class);
        startActivityForResult(intent, SPConstants.WITHDRAWCASHACTIVITY);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SPConstants.WITHDRAWCASHACTIVITY && data != null) {
            mAlipayName = data.getStringExtra("alipay_name");
            mAlipayAccount = data.getStringExtra("alipay_account");
            initData();
        }
    }
}
