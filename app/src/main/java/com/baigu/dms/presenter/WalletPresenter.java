package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Money;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface WalletPresenter extends BasePresenter {
    void getMyMoney();

    interface WalletView {
        void onGetMyMoney(Money result);
    }
}
