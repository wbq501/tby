package com.baigu.dms.common.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.activity.LoginActivity;
import com.micky.logger.Logger;

import java.io.IOException;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/17 20:04
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultUEH;
    private Context mContext;

    private CrashHandler() {
        mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static CrashHandler getInstance() {
        return instance;
    }

    public void init(Context ctx) {
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = ctx;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("CrashHandler", ex.getMessage(), ex);
        Logger.e(ex, ex.getMessage());
        mDefaultUEH.uncaughtException(thread, ex);
        ViewUtils.exitApp(mContext);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}