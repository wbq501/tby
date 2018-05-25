package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface PhoneUpdatePresenter extends BasePresenter {
    void checkOldPhone(String phone, String code);
    void updatePhone(String phone, String code);

    interface PhoneUpdateView {
        void onCheckPhone(boolean b);
        void onUpdatePhone(boolean b);
    }
}
