package com.robin8.rb.activity;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.presenter.LoginPresenter;
import com.robin8.rb.util.TimerUtil;
import com.robin8.rb.view.ILoginView;

import com.robin8.rb.R;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author Figo
 * @Description 登录
 * @date 2016年7月5日
 */
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
    private TimerUtil mTimer;
    private String mKolUuid;
    private boolean mIsFromEffectiveB;
    private TextView mTVCheckNum;
    private View mBTNLogin;
    private LoginPresenter mLoginPresenter;


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
        mTVLoginInfo = (TextView) view.findViewById(R.id.tv_login_info);
        mTVCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        mBTNLogin = view.findViewById(R.id.bt_login);

        View mTVTourist = view.findViewById(R.id.tv_tourist);
        View mIBWeixin = view.findViewById(R.id.ib_weixin);
        View mIBWeibo = view.findViewById(R.id.ib_weibo);
        View mIBQQ = view.findViewById(R.id.ib_qq);

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
                mLoginPresenter.login();
                break;
            case R.id.tv_tourist:
                finish();
                break;
            case R.id.ib_weixin:
                mLoginPresenter.authorize(new Wechat(this));
                break;
            case R.id.ib_weibo:
                mLoginPresenter.authorize(new SinaWeibo(this));
                break;
            case R.id.ib_qq:
                mLoginPresenter.authorize(new QQ(this));
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
}
