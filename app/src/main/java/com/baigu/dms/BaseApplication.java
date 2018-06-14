package com.baigu.dms;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.CrashHandler;
import com.baigu.dms.common.utils.LogUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;

import java.util.Locale;

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
        initBugly();
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

    private void initBugly() {
        Bugly.init(getApplicationContext(),"6732252c38",false);
    }

    public static Context getContext() {
        return sContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

//        Beta.installTinker();
    }

}
