package com.baigu.dms.presenter.impl;

import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.OrderPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class OrderPresenterImpl extends BasePresenterImpl implements OrderPresenter {
    private OrderView mOrderView;

    public OrderPresenterImpl(BaseActivity activity, OrderView orderAddView) {
        super(activity);
        this.mOrderView = orderAddView;
    }

    @Override
    public void loadOrderList(int status, int pageNum, boolean showDialog) {
        String userId = UserCache.getInstance().getUser().getIds();
        switch (status){
            case  Order.Status.ALL:
                status=-1;
                break;
            case  Order.Status.UNPAY:
                status=0;
                break;
            case  Order.Status.UNDELIVER:
                status=10;
                break;
            case  Order.Status.DELIVERED:
                status=20;
                break;
                default:
                    status=-1;
                    break;
        }
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Order>>(mActivity, showDialog) {

            @Override
            protected RxOptional<PageResult<Order>> doInBackground(String... params) {
                RxOptional<PageResult<Order>> rxResult = new RxOptional<>();
                PageResult<Order> pageResult = null;
                try {
                    Call<BaseResponse<PageResult<Order>>> orderListCall;
                    if(Integer.valueOf(params[1])>=0){
                        orderListCall = ServiceManager.createGsonService(ShopService.class).getOrderList(params[0], params[1], params[2]);
                    }else{
                       orderListCall = ServiceManager.createGsonService(ShopService.class).getOrderListAll(params[0], params[2]);
                    }

                    Response<BaseResponse<PageResult<Order>>> orderListResultResponse = orderListCall.execute();
                    rxResult.setCode(orderListResultResponse != null && orderListResultResponse.body() != null ? orderListResultResponse.body().getCode() : -1);
                    if (orderListResultResponse != null && orderListResultResponse.body() != null && BaseResponse.SUCCESS.equals(orderListResultResponse.body().getStatus())) {
                        if (orderListResultResponse.body().getCode() == 10004) {
                            pageResult =  new PageResult<>();
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
                    mOrderView.onLoadOrderList(goodsPageResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderView != null) {
                    mOrderView.onLoadOrderList(null);
                }
            }
        }.execute(userId, String.valueOf(status), String.valueOf(pageNum)));
    }

    @Override
    public void refundOrder(String orderId, String orderDate,String refundReason) {
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
                    Call<BaseResponse> refundOrder = ServiceManager.createGsonService(ShopService.class).refundOrder(params[0], params[1],params[2]);
                    Response<BaseResponse> refundOrderResponse = refundOrder.execute();
                    rxResult.setCode(refundOrderResponse != null && refundOrderResponse.body() != null ? refundOrderResponse.body().getCode() : -1);
                    if (refundOrderResponse != null && refundOrderResponse.body() != null) {
                        result =  BaseResponse.SUCCESS.equals(refundOrderResponse.body().getStatus());
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
                if (mOrderView != null) {
                    mOrderView.onRefundOrder(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderView != null) {
                    mOrderView.onRefundOrder(false);
                }
            }

        }.execute(orderId, orderDate,refundReason));
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
//                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse> cancelOrderCall = ServiceManager.createGsonService(ShopService.class).cancelOrder(params[0], params[1]);
                    Response<BaseResponse> cancelOrderResponse = cancelOrderCall.execute();
                    rxResult.setCode(cancelOrderResponse != null && cancelOrderResponse.body() != null ? cancelOrderResponse.body().getCode() : -1);
                    if (cancelOrderResponse != null && cancelOrderResponse.body() != null) {
                        result =  BaseResponse.SUCCESS.equals(cancelOrderResponse.body().getStatus());
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
                if (mOrderView != null) {
                    mOrderView.onCancelOrder(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderView != null) {
                    mOrderView.onCancelOrder(false);
                }
            }

        }.execute(orderId, orderDate));
    }
}
