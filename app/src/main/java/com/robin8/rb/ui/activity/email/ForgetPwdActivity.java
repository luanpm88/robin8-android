package com.robin8.rb.ui.activity.email;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.ForgetPwdPresenter;
import com.robin8.rb.view.ILoginView;

public class ForgetPwdActivity extends BaseActivity implements ILoginView {

    private ForgetPwdPresenter mUserPwdPresenter;
    private EditText etEmailNum;
    private EditText etEmailCheckNum;
    private  TextView tvCheckNum;

    @Override
    public void setTitleView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mTVCenter.setText(ForgetPwdActivity.this.getResources().getString(R.string.forget_pwd));
    }

    @Override
    public void initView() {

        View view = LayoutInflater.from(this).inflate(R.layout.activity_set_user_pwd, mLLContent, true);
        //忘记密码
        etEmailNum = ((EditText) view.findViewById(R.id.et_email_num));
        etEmailCheckNum = ((EditText) view.findViewById(R.id.et_email_checknum));
        tvCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        Button btnNext = (Button) view.findViewById(R.id.bt_next);
        //重置密码
        mUserPwdPresenter = new ForgetPwdPresenter(ForgetPwdActivity.this, this);

        btnNext.setOnClickListener(this);
        tvCheckNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_checknum:
                //获取验证码
                mUserPwdPresenter.getCheckNumber();
                break;
            case R.id.bt_next:
                mUserPwdPresenter.nextStep();
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }

    @Override
    public String getEmailNumber() {
        String emailNumber = "";
        if (etEmailNum != null) {
            emailNumber = etEmailNum.getText().toString();
        }
        return emailNumber;
    }

    @Override
    public String getEmailPwd() {
        return null;
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public String getCheckCode() {
        String checkCode = "";
        if (etEmailCheckNum != null) {
            checkCode = etEmailCheckNum.getText().toString();
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

        return tvCheckNum;
    }
}
