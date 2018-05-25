package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Money;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 15:18
 */
public interface PayPresenter extends BasePresenter {

    void getMyMoney();
    void payOrder(String orderNo, String orderDate,String payMode);
    void payOrderByWallet(String orderNo, String orderDate,String pwd);
    void payOrderMerge(String orderNo, String orderDate,String walletAmount,String payMode,String payPwd);

    interface PayView {
        void onPayOrder(boolean result);
        void loadMoney(Money result);
        void onPayOrderByWallet(boolean result);
    }
}
