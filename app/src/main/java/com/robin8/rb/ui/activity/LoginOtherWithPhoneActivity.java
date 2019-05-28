package com.robin8.rb.ui.activity;

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
    public String getEmailNumber() {
        return null;
    }

    @Override
    public String getEmailPwd() {
        return null;
    }

    @Override
    public View getTv() {

        return mTVCheckNum;
    }


}
