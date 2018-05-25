package com.baigu.dms.presenter;


public interface BasePresenter {
    void onCreate();
    void onResume();
    void onPause() ;
    void onDestroy();

    interface BaseView {
        void showLoading();
        void hiddenLoading();
    }
}
