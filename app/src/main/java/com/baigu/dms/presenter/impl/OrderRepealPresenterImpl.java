package com.baigu.dms.presenter.impl;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.OrderPresenter;
import com.baigu.dms.presenter.OrderRepealPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class OrderRepealPresenterImpl extends BasePresenterImpl implements OrderRepealPresenter {
    private OrderRepealView mOrderView;

    public OrderRepealPresenterImpl(BaseActivity activity, OrderRepealView orderAddView) {
        super(activity);
        this.mOrderView = orderAddView;
    }

    @Override
    public void loadOrderList(int status, int pageNum, boolean showDialog) {
        String userId = UserCache.getInstance().getUser().getIds();
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Order>>(mActivity, showDialog) {

            @Override
            protected RxOptional<PageResult<Order>> doInBackground(String... params) {
                RxOptional<PageResult<Order>> rxResult = new RxOptional<>();
                PageResult<Order> pageResult = null;

                try {
                    Call<BaseResponse<PageResult<Order>>> orderListCall;
                    orderListCall = ServiceManager.createGsonService(ShopService.class).getOrderList(params[0], params[1], params[2]);
                    Response<BaseResponse<PageResult<Order>>> orderListResultResponse = orderListCall.execute();
                    rxResult.setCode(orderListResultResponse != null && orderListResultResponse.body() != null ? orderListResultResponse.body().getCode() : -1);
                    if (orderListResultResponse != null && orderListResultResponse.body() != null && BaseResponse.SUCCESS.equals(orderListResultResponse.body().getStatus())) {
                        if (orderListResultResponse.body().getCode() == 10004) {
                            pageResult = new PageResult<>();
                            pageResult.firstPage = true;
                            pageResult.list = null;
                        } else {
                            pageResult = orderListResultResponse.body().getData();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(pageResult);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<Order> goodsPageResult) {
                super.onPostExecute(goodsPageResult);
                if (mOrderView != null) {
                    mOrderView.loadOrderList(goodsPageResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderView != null) {
                    mOrderView.loadOrderList(null);
                }
            }
        }.execute(userId, String.valueOf(status), String.valueOf(pageNum)));
    }


}
