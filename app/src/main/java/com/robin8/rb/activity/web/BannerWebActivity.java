package com.robin8.rb.activity.web;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.view.MyDialog;

/**
 banner跳转的Webview */
public class BannerWebActivity extends BaseActivity {
    public final static String BANNER = "banner_web";
    private WebView webView;
    private Dialog loadingDialog;

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
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoad();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                closePrg();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeThis();
    }

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


    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {

    }
}
