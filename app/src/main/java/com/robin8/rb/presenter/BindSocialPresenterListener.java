package com.robin8.rb.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

/**
 @author Figo */
public class BindSocialPresenterListener implements PlatformActionListener {

    protected final String[] otherLoginStrings = new String[]{"wechat", "weibo", "qq"};
    private Context mContext;
    protected String provider;

    public BindSocialPresenterListener(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        if (plat.isAuthValid()) {
            plat.removeAccount(true);
        }
//        if (!qq.isClientValid()) {
//            Toast.makeText(this, "QQ未安装,请先安装QQ", Toast.LENGTH_SHORT).show();
//        }
        plat.SSOSetting(false);//设置false表示使用SSO授权方式
        plat.setPlatformActionListener(this);
        plat.showUser(null);//如果account为null，则表示获取授权账户自己的资料
    }

    @Override
    public void onComplete(Platform platform, int i, final HashMap<String, Object> hashMap) {
        provider = "";
        switch (platform.getName()) {
            case "QQ":
                provider = otherLoginStrings[2];
                break;
            case "Wechat":
                provider = otherLoginStrings[0];
                break;
            case "SinaWeibo":
                provider = otherLoginStrings[1];
                break;
        }
//        Message msg = new Message();
//        msg.what = 0;
//        msg.obj = platform;
//        new Handler(Looper.getMainLooper(), new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Toast.makeText(mContext, "登录成功", Toast.LENGTH_LONG).show();
//                Toast.makeText(mContext, "map:" + hashMap, Toast.LENGTH_LONG).show();
//                Toast.makeText(mContext, "用户信息:     " + ((Platform) msg.obj).getDb().getToken(), Toast.LENGTH_SHORT).show();
//                Log.e("claudia",hashMap+"");
//                return false;
//            }
//        }).sendMessage(msg);
        //    platform.removeAccount(true);
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void onError(Platform platform, int action, final Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
        }
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(mContext, "登录失败" + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
        String failtext = "";
        if (t instanceof WechatClientNotExistException) {
            failtext = "微信客户端不存在";
        } else if (t instanceof WechatTimelineNotSupportedException) {
            failtext = "请重新尝试";
        } else if (t instanceof QQClientNotExistException) {
            failtext = "QQ客户端不存在";
        }
        final String finalFailtext = failtext;
        UIUtils.runInMainThread(new Runnable() {

            @Override
            public void run() {
                if (TextUtils.isEmpty(finalFailtext)){
                    CustomToast.showShort(mContext, "登陆失败");
                }else {
                    CustomToast.showShort(mContext, finalFailtext);
                }
            }
        });

    }


}
