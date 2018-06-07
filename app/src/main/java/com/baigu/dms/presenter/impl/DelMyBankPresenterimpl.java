package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.DelMyBankPresenter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class DelMyBankPresenterimpl extends BasePresenterImpl implements DelMyBankPresenter{

    private DelBankView delBankView;

    public DelMyBankPresenterimpl(Activity activity,DelBankView delBankView) {
        super(activity);
        this.delBankView = delBankView;
    }

    @Override
    public void delBank(String bankId) {
        addDisposable(new BaseAsyncTask<String,Void,String>(mActivity,true){

            @Override
            protected RxOptional<String> doInBackground(String... parms) {
                RxOptional<String> rxOptional = new RxOptional<>();
                String result = mActivity.getString(R.string.login_failed);
                try {
                    Call<BaseResponse<String>> baseResponseCall = ServiceManager.createGsonService(WalletService.class).deleteMyBank(parms[0]);
                    Response<BaseResponse<String>> responseResponse = baseResponseCall.execute();
                    if (responseResponse.body().getCode() == 0){
                        result = responseResponse.body().getData();
                    }else {
                        result = "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rxOptional.setResult(result);
                return rxOptional;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (delBankView != null){
                    delBankView.delBank(s);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (delBankView != null){
                    delBankView.delBank(null);
                }
            }
        }.execute(bankId));
    }
}
