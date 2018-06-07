package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Coupon;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CouponPresenter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CouponPresenterimpl extends BasePresenterImpl implements CouponPresenter{

    private CouponView couponView;

    public CouponPresenterimpl(Activity activity,CouponView couponView) {
        super(activity);
        this.couponView = couponView;
    }

    @Override
    public void getCouponList(final int pageNum, final int state) {
        addDisposable(new BaseAsyncTask<String,Void,Coupon>(mActivity,true){

            @Override
            protected RxOptional<Coupon> doInBackground(String... strings) {
                RxOptional<Coupon> result = new RxOptional<>();
                try {
                    Call<BaseResponse<Coupon>> couponList = ServiceManager.createGsonService(WalletService.class).getCouponList(state, pageNum);
                    Response<BaseResponse<Coupon>> execute = couponList.execute();
                    if (execute.body() != null && execute.body().getData() != null && execute.body().getStatus().equals(BaseResponse.SUCCESS)){
                        result.setResult(execute.body().getData());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Coupon list) {
                super.onPostExecute(list);
                if (couponView != null){
                    couponView.CouponList(list);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (couponView != null){
                    couponView.CouponList(null);
                }
            }
        }.execute());
    }
}
