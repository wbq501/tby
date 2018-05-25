package com.baigu.dms.presenter;

import com.baigu.dms.domain.model.Goods;

import java.util.List;

public interface BlogPresenter extends BasePresenter{
    void loadBlog(String send);

    interface BlogView{
        void loadGoodList(List<Goods> goodsList);
    }
}
