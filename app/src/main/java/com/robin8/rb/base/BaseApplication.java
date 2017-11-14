package com.robin8.rb.base;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.helper.StatisticsAgency;
import com.robin8.rb.model.ContactBean;
import com.robin8.rb.model.LoginBean;
import com.robin8.rb.task.LocationService;
import com.robin8.rb.util.CrashHandler;
import com.robin8.rb.util.GsonTools;
import com.robin8.rb.util.HelpTools;
import com.robin8.rb.util.LogUtil;
import com.robin8.rb.util.StringUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.TextCodec;

public class BaseApplication extends MultiDexApplication {

    private static String SALT = "Robin888";
    private static String ALGORITHM = "HS256";
    private static String JWT = "JWT";
    private static BaseApplication mInstance = null;
    private static long lastTime;
    private static Handler mHandler;// Handler对象
    private static Context mContext;// Context对象
    private static Thread mMainThread;// Thread对象
    private static int mMainThreadIdI;// 主线程Id
    public static float mPixelDensityF;// 像素密度
    private LoginBean mLoginBean;
    private List<ContactBean> mContactBeanList;
    public static LocationService locationService;
    public static Vibrator mVibrator;

    @Override
    public void onCreate() {

        super.onCreate();

        // 监控程序崩溃的异常
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());

        // Handler对象
        mHandler = new Handler();
//        if (mHandler == null) {
//            mHandler = new Handler();
//        }
        // Context
        mContext = getApplicationContext();
        // 主线程id,获取当前方法运行线程id,此方法运行在主线程中,�?��获取的是主线程id
        mMainThreadIdI = android.os.Process.myTid();
        // 主线程对�?
        mMainThread = Thread.currentThread();

        initConfig();
        initData();
    }

    private void initConfig() {
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        //        SDKInitializer.initialize(getApplicationContext());
        //埋点初始化
        StatisticsAgency.init(this);
        ShareSDK.initSDK(this);
    }

    private void initData() {
        mPixelDensityF = getResources().getDisplayMetrics().density;
    }

    public static Handler getHandler() {
        return mHandler;
    }
//    public static void sendMessage(Message msg) {
//        mHandler.sendMessage(msg);
//    }
    public static Context getContext() {

        return mContext;
    }

    public static int getMainThreadId() {

        return mMainThreadIdI;
    }

    public static Thread getMainThread() {

        return mMainThread;
    }

    public static BaseApplication getInstance() {

        if (mInstance == null) {
            mInstance = new BaseApplication();
        }
        return mInstance;
    }

    public LoginBean getLoginBean() {

        if (mLoginBean == null) {
            mLoginBean = GsonTools.jsonToBean(HelpTools.getLoginInfo(HelpTools.LoginBean), LoginBean.class);
        }
        return mLoginBean;
    }

    public boolean hasLogined() {

        LoginBean loginBean = getLoginBean();
        if (loginBean == null || loginBean.getKol() == null || CommonConfig.TOURIST_PHONE.equals(loginBean.getKol().getMobile_number())) {
            return false;
        }

        LoginBean.KolEntity kol = loginBean.getKol();
        if (TextUtils.isEmpty(kol.getIssue_token()) || TextUtils.isEmpty(kol.getMobile_number())) {
            return false;
        }
        LogUtil.logXXfigo("mobile number:" + loginBean.getKol().getMobile_number());
        return true;
    }

    public void setLoginBean(LoginBean loginBean) {

        mLoginBean = loginBean;
        HelpTools.insertLoginInfo(HelpTools.LoginBean, GsonTools.beanToJson(loginBean));
    }

    public List<ContactBean> getContactBeans() {

        return mContactBeanList;
    }

    public void setContactBeans(List<ContactBean> contactBeanList) {

        this.mContactBeanList = contactBeanList;
    }

    /**
     * 获取加密的header
     *
     * @return
     */
    public static String getHeader() {

        Calendar cal = Calendar.getInstance();
        //取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        //取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        //从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, - (zoneOffset + dstOffset));
        long timeInMillis = cal.getTimeInMillis();
        String myToken = HelpTools.getLoginInfo(HelpTools.Token);
        TokenBean tokenBean = new TokenBean(StringUtil.checkString(myToken), ((int) (timeInMillis / 1000)));
       // LogUtil.LogShitou("请求头问题token", "===>" + myToken);
       // LogUtil.LogShitou("请求头问题时间", "===>" + ((int) (timeInMillis / 1000)));
        String s1 = GsonTools.beanToJson(tokenBean);
       // LogUtil.LogShitou("请求头问题s1", "===>" + s1);

        HashMap<String, Object> headMap = new HashMap<>();
        headMap.put("typ", JWT);
        headMap.put("alg", ALGORITHM);

        String compact = Jwts.builder().setHeader(headMap).setPayload(s1).signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(SALT)).compact();

        return compact;
    }

    public static boolean isDoubleClick() {

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastTime < 800) {
            return true;
        }
        lastTime = currentTimeMillis;
        return false;
    }

    /**
     * 加密的bean 用于gson转化为json格式字符串
     */
    public static class TokenBean {

        String get_code;
        String is_new;
        String private_token;
        long time;

        public TokenBean(String private_token, long time) {

            this.private_token = private_token;
            this.time = time;
            this.is_new = "is_new";
            this.get_code = "get_code";
        }
    }

    /**
     * 解密
     *
     * @param token
     * @return
     */
    public static String decodeToken(String token) {

        if (TextUtils.isEmpty(token)) {
            return "";
        }
        Jwt parse = Jwts.parser().setSigningKey(TextCodec.BASE64.encode(SALT)).parse(token);
        DefaultClaims body = (DefaultClaims) parse.getBody();
        Object private_token = body.get("private_token");
        if (private_token == null) {
            return "";
        }
        return private_token.toString();
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private Bitmap screenShot;//摇一摇的截图

    public Bitmap getScreenShot() {

        return screenShot;
    }

    public void setScreenShot(Bitmap screenShot) {

        if (this.screenShot != null)
            this.screenShot.recycle();
        this.screenShot = screenShot;
    }
}
