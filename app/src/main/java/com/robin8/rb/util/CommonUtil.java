package com.robin8.rb.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.robin8.rb.base.BaseApplication;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author DLJ
 * @Description 一些公共工具类
 * @date 2016/1/25 11:36
 */
public class CommonUtil {

    private static String msg;

    /**
     * 版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    /**
     * 版本号
     */

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 获取Imei
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        return tm.getDeviceId();

    }


    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 检查是否联网
     */
    public static Boolean checkHaveNet() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) BaseApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }

        return false;
    }

    /**
     * 获取时间格式化的时间字符串
     *
     * @return
     */
    public static String getFormatTimeString(String pattern, String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date oldDate = null;
        try {
            oldDate = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = "";
        Date currentDate = new Date();
        GregorianCalendar ca = new GregorianCalendar();
        if (oldDate == null) {
            SimpleDateFormat fp = new SimpleDateFormat("HH:mm");
            time = (ca.get(GregorianCalendar.AM_PM) == 0 ? "上午" : "下午");
            return time + fp.format(currentDate);
        }
        long hasTimeForToday = (currentDate.getHours() * 60 * 60 + currentDate.getMinutes() * 60
                + currentDate.getSeconds() + 1) * 1000;
        long oldTime = oldDate.getTime();
        long newTime = currentDate.getTime();
        long someDay = 24 * 60 * 60 * 1000 * 7;//超过当月就提示几个月前
        long between = newTime - oldTime;
        if (between < 60 * 1000) {//1分钟内
            return "刚刚";
        } else if (between < 60 * 60 * 1000) {//1小时内显示 **分钟前
            long minutes = between / (1000 * 60);
            return minutes + "分钟前";
        } else if (between < 24 * 60 * 60 * 1000) {
            long hours = between / (1000 * 60 * 60);
            return hours + "小时前";
        } else if (between < 7 * 24 * 60 * 60 * 1000) {//1小时内显示 **分钟前
            long day = between / (1000 * 60 * 60 * 24);
            if (day == 1)
                return "昨天";
            else if (day == 2)
                return "前天";
            return day + "天前";
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format(oldDate);

        }

    }

    /**
     * 格式化时间
     *
     * @param pattern 解析的格式
     * @param time    string类型的时间
     * @param format  输出的格式
     * @return
     */
    public static String formatTime(String pattern, String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        SimpleDateFormat sdf1 = new SimpleDateFormat(format);
        try {
            Date parse = sdf.parse(time);
            String formatString = sdf1.format(parse);
            return formatString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解析时间 默认输出格式yyyy.MM.dd HH:mm:dd
     * 想要自定义输出格式
     *
     * @param pattern
     * @param time
     * @return
     * @see #formatTime(String, String, String)
     */
    public static String formatTime(String pattern, String time) {
        return formatTime(pattern, time, "yyyy.MM.dd HH:mm:dd");
    }

    /**
     * 检查名字长度
     *
     * @param textview
     * @return
     */
    public static boolean checkNameLength(TextView textview) {
        try {
            int length = textview.getText().toString().length();
            if (length > 10) {
                CustomToast.showShort(BaseApplication.getContext(), "名字长度不能超过10个字符");
                return false;
            }
            if (length == 0) {
                CustomToast.showShort(BaseApplication.getContext(), "宝贝名称不能为空");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 返回集合大小 当为空时返回0
     *
     * @param list
     * @return
     */
    public static int getListSize(List list) {
        if (list != null)
            return list.size();
        return 0;
    }


    /**
     * 动态设置ListView的高度
     *
     * @param listView
     */
    public static int messureHeightForListview(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount();
             i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1)) +
                100;
        listView.setLayoutParams(params);
        listView.requestLayout();
        return params.height;
    }

    /**
     * 动态设置ListView的高度 适用于规则的item
     *
     * @param listView
     */
    public static int messureHeightForRegularListview(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        int count = mAdapter.getCount();
        if (count <= 0)
            return 0;
        View view = mAdapter.getView(0, null, listView);
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        totalHeight += view.getMeasuredHeight() * mAdapter.getCount();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1)) +
                100;
        listView.setLayoutParams(params);
        listView.requestLayout();
        return params.height;
    }

    /**
     * 返回分秒
     *
     * @param s
     * @return
     */
    public static String getTimeString(long s) {
        String time = "";
        long hour = s / 3600;
        //小时
        long minute = s % 3600 / 60;
        //分钟
        long second = s % 60;
        //秒
        if (hour >= 1) {
        } else if (hour < 1) {
            time = minute + "分" + second + "秒";
        } else if (minute < 1) {
            time = second + "秒";
        }

        return time;
    }

    /**
     * 通过view获取截图
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        return bitmap;
    }


    /***
     * MD5加码 生成32位md5码
     */
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * 判断app现在是否可以debug
     *
     * @param context
     * @return
     */
    public static boolean isDebug(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    /**
     * 如果string为空返回""
     *
     * @param s
     * @return
     */
    public static String checkString(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        else
            return s;
    }

    public static void messureView(View view) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
    }

    /**
     * 获取androidmefest下application节点下的mete-data数据
     * @param context
     * @param meteName
     * @return
     */
    public static String getApplicationMeteData(Context context, String meteName) {
        ApplicationInfo info = null;
        try {
            info = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            msg = info.metaData.getString(meteName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 根据uri获取图片的绝对路径
     * @param activity
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity activity
            , Uri uri) {
        String path = "";
        try {
            // can post image
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri,
                    proj,                 // Which columns to return
                    null,       // WHERE clause; which rows to return (all rows)
                    null,       // WHERE clause selection arguments (none)
                    null);                 // Order-by clause (ascending by name)

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            path = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
            path = uri.getPath();
        }
        return path;
    }

}
