package com.robin8.rb.presenter;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

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
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }

    @Override
    public void onError(Platform platform, int action, final Throwable t) {

    }


}
