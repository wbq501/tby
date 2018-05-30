package com.baigu.dms.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.baigu.dms.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/5 22:31
 */
public class SPUtils {

    public static final String SP_NAME = "DMS";
    public static final String BUYTYPE = "buytype";

    public static final String KEY_USER = "key_user";
    public static final String KEY_ACCOUNT = "key_account";
    public static final String KEY_LOGIN_TIME = "key_login_time";
    public static final String KEY_SOUND = "key_sound";
    public static final String KEY_VIBRATE = "key_vibrate";

    //手势锁
    public static final String KEY_GESTURE = "key_gesture_";

    /**
     * 保存对象
     *
     * @param key 键
     * @param obj 值
     */
    public static void putObject(String key, Object obj) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof String) {
            editor.putString(key, (String) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        }
        editor.commit();
    }


    /**
     * 读取对象
     *
     * @param key        键
     * @param defaultObj 默认值
     * @param <T>        返回值类型
     * @return
     */
    public static <T> T getObject(String key, T defaultObj) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);

        if (defaultObj instanceof String) {
            return (T) sp.getString(key, (String) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return (T) (Integer) sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Boolean) {
            return (T) (Boolean) sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return (T) (Float) sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Long) {
            return (T) (Long) sp.getLong(key, (Long) defaultObj);
        }
        return null;
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static void setDataList(String tag, LinkedHashSet<String> datalist) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(BUYTYPE, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static LinkedHashSet<String> getDataList(String tag) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(BUYTYPE, Context
                .MODE_PRIVATE);
        LinkedHashSet<String> datalist=new LinkedHashSet<>();
        String strJson = sp.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<Set<String>>() {
        }.getType());
        return datalist;
    }

    public static void clearBuyType() {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(BUYTYPE, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     *
     * @param key
     */
    public static  void remove(String key) {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
      editor.commit();
    }

    public static void clear() {
        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences(SP_NAME, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}