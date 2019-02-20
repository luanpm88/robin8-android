package com.robin8.rb.util;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.igexin.sdk.PushManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class StringUtil {

    private static final String TAG = StringUtil.class.getCanonicalName();

    /**
     字符串转整数
     @param obj
     @return 转换异常返回 0
     */
    public static int toInt(String obj) {

        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    /**
     字符串转长整数
     @param obj
     @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     字符串转布尔值
     @param b
     @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     字节转为字符串
     @param date
     @param charsetName 指定字符集
     @return
     */
    public static String byteToString(byte[] date, String charsetName) {

        if (date == null) {
            return null;
        }

        if (charsetName == null) {
            return new String(date);
        } else {
            try {
                return new String(date, charsetName);
            } catch (UnsupportedEncodingException e) {
                LogUtil.LogShitou(TAG, e.toString());
                return new String(date);
            }
        }
    }

    /**
     判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串。 若输入字符串为null或空白串，返回true。
     @param input
     @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || input.length() <= 0) {
            return true;
        }
        if (TextUtils.isEmpty(input)) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }

        return true;
    }

    /**
     @param sourceStr 被插入的字符串
     @param position 插入的位置
     @param insertStr 要插入的字符串
     @return 插入后的字符串
     @Description 在字符串的指位置插入一个字符串
     */
    public static String insertString(String sourceStr, int position, String insertStr) {
        int i = position - 1;
        String newString = sourceStr.substring(0, i) + insertStr + sourceStr.substring(i, sourceStr.length());

        return newString;
    }

    /**
     @param sourceStr 被插入的字符串
     @param endString 指定的字符串
     @return 如果字符串中没有指定的字符串，那么返回原字符串，如果有，就返回截取指定字符串之前的子字符串。
     @Description 从字符串中截取指定字符串之前的子字符串
     */
    public static String subStringEndString(String sourceStr, String endString) {

        int position = sourceStr.indexOf(endString);
        if (position == - 1) {
            return sourceStr;
        }

        String newString = sourceStr.substring(0, position);
        return newString;
    }

    /**
     * 截取子串加上省略号，以适应指定的宽度。
     *
     * @param context
     * @param text
     * @param width
     * @param paint
     * @return
     */
    /*
     * public static String subStringAddApostrophe(Context context, String text,
	 * int width, Paint paint) { int textWidth = (int) paint.measureText(text);
	 * 
	 * if (textWidth <= width) { return text; }
	 * 
	 * String newString = null; int newStringWidth; String apostrophe =
	 * context.getString(R.string.apostrophe); int apostropheWidth = (int)
	 * paint.measureText(apostrophe); int subStringWidth = width -
	 * apostropheWidth;
	 * 
	 * int end = text.length() / 2; int low = 1; int high = text.length(); while
	 * (end > 0) { newString = text.substring(0, end); newStringWidth = (int)
	 * paint.measureText(newString);
	 * 
	 * if (newStringWidth > subStringWidth) { high = end; } else if
	 * (newStringWidth < subStringWidth) { low = end; }
	 * 
	 * if (high <= low + 1) { break; } else { end = (high - low) / 2 + low; } }
	 * 
	 * newString = text.substring(0, low); newString += apostrophe;
	 * newStringWidth = (int) paint.measureText(newString); if (newStringWidth
	 * <= width) { return newString; }
	 * 
	 * newString = text.substring(0, low); return newString; }
	 */

    /**
     * 获取一个中文字符
     *
     * @return
     */
    /*
	 * public static String getOneChineseChar(Context context) { if (context ==
	 * null) { return null; }
	 * 
	 * return context.getString(R.string.chineseChar); }
	 */

    /**
     * 获取指定字符数的中文字符串的宽度
     *
     * @param context
     * @param paint
     *            画笔
     * @param num
     *            字符数
     * @return
     */
	/*
	 * public static int getChineseCharWidth(Context context, Paint paint, int
	 * num) { String oneCnChar = getOneChineseChar(context); if (oneCnChar ==
	 * null || num <= 0) { return 0; }
	 * 
	 * String str = null; for (int i = 1; i <= num; i++) { str += oneCnChar; }
	 * 
	 * return (int) paint.measureText(str); }
	 */

    /**
     把多个字符串相加起来
     @param strings
     @return
     */
    public static String addString(String... strings) {
        String addStr = null;

        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null) {
                if (addStr == null) {
                    addStr = strings[i];
                } else {
                    addStr += strings[i];
                }
            }
        }

        return addStr;
    }

    /**
     判断一个字符是否是字母
     @param codePoint
     @return
     */
    public static boolean isLetter(int codePoint) {
        if (('A' <= codePoint && codePoint <= 'Z') || ('a' <= codePoint && codePoint <= 'z')) {
            return true;
        } else {
            return false;
        }
    }

    /**
     判断一个字符是否是字母
     @param c
     @return
     */
    public static boolean isLetter(char c) {
        return isLetter((int) c);
    }

    /**
     把字符和String.xml里的指定字符串进行拼接
     */
    public static String stringFormat(Context context, int StringId, String str) {
        return String.format(context.getString(StringId), str);
    }

    /**
     判断字符是大写
     @param str
     @return
     */
    public static boolean isAcronym(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLowerCase(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     去除尾部.0
     */
    public static String deleteZero(String s) {
        if (TextUtils.isEmpty(s))
            return "0";
        else if (s.endsWith(".0")) {
            return s.substring(0, s.indexOf("."));
        }
        if (s.contains(".") && s.length() - s.indexOf(".") > 3) {
            s = s.substring(0, s.indexOf(".") + 3);
        }
        return s;
    }

    /**
     去除尾部.0
     */
    public static String deleteZero(float f) {
        String s = String.valueOf(f);
        return deleteZero(s);
    }

    /**
     去除尾部.0
     */
    public static String deleteZero(double d) {
        String s = String.valueOf(d);
        return deleteZero(s);
    }

    public static String getNumberFormat(int number) {
        if (number < 10000) {
            return String.valueOf(number);
        }

        if (number < 100000000) {
            return String.valueOf(number / 10000) + "万";
        }

        return String.valueOf(number / 100000000) + "亿";
    }

    public static String getToken(Context context) {
        String clientid = PushManager.getInstance().getClientid(context.getApplicationContext());
        if (TextUtils.isEmpty(clientid)) {
            clientid = AppUtils.getImei(context);
            PushManager.getInstance().bindAlias(context, clientid);
        }
        LogUtil.logXXfigo("clientid=" + clientid);
        return clientid;
    }

    /**
     更改字符串的字体大小和粗体，颜色
     @param view
     @param fontSize 字体大小
     @param start 起始位置
     @param end 截止位置
     @param color 字颜色
     */
    public static void setTextViewSpan(TextView view, int fontSize, int start, int end, int color) {

        Spannable span = new SpannableString(view.getText());
        if (fontSize != 0) {
            span.setSpan(new AbsoluteSizeSpan(fontSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        span.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(span);
        //        view.getPaint().setFakeBoldText(true);
    }

    /**
     如果string为空返回""
     @param s
     @return
     */
    public static String checkString(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        else
            return s;
    }
    /**
     补0
     @param str
     @param strLength
     @return
     */
    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append(str).append("0");
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     list去重
     @param list
     @return
     */
    public static ArrayList<String> removeDuplicate(ArrayList<String> list)
    {
        Set set = new LinkedHashSet<String>();
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

}
