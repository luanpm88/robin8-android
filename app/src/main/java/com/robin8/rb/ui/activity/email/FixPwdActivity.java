package com.robin8.rb.ui.activity.email;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.ForgetPwdPresenter;
import com.robin8.rb.view.ILoginView;

public class FixPwdActivity extends BaseActivity implements ILoginView {
    private EditText etSetNewPwd;
    private EditText etSetAgain;
    private ForgetPwdPresenter mUserPwdPresenter;
    public String textEmailNum;
    private String textVtoken;

    @Override
    public void setTitleView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mTVCenter.setText(FixPwdActivity.this.getResources().getString(R.string.fix_pwd));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_fix_pwd, mLLContent, true);
        etSetNewPwd = ((EditText) view.findViewById(R.id.et_new_pwd));
        etSetAgain = ((EditText) view.findViewById(R.id.et_new_pwd_again));
        Button btnOver = (Button) view.findViewById(R.id.bt_over);
        initData();
        mUserPwdPresenter = new ForgetPwdPresenter(FixPwdActivity.this, FixPwdActivity.this);
        btnOver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mUserPwdPresenter.fixOver();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        textEmailNum = intent.getStringExtra(EmailAddInformationActivity.EXTRA_EMAIL_NUM);
        textVtoken = intent.getStringExtra(EmailAddInformationActivity.EXTRA_EMAIL_TOKEN);
    }

    @Override
    protected void executeOnclickLeftView() {

    }

    @Override
    protected void executeOnclickRightView() {

    }

    /**
     输入密码
     @return
     */
    @Override
    public String getPhoneNumber() {
        String emailPwd = "";
        if (etSetNewPwd != null) {
            emailPwd = etSetNewPwd.getText().toString();
        }
        return emailPwd;
    }

    @Override
    public String getCheckCode() {
        return textVtoken;
    }

    @Override
    public String getInvitationCode() {
        return null;
    }

    @Override
    public void clearEdit(int i) {

    }

    @Override
    public String getEmailNumber() {
        return textEmailNum;
    }

    /**
     确认新密码
     @return
     */
    @Override
    public String getEmailPwd() {
        String emailPwdAgain = "";
        if (etSetAgain != null) {
            emailPwdAgain = etSetAgain.getText().toString();
        }
        return emailPwdAgain;
    }

    @Override
    public View getTv() {
        return null;
    }
}
