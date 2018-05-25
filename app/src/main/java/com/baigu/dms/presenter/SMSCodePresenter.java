package com.baigu.dms.presenter;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/19 18:05
 */
public interface SMSCodePresenter extends BasePresenter {
    void sendSMSCode(String type, String phone);

    interface SMSCodeView {
        void onSendSMSCode(boolean result, String phone);
    }
}
