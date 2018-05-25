package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface AboutUsPresenter extends BasePresenter {
    void getAboutUs();

    interface AboutUsView {
        void onGetAboutUs(String result);
    }
}
