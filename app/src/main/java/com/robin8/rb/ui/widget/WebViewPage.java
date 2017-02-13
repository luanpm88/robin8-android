package com.robin8.rb.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.robin8.rb.R;

import org.apache.http.util.EncodingUtils;


/**
 * 自定义的webview页面，可作为一个单独的view添加进布局
 * 需要传递url和组织好的params格式 ："GoodsInfoId=id&screenWidth=100"
 */
public class WebViewPage extends LinearLayout {
    public WebView myWebView;
    private ProgressBar prgBar;

    public WebViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        intView();
    }

    public void intView() {
        prgBar = (ProgressBar) this.findViewById(R.id.prgBar);
        myWebView = (WebView) this.findViewById(R.id.myWebView);
        WebSettings setting = myWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setBuiltInZoomControls(false);
        myWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(false);
    }

    public void reload() {
        if (myWebView != null)
            myWebView.reload();
    }

    public void startLoadView(String url, Boolean isGet, String params) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Boolean isStartLoad = false;
        if (isGet) {
            if (!TextUtils.isEmpty(url)) {
                myWebView.loadUrl(url);
                isStartLoad = true;
            }
        } else {
            myWebView.postUrl(url, EncodingUtils.getBytes(params, "BASE64"));
            isStartLoad = true;
        }
        if (isStartLoad) {
            prgBar.setVisibility(View.VISIBLE);
            myWebView.setWebChromeClient(new MyWebViewProgressClient());
            myWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.indexOf("tel:") < 0) {// 页面上有数字会导致连接电话
                        view.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri
                                .parse(url));
                        getContext().startActivity(intent);
                    }
                    return true;
                }
            });
            isStartLoad = false;
        }
    }
    public void onDestroy(){
        myWebView.removeAllViews();
        myWebView.destroy();
    }

    private class MyWebViewProgressClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            prgBar.setProgress(newProgress);
            if (100 == newProgress) {
                prgBar.setVisibility(View.GONE);
            }
        }
    }
}
