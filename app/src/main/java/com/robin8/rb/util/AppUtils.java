package com.robin8.rb.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.robin8.rb.constants.SPConstants;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dalvik.system.DexFile;

public class AppUtils {

    public static final String TAG = AppUtils.class.getCanonicalName();

    /**
     数据加密的可以
     */
    private static final String ENCRYPT_KEY = "GUANGSU";

    /**
     根据Apk的路径获取包名
     @param context
     @param apkPath
     @return
     */
    public static String getApkPackageName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info == null) {
            return null;
        }

        ApplicationInfo appInfo = info.applicationInfo;
        String appName = pm.getApplicationLabel(appInfo).toString();
        String packageName = appInfo.packageName; // 得到安装包名称
        String version = info.versionName; // 得到版本信息
        // Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息

        return packageName;
    }

    /**
     获取本应用的包名
     @param context
     @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     得到当前版本号
     @param context -- 上下文
     @return 当前版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            // 根据包名获取版本信息，0表示获取版本信息。
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }

        return 0;
    }

    /**
     得到当前版本名称
     @param context -- 上下文
     @return 当前版本号
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            // 根据包名获取版本信息，0表示获取版本信息。
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }

    /**
     安装apk
     @param context
     @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");

        /**
         * Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法。如果使用
         * Activity的startActivity方法
         * ，不会有任何限制，而如果使用Context的startActivity方法的话，就需要开启一个新的task
         * ，遇到上面那个异常的，都是因为使用了Context的startActivity方法。解决办法是，加一个flag。
         * intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         */
        if (! Activity.class.isAssignableFrom(context.getClass())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);

    }

    /**
     根据包名启动应用
     @param context
     @param packageName
     */
    public static void startApk(Context context, String packageName) {
        List<ResolveInfo> matches = findActivitiesForPackage(context, packageName);
        if ((matches != null) && (matches.size() > 0)) {
            ResolveInfo resolveInfo = matches.get(0);
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            startApk(context, activityInfo.packageName, activityInfo.name);
        }
    }

    /**
     根据包名、activity名启动应用
     @param context
     @param packageName
     @param className
     */
    public static void startApk(Context context, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        ComponentName cn = new ComponentName(packageName, className);
        intent.setComponent(cn);

        if (! Activity.class.isAssignableFrom(context.getClass())) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }

    /**
     根据包名，设置应用的启动页面的Intent，然后获取这个Intent的信息。
     @param context
     @param packageName
     @return
     */
    public static List<ResolveInfo> findActivitiesForPackage(Context context, String packageName) {
        final PackageManager pm = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setPackage(packageName);
        final List<ResolveInfo> apps = pm.queryIntentActivities(mainIntent, 0);
        return apps != null ? apps : new ArrayList<ResolveInfo>();
    }

    /**
     判读apk是否已经安装过了
     @param context
     @param apkPackageName
     @return
     */
    public static boolean isInstalledApk(Context context, String apkPackageName) {

        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> appList = packageManager.getInstalledPackages(0);

        for (int i = 0; i < appList.size(); i++) {
            PackageInfo pinfo = appList.get(i);

            if (pinfo.applicationInfo.packageName.equals(apkPackageName)) {
                return true;
            }

        }

        return false;
    }

    /**
     * 获取已安装的apk列表
     *
     * @param context
     * @return
     */
    /*
     * public static HashMap<String, InstalledApkInfo>
	 * getInstalledApkMap(Context context) {
	 *
	 * HashMap<String, InstalledApkInfo> installedApkMap = new HashMap<String,
	 * InstalledApkInfo>();
	 *
	 * PackageManager packageManager = context.getPackageManager();
	 * List<PackageInfo> appList = packageManager.getInstalledPackages(0);
	 *
	 * for (int i = 0; i < appList.size(); i++) { PackageInfo pinfo =
	 * appList.get(i); if (pinfo != null) { ApplicationInfo appInfo =
	 * pinfo.applicationInfo;
	 *
	 * InstalledApkInfo installedApkInfo = new InstalledApkInfo();
	 * installedApkInfo
	 * .setAppName(packageManager.getApplicationLabel(appInfo).toString());
	 * installedApkInfo.setPackageName(appInfo.packageName);
	 * installedApkInfo.setVersionName(pinfo.versionName);
	 * installedApkInfo.setVersionCode(pinfo.versionCode);
	 *
	 * // LogUtil.log(TAG, installedApkInfo.toString());
	 *
	 * installedApkMap.put(appInfo.packageName, installedApkInfo); }
	 *
	 * }
	 *
	 * return installedApkMap; }
	 */

    /**
     遍历本apk下指定的包，返回遍历到的类名列表
     @param context
     @param packageName
     @return 类名列表
     */
    public static List<String> traversePackage(Context context, String packageName) {
        List<String> classNameList = new ArrayList<String>();

        try {
            String path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            DexFile dexfile = new DexFile(path);

            Enumeration<String> entries = dexfile.entries();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.indexOf(packageName) >= 0) {
                    classNameList.add(name);
                }
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classNameList;
    }

    /**
     一次震动
     @param context
     @param milliseconds 震动多长时间，单位：毫秒。
     @return
     */
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     重复震动
     @param context
     @param pattern 自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长]时长的单位是毫秒
     @param isRepeat 是否反复震动
     @return
     */
    public static void vibrate(final Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : - 1);
    }

    /**
     获取mac地址
     @param context
     @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }

    /**
     获取AndroidID
     @param context
     @return
     */
    public static String getAndroidID(Context context) {
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        return androidId;
    }

    /**
     判断指定的类名是否是当前顶部Activity。
     @return 是，返回ture；否，返回false。
     */
    public static boolean isTopActivity(Context context, String classCanonicalName) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
        String className = cn.getClassName();

        return className.equals(classCanonicalName);
    }

    /**
     用来判断服务是否后台运行
     @param context
     @param className 判断的服务名字
     @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean IsRunning = false;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (! (serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                IsRunning = true;
                break;
            }
        }
        return IsRunning;
    }

    /**
     MD5加密，32位
     @param str
     @return
     */
    public static String toMD5(String str) {
        if (str == null || str.equals("")) {
            return null;
        }

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return null;
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     MD5加密，16位
     @param str
     @return
     */
    public static String to16MD5(String str) {
        String md5Str = toMD5(str);
        if (md5Str == null) {
            return null;
        }

        return md5Str.substring(8, 24);
    }

    /**
     获取状态栏高度
     @param context
     @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            statusHeight = context.getResources().getDimensionPixelSize(i5);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return statusHeight;
    }

    /**
     判断网络是否可用
     @param context
     @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     判断wifi是否可用
     @param context
     @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     判断3G/4G是否可用
     @param context
     @return
     */
    public static boolean is3G4GConnected(Context context) {
        boolean is3G4G = false;
        if (context != null) {

            NetState netCode = isConnected(context);
            switch (netCode) {
                case NET_NO:
                    // 没有网络连接
                    break;
                case NET_2G:
                    // 2g网络
                    break;
                case NET_3G:
                    // 3g网络
                    is3G4G = true;
                    break;
                case NET_4G:
                    // 4g网络
                    is3G4G = true;
                    break;
                case NET_WIFI:
                    // WIFI网络
                    break;
                case NET_UNKNOWN:
                    // 未知网络
                    break;
                default:
                    // 不知道什么情况~>_<~
                    break;
            }
        }
        return is3G4G;
    }

    /**
     获取Imei
     @param context
     @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();

    }

    public static String getDeviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            return null;
        } else {
            return deviceId;
        }
    }

    /**
     * 判断当前是否网络连接
     *
     * @param context
     * @return 状态码
     */
    /**
     枚举网络状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
     NET_UNKNOWN：未知网络
     */
    public static enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
    }

    ;

    public static NetState isConnected(Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (ni != null && ni.isAvailable()) {
            switch (ni.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    stateCode = NetState.NET_WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (ni.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            stateCode = NetState.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            stateCode = NetState.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            stateCode = NetState.NET_4G;
                            break;
                        default:
                            stateCode = NetState.NET_UNKNOWN;
                    }
                    break;
                default:
                    stateCode = NetState.NET_UNKNOWN;
            }

        }
        return stateCode;
    }

    /**
     判断Mobile网络是否可用
     @param context
     @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     获取当前连接的网络类型
     @param context
     @return
     */
    public static int getConnectedNetType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return - 1;
    }

    /**
     获取手机型号
     @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

    /**
     获取当前系统名
     @return
     */
    public static String getSystemName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     获取系统版本
     @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     获取渠道名称
     @param context
     @return
     */
    public static String getChannelName(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            String channelNumber = appInfo.metaData.getString("InstallChannel");
            return channelNumber;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     获取手机号码
     @param context
     @return
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    /**
     测量能够画在指定宽度内字符串的字体大小
     @param text 字符串
     @param width 指定的宽度
     @param maxTextSize 最大的字体大小
     @return
     */

    public static float measureTextSize(String text, int width, float maxTextSize) {
        if (StringUtil.isEmpty(text)) {
            return maxTextSize;
        }

        float textSize = maxTextSize;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(text);

        while (textWidth > width) {
            textSize--;
            paint.setTextSize(textSize);
            textWidth = paint.measureText(text);
        }

        return textSize;
    }

    /**
     对字节数据进行加密：加密算法是异或，所以加密和解密是一样的。
     @param buff
     @return
     */
    public static byte[] decryptOrEncryptByteData(byte[] buff) {
        if (buff == null) {
            return buff;
        }

        byte[] encryptKey = ENCRYPT_KEY.getBytes();
        int keyIndex = 0;
        for (int i = 0; i < buff.length; i++) {
            buff[i] = (byte) (buff[i] ^ encryptKey[keyIndex]);
            keyIndex++;
            if (keyIndex >= encryptKey.length) {
                keyIndex = 0;
            }
        }

        return buff;
    }

    /**
     正则表达式 判断是否是手机号
     @param mobiles
     @return
     */
    public static boolean isMobileNO(String mobiles) {
        // Pattern p =
        // Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Pattern p = Pattern.compile("^[1][3-5|7-8]\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     正则表达式 判断是否是email
     @param email
     @return
     */
    public static boolean isEmail(String email) {
        String stre = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(stre);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     正则表达式判断是否为汉字
     @param text
     @return
     */
    public static boolean isChineseCharacters(String text) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(text);
        return m.matches();

    }

    /**
     正则表达式判断是否为数字
     */
    public final static String REG_DIGIT = "[0-9]*";

    public static boolean isNumDigit(String str) {
        return str.matches(REG_DIGIT);
    }

    public final static String REG_CHAR = "[a-zA-Z]*";

    public static boolean isChar(String str) {
        return str.matches(REG_CHAR);
    }

    /**
     2.判断一个字符串的首字符是否为字母
     @param s
     @return
     */
    public static boolean startIsChar(String s) {
        char c = s.charAt(0);
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static String getImei(Context context) {
        context = context.getApplicationContext();
        String ime = CacheUtils.getString(context, SPConstants.ROBIN_IME, null);
        if (TextUtils.isEmpty(ime)) {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            ime = calculateIme(TelephonyMgr.getDeviceId());
            CacheUtils.putString(context, SPConstants.ROBIN_IME, ime);
        }
        return ime;
    }

    public static String calculateIme(String ime) {
        if (! isfit(ime)) {
            StringBuffer sb = new StringBuffer();
            sb.append(System.currentTimeMillis()).append((int) (Math.random() * 900) + 100);
            ime = sb.toString();
            return ime;
        }
        return ime;
    }

    public static boolean isfit(String ime) {
        if (TextUtils.isEmpty(ime) || ime.length() < 3) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < ime.length() - 1; i++) {
            if (ime.charAt(i) == ime.charAt(i + 1)) {
                count++;
            }
        }
        if (count == ime.length() - 1) {
            return false;
        }
        return true;
    }

    /**
     获取androidmefest下application节点下的mete-data数据
     @param context
     @param meteName
     @return
     */
    public static String getApplicationMeteData(Context context, String meteName) {
        String msg = "";
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (info == null || info.metaData == null) {
                return msg;
            }
            msg = info.metaData.getString(meteName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     判断应用是否已经启动
     @param context 一个context
     @param packageName 要判断应用的包名
     @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }


    /**
     获取已安装应用商店的包名列表
     @param context
     @return
     */
    public static ArrayList<String> queryInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0)
            return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (! TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);

        }
        return pkgs;
    }

    /**
     过滤出已经安装的包名集合
     @param context
     @param pkgs 待过滤包名集合
     @return 已安装的包名集合
     */
    public static ArrayList<String> filterInstalledPkgs(Context context, ArrayList<String> pkgs) {
        ArrayList<String> empty = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0)
            return empty;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                try {
                    installPkg = installedPkgs.get(i).applicationInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    empty.add(installPkg);
                    break;
                }

            }
        }
        return empty;
    }

    /**
     启动到app详情界面
     @param appPkg App的包名
     @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg))
                return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (! TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将汉字转换为全拼
     *
     * @param src
     * @return
     */
    public static String getPinYin(String src) {
        char[] hz = null;
        hz = src.toCharArray();//该方法的作用是返回一个字符数组，该字符数组中存放了当前字符串中的所有字符
        String[] py = new String[hz.length];//该数组用来存储
        //设置汉子拼音输出的格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        String pys = ""; //存放拼音字符串
        int len = hz.length;

        try {
            for (int i = 0; i < len; i++) {
                //先判断是否为汉字字符
                if (Character.toString(hz[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    //将汉字的几种全拼都存到py数组中
                    py = PinyinHelper.toHanyuPinyinStringArray(hz[i], format);
                    //取出改汉字全拼的第一种读音，并存放到字符串pys后
                    pys += py[0];
                } else {
                    //如果不是汉字字符，间接取出字符并连接到 pys 后
                    pys += Character.toString(hz[i]);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pys;
    }

}
