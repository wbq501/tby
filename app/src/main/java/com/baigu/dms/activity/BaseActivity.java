package com.baigu.dms.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;

import java.util.List;

/**
 * @Description 主界面
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 0:13
 */
public class BaseActivity extends AppCompatActivity {
    public static final String ACTION_LOGOUT = "com.baigu.dms.ACTION_LOGOUT";

    private boolean mShowBackButton = true;
    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter filter = new IntentFilter(ACTION_LOGOUT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mExitReceiver, filter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if (mExitReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mExitReceiver);
        }

        super.onDestroy();
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected void initToolBar(boolean showBackButton) {
        mShowBackButton = showBackButton;
        initToolBar();
    }

    protected void initToolBar() {
        mToolbar = findView(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mToolbar.setAlpha(1.0F);
            mToolbar.getBackground().mutate().setAlpha(255);
            setSupportActionBar(mToolbar);
            if (mShowBackButton) {
                mToolbar.setNavigationIcon(R.mipmap.im_btn_back);
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ViewUtils.isFastClick()) return;
                        onBackClick(v);
                    }
                });
            }
        }
    }

    protected void onBackClick(View v) {
        finish();
    }

    public void setTitle(String title) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            super.setTitle(title);
        }
    }

    public void setTitle(int resId) {
        if (mToolbar != null) {
            mToolbar.setTitle(resId);
            super.setTitle(resId);
        }
    }

    private BroadcastReceiver mExitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_LOGOUT.equals(intent.getAction())) {
                finish();
            }
        }
    };

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
