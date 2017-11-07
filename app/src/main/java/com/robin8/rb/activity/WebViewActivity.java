package com.robin8.rb.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robin8.rb.R;
import com.robin8.rb.base.BaseActivity;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseRecyclerViewActivity;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.NotifyManager;
import com.robin8.rb.model.CampaignInviteBean;
import com.robin8.rb.model.NotifyMsgEntity;
import com.robin8.rb.module.create.MediaScanner;
import com.robin8.rb.module.create.activity.EditCreateActivity;
import com.robin8.rb.module.create.model.CpsArticleSharesBean;
import com.robin8.rb.module.create.model.CreateFirstDetailModel;
import com.robin8.rb.module.create.model.Image;
import com.robin8.rb.module.create.model.ProductListModel;
import com.robin8.rb.module.create.model.ShareResultModel;
import com.robin8.rb.module.first.activity.SearchKolActivity;
import com.robin8.rb.module.reword.activity.PostInviteesActivity;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.ui.widget.WProgressDialog;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.BitmapUtil;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.GetIntentUtil;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.HtmlUtils;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.view.widget.CircleImageView;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * webview
 */
public class WebViewActivity extends BaseActivity {
    public static final int TYPE_DOC = 0;
    public static final int TYPE_DOCX = 1;
    public static final int TYPE_XLS = 2;
    public static final int TYPE_XLSX = 3;
    private WProgressDialog mWProgressDialog;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private String url;
    private String title;
    private FrameLayout mWebContainer;
    private int from;
    private int mIntentType;
    private LinearLayout contentShareLL;
    private int id;
    private List<CpsArticleSharesBean> mCpsArticleSharesList;
    private BasePresenter mBasePresenter;
    private CustomDialogManager mCustomDialogManager;
    private String mShareUrl;
    private String mImgUrl;
    private String mShareTitle;
    private String category;
    private CreateFirstDetailModel.CpsArticleBean mCpsArticle;
    private ProductListModel.CpsMaterialsBean mCpsMaterialsBean;
    private List<Image> mWebImageList = new ArrayList<Image>();// 内页图片集合
    private String mHtmlData;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWProgressDialog != null) {
                mWProgressDialog.dismiss();
            }
            if (msg.what == 1) {
                CustomToast.showShort(WebViewActivity.this, getString(R.string.pic_download_success));
                MediaScanner scanner = new MediaScanner(WebViewActivity.this.getApplicationContext());
                scanner.scanFile(BitmapUtil.getPath(WebViewActivity.this.getApplicationContext()), "image/jpeg");
                scanner.scanFile(BitmapUtil.getPath(WebViewActivity.this.getApplicationContext()), "image/png");
            }
        }
    };

    @Override
    public void setTitleView() {
        initData();
        if (from == SPConstants.FIRST_PAGER) {
            mTVCenter.setText(R.string.robin8);
        } else {
            mTVCenter.setText(title);
        }
    }

    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        category = intent.getStringExtra("category");
        url = intent.getStringExtra("url");
        from = intent.getIntExtra("from", 0);
        id = intent.getIntExtra("id", 0);
        mImgUrl = intent.getStringExtra("img_url");
        mShareTitle = intent.getStringExtra("share_title");
        Serializable bean = intent.getSerializableExtra("bean");
        if (bean != null && bean instanceof ProductListModel.CpsMaterialsBean) {
            mCpsMaterialsBean = (ProductListModel.CpsMaterialsBean) bean;
        }

    }

    @Override
    public void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_webview, mLLContent, true);
        mWebContainer = (FrameLayout) view.findViewById(R.id.fl_content);
        mWebView = new WebView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        mWebView.setBackgroundResource(R.color.red_custom);
        mWebContainer.addView(mWebView, lp);
        initFrom(view);
        judge();
    }

    private void initFrom(View view) {
        if (from == SPConstants.CREATE_FIRST_LIST || from == SPConstants.MYCREATE_LIST1) {//我的创作
            initViewWhenFromCreateList(view);
        } else if (from == SPConstants.CREATE_FIRST_LIST_PRODUCT) {//从创作商品库进入进商品库
            initViewWhenFromCreateListToProduct(view);
        } else if (from == SPConstants.EDIT_CREATE_ACTIVITY) {//从编辑创作页进商品库
            initViewWhenFromEditCreate(view);
        } else if (from == SPConstants.ARTICLE_LIST) {//文章列表进文章详情
            initViewWhenFromArticleList(view);
        }
    }

    private void initViewWhenFromArticleList(View view) {
        View llBottom2 = view.findViewById(R.id.ll_bottom2);
        llBottom2.setVisibility(View.VISIBLE);
        View lineMiddle = view.findViewById(R.id.line_middle);
        TextView leftTv = (TextView) view.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        lineMiddle.setVisibility(View.GONE);
        rightTv.setVisibility(View.GONE);

        leftTv.setText(R.string.download_all_images_in_this_paper);
        leftTv.setOnClickListener(this);
    }

    private void initViewWhenFromEditCreate(View view) {
        View llBottom2 = view.findViewById(R.id.ll_bottom2);
        llBottom2.setVisibility(View.VISIBLE);
        TextView leftTv = (TextView) view.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        leftTv.setText(R.string.insert_goods);
        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
    }

    private void initViewWhenFromCreateListToProduct(View view) {
        View llBottom2 = view.findViewById(R.id.ll_bottom2);
        llBottom2.setVisibility(View.VISIBLE);
        TextView leftTv = (TextView) view.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        leftTv.setText(R.string.start_create);
        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);
    }

    private void initViewWhenFromCreateList(View view) {
        View llBottom = view.findViewById(R.id.ll_bottom);
        llBottom.setVisibility(View.VISIBLE);
        contentShareLL = (LinearLayout) view.findViewById(R.id.ll_content_share);
        final View shareTV = view.findViewById(R.id.tv_share);
      //  shareTV.setVisibility(View.INVISIBLE);
        contentShareLL.setOnClickListener(this);
       // shareTV.setOnClickListener(this);

        mBasePresenter = new BasePresenter();
        mBasePresenter.getDataFromServer(true, HttpRequest.GET, HelpTools.getUrl(CommonConfig.CPS_ARTICLES_URL + "/" + String.valueOf(id) + "/show"), null, new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
               // LogUtil.LogShitou("创作之详情",response);
                CreateFirstDetailModel bean = GsonTools.jsonToBean(response, CreateFirstDetailModel.class);
                if (bean == null) {
                    return;
                }
                if (bean.getError() == 0) {
                    mCpsArticle = bean.getCps_article();
                    mCpsArticleSharesList = mCpsArticle.getCps_article_shares();
                    initShareList(contentShareLL, mCpsArticleSharesList);
                    shareTV.setVisibility(View.VISIBLE);
                    shareTV.setOnClickListener(WebViewActivity.this);
                }
            }
        });
    }

