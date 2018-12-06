package com.robin8.rb.activity.web;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.mine.model.MineShowModel;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.MyDialog;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 Banner跳转的Webview
 */
public class BannerWebActivity extends BaseActivity {
    public final static String BANNER = "banner_web";
    public final static String BANNER_BEAN = "banner_bean";
    private WebView webView;
    private Dialog loadingDialog;
    private String mineBean;
    private boolean isStar = false;
    private static boolean isBack = false;
    private String shareUrl;

    @Override
    public void setTitleView() {
        mIVBack.setOnClickListener(this);
        mTVCenter.setText("Robin8");
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_banner_web, mLLContent, true);
        webView = ((WebView) view.findViewById(R.id.web_view));
        loadingDialog = MyDialog.createLoadingDialog(this);
        String url = getIntent().getStringExtra(BANNER);
        mineBean = getIntent().getStringExtra(BANNER_BEAN);
        if (! TextUtils.isEmpty(mineBean)) {
            //网红投票
            infosBean = GsonTools.jsonToBean(mineBean, MineShowModel.class);
            isStar = true;
        }
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);

        webView.getSettings().setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("vote_share")) {
                    if (isStar == true) {
                        dealUrl(url);
                    }
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.LogShitou("当前加载的", "===>" + url);
                showLoad();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                closePrg();
                if (isStar == true) {
                    dealUrl(url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (errorCode == - 2 || errorCode == - 1) {
                    closeThis();
                }
            }
        });

    }

    /**
     处理不同url的返回事件
     */
    private void dealUrl(String url) {
        if (url.contains("vote_detail")) {
            isBack = true;
            shareUrl = url;
        } else if (url.contains("vote_share")) {
            shareUrl = url;
            showShareDialog(shareUrl);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeThis();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            webBack();
        }
        return false;
    }

    private void webBack() {
        if (isBack) {
            if (webView.canGoBack() == true) {
                webView.goBack();
            } else {
                closeThis();
            }
        } else {
            closeThis();
        }
    }

    /**
     关闭页面
     */
    private void closeThis() {
        try {
            if (loadingDialog.isShowing() == true) {
                closePrg();
            }
            finish();
        } catch (Exception e) {
            this.finish();
        }
    }

    /**
     关闭加载
     */
    private void closePrg() {
        try {
            loadingDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoad() {
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String IMAGE_URL = CommonConfig.APP_ICON;
    //  private CustomDialogManager mCustomDialogManager;
    private MineShowModel infosBean;

    private void showShareDialog(final String myShareUrl) {
        View view = LayoutInflater.from(BannerWebActivity.this).inflate(R.layout.dialog_web_share_item, null);

        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tvWechat = (TextView) view.findViewById(R.id.tv_wechat);
        TextView tvWechatmoment = (TextView) view.findViewById(R.id.tv_wechatmoments);
        TextView tvQq = (TextView) view.findViewById(R.id.tv_qq);

        final CustomDialogManager mCustomDialogManager = new CustomDialogManager(BannerWebActivity.this, view);

        tvWechat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
                share(Wechat.NAME, myShareUrl);
            }
        });

        tvWechatmoment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
                share(WechatMoments.NAME, myShareUrl);
            }
        });

        tvQq.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
                share(QQ.NAME, myShareUrl);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    private void share(String platName, String url) {
        CustomToast.showShort(BannerWebActivity.this, "前往分享...");
        //ShareSDK.initSDK(DetailContentActivity.this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        if (infosBean != null) {
            List<MineShowModel.VoteInfosBean> vote_infos = infosBean.getVote_infos();
            oks.setText(vote_infos.get(0).getDesc());
            oks.setTitle(vote_infos.get(0).getTitle());
            oks.setTitleUrl(vote_infos.get(0).getIcon_url());
            oks.setUrl(url);
        }
        oks.setImageUrl(IMAGE_URL);
        oks.setSite(getResources().getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(BannerWebActivity.this);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(BannerWebActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(BannerWebActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(BannerWebActivity.this, "分享取消");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                if (isStar = true) {
                    webBack();
                } else {
                    closeThis();
                }
                break;
        }
    }

    @Override
    protected void executeOnclickLeftView() {
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
