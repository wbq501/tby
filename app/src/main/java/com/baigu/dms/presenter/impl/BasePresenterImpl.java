package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.presenter.BasePresenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:22
 */
public class BasePresenterImpl implements BasePresenter {
    protected Activity mActivity;

    public BasePresenterImpl(Activity activity) {
        mActivity = activity;
    }

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onCreate() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void onDestroy() {
        compositeDisposable.clear();
    }
}