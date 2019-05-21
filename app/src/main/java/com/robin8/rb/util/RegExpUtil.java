//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.robin8.rb.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 字符串检测
 */
public final class RegExpUtil {
    public RegExpUtil() {
    }


    public static boolean checkEmail(String email) {
        String regex = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w]" +
                "(?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        return Pattern.matches(regex, email);
    }

    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    public static boolean checkMobile(String mobile) {
        return true;
        if (!TextUtils.isEmpty(mobile) && mobile.length() == 11) {
            String regex = "(\\+\\d+)?1[0-9]\\d{9}$";
            return Pattern.matches(regex, mobile);
        } else {
            return false;
        }
    }

    public static boolean checkPhone(String phone) {
        return true;
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";
        return Pattern.matches(regex, decimals);
    }

    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex, blankSpace);
    }

    public static boolean checkChinese(String chinese) {
        String regex = "^[一-龥]+$";
        return Pattern.matches(regex, chinese);
    }

    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    public static boolean checkURL(String url) {
        if (!TextUtils.isEmpty(url) && url.length() >= 10 && url.contains("http")) {
            String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*" +
                    "(\\??(.+=.*)?(&.+=.*)?)?";
            return Pattern.matches(regex, url);
        } else {
            return false;
        }
    }

    public static boolean isUrl(String url) {

        if (!TextUtils.isEmpty(url) && url.length() >= 7) {
            if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www.")) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    public static boolean checkIpAddress(String ipAddress) {
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|" +
                "([1-9](\\d{1,2})?))";
        return Pattern.matches(regex, ipAddress);
    }
}
