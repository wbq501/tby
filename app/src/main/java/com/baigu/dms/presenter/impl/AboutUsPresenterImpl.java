package com.baigu.dms.presenter.impl;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.AboutUsPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class AboutUsPresenterImpl extends BasePresenterImpl implements AboutUsPresenter {

    private AboutUsView mAboutView;

    public AboutUsPresenterImpl(BaseActivity activity, AboutUsView aboutUsView) {
        super(activity);
        this.mAboutView = aboutUsView;
    }

    @Override
    public void getAboutUs() {
        addDisposable(new BaseAsyncTask<Void, Void, String>(mActivity, true) {

            @Override
            protected RxOptional<String> doInBackground(Void... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                try {
                    Call<BaseResponse<String>> aboutUsCall = ServiceManager.createGsonService(UserService.class).getAboutUs();
                    Response<BaseResponse<String>> aboutUsResponse = aboutUsCall.execute();
                    if (aboutUsResponse != null && aboutUsResponse.body() != null && BaseResponse.SUCCESS.equals(aboutUsResponse.body().getStatus())) {
                        rxResult.setResult(aboutUsResponse.body().getData());
                    }
                    rxResult.setCode(aboutUsResponse != null && aboutUsResponse.body() != null ? aboutUsResponse.body().getCode() : -1);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (mAboutView != null) {
                    mAboutView.onGetAboutUs(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mAboutView != null) {
                    mAboutView.onGetAboutUs("");
                }
            }
        }.execute());
    }
}
