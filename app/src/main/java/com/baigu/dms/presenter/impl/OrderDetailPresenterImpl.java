package com.baigu.dms.presenter.impl;

import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.presenter.OrderDetailPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/9 13:30
 */
public class OrderDetailPresenterImpl extends BasePresenterImpl implements OrderDetailPresenter {

    private OrderDetailView mOrderDetailView;

    public OrderDetailPresenterImpl(BaseActivity activity, OrderDetailView orderDetailView) {
        super(activity);
        mOrderDetailView = orderDetailView;
    }

    @Override
    public void loadOrderDetail(String orderId, String orderDate) {
        addDisposable(new BaseAsyncTask<String, Void, OrderDetailResult>(mActivity, true) {

            @Override
            protected RxOptional<OrderDetailResult> doInBackground(String... params) {
                RxOptional<OrderDetailResult> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<OrderDetailResult>> orderDetailCall = ServiceManager.createGsonService(ShopService.class).getOrderById(params[0], params[1]);
                    Response<BaseResponse<OrderDetailResult>> orderDetailResponse = orderDetailCall.execute();
                    rxResult.setCode(orderDetailResponse != null && orderDetailResponse.body() != null ? orderDetailResponse.body().getCode() : -1);
                    if (orderDetailResponse != null && orderDetailResponse.body() != null && BaseResponse.SUCCESS.equals(orderDetailResponse.body().getStatus())) {
                        rxResult.setResult(orderDetailResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(OrderDetailResult orderDetailResult) {
                super.onPostExecute(orderDetailResult);
                if (mOrderDetailView != null) {
                    mOrderDetailView.onLoadOrderDetail(orderDetailResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderDetailView != null) {
                    mOrderDetailView.onLoadOrderDetail(null);
                }
            }

        }.execute(orderId, orderDate));
    }

    @Override
    public void refundOrder(String orderId, String orderDate) {
        addDisposable(new BaseAsyncTask<String, Void, Boolean>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<Boolean> doInBackground(String... params) {
                RxOptional<Boolean> rxResult = new RxOptional<>();
                boolean result = false;
                try {
                    Call<BaseResponse> refundOrder = ServiceManager.createGsonService(ShopService.class).refundOrder(params[0], params[1],"");
                    Response<BaseResponse> refundOrderResponse = refundOrder.execute();
                    rxResult.setCode(refundOrderResponse != null && refundOrderResponse.body() != null ? refundOrderResponse.body().getCode() : -1);
                    if (refundOrderResponse != null && refundOrderResponse.body() != null) {
                        result = BaseResponse.SUCCESS.equals(refundOrderResponse.body().getStatus());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (mOrderDetailView != null) {
                    mOrderDetailView.onRefundOrder(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderDetailView != null) {
                    mOrderDetailView.onRefundOrder(false);
                }
            }

        }.execute(orderId, orderDate));
    }


    @Override
    public void cancelOrder(String orderId, String orderDate) {
        addDisposable(new BaseAsyncTask<String, Void, Boolean>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<Boolean> doInBackground(String... params) {
                RxOptional<Boolean> rxResult = new RxOptional<>();
                boolean result = false;
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse> cancelOrderCall = ServiceManager.createGsonService(ShopService.class).cancelOrder(params[0], params[1]);
                    Response<BaseResponse> cancelOrderResponse = cancelOrderCall.execute();
                    rxResult.setCode(cancelOrderResponse != null && cancelOrderResponse.body() != null ? cancelOrderResponse.body().getCode() : -1);
                    if (cancelOrderResponse != null && cancelOrderResponse.body() != null) {
                        result = BaseResponse.SUCCESS.equals(cancelOrderResponse.body().getStatus());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (mOrderDetailView != null) {
                    mOrderDetailView.onCancelOrder(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderDetailView != null) {
                    mOrderDetailView.onCancelOrder(false);
                }
            }

        }.execute(orderId, orderDate));
    }


    @Override
    public void queryLogistics(String orderId, String orderDate) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    Call<BaseResponse<String>> call = ServiceManager.createGsonService(ShopService.class).queryLogistics(params[0], params[1], UserCache.getInstance().getUser().getIds());
                    Response<BaseResponse<String>> response = call.execute();
                    rxResult.setCode(response != null && response.body() != null ? response.body().getCode() : -1);
                    if (response != null && response.body() != null && response.body().getData() != null) {
                        result = response.body().getData();
                    } else if (response.body().getMessage() != null) {
                        result = response.body().getMessage();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (mOrderDetailView != null) {
                    mOrderDetailView.onQueryLogistics(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderDetailView != null) {
                    mOrderDetailView.onQueryLogistics("");
                }
            }

        }.execute(orderId, orderDate));
    }
}
