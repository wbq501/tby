package com.baigu.dms.presenter;

import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;

/**
 * @Description 订单详情 Presenter
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 13:26
 */
public interface OrderDetailPresenter extends BasePresenter {

    void loadOrderDetail(String orderId, String orderDate);

    void cancelOrder(String orderId, String orderDate);
    void refundOrder(String orderId, String orderDate);
    void queryLogistics(String orderId, String orderDate);

    interface OrderDetailView {
        void onLoadOrderDetail(OrderDetailResult orderDetailResult);
        void onCancelOrder(boolean result);
        void onRefundOrder(boolean result);
        void onQueryLogistics(String result);
    }
}
