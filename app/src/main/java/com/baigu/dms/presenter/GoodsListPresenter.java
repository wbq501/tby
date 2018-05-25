package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.dms.domain.model.ShopAdverPictrue;
import com.baigu.dms.domain.netservice.common.model.GoodsResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:12
 */
public interface GoodsListPresenter extends BasePresenter {

    void loadGoodsCategory();

    void loadGoodsPageList(int pageNum, String categoryId);

    void loadGoodsList();

    void searchGoods(String searchName);

    void loadImage();

    interface GoodsListView {
        void onLoadGoodsCategory(List<GoodsCategory> list);
        void onLoadGoodsPageList(PageResult<Goods> goodsPageResult);
        void onLoadGoodsList(GoodsResult goodsList);
        void onSearchGoods(List<Goods> list);
        void onLoadPictrue(BaseResponse<List<ShopAdverPictrue>>  response);
    }
}
