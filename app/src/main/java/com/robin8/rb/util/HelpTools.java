package com.robin8.rb.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.robin8.rb.base.BaseApplication;
import com.robin8.rb.constants.CommonConfig;
import com.robin8.rb.constants.SPConstants;
import com.robin8.rb.model.OtherLoginListBean;
import com.robin8.rb.okhttp.HttpRequest;
import com.robin8.rb.okhttp.RequestCallback;

/**
 * sharePreference思路每个登录账号都创建一个sp 以账号名为名
 * 有一个common的sp 用于存放未登录的一些信息
 */
public class HelpTools {

    public static final String LoginNumber = "loginNumber";// 当前登录人的电话号码
    public static final String LastRequestCodeTime = "lastRequestCode,ime";// 最后一次请求验证码的时间
    public static final String Token = "token";
    public static final String CloudToken = "cloud_token";
    public static final String LoginBean = "loginBean";
    public static final String XiaoXiTiXing = "xiaoxitixing";
    public static final String FirstIn = "FirstIn";
    public static final String SecondIn = "SecondIn";
    public static final String ThirdIn = "ThirdIn";
    public static final String PagerData = "PagerData";
    public static final String InviteDialog = "InviteDialog";
    public static final String MyKolId = "MyKolId";
    public static final String IsBind = "IsBind";
    public static final String ShadowFirst = "ShadowFirst";
    public static final String ShadowMine = "ShadowMine";
    public static final String ShadowCampaign = "ShadowCampaign";
    public static final String isLeader = "isLeader";
    public static final String SERVER = "Service";
    public static final String ISNEWUSER = "isNewUser";
    public static final String REDNUM = "redNum";


    public static final String LOGININFO = "loginInfo";// 本地存储文件名字
    public static final String AppLocation = "appLocation";// 本地存储文件名字
    /**
     * 只能在commonxml中用
     */
    public static String CURRENTUSERID = "currentUserId";
    public static String COMMON = "common";
   // public static String NATIVE = "nativeuid";
    private static SharedPreferences commonSp;// 保存通用数据(与是否登录无关的信息)
    private static SharedPreferences loginSp;// 登录的数据
    private static InputMethodManager inputManager;


    /**
     * @return 检查是否有sd卡
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param partUrl 拼接地址
     * @return
     */
    public static String getUrl(String partUrl) {
        if (TextUtils.isEmpty(partUrl)) {
            return null;
        }
        StringBuilder temp = new StringBuilder();
        temp.append(CommonConfig.SERVICE).append(partUrl.trim());
        String url = temp.toString();
        temp = null;
        return url;
    }


    /**
     * @return 获取登陆数据
     */
    public static String getLoginInfo(String key) {
        if (loginSp == null) {
            String userId = HelpTools.getCommonXml(CURRENTUSERID);
            loginSp = BaseApplication.getContext().getSharedPreferences(userId, Context.MODE_PRIVATE);
        }

        return loginSp.getString(key, null);
    }

    /**
     * @return 将登录数据存在本地
     */
    public static void insertLoginInfo(String key, String value) {
        if (loginSp == null)
            loginSp = BaseApplication.getContext().getSharedPreferences(HelpTools.getCommonXml(CURRENTUSERID),
                    Context.MODE_PRIVATE);
        loginSp.edit().putString(key, value).commit();
    }

    /**
     * 获取通用数据(与登录无关的信息)
     *
     * @param key
     */
    public static String getCommonXml(String key) {
        if (commonSp == null)
            commonSp = BaseApplication.getContext().getSharedPreferences(COMMON, Context.MODE_PRIVATE);
        return commonSp.getString(key, null);
    }

    /**
     * @return 将数据存在本地
     */
    public static void insertCommonXml(String key, String value) {
        if (commonSp == null) {
            commonSp = BaseApplication.getContext().getSharedPreferences(COMMON, Context.MODE_PRIVATE);
        }
        commonSp.edit().putString(key, value).commit();
    }


    /**
     * 清理当前账号的sp
     */
    public static void clearUserInfo() {

        String loginNumber = getLoginInfo(LoginNumber);
        if (loginSp == null) {
            String userId = HelpTools.getCommonXml(CURRENTUSERID);
            loginSp = BaseApplication.getContext().getSharedPreferences(userId, Context.MODE_PRIVATE);
        }
        loginSp.edit().clear().commit();
    }


    /**
     * 退出登录
     *
     * @param activity
     */
    public static void exit(Activity activity) {
        HelpTools.clearActivity();
        HelpTools.clearInfo();
    }

    /**
     * 退出登录 或者发生异常时调用 清理本地缓存信息
     */
    public static void clearInfo() {
        if (loginSp == null) {
            loginSp = BaseApplication.getContext().getSharedPreferences(HelpTools.getCommonXml(CURRENTUSERID),
                    Context.MODE_PRIVATE);
        }
        loginSp.edit().clear().commit();
        BaseApplication.getInstance().setLoginBean(null);
//        MyDBHelper.instance=null;
        insertCommonXml(HelpTools.CURRENTUSERID, "");

    }

    /**
     * 清理myapp中的activity列表
     */
    public static void clearActivity() {
//        ArrayList<Activity> activities = BaseApplication.getInstance().getActivities();
//        for (Iterator<Activity> iterator = activities.iterator(); iterator.hasNext(); ) {
//            Activity next = iterator.next();
//            iterator.remove();
//            next.finish();
//        }
    }

    public static String getKolUUid() {
        String kolUUid = CacheUtils.getString(BaseApplication.getContext(), SPConstants.KOL_UUID, "");

        if (TextUtils.isEmpty(kolUUid)) {
            getKoluuidFromNet();
        }
        return kolUUid;
    }

    public static void getKoluuidFromNet() {
        HttpRequest.getInstance().get(true, HelpTools.getUrl(CommonConfig.START_URL), new RequestCallback() {
            @Override
            public void onError(Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    return;
                }
                OtherLoginListBean bean = GsonTools.jsonToBean(response, OtherLoginListBean.class);
                if (bean != null && bean.getError() == 0) {
                    CacheUtils.putString(BaseApplication.getContext(), SPConstants.KOL_UUID, bean.getKol_uuid());
                }
            }
        });
    }
}