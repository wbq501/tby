package com.baigu.dms.presenter.impl;

import android.util.Log;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.GoodsDetailPresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class GoodsDetailPresenterImpl extends BasePresenterImpl implements GoodsDetailPresenter {
    private GoodsDetailView mGoodsDetailView;

    public GoodsDetailPresenterImpl(BaseActivity activity, GoodsDetailView goodsDetailView) {
        super(activity);
        this.mGoodsDetailView = goodsDetailView;
    }

    @Override
    public void loadGoodsDetail(String id) {
        addDisposable(new BaseAsyncTask<String, Void, Goods>(mActivity, true) {

            @Override
            protected RxOptional<Goods> doInBackground(String... params) {
                RxOptional<Goods> rxResult = new RxOptional<>();
                Goods goods = null;
                try {
                    Call<BaseResponse<Goods>> goodsCall = ServiceManager.createGsonService(ShopService.class).getGoodsDetail(params[0]);
                    Response<BaseResponse<Goods>> goodsResponse = goodsCall.execute();
                    if (goodsResponse != null && goodsResponse.body() != null && goodsResponse.body().getData() != null && BaseResponse.SUCCESS.equals(goodsResponse.body().getStatus())) {
                        goods = goodsResponse.body().getData();
                    }

                    rxResult.setResult(goods);
                    rxResult.setCode(goodsResponse != null && goodsResponse.body() != null ? goodsResponse.body().getCode() : -1);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                rxResult.setResult(goods);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Goods goods) {
                super.onPostExecute(goods);
                if (mGoodsDetailView != null) {
                    mGoodsDetailView.onLoadGoodsDetail(goods);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mGoodsDetailView != null) {
                    mGoodsDetailView.onLoadGoodsDetail(null);
                }
            }

        }.execute(id));
    }
}
