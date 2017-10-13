package com.robin8.rb.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.robin8.rb.listener.BindSocialPresenterListener;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.view.ILoginView;

/**
 * Created by zc on 2017/7/13.
 */

public class SetUserPwdPresenter extends BindSocialPresenterListener implements PresenterI{
    private Activity mActivity;
    private final ILoginView mILoginView;
    public SetUserPwdPresenter(Activity activity,ILoginView mILoginView) {
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

    public void login(){
        String editUserPwd = mILoginView.getPhoneNumber();
        String editUserPwdAgain = mILoginView.getCheckCode();
       if (TextUtils.isEmpty(editUserPwd)){
            CustomToast.showShort(mActivity,"请输入密码");
            return;
        }else if (TextUtils.isEmpty(editUserPwdAgain)){
            CustomToast.showShort(mActivity,"请再次输入密码");
            return;
        }else if (!editUserPwd.equals(editUserPwdAgain)){
            CustomToast.showShort(mActivity,"两次密码输入不一致");
            return;
        }else if (!TextUtils.isEmpty(editUserPwd)&&editUserPwd.length()<6){
           CustomToast.showShort(mActivity,"请输入至少6位的密码");
           return;
       }
       mActivity.finish();
    }
}
