package com.robin8.rb.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.SetUserPwdPresenter;
import com.robin8.rb.view.ILoginView;

public class SetUserPwdActivity extends BaseActivity implements ILoginView {

    private EditText mEditUserPwd;
    private EditText mEditUserPwdAgain;
    private Button btLogin;
    private SetUserPwdPresenter mUserPwdPresenter;

    @Override
    public void setTitleView() {

        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mTVCenter.setText(SetUserPwdActivity.this.getResources().getString(R.string.set_pwd));
    }

    @Override
    public void initView() {

        View view = LayoutInflater.from(this).inflate(R.layout.activity_set_user_pwd, mLLContent, true);
        mEditUserPwd = ((EditText) view.findViewById(R.id.edit_user_pwd));
        mEditUserPwdAgain = ((EditText) view.findViewById(R.id.edit_user_pwd_again));
        btLogin = ((Button) view.findViewById(R.id.bt_login));
        mUserPwdPresenter = new SetUserPwdPresenter(SetUserPwdActivity.this, this);
        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                mUserPwdPresenter.login();
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

    @Override
    public String getPhoneNumber() {

        String phoneNumber = "";
        if (mEditUserPwd != null) {
            phoneNumber = mEditUserPwd.getText().toString();
        }
        return phoneNumber;
    }

    @Override
    public String getCheckCode() {

        String checkCode = "";
        if (mEditUserPwdAgain != null) {
            checkCode = mEditUserPwdAgain.getText().toString();
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

        return null;
    }
}
