package com.robin8.rb.activity.email;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

public class EmailRegiterActivity extends BaseActivity {

    public EditText etEmailNum;
    public TextView tvEmailCheckNum;
    public EditText etEmailCheckNum;
    public Button btnNext;

    @Override
    public void setTitleView() {
        mTVCenter.setText("邮箱注册");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_email_regiter, mLLContent, true);
        etEmailNum = ((EditText) view.findViewById(R.id.et_email_num));
        tvEmailCheckNum = ((TextView) view.findViewById(R.id.tv_email_checknum));
        etEmailCheckNum = ((EditText) view.findViewById(R.id.et_email_checknum));
        btnNext = ((Button) view.findViewById(R.id.bt_next));
        btnNext.setOnClickListener(this);
        tvEmailCheckNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_email_checknum:
                getEmailCode();
                break;
            case R.id.bt_next:
                getNextStep();
                break;
        }
    }

    private void getNextStep() {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(EmailRegiterActivity.this);
        }
        mWProgressDialog.show();
        mRequestParams.put("email", etEmailNum.getText().toString().trim());
        mRequestParams.put("valid_code", etEmailCheckNum.getText().toString().trim());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.EMAIL_CODE_CHECK_URL), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("验证Email验证码", response);
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError()==0){
                    Intent intent = new Intent(EmailRegiterActivity.this, EmailAddInformationActivity.class);
                    intent.putExtra(EmailAddInformationActivity.EXTRA_EMAIL_NUM,etEmailNum.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }

    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;

    private void getEmailCode() {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(EmailRegiterActivity.this);
        }
        mWProgressDialog.show();
        mRequestParams.put("email", etEmailNum.getText().toString().trim());
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.EMAIL_CODE_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("获取Email验证码", response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    CustomToast.showShort(EmailRegiterActivity.this, baseBean.getAlert());
                }
            }
        });
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
