package com.robin8.rb.ui.activity.web;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.constants.CommonConfig;
import com.robin8.rb.ui.dialog.MyDialog;
import com.robin8.rb.ui.module.mine.model.MineShowModel;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.share.RobinShareDialog;

import java.util.List;

/**
 Banner跳转的Webview
 */
public class BannerWebActivity extends BaseActivity{
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
    RobinShareDialog shareDialog;
    private void showShareDialog(final String myShareUrl) {
        RobinShareDialog shareDialog = new RobinShareDialog(this);
        if (infosBean != null) {
            List<MineShowModel.VoteInfosBean> vote_infos = infosBean.getVote_infos();
            shareDialog.shareFacebook(myShareUrl,vote_infos.get(0).getTitle(),vote_infos.get(0).getDesc(),IMAGE_URL);
            shareDialog.show();
//            oks.setText(vote_infos.get(0).getDesc());
//            oks.setTitle(vote_infos.get(0).getTitle());
//            oks.setTitleUrl(vote_infos.get(0).getIcon_url());
//            oks.setUrl(url);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (shareDialog != null){
            shareDialog.onActivityResult(requestCode,resultCode,data);
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
