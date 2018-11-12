package com.robin8.rb.activity.web;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.module.reword.helper.DetailContentHelper;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.view.MyDialog;

import org.json.JSONException;
import org.json.JSONObject;

import static com.robin8.rb.util.LogUtil.LogShitou;

public class PutWebActivity extends BaseActivity {

    private WebView webView;
    private Dialog loadingDialog;
    private String loadLogin;
    private String loadReg;
    private String loadWallet;
    private String loadInto;
    private String loadPwd;
    private static boolean isBack = false;
    private String myJsonReg;
    private String myJsonLogin;
    private String myJsonForGet;

    public static final String PUT_TYPE = "type";
private String extra;
    @Override
    public void setTitleView() {
        mIVBack.setOnClickListener(this);
        mTVCenter.setOnClickListener(this);
    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_put_web, mLLContent, true);
        webView = ((WebView) view.findViewById(R.id.web_view));
        // final String loadurl = "http://192.168.51.170:3000/pages/pmes_demo";
        // final String loadurl = "https://qa.robin8.nt/pages/pmes_demo";
        loadLogin = CommonConfig.WEBPATH + "login.html";
        loadReg = CommonConfig.WEBPATH + "reg.html";
        loadWallet = CommonConfig.WEBPATH + "wallet.html";
        loadPwd = CommonConfig.WEBPATH + "password.html";
        loadInto = HelpTools.getUrl(CommonConfig.PUT_WALLET);
        LogShitou("putAddress是否存在", "--"+HelpTools.getLoginInfo(HelpTools.WEBADDRESS));
        loadingDialog = MyDialog.createLoadingDialog(PutWebActivity.this);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //  webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // Map<String, String> map = new HashMap<String, String>();
        // map.put("Authorization", BaseApplication.getInstance().getLoginBean().getKol().getIssue_token());
        // webView.loadUrl(loadurl, map);
        extra = getIntent().getStringExtra(PUT_TYPE);
        String xml = HelpTools.getLoginInfo(HelpTools.WEBADDRESS);
//        if (TextUtils.isEmpty(xml)) {
//            if (extra.equals("0")) {
//                webView.loadUrl(loadInto);
//            } else if (extra.equals("1")||extra.equals("2")) {
//                webView.loadUrl(loadReg);
//            } else {
//                webView.loadUrl(loadInto);
//            }
//        } else {
//            webView.loadUrl(loadLogin);
//        }
        webView.loadUrl(loadWallet);
        webView.addJavascriptInterface(new JsInterface(), "jwPut");
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

                LogShitou("error", error + "");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogShitou("开始", "==>" + url);
                // showLoad();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mTVCenter.setText(view.getTitle());
                if (url.endsWith("record.html") ||url.endsWith("qa.html")) {
                    isBack = true;
                } else {
                    isBack = false;
                }

