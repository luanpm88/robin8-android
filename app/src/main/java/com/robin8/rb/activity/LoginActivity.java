package com.robin8.rb.activity;

import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.LoginPresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.ILoginView;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 @author Figo
 @Description 登录
 @date 2016年7月5日 */
public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private EditText mETPhoneNumber;
    private EditText mETCheckNum;
    private String phoneNumber;
    private String checkNum;
    private TextView mTVLoginInfo;
    private String mKolUuid;
    private boolean mIsFromEffectiveB;
    private TextView mTVCheckNum;
    private View mBTNLogin;
    private LoginPresenter mLoginPresenter;
    private EditText mETInvitationCode;
    private LinearLayout llToEmailRegister;
    private EditText etEmailNum;
    private EditText etEmailPwd;
    private TextView tvToPhoneLogin;
    private TextView tvToEamaiLogin;
    private int which = 0;
    private LinearLayout llEmailLogin;
    private LinearLayout llPhoneNumLogin;
    private TextView mTVTourist;

    @Override
    public void setTitleView() {
        mLLTitleBar.setBackgroundResource(android.R.color.transparent);
        mTVCenter.setText(LoginActivity.this.getResources().getString(R.string.register_or_login));
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_login_main, mLLContent, true);
        mLLRoot.setBackgroundResource(R.mipmap.mine_bg);
        mTVCenter.getPaint().setFakeBoldText(true);
        mETPhoneNumber = (EditText) view.findViewById(R.id.et_phonenum);
        mETCheckNum = (EditText) view.findViewById(R.id.et_checknum);
        mETInvitationCode = ((EditText) view.findViewById(R.id.et_invitation_code));
        mTVLoginInfo = (TextView) view.findViewById(R.id.tv_login_info);
        mTVCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        mBTNLogin = view.findViewById(R.id.bt_login);

        mTVTourist = (TextView) view.findViewById(R.id.tv_tourist);
        View mIBWeixin = view.findViewById(R.id.ib_weixin);
        View mIBWeibo = view.findViewById(R.id.ib_weibo);
        View mIBQQ = view.findViewById(R.id.ib_qq);
        //邮箱
        llPhoneNumLogin = ((LinearLayout) view.findViewById(R.id.ll_phone_login));
        llEmailLogin = ((LinearLayout) view.findViewById(R.id.ll_email_login));

        tvToPhoneLogin = ((TextView) view.findViewById(R.id.tv_to_phoneNum));
        llToEmailRegister = ((LinearLayout) view.findViewById(R.id.ll_to_register));
        etEmailNum = ((EditText) view.findViewById(R.id.et_email_num));
        etEmailPwd = ((EditText) view.findViewById(R.id.et_password));
        tvToEamaiLogin = ((TextView) view.findViewById(R.id.tv_to_email));

        tvToEamaiLogin.setOnClickListener(this);
        tvToPhoneLogin.setOnClickListener(this);
        llToEmailRegister.setOnClickListener(this);
        mTVCheckNum.setOnClickListener(this);
        mBTNLogin.setOnClickListener(this);
        mTVTourist.setOnClickListener(this);
        mIBWeixin.setOnClickListener(this);
        mIBWeibo.setOnClickListener(this);
        mIBQQ.setOnClickListener(this);

        mTVLoginInfo.setText(Html.fromHtml(getString(R.string.click_login_approve) + "<font color=#2dcad0>" + getString(R.string.serviece_protocol) + "</font>"));
        mLoginPresenter = new LoginPresenter(LoginActivity.this, this);
        mLoginPresenter.init();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_checknum:
                mLoginPresenter.getCheckNumber();
                break;
            case R.id.bt_login:
                mLoginPresenter.login(which);
                break;
            case R.id.tv_tourist:
                mLoginPresenter.backMain(0);
                break;
            case R.id.ib_weixin:
                CustomToast.showShort(this, "前往微信中···");
                mLoginPresenter.authorize(new Wechat(this));
                break;
            case R.id.ib_weibo:
                mLoginPresenter.authorize(new SinaWeibo(this));
                break;
            case R.id.ib_qq:
                mLoginPresenter.authorize(new QQ(this));
                // finish();
                break;
            case R.id.iv_back:
                mLoginPresenter.backMain(0);
                break;
            case R.id.ll_to_register:
                //邮箱注册
                mLoginPresenter.toEmailRegister();
                break;
            case R.id.tv_to_phoneNum:
                which = 0;
                llPhoneNumLogin.setVisibility(View.VISIBLE);
                llEmailLogin.setVisibility(View.GONE);
                mTVTourist.setVisibility(View.VISIBLE);
                llToEmailRegister.setVisibility(View.GONE);
                break;
            case R.id.tv_to_email:
                which = 1;
                llPhoneNumLogin.setVisibility(View.GONE);
                llEmailLogin.setVisibility(View.VISIBLE);
                mTVTourist.setVisibility(View.GONE);
                llToEmailRegister.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    protected void executeOnclickLeftView() {
        //mLoginPresenter.backMain();
        // finish();
    }

    @Override
    protected void executeOnclickRightView() {
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
        String invitationCode = "";
        if (mETInvitationCode != null) {
            invitationCode = mETInvitationCode.getText().toString().trim();
        }
        return invitationCode;
    }

    @Override
    public void clearEdit(int i) {
        if (i == 3) {
            if (mETInvitationCode != null) {
                mETInvitationCode.setText("");
            }
        }
    }

    @Override
    public String getEmailNumber() {
        String emailNum = "";
        if (etEmailNum != null) {
            emailNum = etEmailNum.getText().toString().trim();
        }
        return emailNum;
    }

    @Override
    public String getEmailPwd() {
        String emailPwd = "";
        if (etEmailPwd != null) {
            emailPwd = etEmailPwd.getText().toString().trim();
        }
        LogUtil.LogShitou("输入的密码",emailPwd);
        return emailPwd;
    }

    @Override
    public View getTv() {

        return mTVCheckNum;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mLoginPresenter.backMain(0);
        }
        return false;
    }
}
