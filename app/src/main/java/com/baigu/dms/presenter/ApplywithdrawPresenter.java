package com.baigu.dms.presenter;

public interface ApplywithdrawPresenter extends BasePresenter{
    void getMyMoney(String pwd,String money,String bankid,boolean showDialog);

    interface Applywithdraw{
        void OngetMyMoney(String result);
    }
}
