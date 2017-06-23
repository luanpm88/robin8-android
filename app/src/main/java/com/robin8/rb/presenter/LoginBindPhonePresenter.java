package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.LoginHelper;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.activity.BeKolFirstActivity;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.TimerUtil;
import com.robin8.rb.view.ILoginView;
import com.tendcloud.appcpa.TalkingDataAppCpa;

/**
 * Created by zc on 2017/6/20.
 */

public class LoginBindPhonePresenter extends BindSocialPresenterListener implements PresenterI {
    private final ILoginView mILoginView;
    private Activity mActivity;
    private int from;
    public LoginBindPhonePresenter(Activity activity, ILoginView loginView) {
        super(activity);
        mActivity = activity;
        mILoginView = loginView;
    }

    @Override
    public void init() {
        Intent intent =mActivity.getIntent();
        from = intent.getIntExtra("from", 0);
        LogUtil.LogShitou("重新绑定的form","====>"+from);
    }

    @Override
    public void getDataFromServer(boolean needHeader, int method, String url, RequestParams params, RequestCallback callback) {
        switch (method) {
            case HttpRequest.GET:
                HttpRequest.getInstance().get(needHeader, url, params, callback);
                break;
            case HttpRequest.PUT:
                HttpRequest.getInstance().put(needHeader, url, params, callback);
                break;
        }
    }

    public void getCheckNumber() {
        String phoneNumber = mILoginView.getPhoneNumber();
        if (! RegExpUtil.checkMobile(phoneNumber)) {
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
                LogUtil.LogShitou("验证码数据"+HelpTools.getUrl(CommonConfig.GET_CODE_URL),response);
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                CustomToast.showShort(mActivity, bean.getDetail());
                if (bean.getError() == 0) {
                    CustomToast.showShort(mActivity, "验证码发送成功");
                    new Thread(new TimerUtil(60, null, ((TextView) mILoginView.getTv()), mActivity, "重新获取验证码")).start();
                } else {
                    CustomToast.showShort(mActivity, "验证码发送失败");
                }
            }
        });
    }

    public void complete() {
        String phoneNumber = mILoginView.getPhoneNumber();
        String checkNum = mILoginView.getCheckCode();

        if (TextUtils.isEmpty(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入手机号码!");
            return;
        }

        if (TextUtils.isEmpty(checkNum)) {
            CustomToast.showShort(mActivity, "请输入验证码!");
            return;
        }

        if (!RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的手机号码!");
            return;
        }

      //  BasePresenter basePresenter = new BasePresenter();是否会报401错误
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);
        requestParams.put("code", checkNum);
        LogUtil.LogShitou("发送的参数num",phoneNumber);
        LogUtil.LogShitou("发送的参数code",checkNum);
       getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BIND_MOBILE_URL), requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("登陆成功",response);
                parseJson(response);
            }
        });
    }
    private void parseJson(String response) {

        LoginBean loginBean = GsonTools.jsonToBean(response, LoginBean.class);

        if (loginBean == null) {
            CustomToast.showShort(mActivity, mActivity.getString(R.string.please_data_wrong));
            return;
        }
        if (loginBean.getError() == 0) {
            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
            HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
            BaseApplication.getInstance().setLoginBean(loginBean);
            if (BaseApplication.getInstance().hasLogined()) {
                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
            }
            judgeIskol();
           // LoginHelper.loginSuccess(loginBean, from, mActivity);
        } else {
            CustomToast.showShort(mActivity, loginBean.getDetail());
        }
    }

    private void judgeIskol() {
      getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.MY_SHOW_URL), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                LogUtil.LogShitou("====这是show检测网红=====",response);
                MineShowModel mineShowModel = GsonTools.jsonToBean(response, MineShowModel.class);
                if (mineShowModel != null && mineShowModel.getError() == 0) {
                    CacheUtils.putString(mActivity, SPConstants.MINE_DATA, response);
                    MineShowModel.KolBean kol = mineShowModel.getKol();
                    if(TextUtils.isEmpty(kol.getRole_apply_status())){
                        //不是kol
                        jumpBeKol(kol.getId());
                    }else {
                        if (kol.getRole_apply_status().equals("pending")){
                            jumpBeKol(kol.getId());
                        }else {
                            LoginHelper.loginSuccess(null, from, mActivity);
                        }
                    }
                }
            }

          private void jumpBeKol(int id) {
              TalkingDataAppCpa.onCustEvent1();
              Intent intent = new Intent(mActivity, BeKolFirstActivity.class);
              intent.putExtra("id", id);
              mActivity.startActivity(intent);

          }
      });
    }
}
