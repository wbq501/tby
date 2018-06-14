package com.baigu.dms.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.APKUtils;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.utils.rxpermission.PermissionRequest;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.DownloadingDialog;
import com.baigu.dms.common.view.toast.Toasty;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.DBCore;
import com.baigu.dms.domain.model.Agreement;
import com.baigu.dms.domain.model.ExpressInfo;
import com.baigu.dms.domain.model.ExpressType;
import com.baigu.dms.domain.model.LogisticsInfo;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ExpressService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CheckVersionPresenter;
import com.baigu.dms.presenter.ExpressGetPresenter;
import com.baigu.dms.presenter.LoginPresenter;
import com.baigu.dms.presenter.impl.CheckVersionPresenterImpl;
import com.baigu.dms.presenter.impl.ExpressGetPresenterImpl;
import com.baigu.dms.presenter.impl.LoginPresenterImpl;
import com.hyphenate.chat.ChatClient;
import com.micky.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 17:29
 */
public class SplashActivity extends AppCompatActivity implements CheckVersionPresenter.CheckVersionView, LoginPresenter.LoginView{
    private static String TAG = "aaaa";
    private ConfirmDialog mConfirmDialog;
    private DownloadingDialog mDownProcessDialog;
    private CheckVersionPresenter mCheckVersionPresenter;
    private APKUtils mAPKUtils;

    private LoginPresenter mLoginPresenter;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case APKUtils.HANDLER_MSG_PD_UPDATE:
                    mDownProcessDialog.setProgress(msg.arg1);
                    break;
                case APKUtils.HANDLER_MSG_DOWNLOAD_COMPLET:
                    mDownProcessDialog.dismiss();
                    String packagePath = (String) msg.obj;
                    mAPKUtils.installApp(packagePath);
                    break;
                case APKUtils.HANDLER_MSG_DOWNLOAD_FAILED:
                    mDownProcessDialog.dismiss();
                    ViewUtils.showToastError(R.string.failed_download_apk);
                    if (UserCache.getInstance().isForceUpdate()) {
                        finish();
                    } else {
                        autoLogin();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mCheckVersionPresenter = new CheckVersionPresenterImpl(this, this);
        mLoginPresenter = new LoginPresenterImpl(this, this);
        mAPKUtils = new APKUtils(this);
        String appName = getString(R.string.app_name);
        String tip = getString(R.string.permission_sd, appName, appName);
        PermissionRequest permissionRequest = new PermissionRequest(SplashActivity.this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);
        permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
            @Override
            public void onGrant() {
                Constants.sExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        //数据库初始化
                        DBCore.init(SplashActivity.this);
                        Toasty.Config.getInstance().setTextSize(14).apply();
                        mAPKUtils.deleteDownloadApk();

                    }
                });
                //环信初始化
                ChatClient.getInstance().init(SplashActivity.this, new ChatClient.Options().setConsoleLog(Constants.DEBUG).setAppkey(getString(R.string.hx_appkey))
                        .setTenantId(getString(R.string.hx_tenantId)));
                if (ChatClient.getInstance().isLoggedInBefore()) {
                    ChatClient.getInstance().logout(false, null);
                }
                UserCache.getInstance().setForceUpdate(false);

            }
        });
        permissionRequest.requestPermission(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Flowable.timer(300L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if ((mConfirmDialog != null && mConfirmDialog.isShowing()) || (mDownProcessDialog != null && mDownProcessDialog.isShowing())) {

                        } else {
                            String appName = getString(R.string.app_name);
                            String tip = getString(R.string.permission_sd, appName, appName);
                            PermissionRequest permissionRequest = new PermissionRequest(SplashActivity.this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);
                            permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
                                @Override
                                public void onGrant() {
                                    mCheckVersionPresenter.checkVersion(false);
                                }
                            });
                            permissionRequest.requestPermission(true);
                        }
                    }
                });
    }


    @Override
    public void onCheckVersion(VersionInfo serverVersion) {
        try {
            if (serverVersion == null) {
                ViewUtils.showToastError(R.string.failed_check_version);
                finish();
                return;
            }
            APKUtils.Version localVersion = mAPKUtils.getLocalVersionCode();
            if (localVersion != null) {
                boolean forceUpgrade = serverVersion.getVersionforced() > 0;
                if (!TextUtils.isEmpty(serverVersion.getUrl()) && localVersion.versionCode < Integer.valueOf(serverVersion.getVersioncode())) { //提示更新apk
                    String versionText = serverVersion.getVersionforced() > 0 ? getString(R.string.find_new_version_force, serverVersion.getVersionname()) : getString(R.string.find_new_version, serverVersion.getVersionname());
                    if (mConfirmDialog == null) {
                        mConfirmDialog = new ConfirmDialog(this, versionText, false);
                    }
                    mConfirmDialog.setTitle(versionText);
                    mConfirmDialog.setOnConfirmDialogListener(new OnUpgradeConfirmDialogListener(serverVersion));
                    mConfirmDialog.show();
                    UserCache.getInstance().setForceUpdate(forceUpgrade);
                } else {
                    autoLogin();
                }
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    @Override
    public void onLoad(BaseResponse<Agreement> agreement) {

    }

    class OnUpgradeConfirmDialogListener implements ConfirmDialog.OnConfirmDialogListener2 {
        private VersionInfo serverVersionInfo;

        public OnUpgradeConfirmDialogListener(VersionInfo versionInfo) {
            serverVersionInfo = versionInfo;
        }

        @Override
        public void onOKClick(View v) {
            mConfirmDialog.dismiss();
            if (mDownProcessDialog == null) {
                mDownProcessDialog = new DownloadingDialog(SplashActivity.this, R.style.DownLoadDialog);
                mDownProcessDialog.setCanceledOnTouchOutside(false);
            }
            mDownProcessDialog.show();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ViewUtils.showToastError(R.string.check_sdcard);
                return;
            }
            String appName = getString(R.string.app_name);
            String tip = getString(R.string.permission_sd, appName, appName);
            PermissionRequest permissionRequest = new PermissionRequest(SplashActivity.this, tip, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE);
            permissionRequest.setPermissionListener(new PermissionRequest.PermissionListener() {
                @Override
                public void onGrant() {
                    mAPKUtils.downFile(serverVersionInfo.getUrl(), FileUtils.APK_LOCAL_PATH, serverVersionInfo.getVersionname() + ".apk", mHandler);
                }
            });
            permissionRequest.requestPermission(true);
        }

        @Override
        public void onCancelClick(View v) {
            mConfirmDialog.dismiss();
            if (!UserCache.getInstance().isForceUpdate()) {
                autoLogin();
            } else {
                finish();
            }
        }
    }

    private void autoLogin() {
        User user = UserCache.getInstance().getUser();
        if (user != null && !TextUtils.isEmpty(user.getLoginToken())) {
            try {
                mLoginPresenter.autoLogin();
            } catch (Exception e) {
                Logger.e(e, e.getMessage());
                gotoLogin();
            }
        } else {
            gotoLogin();
        }

    }

    @Override
    public void onLogin(boolean result) {
        if (result) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            gotoLogin();
        }
    }

    private void gotoLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
    }
}
