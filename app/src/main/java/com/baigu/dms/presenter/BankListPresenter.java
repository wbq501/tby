package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Bank;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public interface BankListPresenter extends BasePresenter {

    void loadBankList();

    interface  BankListView{
        void loadBankList(List<Bank> banks);
    }
}