                // closePrg();
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

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            // 弹窗处理
            AlertDialog.Builder b2 = new AlertDialog.Builder(PutWebActivity.this).setTitle(R.string.app_name).setMessage(message).setPositiveButton("ok", new AlertDialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b2.setCancelable(true);
            b2.create();
            b2.show();
            return true;

        }
    }



    public class JsInterface {
        Context mContext;

        public JsInterface() {
        }

        //注册
        @JavascriptInterface
        public void put_reg(String json) {
            myJsonReg = json;
            handler.sendEmptyMessage(0);
        }

        //注册
        @JavascriptInterface
        public void put_begin() {
            handler.sendEmptyMessage(2);
        }

        //登陆
        @JavascriptInterface
        public void put_login(String json) {
            myJsonLogin = json;
            handler.sendEmptyMessage(1);
        }

        //html页面获取数据
        @JavascriptInterface
        public String put_pmes_data() {
            jsonObject = new JSONObject();
            try {
                String web_token = HelpTools.getCommonXml(HelpTools.WEBTOKEN);
                String web_key = HelpTools.getCommonXml(HelpTools.WEBKEY);
                //  LogShitou("本地保存的key", web_key + "/////////\n" + web_token);
                if (! TextUtils.isEmpty(web_token)) {
                    jsonObject.put("token", web_token);
                } else {
                    jsonObject.put("token", "");
                }
                if (! TextUtils.isEmpty(web_key)) {
                    jsonObject.put("public_key", web_key);
                } else {
                    jsonObject.put("public_key", "");
                }
                jsonObject.put("amount_frozen", amount_frozen);
                jsonObject.put("amount_active", amount_active);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String key = jsonObject.toString();
            LogShitou("html调用put_pmes_data", "===>" + key);
            return key;
        }

        @JavascriptInterface
        public String current_token_data(){
            JSONObject  jsonObject = new JSONObject();
            try {
                jsonObject.put("current_token", BaseApplication.getInstance().getLoginBean().getKol().getIssue_token());
            } catch (JSONException e) {
                e.printStackTrace();
            }
           // String key = jsonObject.toString();
            String key = BaseApplication.getInstance().getLoginBean().getKol().getIssue_token();
            LogShitou("html调用流水", "===>" + key);
            return key;
        }

        //html获取登陆后的数据
        @JavascriptInterface
        public String put_wallet_data() {
            jsonObject = new JSONObject();
            try {
//                jsonObject.put("amount_frozen", amount_frozen);
//                jsonObject.put("amount_active", amount_active);
                jsonObject.put("current_token", BaseApplication.getInstance().getLoginBean().getKol().getIssue_token());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String key = jsonObject.toString();
            LogShitou("html调用put_wallet_data", "===>" + key);
            return key;
        }
        //html获取新的add
        @JavascriptInterface
        public String put_put_address(){
            String put_add = HelpTools.getLoginInfo(HelpTools.WEBADDRESS);
            return put_add;
        }

        //html忘记密码更新token
        @JavascriptInterface
        public void put_recover(String json){
            myJsonForGet = json;
            handler.sendEmptyMessage(3);
        }

        //上传本地信息
        @JavascriptInterface
        public String put_ToJs() {
            jsonObject = new JSONObject();
            try {
                String email = BaseApplication.getInstance().getLoginBean().getKol().getEmail();
                String number = BaseApplication.getInstance().getLoginBean().getKol().getMobile_number();
                if (TextUtils.isEmpty(number)) {
                    jsonObject.put("phone_num", "");
                } else {
                    jsonObject.put("phone_num", number);
                }

                jsonObject.put("access_token", BaseApplication.getInstance().getLoginBean().getKol().getIssue_token());
                String deviceId = AppUtils.getDeviceId(PutWebActivity.this);
                if (TextUtils.isEmpty(deviceId)) {
                    jsonObject.put("device_id", "000015910439098");
                } else {
                    jsonObject.put("device_id", deviceId);
                }
                if (TextUtils.isEmpty(email)) {
                    jsonObject.put("email", "");
                } else {
                    jsonObject.put("email", email);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            String s = jsonObject.toString();
            LogShitou("调用put_to_js", "===>" + s);
            return s;
        }
    }

    private JSONObject jsonObject;
    private int amount_active;
    private int amount_frozen;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    LogShitou("html端的注册数据", "===>" + myJsonReg);
                    try {
                        JSONObject object = new JSONObject(myJsonReg);
                        String token = object.getString("token");
                        String public_key = object.getString("public_key");
                        String put_address = object.getString("put_address");
                        String passWord = object.getString("password");
                        amount_active = object.getInt("amount_active");
                        amount_frozen = object.getInt("amount_frozen");
                        HelpTools.insertCommonXml(HelpTools.WEBTOKEN, token);
                        HelpTools.insertCommonXml(HelpTools.WEBKEY, public_key);
                        HelpTools.insertLoginInfo(HelpTools.WEBADDRESS, put_address);
                        if (! TextUtils.isEmpty(passWord) && ! TextUtils.isEmpty(put_address)) {
                            webView.loadUrl(loadWallet);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    LogUtil.LogShitou("html端的登陆数据", "===>" + myJsonLogin);
                    if (! TextUtils.isEmpty(myJsonLogin)) {
                        try {
                            JSONObject object = new JSONObject(myJsonLogin);
                            amount_active = object.getInt("amount_active");
                            amount_frozen = object.getInt("amount_frozen");
                            webView.loadUrl(loadWallet);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 2:
                    webView.loadUrl(loadReg);
                    break;
                case 3:
                    LogShitou("html get new t and pk===>>",myJsonForGet);
                    if (!TextUtils.isEmpty(myJsonForGet)){
                        try {
                            JSONObject object = new JSONObject(myJsonForGet);
                            String token = object.getString("token");
                            String public_key = object.getString("public_key");
                            HelpTools.insertCommonXml(HelpTools.WEBTOKEN, token);
                            HelpTools.insertCommonXml(HelpTools.WEBKEY, public_key);
                            webView.loadUrl(loadLogin);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_center:
                break;
            case R.id.iv_back:
                if (isBack) {
                    if (webView.canGoBack() == true) {
                        webView.goBack();
                    } else {
                        closeThis();
                    }
                } else {
                    closeThis();
                }
                break;
        }
    }

    private void closeThis() {
        try {
            //            if (loadingDialog.isShowing() == true) {
            //                closePrg();
            //            }
            if (extra.equals("2")){
                setResult(DetailContentHelper.IMAGE_REQUEST_PUT_RESULT);
            }
            finish();
        } catch (Exception e) {
            this.finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBack) {
                if (webView.canGoBack() == true) {
                    webView.goBack();
                } else {
                    // finish();
                    closeThis();
                }
            } else {
                // finish();
                closeThis();
            }
        }
        return false;
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
        //closeThis();
    }

    @Override
    protected void executeOnclickRightView() {

    }

}
