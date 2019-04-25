package com.robin8.rb.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.robin8.rb.R;
import com.robin8.rb.ui.activity.email.ForgetPwdActivity;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.ui.model.sortlist.UserFacebookInfo;
import com.robin8.rb.presenter.LoginPresenter;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.view.ILoginView;

import org.json.JSONObject;

import java.util.Arrays;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
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
    public TextView tvForgetPwd;
    private View cursorLeft;
    private View cursorRight;

    private CallbackManager mCallbackManager;
    private LoginButton mBtnFBRoot;

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
        mBtnFBRoot = (LoginButton) view.findViewById(R.id.login_button);
        mETCheckNum = (EditText) view.findViewById(R.id.et_checknum);
        mETInvitationCode = ((EditText) view.findViewById(R.id.et_invitation_code));
        mTVLoginInfo = (TextView) view.findViewById(R.id.tv_login_info);
        mTVCheckNum = (TextView) view.findViewById(R.id.tv_checknum);
        mBTNLogin = view.findViewById(R.id.bt_login);

        mTVTourist = (TextView) view.findViewById(R.id.tv_tourist);
        View mIBWeixin = view.findViewById(R.id.ib_weixin);
        View mIBWeibo = view.findViewById(R.id.ib_weibo);
        View mIBQQ = view.findViewById(R.id.ib_qq);
        View mIBFacebook = view.findViewById(R.id.ib_facebook);
        //邮箱
        llPhoneNumLogin = ((LinearLayout) view.findViewById(R.id.ll_phone_login));
        llEmailLogin = ((LinearLayout) view.findViewById(R.id.ll_email_login));
        tvToPhoneLogin = ((TextView) view.findViewById(R.id.tv_to_phoneNum));
        llToEmailRegister = ((LinearLayout) view.findViewById(R.id.ll_to_register));
        etEmailNum = ((EditText) view.findViewById(R.id.et_email_num));
        etEmailPwd = ((EditText) view.findViewById(R.id.et_password));
        tvToEamaiLogin = ((TextView) view.findViewById(R.id.tv_to_email));
        tvForgetPwd = ((TextView) view.findViewById(R.id.tv_to_forget_pwd));
        cursorLeft = view.findViewById(R.id.view_left);
        cursorRight = view.findViewById(R.id.view_right);

        tvToEamaiLogin.setOnClickListener(this);
        tvToPhoneLogin.setOnClickListener(this);
        llToEmailRegister.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);

        mTVCheckNum.setOnClickListener(this);
        mBTNLogin.setOnClickListener(this);
        mTVTourist.setOnClickListener(this);
        mIBWeixin.setOnClickListener(this);
        mIBWeibo.setOnClickListener(this);
        mIBQQ.setOnClickListener(this);
        mIBFacebook.setOnClickListener(this);

        mTVLoginInfo.setText(Html.fromHtml(getString(R.string.click_login_approve) + "<font color=#2dcad0>" + getString(R.string.serviece_protocol) + "</font>"));
        mLoginPresenter = new LoginPresenter(LoginActivity.this, this);
        mLoginPresenter.init();
        setupFacebook();
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
                mLoginPresenter.authorize(new Wechat());
                break;
            case R.id.ib_weibo:
                mLoginPresenter.authorize(new SinaWeibo());
                break;
            case R.id.ib_qq:
                Platform platform = ShareSDK.getPlatform(QQ.NAME);
                mLoginPresenter.authorize(platform);
                // finish();
                break;
            case R.id.ib_facebook:
                if (isLoggedIn()) {
                    LoginManager.getInstance().logOut();
                }
                mBtnFBRoot.performClick();
                break;
            case R.id.iv_back:
                mLoginPresenter.backMain(0);
                break;
            case R.id.ll_to_register:
                //邮箱注册
                mLoginPresenter.toEmailRegister();
                break;
            case R.id.tv_to_phoneNum:
                //手机号登陆
                which = 0;
                llPhoneNumLogin.setVisibility(View.VISIBLE);
                llEmailLogin.setVisibility(View.GONE);
                mTVTourist.setVisibility(View.VISIBLE);
                llToEmailRegister.setVisibility(View.GONE);
                cursorLeft.setVisibility(View.VISIBLE);
                cursorRight.setVisibility(View.INVISIBLE);
                tvToPhoneLogin.setTextColor(getResources().getColor(R.color.blue_custom));
                tvToEamaiLogin.setTextColor(getResources().getColor(R.color.black_custom));
                break;
            case R.id.tv_to_email:
                //邮箱登陆
                which = 1;
                llPhoneNumLogin.setVisibility(View.GONE);
                llEmailLogin.setVisibility(View.VISIBLE);
                mTVTourist.setVisibility(View.GONE);
                llToEmailRegister.setVisibility(View.VISIBLE);
                cursorLeft.setVisibility(View.INVISIBLE);
                cursorRight.setVisibility(View.VISIBLE);
                tvToEamaiLogin.setTextColor(getResources().getColor(R.color.blue_custom));
                tvToPhoneLogin.setTextColor(getResources().getColor(R.color.black_custom));
                break;
            case R.id.tv_to_forget_pwd:
                //忘记密码
                Intent intent = new Intent(this, ForgetPwdActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * The method is used to setup FacebookSDK
     */
    private void setupFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mCallbackManager = CallbackManager.Factory.create();
        mBtnFBRoot.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        // Callback registration
        mBtnFBRoot.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Gson gson = new GsonBuilder().create();
                                JsonParser parser = new JsonParser();
                                JsonElement mJson =  parser.parse(object.toString());
                                UserFacebookInfo userFacebookInfo = gson.fromJson(mJson, UserFacebookInfo.class);
                                mLoginPresenter.sendOtherLoginFacebookInfo(loginResult.getAccessToken().getToken(), userFacebookInfo);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,address,birthday,link,name,email,gender,picture,hometown");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("fb","cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("fb","onError");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
        //LogUtil.LogShitou("输入的密码",emailPwd);
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
    /**
     * The method is used to check user login facebook status with facebook access token
     *
     * @return true if user has login, otherwise return false
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
