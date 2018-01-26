package com.robin8.rb.activity.email;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;

import static com.robin8.rb.R.id.et_email_name;

public class EmailAddInformationActivity extends BaseActivity {
    public static final String EXTRA_EMAIL_NUM = "extra_email_num";
    public EditText etEmailName;
    public EditText etEmailPwd;
    public Button btnNext;
    public String textEmailNum;
    public String textEmailCode;

    @Override
    public void setTitleView() {

    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_email_add_information, mLLContent, true);
        etEmailName = ((EditText) view.findViewById(et_email_name));
        etEmailPwd = ((EditText) view.findViewById(R.id.et_email_password));
        btnNext = ((Button) view.findViewById(R.id.bt_next));
        btnNext.setOnClickListener(this);
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        textEmailNum = intent.getStringExtra(EXTRA_EMAIL_NUM);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_next:
                putEmailMsg();
                break;
        }
    }

    private BasePresenter mBasePresenter;
    private WProgressDialog mWProgressDialog;
    private RequestParams mRequestParams;

    private void putEmailMsg() {
        if (mBasePresenter == null) {
            mBasePresenter = new BasePresenter();
        }

        if (mRequestParams == null) {
            mRequestParams = new RequestParams();
        }

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(EmailAddInformationActivity.this);
        }
        mWProgressDialog.show();
        mRequestParams.put("email", textEmailNum);
        mRequestParams.put("name", etEmailName.getText().toString().trim());
        mRequestParams.put("password", etEmailPwd.getText().toString().trim());
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.EMAIL_REGISTER_URL), mRequestParams, new RequestCallback() {

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
                LogUtil.LogShitou("获取Email最终信息", response);
                Intent intent = new Intent(EmailAddInformationActivity.this, EmailWelcomeActivity.class);
                intent.putExtra(EmailWelcomeActivity.EXTRA_EMAIL_NAME,etEmailName.getText().toString().trim());
                startActivity(intent);
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
