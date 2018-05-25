package com.baigu.dms.common.utils.rxpermission;

import android.Manifest;
import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.view.PermissionDialog;
import com.micky.logger.Logger;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/26 23:31
 */
public class PermissionRequest {

    private Activity mActivity;
    public String[] mPermissionArr;
    private String mTip;
    private boolean mAbleFinishActivity;

    private PermissionListener mPermissionListener;
    private PermissionDialog mPermissionDialog;
    private RxPermissions mRxPermissions;

    public PermissionRequest(Activity activity, int tipResId, String...permissions) {
        this(activity, activity.getString(tipResId), permissions);
    }

    public PermissionRequest(Activity activity, String tip, String...permissions) {
        mActivity = activity;
        mPermissionArr = permissions;
        mTip = tip;
        mPermissionDialog = new PermissionDialog(activity, R.style.DevelopingDialog);
        mPermissionDialog.setTip(mTip);
        mPermissionDialog.setPermissionDialogListener(new PermissionDialog.PermissionDialogListener() {
            @Override
            public void onCancle() {
                if (mAbleFinishActivity) {
                    mActivity.finish();
                }
            }
            @Override
            public void onSetting() {
                ViewUtils.toAppDetailSetting(mActivity);
            }
        });
        mRxPermissions = new RxPermissions(mActivity);
    }

    public void setPermissionListener(PermissionListener listener) {
        mPermissionListener = listener;
    }

    /**
     * 申请权限
     */
    public void requestPermission() {
        requestPermission(false);
    }

    /**
     * 申请权限
     * @param ableFinishActivity true-如果禁止授权则关闭当前activity
     */
    public void requestPermission(final boolean ableFinishActivity) {
        mAbleFinishActivity = ableFinishActivity;
        mRxPermissions.request(mPermissionArr)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean b) throws Exception {
                        if (b) {
                            if (mPermissionListener != null) {
                                mPermissionListener.onGrant();
                            }
                        } else {
                            mPermissionDialog.show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

    public interface PermissionListener {
        /**已授权*/
        void onGrant();
    }
}
