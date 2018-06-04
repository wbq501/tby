package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.ApplywithdrawPresenter;
import com.micky.logger.Logger;

import java.io.IOException;
import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Response;

public class ApplywithdrawPresenterImpl extends BasePresenterImpl implements ApplywithdrawPresenter{

    private Applywithdraw applywithdraw;

    public ApplywithdrawPresenterImpl(Activity activity,Applywithdraw applywithdraw) {
        super(activity);
        this.applywithdraw = applywithdraw;
    }

    @Override
    public void getMyMoney(String pwd, String money, String bankid, final boolean showDialog) {
        addDisposable(new BaseAsyncTask<String,Double,String>(mActivity, showDialog){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (showDialog) {
                    setLoadingText(R.string.loading);
                }
            }

            @Override
            protected RxOptional<String> doInBackground(String... strings) {
                RxOptional<String> result = new RxOptional<>();
                try {
                    Call<BaseResponse> call = ServiceManager.createGsonService(WalletService.class).applywithdraw(strings[0], strings[1], strings[2]);
                    Response<BaseResponse> bankResponse = call.execute();
                    result.setCode(bankResponse != null && bankResponse.body() != null ? bankResponse.body().getCode() : -1);
                    if (bankResponse != null && bankResponse.body() != null && bankResponse.body().getStatus().equals(BaseResponse.SUCCESS)) {
//                        result.setResult(bankResponse.body().getData().toString());
                        result.setResult(mActivity.getString(R.string.withdraw_success));
                    }else {
                        if (bankResponse.body().getCode() < 0){
                            result.setResult(bankResponse.body().getMessage());
                        }else {
                            result.setResult(mActivity.getString(R.string.withdraw_success));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (applywithdraw != null) {
                    applywithdraw.OngetMyMoney(s);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (applywithdraw != null) {
                    applywithdraw.OngetMyMoney(null);
                }
            }
        }.execute(pwd,money,bankid));
    }
}
