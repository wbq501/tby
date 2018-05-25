package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface RefindPasswdPresenter extends BasePresenter {

    void refindPasswd(String phone, String pwd, String code);

    interface RefindPasswdView {
        void onRefindPasswd(boolean b);
    }
}