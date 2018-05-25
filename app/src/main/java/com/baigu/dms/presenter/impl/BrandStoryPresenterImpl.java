package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BrandStoryPresenter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author lxy
 * @Email 644605843@qq.com
 * @Date
 */
public class BrandStoryPresenterImpl extends BasePresenterImpl implements BrandStoryPresenter {

    private BrandStoryView mView;

    public BrandStoryPresenterImpl(Activity activity, BrandStoryView mView) {
        super(activity);
        this.mView = mView;
    }

    @Override
    public void loadList(String pageNum, boolean isLoading) {
        addDisposable(new BaseAsyncTask<String, Void, BaseResponse<PageResult<BrandStory>>>(mActivity, isLoading) {
            @Override
            protected RxOptional<BaseResponse<PageResult<BrandStory>>> doInBackground(String... strings) {
                RxOptional<BaseResponse<PageResult<BrandStory>>> rxOptional = new RxOptional<>();
                BaseResponse<PageResult<BrandStory>> result = new BaseResponse<>();
                try {
                    Call<BaseResponse<PageResult<BrandStory>>> baseResponseCall = ServiceManager.createGsonService(BrandStoryService.class).getBrandStoryAll(strings[0]);
                    Response<BaseResponse<PageResult<BrandStory>>> response = baseResponseCall.execute();
                    if (response != null &&response.body() != null){
                            result.setCode(response.body().getCode());
                            result.setData(response.body().getData());
                            result.setMessage(response.body().getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rxOptional.setResult(result);
                return rxOptional;
            }

            @Override
            protected void onPostExecute(BaseResponse<PageResult<BrandStory>> listBaseResponse) {
                super.onPostExecute(listBaseResponse);
                if (mView != null) {
                    mView.onLoad(listBaseResponse);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                ViewUtils.showToastError(R.string.connect_server_failed);
            }
        }.execute(pageNum));
    }
}
