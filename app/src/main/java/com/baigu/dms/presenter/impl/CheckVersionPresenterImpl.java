package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Agreement;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CheckVersionPresenter;
import com.micky.logger.Logger;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class CheckVersionPresenterImpl extends BasePresenterImpl implements CheckVersionPresenter {

    private CheckVersionView mCheckVersionView;

    public CheckVersionPresenterImpl(Activity activity, CheckVersionView userView) {
        super(activity);
        this.mCheckVersionView = userView;
    }

    @Override
    public void checkVersion(boolean showDialog) {
        addDisposable(new BaseAsyncTask<String, Void, VersionInfo>(mActivity, showDialog) {

            @Override
            protected RxOptional<VersionInfo> doInBackground(String... params) {
                RxOptional<VersionInfo> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<VersionInfo>> versionCall = ServiceManager.createGsonService(UserService.class).checkAppUpdate("android");
                    Response<BaseResponse<VersionInfo>> versionResponse = versionCall.execute();
                    if (versionResponse != null && versionResponse.body() != null && BaseResponse.SUCCESS.equals(versionResponse.body().getStatus())) {
                        rxResult.setResult(versionResponse.body().getData());
                    }
                    rxResult.setCode(versionResponse != null && versionResponse.body() != null ? versionResponse.body().getCode() : -1);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(VersionInfo result) {
                super.onPostExecute(result);
                //todo test
                result = new VersionInfo();
                result.setVersionCode(1);
                if (mCheckVersionView != null) {
                    mCheckVersionView.onCheckVersion(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mCheckVersionView != null) {
                    //todo test
                    VersionInfo result = new VersionInfo();
                    result.setVersionCode(1);
                    mCheckVersionView.onCheckVersion(result);
                }
            }
        }.execute());
    }

    @Override
    public void loadUrl() {
        addDisposable(new BaseAsyncTask<String, Void, BaseResponse<Agreement>>() {
            @Override
            protected RxOptional<BaseResponse<Agreement>> doInBackground(String... strings) {
                RxOptional<BaseResponse<Agreement>> rxOptional = new RxOptional<>();
                BaseResponse<Agreement> result = new BaseResponse<>();
                try {
                    Call<BaseResponse<Agreement>> responseCall = ServiceManager.createGsonService(UserService.class).getUrl();
                    Response<BaseResponse<Agreement>> response = responseCall.execute();
                    if (response != null && response.body() != null) {
                        result.setCode(response.body().getCode());
                        result.setMessage(response.body().getMessage());
                        result.setData(response.body().getData());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rxOptional.setResult(result);
                return rxOptional;
            }

            @Override
            protected void onPostExecute(BaseResponse<Agreement> agreementBaseResponse) {
                super.onPostExecute(agreementBaseResponse);
                if (mCheckVersionView != null) {
                    mCheckVersionView.onLoad(agreementBaseResponse);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                ViewUtils.showToastError(mActivity.getString(R.string.connect_server_failed));
            }
        }.execute());
    }
}
