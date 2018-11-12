package com.robin8.rb.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.igexin.sdk.PushManager;
import com.robin8.rb.R;
import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.base.BaseBackHomeActivity;
import com.robin8.rb.base.BasePager;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.module.mine.rongcloud.RongCloudBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.pager.FindPager;
import com.robin8.rb.pager.FirstPager;
import com.robin8.rb.pager.InfluencePager;
import com.robin8.rb.pager.MinePager;
import com.robin8.rb.pager.RewordPager;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.receiver.DemoIntentService;
import com.robin8.rb.receiver.DemoPushService;
import com.robin8.rb.task.LocationService;
import com.robin8.rb.update.UpdateNewApk;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.CustomToast;
import com.robin8.rb.util.DensityUtils;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.util.StringUtil;
import com.robin8.rb.view.widget.CustomDialogManager;

import java.util.ArrayList;

public class MainActivity extends BaseBackHomeActivity implements View.OnClickListener {

    private static final int INFLUENCE_LIST = 1;
    private static final int CAMPAIGN_LIST = 0;
    private static final int MY = 3;
    private static final int FIND = 2;
    private ArrayList<BasePager> mPagerList;
    private RewordPager mRewordPager;
    private FirstPager mFirstPager;
    // private CreatePager mCreatePager;
    private FindPager mFindPager;
    private MinePager mMinePager;
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mVPContentPager;
    private RadioGroup mRGContentBottom;
    private RadioButton mRBBottomFirst;
    private RadioButton mRBBottomCampaign;
    // private RadioButton mRBBottomCreate;
    private RadioButton mRBBottomFind;
    private RadioButton mRBBottomInfluence;
    private RadioButton mRBBottomMine;
    private int mLastPosition;
    private UpdateNewApk mUpdateNewApk = null;
    private LocationService locationService;

    private Handler mCheckVersionHandler = new Handler() {

        public void handleMessage(Message msg) {

            new Thread() {

                @Override
                public void run() {

                    super.run();
                }
            }.start();
        }
    };

