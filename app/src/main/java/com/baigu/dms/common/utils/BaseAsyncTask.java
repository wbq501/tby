package com.baigu.dms.common.utils;


import android.app.Activity;
import android.content.Context;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.view.LoadingDialog;
import com.micky.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 22:40
 */
public abstract class BaseAsyncTask<Params, Progress, Result> {
    protected Activity mContext;
    protected boolean mIsShowDialog;
    protected LoadingDialog mLoadingDialog;

    protected abstract RxOptional<Result> doInBackground(Params... params);

    protected void onPostExecute(Result result) {
    }

    protected void onPreExecute() {
    }

    protected void doOnError() {

    }

    // 不传入参数，不显示dialog
    public BaseAsyncTask() {

    }

    public BaseAsyncTask(Activity context, boolean isShowDialog) {
        mContext = context;
        mIsShowDialog = isShowDialog;
    }

    public void setLoadingText(int resId) {
        mLoadingDialog.setText(resId);
    }

    public void setLoadingText(String text) {
        mLoadingDialog.setText(text);
    }

    public Disposable execute(final Params... params) {
        return Observable.just(params).map(new Function<Params[], RxOptional<Result>>() {
            @Override
            public RxOptional<Result> apply(Params[] paramses) throws Exception {
                return doInBackground(paramses);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showDialog();
                        onPreExecute();
                    }
                }).subscribe(new Consumer<RxOptional<Result>>() {
                                 @Override
                                 public void accept(RxOptional<Result> resultRxOptional) throws Exception {
                                     hideDialog();
                                     if (tokenExpired(resultRxOptional.getCode())) {
                                        return;
                                     }
                                     onPostExecute(resultRxOptional.get());
                                 }
                             },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                doOnError();
                                Logger.e(throwable, throwable.getMessage());
                                hideDialog();
                            }
                        }
                );
    }

    protected void showDialog() {
        if (mIsShowDialog) {
            if (mLoadingDialog == null) {
                mLoadingDialog = new LoadingDialog(mContext);
            }
            mLoadingDialog.show();
        }
    }

    protected void hideDialog() {
        if (mIsShowDialog) {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        }
    }

    protected boolean tokenExpired(int code) {
        if (code == 90000 || code == 90003) {
            ViewUtils.showToastError(R.string.failed_request_token_expired);
            ViewUtils.logout(BaseApplication.getContext());
            return true;
        }
        return false;
    }
}
