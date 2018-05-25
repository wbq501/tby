package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.netservice.common.model.PageResult;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/6 15:18
 */
public interface OrderPresenter extends BasePresenter {
    void loadOrderList(int status, int pageNum, boolean showDialog);
    void cancelOrder(String orderId, String orderDate);
    void refundOrder(String orderId, String orderDate,String refundReason);

    interface OrderView {
        void onLoadOrderList(PageResult<Order> orderPageResult);
        void onCancelOrder(boolean result);
        void onRefundOrder(boolean result);
    }
}
