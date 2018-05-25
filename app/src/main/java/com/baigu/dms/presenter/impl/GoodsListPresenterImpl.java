package com.baigu.dms.presenter.impl;

import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.dms.domain.model.ShopAdverPictrue;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.common.model.GoodsResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.GoodsListPresenter;
import com.micky.logger.Logger;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class GoodsListPresenterImpl extends BasePresenterImpl implements GoodsListPresenter {
    private GoodsListView mGoodsListView;

    public GoodsListPresenterImpl(BaseActivity activity, GoodsListView goodsListView) {
        super(activity);
        this.mGoodsListView = goodsListView;
    }

    @Override
    public void loadGoodsCategory() {
        addDisposable(new BaseAsyncTask<String, Void, List<GoodsCategory>>() {

            @Override
            protected RxOptional<List<GoodsCategory>> doInBackground(String... params) {
                RxOptional<List<GoodsCategory>> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<List<GoodsCategory>>> goodsCategoryCall = ServiceManager.createGsonService(ShopService.class).getGoodsCategory();
                    Response<BaseResponse<List<GoodsCategory>>> goodsCategoryResponse = goodsCategoryCall.execute();
                    rxResult.setCode(goodsCategoryResponse != null && goodsCategoryResponse.body() != null ? goodsCategoryResponse.body().getCode() : -1);
                    if (goodsCategoryResponse != null && goodsCategoryResponse.body() != null && BaseResponse.SUCCESS.equals(goodsCategoryResponse.body().getStatus())) {
                        rxResult.setResult(goodsCategoryResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<GoodsCategory> list) {
                super.onPostExecute(list);
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsCategory(list);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsList(null);
                }
            }

        }.execute());
    }

    @Override
    public void loadGoodsPageList(int pageNum, String categoryId) {
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Goods>>() {

            @Override
            protected RxOptional<PageResult<Goods>> doInBackground(String... params) {
                RxOptional<PageResult<Goods>> rxResult = new RxOptional<>();
                PageResult<Goods> result = null;
                try {
                    Call<BaseResponse<PageResult<Goods>>> goodsListCall = ServiceManager.createGsonService(ShopService.class).getGoodsList(params[0], params[1]);
                    Response<BaseResponse<PageResult<Goods>>> goodsListResultResponse = goodsListCall.execute();
                    rxResult.setCode(goodsListResultResponse != null && goodsListResultResponse.body() != null ? goodsListResultResponse.body().getCode() : -1);
                    if (goodsListResultResponse != null && goodsListResultResponse.body() != null && BaseResponse.SUCCESS.equals(goodsListResultResponse.body().getStatus())) {
                        result = goodsListResultResponse.body().getData();
                        if (result == null) {
                            result = new PageResult<>();
                            result.firstPage = false;
                            result.lastPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<Goods> goodsPageResult) {
                super.onPostExecute(goodsPageResult);
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsPageList(goodsPageResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsList(null);
                }
            }
        }.execute(String.valueOf(pageNum), categoryId));
    }

    @Override
    public void loadGoodsList() {
        addDisposable(new BaseAsyncTask<String, Void, GoodsResult>(mActivity, true) {

            @Override
            protected RxOptional<GoodsResult> doInBackground(String... params) {
                RxOptional<GoodsResult> rxResult = new RxOptional<>();
                GoodsResult result = null;
                try {
                    Call<BaseResponse<GoodsResult>> goodsListCall = ServiceManager.createGsonService(ShopService.class).getGoodsList();
                    Response<BaseResponse<GoodsResult>> goodsListResultResponse = goodsListCall.execute();
                    rxResult.setCode(goodsListResultResponse != null && goodsListResultResponse.body() != null ? goodsListResultResponse.body().getCode() : -1);
                    if (goodsListResultResponse != null && goodsListResultResponse.body() != null && BaseResponse.SUCCESS.equals(goodsListResultResponse.body().getStatus())) {
                        result = goodsListResultResponse.body().getData();
                        if (result == null) {
                            result = new GoodsResult();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(GoodsResult result) {
                super.onPostExecute(result);
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsList(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadGoodsList(null);
                }
            }
        }.execute());
    }

    @Override
    public void searchGoods(String searchName) {
        addDisposable(new BaseAsyncTask<String, Void, List<Goods>>(mActivity, true) {

            @Override
            protected RxOptional<List<Goods>> doInBackground(String... params) {
                RxOptional<List<Goods>> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<List<Goods>>> searchCall = ServiceManager.createGsonService(ShopService.class).searchGoods(params[0]);
                    Response<BaseResponse<List<Goods>>> searchResponse = searchCall.execute();
                    rxResult.setCode(searchResponse != null && searchResponse.body() != null ? searchResponse.body().getCode() : -1);
                    if (searchResponse != null && searchResponse.body() != null && BaseResponse.SUCCESS.equals(searchResponse.body().getStatus())) {
                        rxResult.setResult(searchResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<Goods> list) {
                super.onPostExecute(list);
                if (mGoodsListView != null) {
                    mGoodsListView.onSearchGoods(list);
                    for (int i = 0; i < list.size(); i++) {
                        Log.i("test", "onPostExecute: " + list.get(i));
                    }
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mGoodsListView != null) {
                    mGoodsListView.onSearchGoods(null);
                }
            }
        }.execute(searchName));
    }

    @Override
    public void loadImage() {
        addDisposable(new BaseAsyncTask<String, Void, BaseResponse<List<ShopAdverPictrue>>>() {
            @Override
            protected RxOptional<BaseResponse<List<ShopAdverPictrue>>> doInBackground(String... strings) {
                RxOptional<BaseResponse<List<ShopAdverPictrue>>> rxOptional = new RxOptional<>();
                BaseResponse<List<ShopAdverPictrue>> result = new BaseResponse<>();
                try {
                    Call<BaseResponse<List<ShopAdverPictrue>>> baseResponseCall = ServiceManager.createGsonService(ShopService.class).getPictrue();
                    Response<BaseResponse<List<ShopAdverPictrue>>> response = baseResponseCall.execute();
                    if (response != null && response.body() != null) {
                        result.setCode(response.body().getCode());
                        result.setData(response.body().getData());
                        result.setMessage(response.body().getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rxOptional.setResult(result);
                return rxOptional;
            }

            @Override
            protected void onPostExecute(BaseResponse<List<ShopAdverPictrue>> response) {
                super.onPostExecute(response);
                if (mGoodsListView != null) {
                    mGoodsListView.onLoadPictrue(response);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                ViewUtils.showToastError(mActivity.getString(R.string.connect_server_failed));
            }
        }.execute());
    }
}
