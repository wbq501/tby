package com.baigu.dms.common.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.micky.logger.Logger;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/4 19:44
 */
public class StringUtils {

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 是否是网络地址
     * @param url
     * @return
     */
    public static boolean isNetUrl(String url) {
        return ((url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp:")));
    }

    public static String encodeString(String str) {
        return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
    }

    public static String decodeString(String str) {
        byte[] bsByte = Base64.decode(str, Base64.DEFAULT);
        return new String (bsByte);
    }

    public static String getImageSelectButtonText(String text, int selectNum, int maxNum) {
        StringBuilder sb = new StringBuilder(text);
        sb.append("(").append(selectNum).append("/").append(maxNum).append(")");
        return sb.toString();
    }

    public static boolean isValidPhone(String phone) {
//		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher matcher = pattern.matcher(phone);
        if (!TextUtils.isEmpty(phone) && phone.startsWith("1") && phone.length() == 11) {
            return true;
        }
        return false;
    }

    public static String getWeightString(long weight) {
        String result = "0g";
        try {
            if (weight < 0) {
                weight = 0;
            }
            if (weight < 1000) {
                result = weight + "g";
            } else if (weight >= 1000) {
                long kg = weight / 1000;
                long g = weight % 1000;
                if (g > 0) {
                    String dot = String.format("%.1f", g * 1.0F / 1000);
                    result = kg + dot.substring(dot.indexOf(".")) + "kg";
                } else {
                    result = kg + "kg";
                }
            }
            return result;
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return result;
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        String regexEmail = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return Pattern.matches(regexEmail, email);
    }

    public static String decodeHtmlString(String source) {
        if (!TextUtils.isEmpty(source)) {
            source = source.replaceAll("&amp;", "&");
            source = source.replaceAll("&quot;", "\"");
            source = source.replaceAll("&lt;", "<");
            source = source.replaceAll("&gt;", ">");
            source = source.replaceAll("&nbsp;", " ");
        }
        return source;
    }

    public static String getOrderDate(String date) {
        return date != null && date.length() >= 6 ? date.substring(0, 6).replace("-", "") : "";
    }

    public static String getTimeLabelStr(String dateStr) {
        return dateStr != null && dateStr.length() >= 16 ? dateStr.substring(0, 16) : dateStr;
    }
}
