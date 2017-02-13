package com.robin8.rb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestParams;
import com.robin8.rb.pager.CreatePager;
import com.robin8.rb.pager.FirstPager;
import com.robin8.rb.pager.MinePager;
import com.robin8.rb.pager.RewordPager;
import com.robin8.rb.presenter.BasePresenter;
import com.robin8.rb.task.LocationService;
import com.robin8.rb.update.UpdateNewApk;
import com.robin8.rb.util.AppUtils;
import com.robin8.rb.util.CacheUtils;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.NetworkUtil;
import com.robin8.rb.util.StringUtil;

import java.util.ArrayList;

public class MainActivity extends BaseBackHomeActivity implements View.OnClickListener {
    private static final int KOL_LIST = 0;
    private static final int CAMPAIGN_LIST = 1;
    private static final int CREATE_LIST = 2;
    private static final int MY = 3;
    private ArrayList<BasePager> mPagerList;
    private RewordPager mRewordPager;
    private FirstPager mFirstPager;
    private CreatePager mCreatePager;
    private MinePager mMinePager;
    private MyPagerAdapter mPagerAdapter;
    private ViewPager mVPContentPager;
    private RadioGroup mRGContentBottom;
    private RadioButton mRBBottomFirst;
    private RadioButton mRBBottomCampaign;
    private RadioButton mRBBottomCreate;
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

    private final static double UNKNOW = -2000;
    private double latitude = UNKNOW;
    private double longitude = UNKNOW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNotify();
        mPageName = StatisticsAgency.KOL_LIST;
        setContentView(R.layout.activity_main);
        setSwipeBackEnable(false);
        checkNewVersion();
        postData();
        startLocate();
        initView();
        initData();
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
            LogUtil.logXXfigo("UNKNOW longitude="+longitude+"latitude="+latitude);
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
        PushManager.getInstance().initialize(this.getApplicationContext());
        boolean notifyB = CacheUtils.getBoolean(this, SPConstants.NOTIFY_TOGGLE, true);
        if (notifyB) {
            PushManager.getInstance().turnOnPush(this);//打开个推开关
        } else {
            PushManager.getInstance().turnOffPush(this);
        }
    }

    /**
     * 初始化界面
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        mVPContentPager = (ViewPager) findViewById(R.id.vp_content_pager);
        mRGContentBottom = (RadioGroup) findViewById(R.id.rg_content_bottom);
        mRBBottomFirst = (RadioButton) findViewById(R.id.rb_bottom_first);
        mRBBottomCampaign = (RadioButton) findViewById(R.id.rb_bottom_campaign);
        mRBBottomCreate = (RadioButton) findViewById(R.id.rb_bottom_create);
        mRBBottomMine = (RadioButton) findViewById(R.id.rb_bottom_mine);
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
     * 初始化参数
     */
    public void initData() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null && fm.getFragments() != null) {
            fm.getFragments().clear();
        }

        if (mPagerList == null) {
            mPagerList = new ArrayList<BasePager>();
        }
        if (mRewordPager == null) {
            mRewordPager = new RewordPager(this);
        }

        if (mFirstPager == null) {
            mFirstPager = new FirstPager(this);
        }

        if (mCreatePager == null) {
            mCreatePager = new CreatePager(this);
        }

        if (mMinePager == null) {
            mMinePager = new MinePager(this);
        }

        mPagerList.clear();
        mPagerList.add(mFirstPager);
        mPagerList.add(mRewordPager);
        mPagerList.add(mCreatePager);
        mPagerList.add(mMinePager);

        if (mPagerAdapter == null) {
            mPagerAdapter = new MyPagerAdapter();
        }
        mVPContentPager.setAdapter(mPagerAdapter);
        mVPContentPager.setOffscreenPageLimit(4);
        // 设置默认显示的界面 默认显示首页
        mRGContentBottom.check(R.id.rb_bottom_first);
        // 让首页界面加载数据
        mPagerList.get(0).initData();
        // 监听ViewPager的页签的变化
        mVPContentPager.setOnPageChangeListener(new MyOnPageChangeListener());
        // 监听底部 页签单选框
        mRGContentBottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        mRBBottomFirst.setOnClickListener(this);
        mRBBottomCampaign.setOnClickListener(this);
        mRBBottomMine.setOnClickListener(this);
        mRBBottomCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * 当rodioButton 切换时回调 点击RadioButton 切换到相应的Pager界面
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.rb_bottom_first:
                    mVPContentPager.setCurrentItem(0, false);
                    break;
                case R.id.rb_bottom_campaign:
                    mVPContentPager.setCurrentItem(1, false);
                    break;
                case R.id.rb_bottom_create:
                    mVPContentPager.setCurrentItem(2, false);
                    break;
                case R.id.rb_bottom_mine:
                    mVPContentPager.setCurrentItem(3, false);
                    break;
            }
        }
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
         * 当某一个界面被旋转 回调
         */
        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case KOL_LIST:
                    mPageName = StatisticsAgency.KOL_LIST;
                    onePageSelected(KOL_LIST);
                    mRBBottomFirst.setChecked(true);
                    break;
                case CAMPAIGN_LIST:
                    mPageName = StatisticsAgency.CAMPAIGN_LIST;
                    onePageSelected(CAMPAIGN_LIST);
                    mRBBottomCampaign.setChecked(true);
                    break;
                case CREATE_LIST:
                    mPageName = StatisticsAgency.CREATE_LIST;
                    onePageSelected(CREATE_LIST);
                    mRBBottomCreate.setChecked(true);
                    break;

                case MY:
                    mPageName = StatisticsAgency.MY;
                    onePageSelected(MY);
                    mRBBottomMine.setChecked(true);
                    mPagerList.get(MY).initData();
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

        if (mPageName == StatisticsAgency.CAMPAIGN_LIST) {
            BasePager basePager = mPagerList.get(CAMPAIGN_LIST);
            if (basePager instanceof RewordPager) {
                RewordPager pager = (RewordPager) basePager;
                pager.initVpData();
                pager.setVpAuto();
            }
        }

        if (position == mLastPosition) {
            return;
        }
        if (mLastPosition == KOL_LIST) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.KOL_LIST);
        } else if (mLastPosition == MY) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.MY);
        } else if (mLastPosition == CAMPAIGN_LIST) {
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.CAMPAIGN_LIST);
        }else if(mLastPosition == CREATE_LIST){
            StatisticsAgency.onPageEnd(MainActivity.this, StatisticsAgency.CREATE_LIST);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPageName == StatisticsAgency.MY) {
            mPagerList.get(MY).initData();
        }
    }

    private boolean mHasPostLocation;
    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError && !mHasPostLocation) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                mHasPostLocation = true;
                postData();
            }
        }

    };
}
