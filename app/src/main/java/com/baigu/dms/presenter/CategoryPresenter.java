package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.common.model.PageResult;

import java.util.List;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public interface CategoryPresenter extends  BasePresenter {
    void loadGoodList(String categoryId);

    interface CategoryView{
        void loadGoodList(List<Goods> goodsList);
    }
}
