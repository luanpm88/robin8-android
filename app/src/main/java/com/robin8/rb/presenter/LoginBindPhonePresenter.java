package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.activity.MainActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.model.BaseBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.module.social.MeasureInfluenceActivity;
import com.robin8.rb.module.social.MeasureInfluenceManActivity;
import com.robin8.rb.module.social.SocialBindActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.RegExpUtil;
import com.robin8.rb.util.TimerUtilTwo;
import com.robin8.rb.view.ILoginView;
import com.tendcloud.appcpa.TalkingDataAppCpa;

/**
 Created by zc on 2017/6/20. */

public class LoginBindPhonePresenter extends BindSocialPresenterListener implements PresenterI {
    private final ILoginView mILoginView;
    private Activity mActivity;
    private int from;
    private int influence;

    public LoginBindPhonePresenter(Activity activity, ILoginView loginView) {
        super(activity);
        mActivity = activity;
        mILoginView = loginView;
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        from = intent.getIntExtra("from", 0);
        if (!TextUtils.isEmpty(intent.getStringExtra("influence"))){
            influence=1;
        }else {
            influence=0;
        }
        //        LogUtil.LogShitou("重新绑定的form","====>"+from);
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
        //   HelpTools.insertLoginInfo(HelpTools.Token, "");//此时应没有token 手动清理一下
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);
        //        LogUtil.LogShitou("发送的参数num",phoneNumber);
        getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.GET_CODE_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {
                CustomToast.showShort(mActivity, "验证码发送失败");
            }

            @Override
            public void onResponse(String response) {
                //                LogUtil.LogShitou("验证码数据"+HelpTools.getUrl(CommonConfig.GET_CODE_URL),response);
                BaseBean bean = GsonTools.jsonToBean(response, BaseBean.class);
                CustomToast.showShort(mActivity, bean.getDetail());
                if (bean.getError() == 0) {
                    CustomToast.showShort(mActivity, "验证码发送成功");
                    new Thread(new TimerUtilTwo(60, null, ((TextView) mILoginView.getTv()), mActivity, "重新获取验证码")).start();
                   // new Thread(new TimerUtil(60, null, ((TextView) mILoginView.getTv()),null, mActivity, "重新获取验证码", "s后重新获取",mActivity.getResources().getColor(R.color.color_checknum),mActivity.getResources().getColor(R.color.color_checknum))).start();

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

        if (! RegExpUtil.checkMobile(phoneNumber)) {
            CustomToast.showShort(mActivity, "请输入正确的手机号码!");
            return;
        }
        //   HelpTools.insertLoginInfo(HelpTools.Token, "");//此时应没有token 手动清理一下
        //  BasePresenter basePresenter = new BasePresenter();//是否会报401错误
        //        LogUtil.LogShitou("清理之后","===》"+HelpTools.getLoginInfo(HelpTools.Token));
        RequestParams requestParams = new RequestParams();
        requestParams.put("mobile_number", phoneNumber);
        requestParams.put("code", checkNum);
        //        LogUtil.LogShitou("发送的参数num",phoneNumber);
        //        LogUtil.LogShitou("发送的参数code",checkNum);
        getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.BIND_MOBILE_URL), requestParams, new RequestCallback() {

            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                //                LogUtil.LogShitou("绑定手机号api/v1/kols/bind_mobile",response);
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
            TalkingDataAppCpa.onRegister(loginBean.getKol().getMobile_number());
            HelpTools.insertLoginInfo(HelpTools.Token, BaseApplication.decodeToken(loginBean.getKol().getIssue_token()));
            HelpTools.insertLoginInfo(HelpTools.LoginNumber, loginBean.getKol().getMobile_number());
            BaseApplication.getInstance().setLoginBean(loginBean);
            if (BaseApplication.getInstance().hasLogined()) {
                TalkingDataAppCpa.onCustEvent1();
                NotifyManager.getNotifyManager().notifyChange(NotifyManager.TYPE_LOGIN);//发送消息
                //判断是否是kol，如果不是kol，点击申请kol
                //  judgeIskol();
                //                String mMineData = CacheUtils.getString(mActivity, SPConstants.MINE_DATA, null);
                //                MineShowModel mineShowModel = GsonTools.jsonToBean(mMineData, MineShowModel.class);
                //                if (mineShowModel != null && mineShowModel.getError() == 0) {
                //                    //本地缓存判断
                //                    CacheUtils.putString(mActivity, SPConstants.MINE_DATA, mMineData);
                //                    MineShowModel.KolBean kol = mineShowModel.getKol();
                //                    if (mineShowModel !=null){
                //                        if (!kol.getRole_apply_status().equals("pending")){
                //                            jumpBeKol(kol.getId());
                //                        }
                //                    }
                //                }

            }
            //            LogUtil.LogShitou("绑定手机号此时的from、",""+from);
            //    LoginHelper.loginSuccess(loginBean, from, mActivity);

            //==============================
            if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.SecondIn))){
                //first走过
                jumpActivity(1);
            }else if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.ThirdIn))){
                //first和second都走过
                jumpActivity(2);
            }else{
                //从影响力来的就回去影响力，否则回到活动页面
                backMain();
            }
//            Intent intent = new Intent(mActivity, MainActivity.class);
//            //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent.putExtra("register_main", "zhu");
//            mActivity.startActivity(intent);
//            mActivity.finish();
        } else {
            CustomToast.showShort(mActivity, loginBean.getDetail());
        }
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
    public void backMain() {
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
            Intent intent = new Intent(mActivity, MainActivity.class);
            intent.putExtra("register_main", "zhu");
            mActivity.startActivity(intent);
            mActivity.finish();
            mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            // mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
    }
    private void judgeIskol() {
        BasePresenter basePresenter = new BasePresenter();//是否会报401错误
        basePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.MY_SHOW_URL), null, new RequestCallback() {

            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                //                LogUtil.LogShitou("====这是登录后的检测网红=====",response);
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
                        }
                        //                        LogUtil.LogShitou("哈哈哈哈","---//--");
                    }
                    // mActivity.finish();
                }
            }


        });
    }

    private void jumpBeKol(int id) {
        TalkingDataAppCpa.onCustEvent1();
        //        Intent intent = new Intent(mActivity, BeKolFirstActivity.class);
        //        intent.putExtra("id", id);
        //        intent.putExtra("jump", "register");
        //        mActivity.startActivity(intent);
        Intent intent1 = new Intent(mActivity, MainActivity.class);
        intent1.putExtra("register_main", "zhu");
        mActivity.startActivity(intent1);
    }
}
