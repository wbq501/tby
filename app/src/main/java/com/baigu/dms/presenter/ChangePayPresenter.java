package com.baigu.dms.presenter;

public interface ChangePayPresenter extends BasePresenter{

    void resetpay(String phone, String code, String pwd, String idCard);

    interface ChangePayView{
        void changePayState(String state);
    }
}
