package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.WithdrawHistory;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.WithdrawHistoryPresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class WithdrewHistoryPresenterImpl extends BasePresenterImpl implements WithdrawHistoryPresenter{

    private WithdrawHistoryView withdrawHistoryView;

    public WithdrewHistoryPresenterImpl(Activity activity, WithdrawHistoryView withdrawHistoryView) {
        super(activity);
        this.withdrawHistoryView = withdrawHistoryView;
    }

    @Override
    public void getHistory(String menberId, final int page, boolean showDialog, final boolean isWithdrawHistory) {
        addDisposable(new BaseAsyncTask<String, Void, List<WithdrawHistory>>(mActivity, showDialog) {

            @Override
            protected RxOptional<List<WithdrawHistory>> doInBackground(String... params) {
                RxOptional<List<WithdrawHistory>> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<List<WithdrawHistory>>> walletCall = null;
                    if(isWithdrawHistory){
                        walletCall = ServiceManager.createGsonService(WalletService.class).withdrawRecord(params[0],page);
                    }else {
                        walletCall = ServiceManager.createGsonService(WalletService.class).detail(params[0],page);
                    }
                    Response<BaseResponse<List<WithdrawHistory>>> walletResponse = walletCall.execute();
                    rxResult.setCode(walletResponse != null && walletResponse.body() != null ? walletResponse.body().getCode() : -1);
                    if (walletResponse != null && walletResponse.body() != null && BaseResponse.SUCCESS.equals(walletResponse.body().getStatus())) {
                        rxResult.setResult(walletResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<WithdrawHistory> result) {
                super.onPostExecute(result);
                if (withdrawHistoryView != null) {
                    withdrawHistoryView.getHistory(result);
                }
            }

            @Override
            protected void doOnError() {
                if (withdrawHistoryView != null) {
                    withdrawHistoryView.getHistory(null);
                }
            }
        }.execute(menberId));
    }
}
