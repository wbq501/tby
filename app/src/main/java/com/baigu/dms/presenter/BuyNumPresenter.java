package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.BuyNum;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;

import java.util.List;

public interface BuyNumPresenter extends BasePresenter{

    void buyNum(List<Goods> goodsList);

    interface BuyNumView{
        void isBuy(BuyNum buyNum);
    }

}
