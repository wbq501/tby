package com.baigu.dms.presenter.impl;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BankTypePresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class BankTypePresenterImpl extends BasePresenterImpl implements BankTypePresenter {
    private BankTypeView mBankView;

    public BankTypePresenterImpl(BaseActivity activity, BankTypeView bankView) {
        super(activity);
        mBankView = bankView;
    }

    @Override
    public void loadBankList() {
        addDisposable(new BaseAsyncTask<String, Void, List<BankType>>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.loading);
            }

            @Override
            protected RxOptional<List<BankType>> doInBackground(String... params) {
                RxOptional<List<BankType>> rxResult = new RxOptional<>();
                List<BankType> bankList = null;
                try {
//                    Call<BaseResponse<List<BankType>>> bankCall = ServiceManager.createGsonService(UserService.class).getBankTypeList();
//                    Response<BaseResponse<List<BankType>>> bankResponse = bankCall.execute();
//                    rxResult.setCode(bankResponse != null && bankResponse.body() != null ? bankResponse.body().getCode() : -1);
//                    if (bankResponse != null && bankResponse.body() != null && BaseResponse.SUCCESS.equals(bankResponse.body().getStatus())) {
//                        bankList = bankResponse.body().getData();
//                    }
                    bankList = RepositoryFactory.getInstance().getBankRepository().queryAllBank();
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(bankList);
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<BankType> result) {
                super.onPostExecute(result);
                if (mBankView != null) {
                    mBankView.onLoadBankList(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mBankView != null) {
                    mBankView.onLoadBankList(null);
                }
            }
        }.execute());
    }
}
