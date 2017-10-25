package com.robin8.rb.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.LoginBindPhonePresenter;
import com.robin8.rb.view.ILoginView;

/**
 * Created by IBM on 2016/8/22.
 */
public class LoginOtherWithPhoneActivity extends BaseActivity implements ILoginView {

    private EditText mETPhoneNumber;
    private EditText mETCheckNum;
    private int from;
    private LoginBindPhonePresenter loginBindPresenter;
    private TextView mTVCheckNum;

    @Override
    public void setTitleView() {

        mLLTitleBar.setBackgroundResource(android.R.color.transparent);

        mTVCenter.setText(R.string.bind_mobile);
    }

    @Override
    public void initView() {

        View view = LayoutInflater.from(this).inflate(R.layout.activity_login_other_with_phone, mLLContent);
        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        mTVCenter.getPaint().setFakeBoldText(true);
        mETPhoneNumber = (EditText) view.findViewById(R.id.et_phonenum);
        mETCheckNum = (EditText) view.findViewById(R.id.et_checknum);
        mTVCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        View mCompleteTv = view.findViewById(R.id.tv_complete);
        mTVCheckNum.setOnClickListener(this);
        mCompleteTv.setOnClickListener(this);
        loginBindPresenter = new LoginBindPhonePresenter(LoginOtherWithPhoneActivity.this, this);
        loginBindPresenter.init();
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
                loginBindPresenter.complete();
                break;
            case R.id.tv_checknum:
                loginBindPresenter.getCheckNumber();
                break;
        }
    }

    //    private void complete() {
    //            String phoneNumber = mETPhoneNumber.getText().toString();
    //            String checkNum = mETCheckNum.getText().toString();
    //
    //            if (TextUtils.isEmpty(phoneNumber)) {
    //                CustomToast.showShort(this, "请输入手机号码!");
    //                return;
    //            }
    //
    //            if (TextUtils.isEmpty(checkNum)) {
    //                CustomToast.showShort(this, "请输入验证码!");
    //                return;
    //            }
    //
    //            if (!RegExpUtil.checkMobile(phoneNumber)) {
    //            CustomToast.showShort(this, "请输入正确的手机号码!");
    //            return;
    //        }
    //BasePresenter basePresenter = new BasePresenter();
    //        RequestParams requestParams = new RequestParams();
    //        requestParams.put("mobile_number", phoneNumber);
    //        requestParams.put("code", checkNum);
    //        basePresenter.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BIND_MOBILE_URL), requestParams, new RequestCallback() {
    //            @Override
    //            public void onError(Exception e) {
    //
    //            }
    //
    //            @Override
    //            public void onResponse(String response) {
    //                parseJson(response);
    //            }
    //        });
    //    }
    //
    //    private void parseJson(String response) {
    //        LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
    //
    //        if (loginBean == null) {
    //            CustomToast.showShort(this, getString(R.string.please_data_wrong));
    //            return;
    //        }
    //
    //        if (loginBean.getError() == 0) {
    //            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
    //            HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
    //            BaseApplication.getInstance().setLoginBean(loginBean);
    //            if (BaseApplication.getInstance().hasLogined()) {
    //                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
    //            }
    //            LoginHelper.loginSuccess(loginBean, from, this);
    //        } else {
    //            CustomToast.showShort(this, loginBean.getDetail());
    //        }
    //    }

    @Override
    public String getPhoneNumber() {

        String phoneNumber = "";
        if (mETPhoneNumber != null) {
            phoneNumber = mETPhoneNumber.getText().toString();
        }
        return phoneNumber;
    }

    @Override
    public String getCheckCode() {

        String checkCode = "";
        if (mETCheckNum != null) {
            checkCode = mETCheckNum.getText().toString();
        }
        return checkCode;
    }

    @Override
    public String getInvitationCode() {
        return null;
    }

    @Override
    public void clearEdit(int i) {

    }

    @Override
    public View getTv() {

        return mTVCheckNum;
    }


}
