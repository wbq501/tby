package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.MyDataPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class MyDataPresenterImpl extends BasePresenterImpl implements MyDataPresenter {

    private MyDataView mMyDataView;

    public MyDataPresenterImpl(Activity activity, MyDataView myDataView) {
        super(activity);
        this.mMyDataView = myDataView;
    }

    @Override
    public void getMyData() {
        addDisposable(new BaseAsyncTask<String, Void, MyDataResult>(mActivity, false) {

            @Override
            protected RxOptional<MyDataResult> doInBackground(String... params) {
                RxOptional<MyDataResult> rxResult = new RxOptional<>();
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse<MyDataResult>> myCall = ServiceManager.createGsonService(UserService.class).getMyData(userId);
                    Response<BaseResponse<MyDataResult>> myCallResponse = myCall.execute();
                    rxResult.setCode(myCallResponse != null && myCallResponse.body() != null ? myCallResponse.body().getCode() : -1);
                    if (myCallResponse != null && myCallResponse.body() != null && BaseResponse.SUCCESS.equals(myCallResponse.body().getStatus())) {
                        rxResult.setResult(myCallResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(MyDataResult result) {
                super.onPostExecute(result);
                if (mMyDataView != null) {
                    mMyDataView.onGetMyData(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mMyDataView != null) {
                    mMyDataView.onGetMyData(null);
                }
            }
        }.execute());
    }
}
