package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.model.PayResult;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.PayPresenter;
import com.micky.logger.Logger;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class PayPresenterImpl extends BasePresenterImpl implements PayPresenter {

    private PayView mPayView;

    public PayPresenterImpl(BaseActivity activity, PayView payView) {
        super(activity);
        this.mPayView = payView;
    }

    @Override
    public void getMyMoney() {
        addDisposable(new BaseAsyncTask<String, Void, Money>(mActivity, false) {

            @Override
            protected RxOptional<Money> doInBackground(String... params) {
                RxOptional<Money> rxResult = new RxOptional<>();
                Money result = null;
                try {
                    User user = UserCache.getInstance().getUser();
                    if (user != null) {
                        Call<BaseResponse<Money>> walletCall = ServiceManager.createGsonService(WalletService.class).getMyMoney(UserCache.getInstance().getUser().getIds());
                        Response<BaseResponse<Money>> walletResponse = walletCall.execute();
                        rxResult.setCode(walletResponse != null && walletResponse.body() != null ? walletResponse.body().getCode() : -1);
                        if (walletResponse != null && walletResponse.body() != null && BaseResponse.SUCCESS.equals(walletResponse.body().getStatus())) {
                            result = walletResponse.body().getData();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Money result) {
                super.onPostExecute(result);
                if (mPayView != null) {
                    mPayView.loadMoney(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mPayView != null) {
                    mPayView.loadMoney(null);
                }
            }
        }.execute());
    }

    @Override
    public void payOrder(String orderId, String orderDate, String payMode) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, false) {

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BaseApplication.getContext().getString(R.string.pay_point))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Call<BaseResponse<String>> orderInfoCall = retrofit.create(ShopService.class).getPayOrder(params[0], params[1], params[2]);
//                    Call<BaseResponse<String>> orderInfoCall = ServiceManager.createGsonService(ShopService.class).getPayOrder(params[0], params[1],params[2]);
                    Response<BaseResponse<String>> orderInfoResponse = orderInfoCall.execute();
//                   Log.i("test",orderInfoResponse.toString());
                    rxResult.setCode(orderInfoResponse != null && orderInfoResponse.body() != null ? orderInfoResponse.body().getCode() : -1);
                    if (orderInfoResponse != null && orderInfoResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(orderInfoResponse.body().getStatus()) && !TextUtils.isEmpty(orderInfoResponse.body().getData())) {
                            PayTask alipay = new PayTask(mActivity);
                            Map<String, String> resultMap = alipay.payV2(orderInfoResponse.body().getData(), true);
                            PayResult payResult = new PayResult(resultMap);
                            String resultStatus = payResult.getResultStatus();
                            // 判断resultStatus 为9000则代表支付成功
                            if (TextUtils.equals(resultStatus, "9000")) {
                                result = "success";
//                                if (!updateOrder(payResult.getResult())) {
//                                    result = mActivity.getString(R.string.failed_update_order_status);
//                                }
                            } else {
                                result = payResult.getMemo();
                            }

                        } else {
                            result = orderInfoResponse.body().getDescription();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                if (TextUtils.isEmpty(result)) {
                    result = mActivity.getString(R.string.failed_pay);
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                boolean success = "success".equals(result);
                if (!success) {
                    ViewUtils.showToastError(result);
                }
                if (mPayView != null) {
                    mPayView.onPayOrder(success);
                }
            }

            @Override
            protected void doOnError() {
                if (mPayView != null) {
                    mPayView.onPayOrder(false);
                }
            }
        }.execute(orderId, orderDate, payMode));
    }

    @Override
    public void payOrderByWallet(String orderNo, String orderDate,String payPwd) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {
            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BaseApplication.getContext().getString(R.string.pay_point))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Call<BaseResponse<String>> orderInfoCall = retrofit.create(ShopService.class).getPayOrderByWallet(params[0], params[1], params[2]);
                    Response<BaseResponse<String>> orderInfoResponse = orderInfoCall.execute();
                    rxResult.setCode(orderInfoResponse != null && orderInfoResponse.body() != null ? orderInfoResponse.body().getCode() : -1);
                    if (orderInfoResponse != null && orderInfoResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(orderInfoResponse.body().getStatus())) {
                            result = "success";
                        } else {
                            result = orderInfoResponse.body().getDescription();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                if (TextUtils.isEmpty(result)) {
                    result = mActivity.getString(R.string.failed_pay);
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if ("success".equals(s)) {
                    mPayView.onPayOrderByWallet(true);
                } else {
                    ViewUtils.showToastError(s);
                    mPayView.onPayOrderByWallet(false);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                mPayView.onPayOrderByWallet(false);
            }
        }.execute(orderNo, orderDate,payPwd));
    }

    @Override
    public void payOrderMerge(String orderNo, String orderDate, String walletAmount, String payMode,String payPwd) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {
            @Override
            protected RxOptional<String> doInBackground(String... strings) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BaseApplication.getContext().getString(R.string.pay_point))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Call<BaseResponse<String>> orderInfoCall = retrofit.create(ShopService.class).getPayOrderMerge(strings[0], strings[1], strings[2],strings[3],strings[4]);
//                    Call<BaseResponse<String>> orderInfoCall = ServiceManager.createGsonService(ShopService.class).getPayOrder(params[0], params[1],params[2]);
                    Response<BaseResponse<String>> orderInfoResponse = orderInfoCall.execute();
                    rxResult.setCode(orderInfoResponse != null && orderInfoResponse.body() != null ? orderInfoResponse.body().getCode() : -1);
                    if (orderInfoResponse != null && orderInfoResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(orderInfoResponse.body().getStatus()) && !TextUtils.isEmpty(orderInfoResponse.body().getData())) {
                            PayTask alipay = new PayTask(mActivity);
                            Map<String, String> resultMap = alipay.payV2(orderInfoResponse.body().getData(), true);
                            PayResult payResult = new PayResult(resultMap);
                            String resultStatus = payResult.getResultStatus();
                            // 判断resultStatus 为9000则代表支付成功
                            if (TextUtils.equals(resultStatus, "9000")) {
                                result = "success";
//                                if (!updateOrder(payResult.getResult())) {
//                                    result = mActivity.getString(R.string.failed_update_order_status);
//                                }
                            } else {
                                result = payResult.getMemo();
                            }

                        } else {
                            result = orderInfoResponse.body().getDescription();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                if (TextUtils.isEmpty(result)) {
                    result = mActivity.getString(R.string.failed_pay);
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                boolean success = "success".equals(s);
                if (!success) {
                    ViewUtils.showToastError(s);
                }
                if (mPayView != null) {
                    mPayView.onPayOrder(success);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
            }
        }.execute(orderNo,orderDate,walletAmount,payMode,payPwd));
    }

    private boolean updateOrder(String alipayResult) {
        boolean result = false;
        try {
            Call<BaseResponse> orderUpdateResponse = ServiceManager.createGsonService(ShopService.class).updateOrderStatus(alipayResult);
            Response<BaseResponse> orderInfoResponse = orderUpdateResponse.execute();
            result = BaseResponse.SUCCESS.equals(orderInfoResponse.body().getStatus());
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return result;
    }
}
