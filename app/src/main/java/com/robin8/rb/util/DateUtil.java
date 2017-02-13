package com.robin8.rb.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Figo
 * @Description: 日期工具类
 * @date 2016-6-26
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /**
     * 将当天日期格式化，比如2016-4-6
     */
    public static String format2String() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间，如格式是：yyyy-MM-dd HH:mm:ss
     */
    public static String getFormatTime(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }


    public static void getAllYearDate(List<String> list) throws ParseException {
        String today = getNowTime("yyyy-MM-dd");
        for (int i = 1; i <= 12; i++) {
            String sDate = null;
            if (i <= 9) {
                sDate = getNowTime("yyyy-0") + i;
            } else {
                sDate = getNowTime("yyyy-") + i;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = new GregorianCalendar();
            Date date1 = sdf.parse(sDate);
            calendar.setTime(date1); //放入你的日期
            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int j = 1; j <= days; j++) {
                String sDate2 = null;
                if (j <= 9) {
                    sDate2 = sDate + "-0" + j;
                } else {
                    sDate2 = sDate + "-" + j;
                }

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Date date2 = sdf2.parse(sDate2);

                String result = null;
                if (sDate2.equals(today)) {
                    result = "今天";
                } else {
                    result = i + "月" + j + "日 " + getWeek(date2);
                }

                list.add(result);
            }
        }
    }

    //获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    //根据日期取得星期几
    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    /**
     * 发布时间的显示格式：
     * 1.距离发布评论时间 ≤60s，显示已一分钟内
     * 2.60s＜距离发布时间＜60min，显示1分钟前，2分钟前，3分钟前，n分钟前
     * 3.1h≤距离发布时间＜24h，显示1小时前，2小时前，n小时前
     * 4.距离发布时间≥24h，显示时间样式：05-10 21:28
     */
    public static String transferToDetailTime(long millionSeconds) {
        String des = "";
        long currentTimeMillis = System.currentTimeMillis();
        int time = (int) ((currentTimeMillis - millionSeconds) / 60000);
        if (time > 0) {
            if (time <= 60) {
                des = time + "分钟前";
            } else if (time < 24 * 60 && time >= 60) {
                des = time / (60) + "小时前";
            } else {
                Date d = new Date(millionSeconds);
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                des = sdf.format(d);
            }
        } else {
            des = "1分钟内";
        }
        return des;
    }


    /**
     * 发布时间的显示格式：
     * 1.1天内显示为：hh：mm
     * 2.前一天显示为：昨天 hh：mm
     * 3.前两天显示为：前天 hh：mm
     * 4.三天前显示为：三天前
     */
    public static String transferToRefreshTime(long millionSeconds) {
        String des = "";
        long currentTimeMillis = System.currentTimeMillis();
        int time = (int) ((currentTimeMillis - millionSeconds) / 60000);
        if (time >= 0 && time < 24 * 60) {
            Date d = new Date(millionSeconds);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            des = sdf.format(d);
        } else if (time >= 24 * 60 && time < 24 * 60 * 2) {
            Date d = new Date(millionSeconds);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            des = "昨天" + sdf.format(d);
        } else if (time >= 24 * 60 * 2 && time < 24 * 60 * 3) {
            Date d = new Date(millionSeconds);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            des = "前天" + sdf.format(d);
        } else {
            des = "3天前";
        }
        return des;
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

    public static String formatTime(String time) {
        if (TextUtils.isEmpty(time) || !time.contains("T") || !time.contains(".")) {
            return "";
        }
        String t = time.replace("T", " ");
        String substring = t.substring(0, t.indexOf("."));
        return substring;
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


    public static long getTimeLong(String timeUTC) {
        long time;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date date = format.parse(timeUTC);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return time;
    }

    public static String getCountdown(String timeU) {
        String timeCountdown = null;
        long millionSeconds = getTimeLong(timeU);
        long currentTimeMillis = System.currentTimeMillis();
        int time = (int) ((currentTimeMillis - millionSeconds) / 60000);

        if (time < 60) {
            timeCountdown = "1小时内";
        } else if (time < 60 * 24) {
            timeCountdown = String.valueOf(time / 60) + "小时前";
        } else if (time < 60 * 24 * 6) {
            timeCountdown = String.valueOf(time / (60 * 24)) + "天前";
        } else {
            timeCountdown = getFormatTime(millionSeconds, "yyyy-MM-dd");
        }
        return timeCountdown;
    }
}