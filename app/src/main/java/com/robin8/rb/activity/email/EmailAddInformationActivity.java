package com.robin8.rb.activity.email;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.rongcloud.RongCloudBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;

import static com.robin8.rb.R.id.et_email_name;

public class EmailAddInformationActivity extends BaseActivity {
    public static final String EXTRA_EMAIL_NUM = "extra_email_num";
    public static final String EXTRA_EMAIL_TOKEN = "extra_email_token";
    public EditText etEmailName;
    public EditText etEmailPwd;
    public Button btnNext;
    public String textEmailNum;
    private String textVtoken;

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
        textVtoken = intent.getStringExtra(EXTRA_EMAIL_TOKEN);
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
        if (TextUtils.isEmpty(etEmailName.getText().toString().trim())){
            CustomToast.showShort(EmailAddInformationActivity.this,"请输入昵称");
            return;
        }else if (TextUtils.isEmpty(etEmailPwd.getText().toString().trim())){
            CustomToast.showShort(EmailAddInformationActivity.this,getString(R.string.put_pwd));
            return;
        }
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
        mRequestParams.put("vtoken", textVtoken);
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
             //   LogUtil.LogShitou("获取Email最终信息", response);
                LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                if (loginBean == null) {
                    CustomToast.showShort(EmailAddInformationActivity.this, getString(R.string.please_data_wrong));
                    return;
                }
                if (loginBean.getError()==0){
                    HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                    HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                    BaseApplication.getInstance().setLoginBean(loginBean);
                    if (BaseApplication.getInstance().hasLogined()) {
                        NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                    }
                    initGetRongCloud(textEmailNum,loginBean.getKol().getName(),loginBean.getKol().getAvatar_url());
                    Intent intent = new Intent(EmailAddInformationActivity.this, EmailWelcomeActivity.class);
                    intent.putExtra(EmailWelcomeActivity.EXTRA_EMAIL_NAME,etEmailName.getText().toString().trim());
                    startActivity(intent);
                    finish();
                }else {
                    CustomToast.showShort(getApplicationContext(), loginBean.getDetail());
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
    private void initGetRongCloud(String id,String name,String imgUrl) {
        BasePresenter base = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        if (TextUtils.isEmpty(id)){
            //游客
            requestParams.put("userId",CommonConfig.TOURIST_PHONE);
            requestParams.put("name","游客");
            requestParams.put("portraitUri","http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/ad9d7a31d7!avatar");
        }else {
            requestParams.put("userId",id);
            if (TextUtils.isEmpty(name)){
                requestParams.put("name",id);
            }else{
                requestParams.put("name",name);
            }
            if (TextUtils.isEmpty(imgUrl)){
                requestParams.put("portraitUri","http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/22494f2caf!avatar");
            }else{
                requestParams.put("portraitUri",imgUrl);
            }
        }
        base.getDataFromServer(false, HttpRequest.POST, CommonConfig.RONG_CLOUD_URL, requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(EmailAddInformationActivity.this,getString(R.string.no_net));
            }
            @Override
            public void onResponse(String response) {
                RongCloudBean rongCloudBean = GsonTools.jsonToBean(response, RongCloudBean.class);
                if (rongCloudBean.getCode()==200){
                    HelpTools.insertCommonXml(HelpTools.CloudToken,rongCloudBean.getToken());
                }else{
                    HelpTools.insertCommonXml(HelpTools.CloudToken,"");
                }

            }
        });

    }
}