//    private void parseJson(String response) {
//
//    }

    private void initShareList(LinearLayout linearLayout, List<CpsArticleSharesBean> list) {
        Context context = linearLayout.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout.removeAllViews();
        if (list != null && list.size() >= 0) {
            int size = list.size();
            if (size > 3) {
                size = 3;
            }
            for (int i = 0; i < size; i++) {
                View view = inflater.inflate(R.layout.invitee_item, null);
                CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civ_image);
                BitmapUtil.loadImage(context, list.get(i).getKol_avatar_url(), civImage);
                linearLayout.addView(view);
            }
        }

        View view = inflater.inflate(R.layout.invitee_item, null);
        CircleImageView civImage = (CircleImageView) view.findViewById(R.id.civ_image);
        civImage.setBackgroundResource(R.mipmap.icon_user_defaul);
        linearLayout.addView(view);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (!isDoubleClick()) {
            switch (v.getId()) {
                case R.id.tv_share:

                    shareArticle();
                    break;
                case R.id.ll_content_share:
                    //参与人员
                    skipToShareDetail();
                    break;
                case R.id.tv_left:
                    clickLeftTv();
                    break;
                case R.id.tv_right:
                    clickRightTv();
                    break;

                case R.id.tv_expected_income:
                    skipToDetail();
                    break;
                case R.id.tv_weixin:
                    share(mShareUrl, mImgUrl, Wechat.NAME);
                    break;
                case R.id.tv_wechatmoments:
                    share(mShareUrl, mImgUrl, WechatMoments.NAME);
                    break;
                case R.id.tv_weibo:
                    share(mShareUrl, mImgUrl, SinaWeibo.NAME);
                    break;
                case R.id.tv_qq:
                    share(mShareUrl, mImgUrl, QQ.NAME);
                    break;
                case R.id.tv_qonze:
                    share(mShareUrl, mImgUrl, QZone.NAME);
                    break;
            }
        }
    }

    private void skipToDetail() {
        Intent intent = new Intent(this, BaseRecyclerViewActivity.class);
        intent.putExtra("destination", SPConstants.PRODUCT_LIST);
        intent.putExtra("from", SPConstants.ARTICLE_SHARE);
        intent.putExtra("url", HelpTools.getUrl(CommonConfig.CPS_ARTICLES_URL) + "/" + String.valueOf(mCpsArticle.getId()) + "/materials");
        intent.putExtra("title", getString(R.string.product));
        startActivity(intent);
    }

    private void shareArticle() {
        if (!BaseApplication.getInstance().hasLogined()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (mCpsArticle.getMaterial_total_price() != 0) {
            popSharePromptDialog();
        } else {
            requestShareUrl();
        }
    }

    /**
     * 创作页面分享
     */
    private void popSharePromptDialog() {
        View view = LayoutInflater.from(WebViewActivity.this).inflate(R.layout.dialog_reject_screenshot, null);
        TextView leftTV = (TextView) view.findViewById(R.id.tv_confirm);
        TextView topTv = (TextView) view.findViewById(R.id.tv_top);
        TextView infoTv = (TextView) view.findViewById(R.id.tv_info);
        TextView rightTv = (TextView) view.findViewById(R.id.tv_right);
        infoTv.setText(R.string.share_article_tips);
        topTv.setVisibility(View.GONE);
        rightTv.setText(R.string.cancel);
        final CustomDialogManager cdm = new CustomDialogManager(WebViewActivity.this, view);
        leftTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestShareUrl();
                cdm.dismiss();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdm.dismiss();
            }
        });
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }

    private void clickRightTv() {
        Intent intent = new Intent(this, SearchKolActivity.class);
        intent.putExtra("from", SPConstants.ARTICLE_SEARCH);
        intent.putExtra("url", CommonConfig.ARTICLES_SEARCH_URL);
        if (mCpsMaterialsBean != null) {
            intent.putExtra("title", mCpsMaterialsBean.getGoods_name());
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void clickLeftTv() {
        if (from == SPConstants.CREATE_FIRST_LIST_PRODUCT) {
            if (BaseApplication.getInstance().hasLogined()) {
                Intent intent = new Intent(this, EditCreateActivity.class);
                intent.putExtra("bean", mCpsMaterialsBean);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        } else if (from == SPConstants.EDIT_CREATE_ACTIVITY) {
            NotifyMsgEntity entity = new NotifyMsgEntity(NotifyManager.TYPE_INSERT_PRODUCT, mCpsMaterialsBean);
            NotifyManager.getNotifyManager().notifyChange(entity);
            finish();
        } else if (from == SPConstants.ARTICLE_LIST) {
            downLoadHtml();
        }
    }

    /**
     * 预加载单一条目
     */
    private void downLoadHtml() {
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    final Response response = HttpRequest.getInstance().getSynchrony(url);
                    if (response.isSuccessful()) {
                        mHtmlData = response.body().string();
                        if (!TextUtils.isEmpty(mHtmlData)) {
                            mWebImageList = HtmlUtils.getHtmlImageList(mHtmlData);
                            for (int i = 0; i < mWebImageList.size(); i++) {
                                String src = mWebImageList.get(i).getSrc();
                                if (!TextUtils.isEmpty(src)) {
                                    try {
                                        OkHttpClient client = new OkHttpClient();
                                        Request request = new Request.Builder().url(src).build();//获取请求对象
                                        ResponseBody body = client.newCall(request).execute().body(); //获取响应体
                                        InputStream in = body.byteStream();//获取流
                                        Bitmap bitmap = BitmapFactory.decodeStream(in);//转化为bitmap
                                        BitmapUtil.saveBitmap(BitmapUtil.getPath(WebViewActivity.this) + File.separator + AppUtils.toMD5(src), bitmap);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        //失败
                                        if (mHandler != null) {
                                            mHandler.sendEmptyMessage(0);
                                        }
                                    }
                                }
                                if (i == mWebImageList.size() - 1 && mHandler != null) {
                                    mHandler.sendEmptyMessage(1);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(0);
                    }
                }
            }
        };
        t.start();
    }

    /**
     * 请求分享链接
     */
    private void requestShareUrl() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("cps_article_id", String.valueOf(id));
        mBasePresenter.getDataFromServer(true, HttpRequest.POST, HelpTools.getUrl(CommonConfig.SHARE_ARTICLE_URL), requestParams, new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                ShareResultModel shareResultModel = GsonTools.jsonToBean(response, ShareResultModel.class);
                if (shareResultModel.getError() == 0 && !TextUtils.isEmpty(shareResultModel.getCps_article_share().getShare_url())) {
                    mShareUrl = shareResultModel.getCps_article_share().getShare_url();
                    popShareDialog();
                }
            }
        });
    }

    /**
     * 弹分享框
     */
    private void popShareDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.invite_friends_dialog, null);
        TextView weixinTV = (TextView) view.findViewById(R.id.tv_weixin);
        TextView wechatmomentsTV = (TextView) view.findViewById(R.id.tv_wechatmoments);
        TextView weiboTV = (TextView) view.findViewById(R.id.tv_weibo);
        TextView qqTV = (TextView) view.findViewById(R.id.tv_qq);
        TextView qonzeTV = (TextView) view.findViewById(R.id.tv_qonze);
        TextView cancelTV = (TextView) view.findViewById(R.id.tv_cancel);
        TextView expectedIncomeTV = (TextView) view.findViewById(R.id.tv_expected_income);
        mCustomDialogManager = new CustomDialogManager(this, view);
        expectedIncomeTV.setVisibility(View.VISIBLE);
        expectedIncomeTV.setOnClickListener(this);
        weixinTV.setOnClickListener(this);
        wechatmomentsTV.setOnClickListener(this);
        weiboTV.setOnClickListener(this);
        qqTV.setOnClickListener(this);
        qonzeTV.setOnClickListener(this);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialogManager.dismiss();
            }
        });
        mCustomDialogManager.dg.setCanceledOnTouchOutside(true);
        mCustomDialogManager.dg.getWindow().setGravity(Gravity.BOTTOM);
        mCustomDialogManager.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        mCustomDialogManager.showDialog();
    }

    /**
     * 分享
     *
     * @param shareUrl
     */
    private void share(String shareUrl, String imgUrl, String platName) {
        if (mCustomDialogManager != null) {
            mCustomDialogManager.dismiss();
        }

        CustomToast.showShort(this, "正在前往分享...");
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        oks.setCallback(new MySharedListener());
        oks.setPlatform(platName);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setTitle(mShareTitle);
        if (SinaWeibo.NAME.equals(platName)) {
            oks.setText(mShareTitle + shareUrl);
        } else {
            oks.setText(mShareTitle);
        }
        oks.setTitleUrl(shareUrl);
        oks.setImageUrl(imgUrl);
        oks.setUrl(shareUrl);
        oks.setSite(getString(R.string.app_name));
        oks.setSiteUrl(CommonConfig.SITE_URL);
        oks.show(this);
    }

    private class MySharedListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            CustomToast.showShort(WebViewActivity.this, "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            CustomToast.showShort(WebViewActivity.this, "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            CustomToast.showShort(WebViewActivity.this, "分享取消");
        }
    }

    private void skipToShareDetail() {
        Intent intent = new Intent(this, PostInviteesActivity.class);
        List list = new ArrayList();
        if (mCpsArticleSharesList != null && mCpsArticleSharesList.size() > 0) {
            for (int i = 0; i < mCpsArticleSharesList.size(); i++) {
                CpsArticleSharesBean cpsArticleSharesBean = mCpsArticleSharesList.get(i);
                CampaignInviteBean.InviteesBean bean = new CampaignInviteBean.InviteesBean();
                bean.setName(cpsArticleSharesBean.getKol_name());
                bean.setAvatar_url(cpsArticleSharesBean.getKol_avatar_url());
                if (!TextUtils.isEmpty(cpsArticleSharesBean.getKol_id())){
                    bean.setId(Integer.valueOf(cpsArticleSharesBean.getKol_id()));
                }else{
                    LogUtil.LogShitou("谁的是空name","===>"+cpsArticleSharesBean.getKol_name());
                    LogUtil.LogShitou("谁的是空id","===>"+cpsArticleSharesBean.getId());
                }
              //  bean.setId(Integer.valueOf(cpsArticleSharesBean.getKol_id()));

                list.add(bean);
            }
        }
        intent.putExtra("invitees", (Serializable) list);
        startActivity(intent);
    }

    public void judge() {

        if(TextUtils.isEmpty(url)){
            return;
        }

        if (url.equals(".doc")) {
            mIntentType = TYPE_DOC;
            downLoad("asdf.doc");
        } else if (url.endsWith(".docx")) {
            mIntentType = TYPE_DOCX;
            downLoad("asdf.docx");
        } else if (url.endsWith(".xls")) {
            mIntentType = TYPE_XLS;
            downLoad("asdf.xls");
        } else if (url.endsWith(".xlsx")) {
            mIntentType = TYPE_XLSX;
            downLoad("asdf.xlsx");
        } else {
            initWebView();
        }
    }

    private void downLoad(String name) {
        HttpRequest.getInstance().downLoad(url, name, new RequestCallback() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }

                Intent intent = null;
                switch (mIntentType) {
                    case TYPE_DOC:
                    case TYPE_DOCX:
                        intent = GetIntentUtil.getWordFileIntent(response);
                        startActivity(intent);
                        break;

                    case TYPE_XLS:
                    case TYPE_XLSX:
                        intent = GetIntentUtil.getExcelFileIntent(response);
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    public void initWebView() {
        initWebSettings(mWebView);
        mWebView.loadUrl(url);
        if (mWProgressDialog == null) {
            mWProgressDialog = WProgressDialog.createDialog(this);
        }
        mWProgressDialog.show();
    }

    /**
     * 设置webview
     *
     * @param webView
     */
    private void initWebSettings(WebView webView) {
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        if (NetworkUtil.isNetworkAvailable(this)) {
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebSettings.setUseWideViewPort(false);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSavePassword(true);
        mWebSettings.setSaveFormData(true);
        mWebSettings.setGeolocationEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        webView.requestFocus();

        mWebSettings.setAppCacheMaxSize(0);
        mWebSettings.setAllowFileAccess(false);
        mWebSettings.setAppCacheEnabled(false);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    @Override
    protected void onDestroy() {
        if (mWProgressDialog != null) {
            mWProgressDialog.dismiss();
            mWProgressDialog = null;
        }

        if (mWebContainer != null) {
            mWebContainer.removeAllViews();
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        mWebView.setVisibility(View.GONE);
        mWebView.loadUrl("about:blank");
        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    protected void executeOnclickLeftView() {
        finish();
    }

    @Override
    protected void executeOnclickRightView() {
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.logXXfigo("onPageStarted url=" + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.logXXfigo("shouldOverrideUrlLoading url=" + url);

            if (url.startsWith("weixin://wap/pay?")){
                try{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);

                }catch (ActivityNotFoundException e){
                   CustomToast.showShort(WebViewActivity.this,"请安装微信最新版！");
                }
            }else if (url.startsWith("openapp.jdmobile://virtual?")){
                try{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    CustomToast.showShort(WebViewActivity.this,"请安装京东最新版！");
                }
            }else if (url.endsWith(".apk")) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mWProgressDialog != null) {
                mWProgressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (mWProgressDialog != null) {
                mWProgressDialog.dismiss();
            }
            try {
                view.clearView();
                view.clearHistory();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
        }

    }

}
