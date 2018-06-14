package com.baigu.dms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.APKUtils;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.DownloadingDialog;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Agreement;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.domain.netservice.URLFactory;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CheckVersionPresenter;
import com.baigu.dms.presenter.impl.CheckVersionPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.micky.logger.Logger;

/**
 * @Description 关于我们
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class AboutUsActivity extends BaseActivity implements View.OnClickListener, CheckVersionPresenter.CheckVersionView {

    private TextView mTvLatestVersion;
    private ConfirmDialog mConfirmDialog;
    private DownloadingDialog mDownProcessDialog;
    private CheckVersionPresenter mCheckVersionPresenter;
    private APKUtils mAPKUtils;
    private   Agreement agreementData;

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
                        ViewUtils.exitApp(AboutUsActivity.this);
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
        setContentView(R.layout.activity_about_us);
        initToolBar();
        setTitle(R.string.about_us);
        mAPKUtils = new APKUtils(this);
        mCheckVersionPresenter = new CheckVersionPresenterImpl(this, this);

        initView();
    }

    private void initView() {
        mTvLatestVersion = findView(R.id.tv_latest_version);
        findView(R.id.ll_version_check).setOnClickListener(this);
        findView(R.id.tv_app_use_agreement).setOnClickListener(this);
        ImageView ivLogo = findView(R.id.iv_logo);
        Glide.with(this).load(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(this)).into(ivLogo);
        TextView tvVersion = findView(R.id.tv_version);
        APKUtils.Version version = APKUtils.getLocalVersionCode(this);
        if (version != null) {
            tvVersion.setText(getString(R.string.version) + version.versionName);
        }
        mCheckVersionPresenter.loadUrl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserCache.getInstance().isForceUpdate()) {
            if ((mConfirmDialog != null && mConfirmDialog.isShowing()) || (mDownProcessDialog != null && mDownProcessDialog.isShowing())) {

            } else {
                mConfirmDialog.show();
            }
        }
    }

    @Override
    public void onCheckVersion(VersionInfo serverVersion) {
        try {
            if (serverVersion == null) {
                ViewUtils.showToastError(R.string.failed_check_version);
                return;
            }
            APKUtils.Version localVersion = mAPKUtils.getLocalVersionCode();
            if (localVersion != null) {
                boolean forceUpgrade = serverVersion.getVersionforced() > 0;
                if (!TextUtils.isEmpty(serverVersion.getUrl()) && localVersion.versionCode < Integer.valueOf(serverVersion.getVersioncode())) { //提示更新apk
                    String versionText = serverVersion.getVersionforced() > 0 ? getString(R.string.find_new_version_force, serverVersion.getVersionname()) : getString(R.string.find_new_version, serverVersion.getVersionname());
                    if (mConfirmDialog == null) {
                        mConfirmDialog = new ConfirmDialog(this, versionText, !forceUpgrade);
                    }
                    mConfirmDialog.setTitle(versionText);
                    mConfirmDialog.setOnConfirmDialogListener(new OnUpgradeConfirmDialogListener(serverVersion));
                    mConfirmDialog.show();
                    UserCache.getInstance().setForceUpdate(forceUpgrade);
                } else {
                    mTvLatestVersion.setVisibility(View.VISIBLE);
                    ViewUtils.showToastInfo(R.string.latest_version);
                }
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    @Override
    public void onLoad(BaseResponse<Agreement> agreement) {
        if(agreement.getCode()==1){
            agreementData = agreement.getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConfirmDialog != null) {
            mConfirmDialog.dismiss();
        }
        if (mDownProcessDialog != null) {
            mDownProcessDialog.dismiss();
        }
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
                mDownProcessDialog = new DownloadingDialog(AboutUsActivity.this, R.style.DownLoadDialog);
                mDownProcessDialog.setCanceledOnTouchOutside(false);
            }
            mDownProcessDialog.show();
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                ViewUtils.showToastError(R.string.check_sdcard);
                return;
            }
            mAPKUtils.downFile(serverVersionInfo.getUrl(), FileUtils.APK_LOCAL_PATH, serverVersionInfo.getVersionname() + ".apk", mHandler);
        }

        @Override
        public void onCancelClick(View v) {
            mConfirmDialog.dismiss();
            if (UserCache.getInstance().isForceUpdate()) {
                ViewUtils.exitApp(AboutUsActivity.this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.ll_version_check:
                if (mTvLatestVersion.getVisibility() == View.VISIBLE) {
                    ViewUtils.showToastInfo(R.string.latest_version);
                    return;
                }
                mCheckVersionPresenter.checkVersion(true);
                break;
            case R.id.tv_app_use_agreement:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", getString(R.string.app_use_agreement));
//                intent.putExtra("url", URLFactory.getInstance().getUseAgreement());
                intent.putExtra("content",agreementData.getContent());
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
