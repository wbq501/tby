package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface AddOrderPresenter extends BasePresenter {

    void getDefaultAddress(boolean showDialog);

    void expressCompute(String cityId, String expressId,String logisticsName, List<Goods> goodsList);

    void addOrder(List<Goods> goodsList, Address address, boolean saveAddress, String expressId,String logisticsName, String remark);
    void checkGoodsStock(List<Goods> goodsList);

    interface OrderAddView {
        void onGetDefaultAddress(Address address);
        void onExpressCompute(List<Double> expressPrice);
        void onAddOrder(OrderDetailResult result);
        void onCheckGoodsStock(Boolean result);
    }
}
