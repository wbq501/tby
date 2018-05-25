package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface LoginPresenter extends BasePresenter {

    void autoLogin();
    void login(String phone, String pwd);

    interface LoginView {
        void onLogin(boolean result);
    }
}
