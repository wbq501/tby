package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BankPresenter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class BankPresenterImpl extends BasePresenterImpl implements BankPresenter {

    AddBankView addBankView;

    public BankPresenterImpl(Activity activity, AddBankView addBankView) {
        super(activity);
        this.addBankView = addBankView;
    }

    @Override
    public void addBank(final Bank bank) {
        addDisposable(new BaseAsyncTask<String, Void, Boolean>() {

            @Override
            protected RxOptional<Boolean> doInBackground(String... strings) {
                RxOptional<Boolean> rxResult = new RxOptional<>();
                try {
                    //银卡信息
                    Gson gson = new Gson();
                    String bankJson= gson.toJson(bank);
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), bankJson);
                    Call<BaseResponse> responseCall = ServiceManager.createGsonService(WalletService.class).addBank(body);
                    Response<BaseResponse> response = responseCall.execute();
                    if (response != null && response.body() != null && response.body().getStatus().equals(BaseResponse.SUCCESS)) {
                        rxResult.setResult(true);
                    } else {
                        rxResult.setResult(false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return rxResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if(addBankView!=null){
                    addBankView.addBank(result);
                }

            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if(addBankView!=null){
                    addBankView.addBank(false);
                }

            }
        }.execute());
    }
}
