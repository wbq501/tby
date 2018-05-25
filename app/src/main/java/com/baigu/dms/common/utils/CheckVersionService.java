package com.baigu.dms.common.utils;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/6 14:19
 */
public class CheckVersionService extends IntentService {
    private boolean mRunning = true;
    private LocalBroadcastManager mLocalBroadcastManager;

    public CheckVersionService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            mRunning = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mRunning = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunning = false;
    }
}
