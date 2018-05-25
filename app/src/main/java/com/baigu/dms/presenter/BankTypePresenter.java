package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BankType;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface BankTypePresenter extends BasePresenter {

    void loadBankList();

    interface BankTypeView {
        void onLoadBankList(List<BankType> list);
    }
}
