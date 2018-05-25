package com.baigu.dms.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.APKUtils;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.IMHelper;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.ConfirmDialog;
import com.baigu.dms.common.view.DownloadingDialog;
import com.baigu.dms.common.view.LoadingDialog;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.presenter.CheckVersionPresenter;
import com.baigu.dms.presenter.impl.CheckVersionPresenterImpl;
import com.bumptech.glide.Glide;
import com.hyphenate.chat.ChatClient;
import com.micky.logger.Logger;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/25 11:32
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private ConfirmDialog mConfirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();
        setTitle(R.string.setting);
        initView();
    }

    private void initView() {
        findView(R.id.tv_update_phone).setOnClickListener(this);
        findView(R.id.tv_update_login_pwd).setOnClickListener(this);
        findView(R.id.tv_update_pay_pwd).setOnClickListener(this);
        findView(R.id.tv_clear_cache).setOnClickListener(this);
        findView(R.id.tv_faze_mode).setOnClickListener(this);
        findView(R.id.tv_about).setOnClickListener(this);
        findView(R.id.btn_exit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ViewUtils.isFastClick()) return;
        switch (v.getId()) {
            case R.id.tv_update_phone:
//                startActivity(new Intent(this, MyPhoneActivity.class));
                ViewUtils.showToastError(R.string.failed_get_reward);
                break;
            case R.id.tv_update_login_pwd:
                startActivity(new Intent(this, UpdatePasswdActivity.class));
                break;
            case R.id.tv_update_pay_pwd:
                User user = UserCache.getInstance().getUser();
                String idcardstatus = user.getIdcardstatus();
                startActivity(new Intent(this, UpdatePayPasswdActivity.class));
//                if (idcardstatus.equals("1")){
//                }else{
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                    dialog.setMessage(R.string.go_realname).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (dialog != null)
//                                dialog.dismiss();
//                        }
//                    }).setPositiveButton(R.string.go_finish, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(SettingActivity.this, WalletActivity.class));
//                        }
//                    }).create();
//                    dialog.show();
//                }
                break;
            case R.id.tv_faze_mode:
                startActivity(new Intent(this, FazeModeActivity.class));
                break;
            case R.id.tv_clear_cache:
                if (mConfirmDialog == null) {
                    mConfirmDialog = new ConfirmDialog(this, R.string.confirm_clear_cache);
                }
                mConfirmDialog.setOnConfirmDialogListener(new ConfirmDialog.OnConfirmDialogListener() {
                    @Override
                    public void onOKClick(View v) {
                        clearCache();
                        mConfirmDialog.dismiss();
                    }
                });
                mConfirmDialog.show();
                break;
            case R.id.tv_about:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_exit:
                ViewUtils.logout(this);
                break;
            default:
                break;
        }
    }

    private void clearCache() {

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        Observable.create(new ObservableOnSubscribe<RxOptional<Boolean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RxOptional<Boolean>> observableEmitter) throws Exception {
                FileUtils.deleteFileInDir(new File(FileUtils.BASE_FILE_PATH));
                Glide.get(SettingActivity.this).clearDiskCache();
                //TODO 环信缓存清理
//                IMHelper.getInstance().clearCache();
                observableEmitter.onNext(new RxOptional<>(true));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RxOptional<Boolean>>() {
                    @Override
                    public void accept(RxOptional<Boolean> optionalObservable) throws Exception {
                        loadingDialog.hide();
                        ViewUtils.showToastSuccess(R.string.success_clear_cache);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        loadingDialog.hide();
                        ViewUtils.showToastSuccess(R.string.failed_clear_cache);
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

}
