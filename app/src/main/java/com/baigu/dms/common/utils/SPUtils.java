package com.baigu.dms.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.baigu.dms.BaseApplication;


/**
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/5 22:31
 */
public class SPUtils {

    public static final String SP_NAME = "DMS";

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