package com.baigu.dms.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import com.baigu.dms.R;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


public class ShareToQQ {
    private Tencent mTencent;
    private Context context;
    private static ShareToQQ instance;
    private ShareListener mShareListener;
    private static final int SHARE_PIC = 0x123;

    private ShareToQQ(Context context) {
        this.context = context;
        mTencent = Tencent.createInstance(context.getString(R.string.qq_appid), context);
        mShareListener = new ShareListener();
    }

    public static ShareToQQ getInstance(Context context) {
        if (instance == null) {
            synchronized (ShareToQQ.class) {
                if (instance == null)
                    instance = new ShareToQQ(context);
            }
        }
        return instance;
    }

    public ShareListener getShareListener() {
        if (mShareListener != null) {
            return mShareListener;
        }
        return null;
    }

    public void shareTextToFriend(String title, String shareText, String url) {
        shareText(title, shareText, url, 0);
    }

    public void shareTextToZone(String title, String shareText, String url) {
        shareText(title, shareText, url, 1);
    }

    public void shareText(String title, String shareText, String url, int flag) {
        try {
            if (!isQQInstall()) {
                return;
            }
            final Bundle params = new Bundle();
            // 必填参数
            //            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, getShareLogo());
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, flag);
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            if (!TextUtils.isEmpty(title)) {
                params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            }
            if (!TextUtils.isEmpty(url)) {
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
            }
            if (!TextUtils.isEmpty(shareText)) {
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareText);
            }
            mTencent.shareToQQ((Activity) context, params, mShareListener);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ShareListener implements IUiListener {

        @Override
        public void onCancel() {
//            Toast.makeText(context, context.getString(R.string.cancel_success), Toast
//                    .LENGTH_SHORT).show();

        }

        @Override
        public void onComplete(Object arg0) {

//            Toast.makeText(context, context.getString(R.string.share_success), Toast
//                    .LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
//            Toast.makeText(context, context.getString(R.string.share_fail), Toast.LENGTH_SHORT)
//                    .show();
        }

    }

    /**
     * 判断qq是否安装
     *
     * @param
     * @return
     */
    public boolean isQQInstall() {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        Toast.makeText(context, context.getString(R.string.no_intall_app), Toast.LENGTH_SHORT)
                .show();
        return false;
    }

}