    private final static double UNKNOW = - 2000;
    private double latitude = UNKNOW;
    private double longitude = UNKNOW;
    //  private NotificationPager mNotificationPager;
    private InfluencePager mInfluencePager;
    // private CustomRedDotRadioButton mRBBottomNotification;
    private Intent intent;
    private String register_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initNotify();
        intent = getIntent();
        register_main = intent.getStringExtra("register_main");
        if (! TextUtils.isEmpty(register_main)) {
            if (register_main.equals("zhu")) {
                mPageName = StatisticsAgency.CAMPAIGN_LIST;
            } else if (register_main.equals("influence")) {
                mPageName = StatisticsAgency.INFLUENCE_LIST;
            }else if (register_main.equals("read")){
                mPageName = StatisticsAgency.FIND_LIST;
            }
        }
        //        else {
        //            mPageName = StatisticsAgency.CAMPAIGN_LIST;
        //        }
        setContentView(R.layout.activity_main);
        setSwipeBackEnable(false);
        checkNewVersion();
        postData();
        startLocate();
        initView();
        initData();
        if (TextUtils.isEmpty(HelpTools.getCommonXml(HelpTools.CloudToken))) {
            initGetRongCloud();
        }
        if (TextUtils.isEmpty((HelpTools.getCommonXml(HelpTools.ShadowFirst)))) {
            showShadowDialog(MainActivity.this, 0);
        } else {
            if (! (HelpTools.getCommonXml(HelpTools.ShadowFirst)).equals(getString(R.string.submit))) {
                showShadowDialog(MainActivity.this, 0);
            }
        }
        //01 演示图片.jpg==||||||==/storage/emulated/0/Pictures/01 演示图片.jpg
        // showShadowDialog(MainActivity.this, 0);
    }

    private void startLocate() {
        // -----------location config ------------
        locationService = BaseApplication.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    @Override
    protected void onStop() {

        if (locationService != null && mListener != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
    }

    private void postData() {

        BasePresenter base = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        requestParams.put("app_platform", SPConstants.ANDROID);
        requestParams.put("app_version", AppUtils.getVersionName(this.getApplicationContext()));
        requestParams.put("os_version", AppUtils.getSystemVersion());
        requestParams.put("device_token", StringUtil.getToken(this.getApplicationContext()));
        requestParams.put("device_model", android.os.Build.MODEL);
        requestParams.put("city_name", CacheUtils.getString(this, SPConstants.LOCATION_CITY, ""));
        requestParams.put("IMEI", AppUtils.getImei(this));
        if (longitude != UNKNOW && latitude != UNKNOW) {
            requestParams.put("longitude", longitude);
            requestParams.put("latitude", latitude);
            LogUtil.logXXfigo("UNKNOW longitude=" + longitude + "latitude=" + latitude);
        }
        base.getDataFromServer(true, HttpRequest.PUT, HelpTools.getUrl(CommonConfig.UPDATE_PROFILE_URL), requestParams, null);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mUpdateNewApk != null) {
            mUpdateNewApk = null;
        }

        if (mCheckVersionHandler != null) {
            mCheckVersionHandler.removeCallbacksAndMessages(null);
            mCheckVersionHandler = null;
        }

    }

    public void checkNewVersion() {
        // 版本检查更新
        if (NetworkUtil.isNetworkAvailable(this.getApplicationContext())) {// 有网络
            mUpdateNewApk = new UpdateNewApk(this, mCheckVersionHandler);
            mUpdateNewApk.checkNewVersion(false);
            mCheckVersionHandler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    mCheckVersionHandler.sendEmptyMessage(0);
                }
            }, 1000);
        } else {
            mCheckVersionHandler.sendEmptyMessage(0);
        }
    }

    private void initNotify() {
        //PushManager.getInstance().initialize(this.getApplicationContext());
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        boolean notifyB = CacheUtils.getBoolean(this, SPConstants.NOTIFY_TOGGLE, true);
        if (notifyB) {
            PushManager.getInstance().turnOnPush(this);//打开个推开关
        } else {
            PushManager.getInstance().turnOffPush(this);
        }
    }

    /**
     初始化界面
     */
    @SuppressWarnings("deprecation")
    private void initView() {

        mVPContentPager = (ViewPager) findViewById(R.id.vp_content_pager);
        mRGContentBottom = (RadioGroup) findViewById(R.id.rg_content_bottom);
        mRBBottomFirst = (RadioButton) findViewById(R.id.rb_bottom_first);
        //  mRBBottomNotification = (CustomRedDotRadioButton) findViewById(R.id.rb_bottom_notification);
        mRBBottomCampaign = (RadioButton) findViewById(R.id.rb_bottom_campaign);
        mRBBottomInfluence = (RadioButton) findViewById(R.id.rb_bottom_influence);
        //  mRBBottomCreate = (RadioButton) findViewById(R.id.rb_bottom_create);
        mRBBottomFind = (RadioButton) findViewById(R.id.rb_bottom_find);
        mRBBottomMine = (RadioButton) findViewById(R.id.rb_bottom_mine);
        setRadioButtonDrawableSize(mRBBottomFirst);
        setRadioButtonDrawableSize(mRBBottomInfluence);
        setRadioButtonDrawableSize(mRBBottomCampaign);
        setRadioButtonDrawableSize(mRBBottomFind);
        setRadioButtonDrawableSize(mRBBottomMine);
    }

    /**
     调整bottomButton图片大小
     @param radioButton
     */
    private void setRadioButtonDrawableSize(RadioButton radioButton) {

        Drawable drawable = null;
        switch (radioButton.getId()) {
            case R.id.rb_bottom_campaign:
                drawable = ContextCompat.getDrawable(this, R.drawable.bottom_reword_selector);
                break;
            case R.id.rb_bottom_influence:
                drawable = ContextCompat.getDrawable(this, R.drawable.bottom_influence_selector);
                break;
            case R.id.rb_bottom_find:
                drawable = ContextCompat.getDrawable(this, R.drawable.bottom_create_selector);
                break;

            case R.id.rb_bottom_mine:
                drawable = ContextCompat.getDrawable(this, R.drawable.bottom_mine_selector);
                break;
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, DensityUtils.dp2px(this, 25), DensityUtils.dp2px(this, 25));
            radioButton.setCompoundDrawables(null, drawable, null, null);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     初始化参数
     */
    public void initData() {

        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fm.getFragments() != null) {
            fm.getFragments().clear();
        }

        if (mPagerList == null) {
            mPagerList = new ArrayList<BasePager>();
        }
        //        if (mFirstPager == null) {
        //            //  mFirstPager = new FirstPager(this, mVPContentPager);
        //            mFirstPager = new FirstPager(this);
        //        }
        if (mRewordPager == null) {
            mRewordPager = new RewordPager(this);
        }

        if (mInfluencePager == null) {
            mInfluencePager = new InfluencePager(this);
        }

        if (mFindPager == null) {
            mFindPager = new FindPager(this);
        }

        if (mMinePager == null) {
            mMinePager = new MinePager(this);
        }
        //通知pager
        //        if (mNotificationPager == null) {
        //  mNotificationPager = new NotificationPager(this);
        //        }

        mPagerList.clear();
        //mPagerList.add(mFirstPager);
        // mPagerList.add(mNotificationPager);
        mPagerList.add(mRewordPager);
        mPagerList.add(mInfluencePager);
        mPagerList.add(mFindPager);
        mPagerList.add(mMinePager);

        if (mPagerAdapter == null) {
            mPagerAdapter = new MyPagerAdapter();
        }
        mVPContentPager.setAdapter(mPagerAdapter);
        mVPContentPager.setOffscreenPageLimit(4);
        //
        // 设置默认显示的界面 默认显示首页
        // mRGContentBottom.check(R.id.rb_bottom_first);
        if (! TextUtils.isEmpty(register_main)) {
            if (register_main.equals("zhu")) {
                mRGContentBottom.check(R.id.rb_bottom_campaign);
                mVPContentPager.setCurrentItem(0);
            } else if (register_main.equals("influence")) {
                mRGContentBottom.check(R.id.rb_bottom_influence);
                mVPContentPager.setCurrentItem(1);
                // mPagerList.get(2).initData();
            } else if (register_main.equals("read")){
                mRGContentBottom.check(R.id.rb_bottom_find);
                mVPContentPager.setCurrentItem(2);
            }else {
                mRGContentBottom.check(R.id.rb_bottom_campaign);
            }
        } else {
            mRGContentBottom.check(R.id.rb_bottom_campaign);
        }
        // 让首页界面加载数据
        mPagerList.get(0).initData();
        // 监听ViewPager的页签的变化
        mVPContentPager.setOnPageChangeListener(new MyOnPageChangeListener());
        // 监听底部 页签单选框
        mRGContentBottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        // mRBBottomFirst.setOnClickListener(this);
        mRBBottomCampaign.setOnClickListener(this);
        mRBBottomMine.setOnClickListener(this);
        mRBBottomFind.setOnClickListener(this);
        mRBBottomInfluence.setOnClickListener(this);
    }

    /**
     获取融云的token
     */
    private void initGetRongCloud() {
        BasePresenter base = new BasePresenter();
        RequestParams requestParams = new RequestParams();
        if (! TextUtils.isEmpty(HelpTools.getLoginInfo(HelpTools.LoginNumber))) {
            requestParams.put("userId", HelpTools.getLoginInfo(HelpTools.LoginNumber));
            LoginBean loginBean = BaseApplication.getInstance().getLoginBean();
            if (loginBean!=null){
                String name = loginBean.getKol().getName();
                String avatar_url = loginBean.getKol().getAvatar_url();
                if (TextUtils.isEmpty(name)) {
                    requestParams.put("name", HelpTools.getLoginInfo(HelpTools.LoginNumber));
                } else {
                    requestParams.put("name", BaseApplication.getInstance().getLoginBean().getKol().getName());
                }
                if (TextUtils.isEmpty(avatar_url)) {
                    requestParams.put("portraitUri", CommonConfig.APP_ICON);
                } else {
                    requestParams.put("portraitUri", BaseApplication.getInstance().getLoginBean().getKol().getAvatar_url());
                }
            }else {
                requestParams.put("name", HelpTools.getLoginInfo(HelpTools.LoginNumber));
                requestParams.put("portraitUri", CommonConfig.APP_ICON);
            }


            base.getDataFromServer(false, HttpRequest.POST, CommonConfig.RONG_CLOUD_URL, requestParams, new RequestCallback() {

                @Override
                public void onError(Exception e) {
                    CustomToast.showShort(MainActivity.this, getString(R.string.no_net));
                }

                @Override
                public void onResponse(String response) {
                    RongCloudBean rongCloudBean = GsonTools.jsonToBean(response, RongCloudBean.class);
                    if (rongCloudBean.getCode() == 200) {
                        HelpTools.insertCommonXml(HelpTools.CloudToken, rongCloudBean.getToken());
                    } else {
                        HelpTools.insertCommonXml(HelpTools.CloudToken, "");
                    }

                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }

    //    public void hideNotificationRedDot(boolean b) {
    //
    //        if (mRBBottomNotification == null) {
    //            return;
    //        }
    //        if (b) {
    //            mRBBottomNotification.hidRedDot();
    //        } else {
    //            mRBBottomNotification.showRedDot();
    //        }
    //    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         当rodioButton 切换时回调 点击RadioButton 切换到相应的Pager界面
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {

                case R.id.rb_bottom_campaign:
                    mVPContentPager.setCurrentItem(0, false);
                    break;
                case R.id.rb_bottom_influence:
                    if (! isLogined()) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("influence", StatisticsAgency.INFLUENCE_LIST);
                        startActivity(intent);
                    } else {
                        mVPContentPager.setCurrentItem(1, false);
                    }
                    break;
                case R.id.rb_bottom_find:
                    mVPContentPager.setCurrentItem(2, false);
                    break;
                case R.id.rb_bottom_mine:
                    mVPContentPager.setCurrentItem(3, false);
                    break;
            }
        }
    }


    private boolean isLogined() {
        BaseApplication baseApplication = BaseApplication.getInstance();
        return baseApplication.hasLogined();
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            BasePager pager = mPagerList.get(position);
            View view = pager.rootView;// 根据position获取 3个子界面的一个View
            container.addView(view);
            if (position != 0) {
                //                                if (position!=2){
                //                                    mPagerList.get(position).initData();// 让具体的Pager子类加载数据
                //                                }
                mPagerList.get(position).initData();// 让具体的Pager子类加载数据
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);// 移除Item
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         当某一个界面被旋转 回调
         */
        @Override
        public void onPageSelected(int position) {

            switch (position) {

                case CAMPAIGN_LIST:
                    mPageName = StatisticsAgency.CAMPAIGN_LIST;
                    onePageSelected(CAMPAIGN_LIST);
                    mRBBottomCampaign.setChecked(true);
                    break;
                case INFLUENCE_LIST:
                    mPageName = StatisticsAgency.INFLUENCE_LIST;
                    onePageSelected(INFLUENCE_LIST);
                    mRBBottomInfluence.setChecked(true);
                    mPagerList.get(INFLUENCE_LIST).initData();
                    break;
                case FIND:
                    mPageName = StatisticsAgency.FIND_LIST;
                    onePageSelected(FIND);
                    mRBBottomFind.setChecked(true);
                    mPagerList.get(FIND).initData();
                    break;
                case MY:
                    mPageName = StatisticsAgency.MY;
                    onePageSelected(MY);
                    mRBBottomMine.setChecked(true);
                    mPagerList.get(MY).initData();
                    //我的页面蒙版
                    if (TextUtils.isEmpty((HelpTools.getCommonXml(HelpTools.ShadowMine)))) {
                        showShadowDialog(MainActivity.this, 4);
                    } else {
                        if (! (HelpTools.getCommonXml(HelpTools.ShadowMine)).equals(getString(R.string.submit))) {
                            showShadowDialog(MainActivity.this, 4);
                        }
                    }
                    // showShadowDialog(MainActivity.this, 4);

                    break;
            }
            StatisticsAgency.onPageStart(MainActivity.this, mPageName);
            mLastPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void onePageSelected(int position) {

        //        if (mPageName == StatisticsAgency.CAMPAIGN_LIST) {
        //            BasePager basePager = mPagerList.get(CAMPAIGN_LIST);
        //            if (basePager instanceof RewordPager) {
        //                RewordPager pager = (RewordPager) basePager;
        //                pager.initVpData();
        //                pager.setVpAuto();
        //            }
        //        }

        if (position == mLastPosition) {
            return;
        }
        if (mLastPosition == MY) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.MY);
        } else if (mLastPosition == CAMPAIGN_LIST) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.CAMPAIGN_LIST);
        } else if (mLastPosition == INFLUENCE_LIST) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.INFLUENCE_LIST);
        } else if (mLastPosition == FIND) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.FIND_LIST);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //        if (! TextUtils.isEmpty(register_main)) {
        //            if (register_main.equals("zhu")) {
        //                LogUtil.LogShitou("是你吗","CAMPAIGN_LIST");
        //                mPageName = StatisticsAgency.CAMPAIGN_LIST;
        //            } else if (register_main.equals("influence")) {
        //                mPageName = StatisticsAgency.INFLUENCE_LIST;
        //                // onePageSelected(INFLUENCE_LIST);
        //            }
        //        }
