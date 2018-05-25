package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BrandQuestionPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class BrandQuestionPresenterImpl extends BasePresenterImpl implements BrandQuestionPresenter {

    private BrandQuestionView brandQuestionView;

    public BrandQuestionPresenterImpl(Activity activity, BrandQuestionView brandQuestionView) {
        super(activity);
        this.brandQuestionView = brandQuestionView;
    }


    @Override
    public void getBrandQuestion(int pageNum) {
        addDisposable(new BaseAsyncTask<Integer, Void, PageResult<BrandQuestion>>(mActivity, false) {

            @Override
            protected RxOptional<PageResult<BrandQuestion>> doInBackground(Integer... params) {
                RxOptional<PageResult<BrandQuestion>> rxResult = new RxOptional<>();
                PageResult<BrandQuestion> result = null;
                try {
                    Call<BaseResponse<PageResult<BrandQuestion>>> brandQuestionCall = ServiceManager.createGsonService(UserService.class).getBrandQuestion(params[0]);
                    Response<BaseResponse<PageResult<BrandQuestion>>> brandQuestionResponse = brandQuestionCall.execute();
                    rxResult.setCode(brandQuestionResponse != null && brandQuestionResponse.body() != null ? brandQuestionResponse.body().getCode() : -1);
                    if (brandQuestionResponse != null && brandQuestionResponse.body() != null && BaseResponse.SUCCESS.equals(brandQuestionResponse.body().getStatus())) {
                        result = brandQuestionResponse.body().getData();
                        if (result == null) {
                            result = new PageResult<>();
                            result.firstPage = false;
                            result.lastPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<BrandQuestion> result) {
                super.onPostExecute(result);
                if (brandQuestionView != null) {
                    brandQuestionView.onGetBrandQuestion(result);
                }
            }

            @Override
            protected void doOnError() {
                if (brandQuestionView != null) {
                    brandQuestionView.onGetBrandQuestion(null);
                }
            }
        }.execute(pageNum));
    }

    @Override
    public void getBrandQuestionDetail(final String id) {
        addDisposable(new BaseAsyncTask<String, Void, BrandQuestion>(mActivity, true) {

            @Override
            protected RxOptional<BrandQuestion> doInBackground(String... params) {
                RxOptional<BrandQuestion> rxResult = new RxOptional<>();
                BrandQuestion result = null;
                try {
                    Call<BaseResponse<BrandQuestion>> brandQuestionCall = ServiceManager.createGsonService(UserService.class).getBrandQuestionDetail(params[0]);
                    Response<BaseResponse<BrandQuestion>> brandQuestionResponse = brandQuestionCall.execute();
                    rxResult.setCode(brandQuestionResponse != null && brandQuestionResponse.body() != null ? brandQuestionResponse.body().getCode() : -1);
                    if (brandQuestionResponse != null && brandQuestionResponse.body() != null && BaseResponse.SUCCESS.equals(brandQuestionResponse.body().getStatus())) {
                        result = brandQuestionResponse.body().getData();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(BrandQuestion result) {
                super.onPostExecute(result);
                if (brandQuestionView != null) {
                    brandQuestionView.onGetBrandQuestionDetail(result);
                }
            }

            @Override
            protected void doOnError() {
                if (brandQuestionView != null) {
                    brandQuestionView.onGetBrandQuestionDetail(null);
                }
            }
        }.execute(id));
    }
}
