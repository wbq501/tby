package com.baigu.dms.presenter;

public interface DelMyBankPresenter extends BasePresenter{

    void delBank(String bankId);

    interface DelBankView{
        void delBank(String result);
    }
}
