package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.netservice.common.model.PageResult;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface OrderRepealPresenter extends  BasePresenter{
    void loadOrderList(int status, int pageNum, boolean showDialog);

    interface OrderRepealView{
        void loadOrderList(PageResult<Order> orderPageResult);
    }
}
