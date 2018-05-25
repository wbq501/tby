package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Goods;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface GoodsDetailPresenter extends BasePresenter {

    void loadGoodsDetail(String id);


    interface GoodsDetailView {
        void onLoadGoodsDetail(Goods goods);
    }
}
