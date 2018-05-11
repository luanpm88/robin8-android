package com.robin8.rb.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 @author Figo
 @Description: 日期工具类
 @date 2016-6-26 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /**
     将当天日期格式化，比如2016-4-6
     */
    public static String format2String() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     获取当前时间，如格式是：yyyy-MM-dd HH:mm:ss
     */
    public static String getFormatTime(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }

    /**
     获取到当前时间的毫秒值
     @param format
     @return
     */
    public static String getNowTimeMs(String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String format2 = format1.format(new Date());
        long nowTimes = 0;
        try {
            nowTimes = format1.parse(format2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(nowTimes);
    }
    /**
     获取到当前时间的秒值
     @param format
     @return
     */
    public static String getNowTimeM(String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String format2 = format1.format(new Date());
        long nowTimes = 0;
        try {
            nowTimes = format1.parse(format2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(nowTimes/1000);
    }

    /**
     日期转换成秒数
     */
    public static long getSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (long) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
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
     发布时间的显示格式：
     1.距离发布评论时间 ≤60s，显示已一分钟内
     2.60s＜距离发布时间＜60min，显示1分钟前，2分钟前，3分钟前，n分钟前
     3.1h≤距离发布时间＜24h，显示1小时前，2小时前，n小时前
     4.距离发布时间≥24h，显示时间样式：05-10 21:28
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
     发布时间的显示格式：
     1.1天内显示为：hh：mm
     2.前一天显示为：昨天 hh：mm
     3.前两天显示为：前天 hh：mm
     4.三天前显示为：三天前
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
     解析时间 默认输出格式yyyy.MM.dd HH:mm:dd
     想要自定义输出格式
     @param pattern
     @param time
     @return
     @see #formatTime(String, String, String)
     */
    public static String formatTime(String pattern, String time) {
        return formatTime(pattern, time, "yyyy.MM.dd HH:mm:dd");
    }

    public static String formatTime(String time) {
        if (TextUtils.isEmpty(time) || ! time.contains("T") || ! time.contains(".")) {
            return "";
        }
        String t = time.replace("T", " ");
        String substring = t.substring(0, t.indexOf("."));
        return substring;
    }

    /**
     格式化时间
     @param pattern 解析的格式
     @param time string类型的时间
     @param format 输出的格式
     @return
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
        if (time < 1) {
            timeCountdown = "刚刚";
        } else if (time < 60) {
            timeCountdown = time + "分钟前";
        } else if (time < 60 * 24) {
            timeCountdown = String.valueOf(time / 60) + "小时前";
        } else if (time < 60 * 24 * 6) {
            timeCountdown = String.valueOf(time / (60 * 24)) + "天前";
        } else {
            timeCountdown = getFormatTime(millionSeconds, "yyyy-MM-dd");
        }
        return timeCountdown;
    }

    public static long getTimeLongMore(String tem, String timeUTC) {
        long time;
        SimpleDateFormat format = new SimpleDateFormat(tem);
        try {
            Date date = format.parse(timeUTC);
            time = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return time;
    }

    public static String getCountdownMore(String tem, String timeU) {
        String timeCountdown = null;
        long millionSeconds = getTimeLongMore(tem, timeU);
        long currentTimeMillis = System.currentTimeMillis();
        int time = (int) ((currentTimeMillis - millionSeconds) / 60000);
        if (time < 1) {
            timeCountdown = "刚刚";
        } else if (time < 60) {
            timeCountdown = time + "分钟前";
        } else if (time < 60 * 24) {
            timeCountdown = String.valueOf(time / 60) + "小时前";
        } else if (time < 60 * 24 * 6) {
            timeCountdown = String.valueOf(time / (60 * 24)) + "天前";
        } else {
            timeCountdown = getFormatTime(millionSeconds, "yyyy-MM-dd");
        }
        return timeCountdown;
    }

    /**
     根据传入的年份和月份，判断上一个有多少天
     @param year
     @param month
     @return
     */
    public static int getLastDaysOfMonth(int year, int month) {
        int lastDaysOfMonth = 0;
        if (month == 1) {
            lastDaysOfMonth = getDaysOfMonth(year - 1, 12);
        } else {
            lastDaysOfMonth = getDaysOfMonth(year, month - 1);
        }
        return lastDaysOfMonth;
    }

    /**
     根据传入的年份和月份，判断当前月有多少天
     @param year
     @param month
     @return
     */
    public static int getDaysOfMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (isLeap(year)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return - 1;
    }

    /**
     判断是否为闰年
     @param year
     @return
     */
    public static boolean isLeap(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }

    /**
     根据传入的年份和月份，获取当前月份的日历分布
     @param year
     @param month
     @return
     */
    public static int[][] getDayOfMonthFormat(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);//设置时间为每月的第一天
        //设置日历格式数组,6行7列
        int days[][] = new int[6][7];
        //设置该月的第一天是周几
        int daysOfFirstWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //设置本月有多少天
        int daysOfMonth = getDaysOfMonth(year, month);
        //设置上个月有多少天
        int daysOfLastMonth = getLastDaysOfMonth(year, month);
        int dayNum = 1;
        int nextDayNum = 1;
        //将日期格式填充数组
        for (int i = 0; i < days.length; i++) {
            for (int j = 0; j < days[i].length; j++) {
                if (i == 0 && j < daysOfFirstWeek - 1) {
                    days[i][j] = daysOfLastMonth - daysOfFirstWeek + 2 + j;
                } else if (dayNum <= daysOfMonth) {
                    days[i][j] = dayNum++;
                } else {
                    days[i][j] = nextDayNum++;
                }
            }
        }
        return days;
    }

    public static List<Integer> getLastDaysNow(int year, int month) {
        List<Integer> list = new ArrayList<>();
        int[] days = new int[42];
        int[][] ints = getDayOfMonthFormat(year, month);
        int dayNum = 0;

        //将二维数组转化为一维数组，方便使用
        for (int i = 0; i < ints.length; i++) {
            for (int j = 0; j < ints[i].length; j++) {
                days[dayNum] = ints[i][j];
                dayNum++;
            }
        }
        for (int i = 0; i < 7; i++) {
            if (days[i] > 20) {
                list.add(days[i]);
            }
        }
        return list;
    }

    /**
     获取当前年份
     @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     获取当前月份
     @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     返回文字描述的日期
     @param timeUTC
     @return
     */
    public static String getTimeFormatText(String timeUTC) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            date = format.parse(timeUTC);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

}