//        if (mPageName == StatisticsAgency.MY) {
//            mPagerList.get(MY).initData();
//        }

        //        if (mPageName == StatisticsAgency.INFLUENCE_LIST) {
        //            if (! TextUtils.isEmpty(register_main)) {
        //               if (register_main.equals("influence")) {
        //                    mPageName = StatisticsAgency.INFLUENCE_LIST;
        //                }else {
        //                   mPagerList.get(INFLUENCE_LIST).pasue();
        //               }
        //            }else {
        //                mPagerList.get(INFLUENCE_LIST).pasue();
        //            }
        //        }
        //        if (mPageName.equals(NOTIFICATION_LIST)) {
        //            mPagerList.get(NOTIFICATION_LIST).initData();
        //        }
        //        if (mPageName.equals(StatisticsAgency.CAMPAIGN_LIST)) {
        //            mPagerList.get(CAMPAIGN_LIST).initData();
        //        }
        //        if (mFirstPager != null) {
        //            mFirstPager.changeVisibleView();
        //        }
    }

    private boolean mHasPostLocation;
    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError && ! mHasPostLocation) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                mHasPostLocation = true;
                postData();
            }
        }

    };

    public void showShadowDialog(final Activity activity, final int page) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_shadow_layout, null);
        ImageView imgBg = (ImageView) view.findViewById(R.id.img_shadow_mine);
        final ImageView imgBgFirst = (ImageView) view.findViewById(R.id.img_shadow_first);
        final ImageView imgBgFirstRight = (ImageView) view.findViewById(R.id.img_shadow_first_right);
        RelativeLayout llShadow = (RelativeLayout) view.findViewById(R.id.ll_shadow);
        LinearLayout llFirstBottom = (LinearLayout) view.findViewById(R.id.ll_first_bottom);
        final ImageView imgStartLeft = (ImageView) view.findViewById(R.id.img_shadow_first_start_left);
        final ImageView imgStartRight = (ImageView) view.findViewById(R.id.img_shadow_first_start_right);
        if (page == 0) {
            imgBgFirst.setVisibility(View.VISIBLE);
            imgBgFirstRight.setVisibility(View.INVISIBLE);
            llFirstBottom.setVisibility(View.VISIBLE);
            imgStartLeft.setVisibility(View.VISIBLE);
            imgStartRight.setVisibility(View.INVISIBLE);
        } else {
            imgBg.setVisibility(View.VISIBLE);
        }
        final CustomDialogManager cdm = new CustomDialogManager(activity, view);
        llShadow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (page == 0) {
                    HelpTools.insertCommonXml(HelpTools.ShadowFirst, getString(R.string.submit));
                    if (imgStartLeft.getVisibility() == View.VISIBLE) {
                        imgStartLeft.setVisibility(View.INVISIBLE);
                        imgBgFirst.setVisibility(View.INVISIBLE);
                        imgBgFirstRight.setVisibility(View.VISIBLE);
                        imgStartRight.setVisibility(View.VISIBLE);
                    } else {
                        cdm.dismiss();
                    }
                } else {
                    HelpTools.insertCommonXml(HelpTools.ShadowMine, getString(R.string.submit));
                    cdm.dismiss();
                }
            }
        });

        Window win = cdm.dg.getWindow();

        WindowManager.LayoutParams lp = win.getAttributes();

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        lp.dimAmount = 0.2f;

        win.setAttributes(lp);
        cdm.dg.setCanceledOnTouchOutside(true);
        cdm.dg.getWindow().setGravity(Gravity.CENTER);
        cdm.dg.getWindow().setWindowAnimations(R.style.umeng_socialize_dialog_anim_fade);
        cdm.showDialog();
    }


}
