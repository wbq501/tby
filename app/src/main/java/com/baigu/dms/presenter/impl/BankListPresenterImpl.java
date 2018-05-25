package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BankListPresenter;
import com.micky.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/11/22.
 */

public class BankListPresenterImpl extends BasePresenterImpl implements BankListPresenter {

    BankListView listView;

    public BankListPresenterImpl(Activity activity, BankListView listView) {
        super(activity);
        this.listView = listView;
    }


    @Override
    public void loadBankList() {
        addDisposable(new BaseAsyncTask<String, Void, List<Bank>>() {
            @Override
            protected RxOptional<List<Bank>> doInBackground(String... params) {
                RxOptional<List<Bank>> result = new RxOptional<>();
                try {
                    Call<BaseResponse<List<Bank>>> call = ServiceManager.createGsonService(WalletService.class).getBankList(UserCache.getInstance().getUser().getIds());
                    Response<BaseResponse<List<Bank>>> bankResponse = call.execute();
                    result.setCode(bankResponse != null && bankResponse.body() != null ? bankResponse.body().getCode() : -1);
                    if (bankResponse != null && bankResponse.body() != null && bankResponse.body().getStatus().equals(BaseResponse.SUCCESS)) {
                        result.setResult(bankResponse.body().getData());

                    }
                } catch (IOException e) {
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<Bank> banks) {
                super.onPostExecute(banks);
                if (listView != null) {
                    if (banks == null) {
                        listView.loadBankList(new ArrayList<Bank>());
                    } else {
                        listView.loadBankList(banks);
                    }

                }


            }

            @Override
            protected void doOnError() {
                super.doOnError();
                listView.loadBankList(null);
            }
        }.execute());
    }


}
