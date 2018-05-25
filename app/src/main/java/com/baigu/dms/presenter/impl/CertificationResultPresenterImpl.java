package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.CertificationResult;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CertificationResultPresenter;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class CertificationResultPresenterImpl extends BasePresenterImpl implements CertificationResultPresenter {

    private CertificationResultView mView;

    public CertificationResultPresenterImpl(Activity activity, CertificationResultView mView) {
        super(activity);
        this.mView = mView;
    }

    @Override
    public void loadCertificationResult(String userId) {
        addDisposable(new BaseAsyncTask<String, Void, BaseResponse<CertificationResult>>(mActivity, true) {
            @Override
            protected RxOptional<BaseResponse<CertificationResult>> doInBackground(String... strings) {
                RxOptional<BaseResponse<CertificationResult>> rxOptional = new RxOptional<>();
                BaseResponse<CertificationResult> result = new BaseResponse();
                try {
                    Call<BaseResponse<CertificationResult>> certificationResult = ServiceManager.createGsonService(WalletService.class).getCertificationResult(strings[0]);
                    Response<BaseResponse<CertificationResult>> response = certificationResult.execute();
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
            protected void onPostExecute(BaseResponse<CertificationResult> baseResponse) {
                super.onPostExecute(baseResponse);
                if (mView != null) {
                    mView.loadResult(baseResponse);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                ViewUtils.showToastError(mActivity.getString(R.string.connect_failuer_toast));
            }
        }.execute(userId));
    }
}
