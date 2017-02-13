package com.robin8.rb.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.LoginHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;

import com.robin8.rb.R;

/**
 * Created by IBM on 2016/8/22.
 */
public class LoginOtherWithPhoneActivity extends BaseActivity {
    private EditText mETPhoneNumber;
    private EditText mETCheckNum;
    private int from;

    @Override
    public void setTitleView() {
        Intent intent = getIntent();
        from = intent.getIntExtra("from", 0);
        mTVCenter.setText(R.string.bind_mobile);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_login_other_with_phone, mLLContent);
        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        mTVCenter.getPaint().setFakeBoldText(true);
        mETPhoneNumber = (EditText) view.findViewById(R.id.et_phonenum);
        mETCheckNum = (EditText) view.findViewById(R.id.et_checknum);
        TextView mTVCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        View mCompleteTv = view.findViewById(R.id.tv_complete);

        mTVCheckNum.setOnClickListener(this);
        mCompleteTv.setOnClickListener(this);
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_complete:
                complete();
                break;
        }
    }

    private void complete() {
        String phoneNumber = mETPhoneNumber.getText().toString();
        String checkNum = mETCheckNum.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            CustomToast.showShort(this, "请输入手机号码!");
            return;
        }

        if (TextUtils.isEmpty(checkNum)) {
            CustomToast.showShort(this, "请输入验证码!");
            return;
        }

        if (!RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(this, "请输入正确的手机号码!");
            return;
        }
        BasePresenter basePresenter = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);
        requestParams.put("code", checkNum);
        basePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BIND_MOBILE_URL), requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                parseJson(response);
            }
        });
    }

    private void parseJson(String response) {
        LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);

        if (loginBean == null) {
            CustomToast.showShort(this, getString(R.string.please_data_wrong));
            return;
        }

        if (loginBean.getError() == 0) {
            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
            HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
            BaseApplication.getInstance().setLoginBean(loginBean);
            if (BaseApplication.getInstance().hasLogined()) {
                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
            }
            LoginHelper.loginSuccess(loginBean, from, this);
        } else {
            CustomToast.showShort(this, loginBean.getDetail());
        }
    }
}
