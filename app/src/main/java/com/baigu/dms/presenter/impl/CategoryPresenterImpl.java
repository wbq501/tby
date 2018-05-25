package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.alipay.tscenter.biz.rpc.vkeydfp.result.BaseResult;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CategoryPresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class CategoryPresenterImpl extends BasePresenterImpl implements CategoryPresenter {
    private CategoryView categoryView;

    public CategoryPresenterImpl(Activity activity,CategoryView categoryView) {
        super(activity);
        this.categoryView=categoryView;
    }

    @Override
    public void loadGoodList( String categoryId) {
        addDisposable(new BaseAsyncTask<String, Void, List<Goods>>() {

            @Override
            protected RxOptional<List<Goods>> doInBackground(String... params) {
                RxOptional<List<Goods>> rxResult = new RxOptional<>();
                List<Goods> result = null;
                try {
                  Call<BaseResponse<List<Goods>>> goodsListCall = ServiceManager.createGsonService(ShopService.class).getGoodsListByCategory(params[0]);
                    Response<BaseResponse<List<Goods>>> goodsListResultResponse = goodsListCall.execute();
                    rxResult.setCode(goodsListResultResponse != null && goodsListResultResponse.body() != null ? goodsListResultResponse.body().getCode() : -1);
                    if (goodsListResultResponse != null && goodsListResultResponse.body() != null && BaseResponse.SUCCESS.equals(goodsListResultResponse.body().getStatus())) {
                        result = goodsListResultResponse.body().getData();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<Goods> goodsPageResult) {
                super.onPostExecute(goodsPageResult);
                if (categoryView != null) {
                    categoryView.loadGoodList(goodsPageResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (categoryView != null) {
                    categoryView.loadGoodList(null);
                }
            }
        }.execute(categoryId));
    }
}
