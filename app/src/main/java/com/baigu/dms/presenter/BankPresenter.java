package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Bank;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface BankPresenter {
    void addBank(Bank bank);

    interface AddBankView{
        void addBank(Boolean result);
    }
}
