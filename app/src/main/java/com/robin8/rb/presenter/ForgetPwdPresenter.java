package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.activity.email.EmailAddInformationActivity;
import com.robin8.rb.activity.email.FixPwdActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.rongcloud.RongCloudBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.TimerUtilTwo;
import com.robin8.rb.view.ILoginView;

/**
 Created by zc on 2017/7/13. */

public class ForgetPwdPresenter extends BindSocialPresenterListener implements PresenterI {
    private Activity mActivity;
    private final ILoginView mILoginView;
    private WProgressDialog mWProgressDialog;
    public ForgetPwdPresenter(Activity activity, ILoginView mILoginView) {
        super(activity);
        this.mActivity = activity;
        this.mILoginView = mILoginView;
    }

    @Override
    public void init() {

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
    /**
     获取验证码
     */

    public void getCheckNumber() {

        String emailNumber = mILoginView.getEmailNumber();
        if (! RegExpUtil.checkEmail(emailNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的邮箱账号!");
            return;
        }
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();
        HelpTools.insertLoginInfo(HelpTools.Token, "");//此时应没有token 手动清理一下
        RequestParams mRequestParams = new RequestParams();
        mRequestParams.put("mobile_number", emailNumber);

        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(mActivity);
        }
        mWProgressDialog.show();
        mRequestParams.put("email", emailNumber);
        mRequestParams.put("type", "forget_password");
        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.EMAIL_CODE_URL), mRequestParams, new RequestCallback() {

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
              //  LogUtil.LogShitou("获取Email验证码", response);
                BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                if (baseBean.getError() == 0) {
                    CustomToast.showShort(mActivity, baseBean.getAlert());
                    new Thread(new TimerUtilTwo(60, null, ((TextView) mILoginView.getTv()), mActivity, "重新获取验证码")).start();
                }else {
                    CustomToast.showShort(mActivity,baseBean.getDetail());
                }
            }
        });
    }

    public void nextStep() {
        final String etEmailNum = mILoginView.getEmailNumber();
        String etCode = mILoginView.getCheckCode();
        if (!RegExpUtil.checkEmail(etEmailNum)){
            CustomToast.showShort(mActivity, "请输入正确的邮箱账号!");
            return;
        } else if (TextUtils.isEmpty(etCode)) {
            CustomToast.showShort(mActivity, "请输入验证码!");
            return;
        }else {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            mWProgressDialog.show();
            RequestParams mRequestParams = new RequestParams();
            mRequestParams.put("email", etEmailNum);
            mRequestParams.put("valid_code", etCode);
            getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.EMAIL_CODE_CHECK_URL), mRequestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                //    LogUtil.LogShitou("验证Email验证码", response);
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    BaseBean baseBean = GsonTools.jsonToBean(response, BaseBean.class);
                    if (baseBean.getError()==0){
                        //验证码获取通过之后，显示重置密码
                        Intent intent = new Intent(mActivity, FixPwdActivity.class);
                        intent.putExtra(EmailAddInformationActivity.EXTRA_EMAIL_NUM,etEmailNum);
                        intent.putExtra(EmailAddInformationActivity.EXTRA_EMAIL_TOKEN,baseBean.getVtoken());
                        mActivity.startActivity(intent);
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else {
                        CustomToast.showShort(mActivity,baseBean.getDetail());
                    }
                }
            });
        }
    }

    public void fixOver() {
        //新密码
        String editUserPwd = mILoginView.getPhoneNumber();
        //确认新密码
        String editUserPwdAgain = mILoginView.getEmailPwd();
        if (TextUtils.isEmpty(editUserPwd)) {
            CustomToast.showShort(mActivity, "请输入密码");
            return;
        } else if (TextUtils.isEmpty(editUserPwdAgain)) {
            CustomToast.showShort(mActivity, "请再次输入密码");
            return;
        } else if (! editUserPwd.equals(editUserPwdAgain)) {
            CustomToast.showShort(mActivity, "两次密码输入不一致");
            return;
        } else if (! TextUtils.isEmpty(editUserPwd) && editUserPwd.length() < 6) {
            CustomToast.showShort(mActivity, "请输入至少6位的密码");
            return;
        }else {
            if (mWProgressDialog == null) {
                mWProgressDialog = WProgressDialog.createDialog(mActivity);
            }
            mWProgressDialog.show();
            RequestParams mRequestParams = new RequestParams();
            mRequestParams.put("email", mILoginView.getEmailNumber());
            mRequestParams.put("new_password", editUserPwd);
            mRequestParams.put("new_password_confirmation", editUserPwdAgain);
            mRequestParams.put("vtoken", mILoginView.getCheckCode());
            getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.EMAIL_FIXPWD_URL), mRequestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                }

                @Override
                public void onResponse(String response) {
                 //   LogUtil.LogShitou("修改Email密码", response);
                    if (mWProgressDialog != null) {
                        mWProgressDialog.dismiss();
                    }
                    LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);
                    if (loginBean == null) {
                        CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
                        return;
                    }
                    if (loginBean.getError()==0){
                        HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
                        HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
                        BaseApplication.getInstance().setLoginBean(loginBean);
                        if (BaseApplication.getInstance().hasLogined()) {
                            NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                        }
                        initGetRongCloud(mILoginView.getEmailNumber(),loginBean.getKol().getName(),loginBean.getKol().getAvatar_url());
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.putExtra("register_main", "zhu");
                        mActivity.startActivity(intent);
                        mActivity.finish();
                        mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }else {
                        CustomToast.showShort(mActivity.getApplicationContext(), loginBean.getDetail());
                    }

                }
            });
        }
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
                CustomToast.showShort(mActivity,mActivity.getString(R.string.no_net));
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
}
