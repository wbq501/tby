package com.baigu.dms;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.baigu.dms.activity.SplashActivity;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.CrashHandler;
import com.baigu.dms.common.utils.LogUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.toast.Toasty;
import com.baigu.dms.domain.db.DBCore;
import com.hyphenate.chat.ChatClient;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 0:13
 */
public class BaseApplication extends MultiDexApplication {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        CrashHandler.getInstance().init(this);
        LogUtils.initLogger();

        //umeng初始化
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, getString(R.string.um_app_key));
        UMConfigure.setLogEnabled(Constants.DEBUG);
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Log.d("app", "onCoreInitFinished: "+b);
            }
        };
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
