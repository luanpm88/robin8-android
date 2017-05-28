package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginOtherWithPhoneActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.LoginHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.task.LoginTask;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.ILoginView;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;

/**
 * @author Figo
 */
public class LoginPresenter extends BindSocialPresenterListener implements PresenterI {

    private final ILoginView mILoginView;
    private Activity mActivity;
    private String mKolUuid;
    private ArrayList<OtherLoginListBean.OtherLoginBean> mOtherLoginList;
    private boolean mUploadedContactsB;
    private LayoutInflater mLayoutInflater;
    private int from;
    private WProgressDialog mWProgressDialog;

    public LoginPresenter(Activity activity, ILoginView loginView) {
        super(activity);
        mActivity = activity;
        mILoginView = loginView;
    }

    public void init() {
        Intent intent = mActivity.getIntent();
        Bundle rootBundle = intent.getExtras();
        if(rootBundle!=null){
            mKolUuid = rootBundle.getString("kol_uuid", "");
            from = rootBundle.getInt("from", SPConstants.MAINACTIVITY);
        }
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.POST:
                HttpRequest.getInstance().post(needHeader, url, params, callback);
                break;
        }
    }

    @Override
    public void onComplete(final Platform platform, int action, final HashMap<String, Object> res) {
        super.onComplete(platform, action, res);

        //第三方登录成功
        if (action == Platform.ACTION_USER_INFOR) {
            if (action == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                final String token = platDB.getToken();
                final String userGender = platDB.getUserGender();
                final String userIcon = platDB.getUserIcon();
                final String userId = platDB.getUserId();
                final String userName = platDB.getUserName();
                UIUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        sendOtherLoginInfo(platform.getName(), token, userGender, userIcon, userId, userName, res);
                    }
                });
            }
        }
    }

    private void sendOtherLoginInfo(String plat, String token, String userGender, String userIcon,
                                    String userId, String userName, HashMap<String, Object> res) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("app_platform", "Android");
        requestParams.put("app_version", AppUtils.getVersionName(mActivity.getApplicationContext()));
        requestParams.put("device_token", StringUtil.getToken(mActivity.getApplicationContext()));
        requestParams.put("os_version", AppUtils.getSystemVersion());
        requestParams.put("device_model", android.os.Build.MODEL);
        requestParams.put("IMEI", AppUtils.getIMEI(mActivity.getApplicationContext()));
        requestParams.put("city_name",  CacheUtils.getString(mActivity, SPConstants.LOCATION_CITY, ""));
        requestParams.put("IDFA",android.os.Build.MODEL);
        requestParams.put("provider", provider);
        requestParams.put("uid", userId);
        requestParams.put("token", token);
        requestParams.put("name", userName);
        requestParams.put("gender", userGender);
        requestParams.put("url", String.valueOf(res.get("url")));
        requestParams.put("avatar_url", userIcon);
        requestParams.put("desc", String.valueOf(res.get("description")));
        requestParams.put("serial_params", GsonTools.mapToJson(res));
        requestParams.put("utm_source", AppUtils.getApplicationMeteData(mActivity.getApplicationContext(), "TD_CHANNEL_ID"));

        if ("SinaWeibo".equals(plat)) {
            requestParams.put("followers_count", res.get("followers_count"));//粉丝数
            requestParams.put("statuses_count", res.get("statuses_count"));//微博数
            requestParams.put("registered_at", res.get("created_at"));//微博注册时间
            requestParams.put("verified", String.valueOf(res.get("verified")));//微博是否加V验证
            requestParams.put("refresh_token", res.get("refresh_token"));//微博令牌刷新token
        } else if ("Wechat".equals(plat)) {
            requestParams.put("uid", res.get("openid"));
            requestParams.put("unionid", String.valueOf(res.get("unionid")));
        }
        requestParams.put("province", res.get("province"));
        requestParams.put("city", res.get("city"));

        getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.OAUTH_LOGIN_URL), requestParams, new RequestCallback() {
                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                        if (loginBean.getError() == 0) {
                            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));//保存token
                            if (!TextUtils.isEmpty(loginBean.getKol().getMobile_number())) {//已经绑定了手机号
                                HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                                BaseApplication.getInstance().setLoginBean(loginBean);
                                if (BaseApplication.getInstance().hasLogined()) {
                                    NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                                }
                                LoginHelper.loginSuccess(loginBean,from,mActivity);
                            } else {//尚未绑定手机号
                                Intent intent = new Intent(mActivity, LoginOtherWithPhoneActivity.class);
                                intent.putExtra("from", from);
                                mActivity.startActivity(intent);
                                mActivity.finish();
                            }
                        }else {
                            CustomToast.showShort(mActivity,loginBean.getError());
                        }
                    }
                }
        );
    }

    /**
     * 获取验证码
     */

    public void getCheckNumber() {
        String phoneNumber = mILoginView.getPhoneNumber();
        if (!RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的手机号码!");
            return;
        }
        HelpTools.insertLoginInfo(HelpTools.Token, "");//此时应没有token 手动清理一下
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);

        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.GET_CODE_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(mActivity, "验证码发送失败");
            }

            @Override
            public void onResponse(String response) {
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                CustomToast.showShort(mActivity, bean.getDetail());
                if (bean.getError() == 0) {
                    CustomToast.showShort(mActivity, "验证码发送成功");
                } else {
                    CustomToast.showShort(mActivity, "验证码发送失败");
                }
            }
        });
    }

    /**
     * 登录
     */
    public void login() {
        final String phoneNumber = mILoginView.getPhoneNumber();
        String checkCode = mILoginView.getCheckCode();
        if (!RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的手机号码!");
        } else {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            mWProgressDialog.show();

            LoginTask.newInstance(mActivity.getApplicationContext()).start(new RequestCallback() {
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
                    // 登陆成功 埋点
                    TalkingDataAppCpa.onRegister(phoneNumber);
                    LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                    if(loginBean == null ){
                        CustomToast.showShort(mActivity.getApplicationContext(), mActivity.getString(R.string.please_data_wrong));
                        return;
                    }

                    if (loginBean.getError() == 0) {// 登陆成功
                        LoginHelper.loginSuccess(loginBean, from, mActivity);
                    } else {
                        CustomToast.showShort(mActivity.getApplicationContext(), loginBean.getDetail());
                    }
                }
            }, phoneNumber, checkCode, mKolUuid);
        }
    }
}
