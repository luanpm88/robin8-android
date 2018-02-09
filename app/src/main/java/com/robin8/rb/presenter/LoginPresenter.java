package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.LoginOtherWithPhoneActivity;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.activity.email.EmailRegiterActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.LoginHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.IndentyBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.module.mine.rongcloud.RongCloudBean;
import com.robin8.rb.module.social.MeasureInfluenceActivity;
import com.robin8.rb.module.social.MeasureInfluenceManActivity;
import com.robin8.rb.module.social.SocialBindActivity;
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
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.util.TimerUtilTwo;
import com.robin8.rb.util.UIUtils;
import com.robin8.rb.view.ILoginView;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;

/**
 @author Figo */
public class LoginPresenter extends BindSocialPresenterListener implements PresenterI {

    private final ILoginView mILoginView;
    private Activity mActivity;
    private String mKolUuid;
    private ArrayList<OtherLoginListBean.OtherLoginBean> mOtherLoginList;
    private boolean mUploadedContactsB;
    private LayoutInflater mLayoutInflater;
    private int from;
    private WProgressDialog mWProgressDialog;
    private int influence = 0;

    public LoginPresenter(Activity activity, ILoginView loginView) {

        super(activity);
        mActivity = activity;
        mILoginView = loginView;
    }

    public void init() {

        Intent intent = mActivity.getIntent();
        Bundle rootBundle = intent.getExtras();
        if (rootBundle != null) {
            mKolUuid = rootBundle.getString("kol_uuid", "");
            from = rootBundle.getInt("from", SPConstants.MAINACTIVITY);
        }
        if (intent.getStringExtra("influence") != null) {
            if (intent.getStringExtra("influence").equals(StatisticsAgency.INFLUENCE_LIST)) {
                influence = 1;
            }
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

    private void sendOtherLoginInfo(final String plat, final String token, final String userGender, final String userIcon, final String userId, final String userName, final HashMap<String, Object> res) {

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();
        RequestParams requestParams = new RequestParams();
        requestParams.put("app_platform", "Android");
        requestParams.put("app_version", AppUtils.getVersionName(mActivity.getApplicationContext()));
        requestParams.put("device_token", StringUtil.getToken(mActivity.getApplicationContext()));
        requestParams.put("os_version", AppUtils.getSystemVersion());
        requestParams.put("device_model", android.os.Build.MODEL);
        requestParams.put("IMEI", AppUtils.getIMEI(mActivity.getApplicationContext()));
        requestParams.put("city_name", CacheUtils.getString(mActivity, SPConstants.LOCATION_CITY, ""));
        requestParams.put("IDFA", android.os.Build.MODEL);
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

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {

                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
                final LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                if (loginBean.getError() == 0) {
                    HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));//保存token
                    afterBind(loginBean);
                } else {
                    if (! TextUtils.isEmpty(loginBean.getDetail())) {
                        CustomToast.showShort(mActivity, loginBean.getDetail());
                    }
                }
            }
        });
    }

    private void toBind(final String provider, final String plat, final String token, String userGender, final String userIcon, final String userId, final String userName, final HashMap<String, Object> res, final LoginBean loginBean) {
        LogUtil.LogShitou("11111", "111111");
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("provider", provider);
        mRequestParams.put("token", token);
        mRequestParams.put("name", userName);
        mRequestParams.put("uid", userId);
        mRequestParams.put("url", "");
        mRequestParams.put("avatar_url", userIcon);
        mRequestParams.put("desc", String.valueOf(res.get("description")));
        mRequestParams.put("serial_params", GsonTools.mapToJson(res));
        if ("SinaWeibo".equals(plat)) {
            mRequestParams.put("uid", userId);
            mRequestParams.put("followers_count", String.valueOf(res.get("followers_count")));//粉丝数
            mRequestParams.put("statuses_count", String.valueOf(res.get("statuses_count")));//微博数
            mRequestParams.put("registered_at", String.valueOf(res.get("created_at")));//微博注册时间
            mRequestParams.put("verified", String.valueOf(res.get("verified")));//微博是否加V验证
            mRequestParams.put("refresh_token", String.valueOf(res.get("refresh_token")));//微博令牌刷新token
        } else if ("Wechat".equals(plat)) {
            mRequestParams.put("uid", String.valueOf(res.get("openid")));
            mRequestParams.put("unionid", String.valueOf(res.get("unionid")));
        }

        mRequestParams.put("province", String.valueOf(res.get("province")));
        mRequestParams.put("city", String.valueOf(res.get("city")));
        mRequestParams.put("gender", String.valueOf(res.get("gender")));
        mRequestParams.put("is_vip", String.valueOf(res.get("is_vip")));
        mRequestParams.put("is_yellow_vip", String.valueOf(res.get("is_yellow_vip")));
        getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.KOLS_IDENTITY_BIND_URL_OLD), mRequestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
              //  LogUtil.LogShitou("第三方绑定登陆成功后======>", response);
                IndentyBean indentyBean = GsonTools.jsonToBean(response, IndentyBean.class);
                if (indentyBean.getError() == 0) {
                    if ("wechat".equals(provider)) {
                        mypostData(mActivity.getString(R.string.weixin), userName, loginBean);
                    } else if ("qq".equals(provider)) {
                        mypostData(mActivity.getString(R.string.qq), userName, loginBean);
                    } else if ("weibo".equals(provider)) {
                        mypostData(mActivity.getString(R.string.weibo), userName, loginBean);
                    }
                } else if (indentyBean.getError() == 1) {
                    afterBind(loginBean);
                } else {
                    CustomToast.showShort(mActivity, "授权失败，请重试");
                }

            }
        });
    }

    private void mypostData(String name, String userName, final LoginBean loginBean) {
        String url;
        BasePresenter mBasePresenter = new BasePresenter();
        RequestParams params = new RequestParams();
        params.put("provider_name", name);
        params.put("price", "0.1");
        params.put("followers_count", "0.1");
        params.put("username", userName);
        url = HelpTools.getUrl(CommonConfig.UPDATE_SOCIAL_URL);

        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, params, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);

                if (bean == null) {
                    return;
                }

                if (bean.getError() == 0) {
                    //   LogUtil.LogShitou("走到这里没有","直接绑定qq");
                    postData(loginBean);

                } else {
                    CustomToast.showShort(mActivity, bean.getDetail());
                }
            }
        });
    }

    private void postData(final LoginBean loginBean) {


        BasePresenter mBasePresenter = new BasePresenter();

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();

        String url = HelpTools.getUrl(CommonConfig.SUBMIT_APPLY_URL);
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, url, null, new RequestCallback() {

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
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean == null) {
                    CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                    return;
                }

                if (bean.getError() == 0) {
                    afterBind(loginBean);
                } else {
                    CustomToast.showShort(mActivity, bean.getDetail());
                }
            }
        });
    }

    private void afterBind(LoginBean loginBean) {
        //登陆即绑定然后判断是否绑定手机号
        if (! TextUtils.isEmpty(loginBean.getKol().getMobile_number())) {//已经绑定了手机号
            TalkingDataAppCpa.onLogin(loginBean.getKol().getMobile_number());
            HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
            BaseApplication.getInstance().setLoginBean(loginBean);
            if (BaseApplication.getInstance().hasLogined()) {
                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
            }
            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.putExtra("register_main", "zhu");
            mActivity.startActivity(intent);
            mActivity.finish();
        } else {//尚未绑定手机号
            BaseApplication.getInstance().setLoginBean(loginBean);
            Intent intent = new Intent(mActivity, LoginOtherWithPhoneActivity.class);
            intent.putExtra("from", from);
            if (influence == 1) {
                intent.putExtra("influence", StatisticsAgency.INFLUENCE_LIST);
            }
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    /**
     获取验证码
     */

    public void getCheckNumber() {

        String phoneNumber = mILoginView.getPhoneNumber();
        if (! RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的手机号码!");
            return;
        }
        HelpTools.insertLoginInfo(HelpTools.Token, "");//此时应没有token 手动清理一下
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);

        getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.GET_CODE_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(mActivity, "验证码发送失败!");
            }

            @Override
            public void onResponse(String response) {

                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                if (bean!=null){
                    if (bean.getError() == 0) {
                        CustomToast.showShort(mActivity, "验证码发送成功");
                        new Thread(new TimerUtilTwo(60, null, ((TextView) mILoginView.getTv()), mActivity, "重新获取验证码")).start();
                    } else {
                        CustomToast.showShort(mActivity, "验证码发送失败");
                    }
                }else {
                    CustomToast.showShort(mActivity, "验证码发送失败");
                }

            }
        });
    }

    /**
     登录
     @param which 0:手机号登陆；1：邮箱登陆
     */
    public void login(int which) {
        if (which==0){
        final String phoneNumber = mILoginView.getPhoneNumber();
        String checkCode = mILoginView.getCheckCode();
        String invitationCode = mILoginView.getInvitationCode();
        if (! RegExpUtil.checkMobile(phoneNumber)) {
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
                    // LogUtil.LogShitou("手机号登陆返回数据", response);
                    if (mWProgressDialog != null) {
                        try {
                            mWProgressDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                    if (loginBean == null) {
                        CustomToast.showShort(mActivity.getApplicationContext(), mActivity.getString(R.string.please_data_wrong));
                        return;
                    }
                    // 登陆成功 埋点
                    if (loginBean.getError() == 0) {// 登陆成功
                        // talkingData 统计
                        TalkingDataAppCpa.onRegister(phoneNumber);
                        TalkingDataAppCpa.onLogin(phoneNumber);
                        //  LoginHelper.loginSuccess(loginBean, from, mActivity);
                        HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                        HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                        BaseApplication.getInstance().setLoginBean(loginBean);
                        if (BaseApplication.getInstance().hasLogined()) {
                            NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                        }
                        initGetRongCloud(loginBean.getKol().getMobile_number(),loginBean.getKol().getName(),loginBean.getKol().getAvatar_url());
                        //登陆成功之后去绑定社交账号页面
                        int is = 0;
                        if (loginBean.getKol_identities() != null) {
                            if (loginBean.getKol_identities().size() != 0) {
                                for (int i = 0; i < loginBean.getKol_identities().size(); i++) {
                                    if (loginBean.getKol_identities().get(i).getProvider().equals("weibo") || loginBean.getKol_identities().get(i).getProvider().equals("wechat")) {
                                        is = 1;
                                    }
                                    if (loginBean.getKol_identities().get(i).getProvider().equals("weibo")) {
                                        HelpTools.insertCommonXml(HelpTools.IsBind, "is");
                                    }
                                }
                                if (is == 1) {
                                    if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.SecondIn))) {
                                        //都没有走过，邦过微信／微博，跳过first
                                        jumpActivity(1);
                                    } else {
                                        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ThirdIn))) {
                                            jumpActivity(2);
                                        } else {
                                            backMain(1);
                                        }
                                    }
                                } else {
                                    jumpActivity(1);
                                }
                            } else {
                                //没有绑定
                                jumpActivity(1);

                            }
                        } else {
                            jumpActivity(1);
                        }
                    } else if (loginBean.getError() == 1) {
                            try {
                                if (! TextUtils.isEmpty(loginBean.getDetail())) {
                                    mILoginView.clearEdit(3);//无效验证码清空edit
                                    CustomToast.showShort(mActivity.getApplicationContext(), loginBean.getDetail());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }else {
                        try {
                            if (! TextUtils.isEmpty(loginBean.getDetail())) {
                                CustomToast.showShort(mActivity.getApplicationContext(), loginBean.getDetail());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, phoneNumber, checkCode, mKolUuid, invitationCode);
        }
        } else if (which==1){
            final String emailNumber = mILoginView.getEmailNumber();
            String emailPwd = mILoginView.getEmailPwd();
            if (! RegExpUtil.checkEmail(emailNumber)) {
                CustomToast.showShort(mActivity, mActivity.getString(R.string.put_right_email_address));
            }else if (TextUtils.isEmpty(emailPwd)){
                CustomToast.showShort(mActivity, mActivity.getString(R.string.put_pwd));
            }else {
                if (mWProgressDialog == null) {
                    mWProgressDialog = WProgressDialog.createDialog(mActivity);
                }
                mWProgressDialog.show();
                BasePresenter mBasePresenter = new BasePresenter();
                RequestParams mRequestParams = new RequestParams();
                mRequestParams.put("login",emailNumber);
                mRequestParams.put("password",emailPwd);
                mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.EMAIL_LOGIN_URL), mRequestParams, new RequestCallback() {

                    @Override
                    public void onError(Exception e) {
                        if (mWProgressDialog != null) {
                            try {
                                mWProgressDialog.dismiss();
                            } catch (Exception es) {
                                es.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onResponse(String response) {
                        if (mWProgressDialog != null) {
                            try {
                                mWProgressDialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                      //  LogUtil.LogShitou("邮箱登陆结果",response);
                      //  TalkingDataAppCpa.onRegister(phoneNumber);
                      //  TalkingDataAppCpa.onLogin(phoneNumber);
                        //  LoginHelper.loginSuccess(loginBean, from, mActivity);
                        LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                        if (loginBean == null) {
                            CustomToast.showShort(mActivity.getApplicationContext(), mActivity.getString(R.string.please_data_wrong));
                            return;
                        }
                        if (loginBean.getError()==0){
                            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                           // HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                            BaseApplication.getInstance().setLoginBean(loginBean);
                            if (BaseApplication.getInstance().hasLogined()) {
                                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                            }
                            initGetRongCloud(emailNumber,loginBean.getKol().getName(),loginBean.getKol().getAvatar_url());
                            backMain(1);
                        }else {
                            CustomToast.showShort(mActivity.getApplicationContext(), loginBean.getDetail());
                        }

                    }
                });

            }
        }
    }


    private void judgeIskol() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();
        BasePresenter basePresenter = new BasePresenter();//是否会报401错误
        basePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.MY_SHOW_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                if (mWProgressDialog != null) {
                    mWProgressDialog.dismiss();
                }
            }

            @Override
            public void onResponse(String response) {
                if (mWProgressDialog != null) {
                    try {
                        mWProgressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                MineShowModel mineShowModel = GsonTools.jsonToBean(response, MineShowModel.class);
                if (mineShowModel != null && mineShowModel.getError() == 0) {
                    // CacheUtils.putString(mActivity, SPConstants.MINE_DATA, response);
                    MineShowModel.KolBean kol = mineShowModel.getKol();
                    if (TextUtils.isEmpty(kol.getRole_apply_status())) {
                        //不是kol
                        jumpBeKol(kol.getId());
                    } else {
                        if (kol.getRole_apply_status().equals("pending")) {
                            jumpBeKol(kol.getId());
                        } else {
                            LoginHelper.loginSuccess(null, from, mActivity);
                        }

                    }
                }
            }


        });
    }
    private void initGetRongCloud(String id,String name,String imgUrl) {
        BasePresenter base = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        if (TextUtils.isEmpty(id)){
            //游客
            requestParams.put("userId",CommonConfig.TOURIST_PHONE);
            requestParams.put("name","游客");
            requestParams.put("portraitUri","http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/ad9d7a31d7!avatar");
        }else {
            requestParams.put("userId",id);
            if (TextUtils.isEmpty(name)){
                requestParams.put("name",id);
            }else{
                requestParams.put("name",name);
            }
            if (TextUtils.isEmpty(imgUrl)){
                requestParams.put("portraitUri","http://7xozqe.com2.z0.glb.qiniucdn.com/uploads/kol/avatar/109050/22494f2caf!avatar");
            }else{
                requestParams.put("portraitUri",imgUrl);
            }
        }
        base.getDataFromServer(false, HttpRequest.POST, CommonConfig.RONG_CLOUD_URL, requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(mActivity,mActivity.getResources().getString(R.string.no_net));
            }
            @Override
            public void onResponse(String response) {
                RongCloudBean rongCloudBean = GsonTools.jsonToBean(response, RongCloudBean.class);
                if (rongCloudBean.getCode()==200){
                    HelpTools.insertCommonXml(HelpTools.CloudToken,rongCloudBean.getToken());
                }else{
                    HelpTools.insertCommonXml(HelpTools.CloudToken,"");
                }

            }
        });

    }
    private void jumpBeKol(int id) {
        //  TalkingDataAppCpa.onCustEvent1();
        Intent intent1 = new Intent(mActivity, MainActivity.class);
        intent1.putExtra("register_main", "zhu");
        mActivity.startActivity(intent1);
        //        Intent intent = new Intent(mActivity, BeKolFirstActivity.class);
        //        intent.putExtra("id", id);
        //        intent.putExtra("jump", "register");
        //        mActivity.startActivity(intent);
    }

    private void jumpActivity(int i) {
        if (i == 0) {
            //没有微博或者微信
            Intent intent = new Intent(mActivity, SocialBindActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (i == 1) {
            //有微博或者微信
            Intent intent = new Intent(mActivity, MeasureInfluenceActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (i == 2) {
            //跳转到man
            Intent intent = new Intent(mActivity, MeasureInfluenceManActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public void backMain(int i) {
        //  LogUtil.LogShitou("？？？？？？？","=influence==>"+influence+"==i==="+i);
        if (influence == 1) {
            //返回影响力界面
            if (BaseApplication.getInstance().hasLogined()) {
                //登陆成功就返回影响力界面
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("register_main", "influence");
                mActivity.startActivity(intent);
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                //为登陆就返回活动页面
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                mActivity.startActivity(intent);
                mActivity.finish();
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        } else {
            if (i == 1) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("register_main", "zhu");
                mActivity.startActivity(intent);
            }
            mActivity.finish();
            //mActivity.finish();
            // mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }
    public void toEmailRegister(){
        Intent intent = new Intent(mActivity, EmailRegiterActivity.class);
        mActivity.startActivity(intent);
    }
}
