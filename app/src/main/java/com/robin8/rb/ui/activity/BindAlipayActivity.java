package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.base.constants.SPConstants;
import com.robin8.rb.ui.model.LoginBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.IdcardValidator;

import com.robin8.rb.R;

/**
 * 支付宝绑定 页面
 */
public class BindAlipayActivity extends BaseActivity {


    private TextView mSubmitTv;
    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private EditText mAlipayAccountItemContentEt;
    private EditText mAlipayNameItemContentEt;
    private EditText mIdNumberItemContentEt;
    private Handler mHandler;
    private String mAlipayAccountETstr;
    private String mAlipayNameETstr;
    private String mIDNumberETstr;

    @Override
    public void setTitleView() {
        mTVCenter.setText(this.getText(R.string.alipay_bind));
    }

    @Override
    public void initView() {

        LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
        LoginBean.KolEntity kol = loginBean.getKol();
        String id = loginBean.getId();
        String alipayAccount = kol.getAlipay_account();
        String alipayName = kol.getAlipay_name();

        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_alipay_bind, mLLContent, true);

        View alipayAccountView = view.findViewById(R.id.alipay_account);
        TextView itemAlipayAccountTitleTv = (TextView) alipayAccountView.findViewById(R.id.tv_item_title);
        mAlipayAccountItemContentEt = (EditText) alipayAccountView.findViewById(R.id.et_item_content);
        if (TextUtils.isEmpty(alipayAccount)) {
            mAlipayAccountItemContentEt.setHint(R.string.please_write_alipay_account);
        } else {
            mAlipayAccountETstr = alipayAccount;
            mAlipayAccountItemContentEt.setText(alipayAccount);
        }
        itemAlipayAccountTitleTv.setText(getString(R.string.alipay_account));

        View alipayNameView = view.findViewById(R.id.alipay_name);
        TextView itemAlipayNameTitleTv = (TextView) alipayNameView.findViewById(R.id.tv_item_title);
        mAlipayNameItemContentEt = (EditText) alipayNameView.findViewById(R.id.et_item_content);
        if (TextUtils.isEmpty(alipayName)) {
            mAlipayNameItemContentEt.setHint(R.string.please_write_alipay_name);
        } else {
            mAlipayNameETstr = alipayName;
            mAlipayNameItemContentEt.setText(alipayName);
        }
        itemAlipayNameTitleTv.setText(getString(R.string.alipay_name));

        View idNumber = view.findViewById(R.id.id_number);
        TextView itemIdNumberTitleTv = (TextView) idNumber.findViewById(R.id.tv_item_title);
        mIdNumberItemContentEt = (EditText) idNumber.findViewById(R.id.et_item_content);
        if (TextUtils.isEmpty(id)) {
            mIdNumberItemContentEt.setHint(R.string.please_write_id_number);
        } else {
            mIdNumberItemContentEt.setText(id);
        }
        itemIdNumberTitleTv.setText(getString(R.string.id_card));

        mSubmitTv = (TextView) view.findViewById(R.id.tv_submit);
        mSubmitTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        mAlipayAccountETstr = mAlipayAccountItemContentEt.getText().toString();
        if (TextUtils.isEmpty(mAlipayAccountETstr)) {
            CustomToast.showShort(this, getString(R.string.please_write_alipay_account));
            return;
        }

        mAlipayNameETstr = mAlipayNameItemContentEt.getText().toString();
        if (TextUtils.isEmpty(mAlipayNameETstr)) {
            CustomToast.showShort(this, getString(R.string.please_write_alipay_name));
            return;
        }

        mIDNumberETstr = mIdNumberItemContentEt.getText().toString();
        if (TextUtils.isEmpty(mIDNumberETstr)) {
            CustomToast.showShort(this, getString(R.string.please_write_id_number));
            return;
        }
        IdcardValidator idcardValidator = new IdcardValidator();
        if(!idcardValidator.isValidatedAllIdcard(mIDNumberETstr)){
            CustomToast.showShort(this, getString(R.string.please_check_id_number));
            return;
        }

        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        mWProgressDialog = WProgressDialog.createDialog(this);
        mWProgressDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("alipay_account", mAlipayAccountETstr);
        requestParams.put("alipay_name", mAlipayNameETstr);
        requestParams.put("id_card", mIDNumberETstr);

        mBasePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BIND_ALIPAY_URL), requestParams, new RequestCallback() {

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
                LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                if (loginBean != null && loginBean.getError() == 0) {
                    loginBean.setId(mIDNumberETstr);
                    loginBean.getKol().setAlipay_account(mAlipayAccountETstr);
                    loginBean.getKol().setAlipay_name(mAlipayNameETstr);
                    CustomToast.showShort(BindAlipayActivity.this, "绑定支付宝成功");
                    HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                    HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                    BaseApplication.getInstance().setLoginBean(loginBean);
                    mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            executeOnclickLeftView();
                        }
                    }, 1000);
                }else if(loginBean != null){
                    CustomToast.showShort(BindAlipayActivity.this, loginBean.getDetail());
                }
            }
        });
    }


    @Override
    protected void executeOnclickLeftView() {
        Intent intent = getIntent();
        intent.putExtra("alipay_name", mAlipayNameETstr);
        intent.putExtra("alipay_account", mAlipayAccountETstr);
        setResult(SPConstants.WITHDRAWCASHACTIVITY, intent);
        finish();
    }


    @Override
    protected void executeOnclickRightView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mBasePresenter == null) {
            mBasePresenter = null;
        }
    }
}
