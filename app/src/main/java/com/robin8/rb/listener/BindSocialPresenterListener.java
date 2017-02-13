package com.robin8.rb.listener;

import android.content.Context;

import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.UIUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

/**
 * @author Figo
 */
public class BindSocialPresenterListener implements PlatformActionListener {

    protected final String[] otherLoginStrings = new String[]{"wechat", "weibo", "qq"};
    private Context mContext;
    protected String provider;

    public BindSocialPresenterListener(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void authorize(Platform plat) {
        if (plat.isValid()) {
            plat.removeAccount();
        }
        plat.SSOSetting(false);//设置false表示使用SSO授权方式
        plat.setPlatformActionListener(this);
        plat.showUser(null);//如果account为null，则表示获取授权账户自己的资料
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
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
    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
        }
        String failtext = "";
        if (t instanceof WechatClientNotExistException) {
            failtext = "微信客户端不存在";
        } else if (t instanceof WechatTimelineNotSupportedException) {
        } else if (t instanceof QQClientNotExistException) {
            failtext = "QQ客户端不存在";
        }
        final String finalFailtext = failtext;
        UIUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                CustomToast.showShort(mContext, finalFailtext);
            }
        });

    }
}
