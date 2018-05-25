package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.InvitationPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class InvitationPresenterImpl extends BasePresenterImpl implements InvitationPresenter {
    private InvitaionView mInvitaionView;

    public InvitationPresenterImpl(Activity activity, InvitaionView invitaionView) {
        super(activity);
        mInvitaionView = invitaionView;
    }

    @Override
    public void loadInvitationVerified(int pageNum, boolean showDialog) {
        addDisposable(new BaseAsyncTask<Integer, Void, PageResult<User>>(mActivity, showDialog) {

            @Override
            protected RxOptional<PageResult<User>> doInBackground(Integer... params) {
                RxOptional<PageResult<User>> rxResult = new RxOptional<>();
                PageResult result = null;
                try {
                    User user = UserCache.getInstance().getUser();
                    Call<BaseResponse<PageResult<User>>> invitationCall = ServiceManager.createGsonService(UserService.class).getInvitationVerified(user.getIds(), String.valueOf(params[0]),1);
                    Response<BaseResponse<PageResult<User>>> invitaionResponse = invitationCall.execute();
                    rxResult.setCode(invitaionResponse != null && invitaionResponse.body() != null ? invitaionResponse.body().getCode() : -1);
                    if (invitaionResponse != null && invitaionResponse.body() != null && BaseResponse.SUCCESS.equals(invitaionResponse.body().getStatus())) {
                        result = invitaionResponse.body().getData();
                        if (result == null) {
                            result = new PageResult();
                            result.lastPage = false;
                            result.firstPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<User> result) {
                super.onPostExecute(result);
                if (mInvitaionView != null) {
                    mInvitaionView.onLoadInvitationVerified(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mInvitaionView != null) {
                    mInvitaionView.onLoadInvitationVerified(null);
                }
            }
        }.execute(pageNum));
    }

    @Override
    public void loadInvitationUnVerify(int pageNum, boolean showDialog) {
        addDisposable(new BaseAsyncTask<Integer, Void, PageResult<User>>(mActivity, showDialog) {

            @Override
            protected RxOptional<PageResult<User>> doInBackground(Integer... params) {
                RxOptional<PageResult<User>> rxResult = new RxOptional<>();
                PageResult result = null;
                try {
                    User user = UserCache.getInstance().getUser();
                    Call<BaseResponse<PageResult<User>>> invitationCall = ServiceManager.createGsonService(UserService.class).getInvitationUnVerify(user.getIds(), String.valueOf(params[0]),0);
                    Response<BaseResponse<PageResult<User>>> invitaionResponse = invitationCall.execute();
                    rxResult.setCode(invitaionResponse != null && invitaionResponse.body() != null ? invitaionResponse.body().getCode() : -1);
                    if (invitaionResponse != null && invitaionResponse.body() != null && BaseResponse.SUCCESS.equals(invitaionResponse.body().getStatus())) {
                        result = invitaionResponse.body().getData();
                        if (result == null) {
                            result = new PageResult();
                            result.lastPage = false;
                            result.firstPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<User> result) {
                super.onPostExecute(result);
                if (mInvitaionView != null) {
                    mInvitaionView.onLoadInvitationVerified(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mInvitaionView != null) {
                    mInvitaionView.onLoadInvitationVerified(null);
                }
            }
        }.execute(pageNum));
    }

    @Override
    public void verify(String requestUserId) {
        addDisposable(new BaseAsyncTask<String, Void, Boolean>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<Boolean> doInBackground(String... params) {
                RxOptional<Boolean> rxResult = new RxOptional<>();
                boolean result = false;
                try {
                    User user = UserCache.getInstance().getUser();
                    Call<BaseResponse> invitationCall = ServiceManager.createGsonService(UserService.class).invitationVerify(user.getIds(), String.valueOf(params[0]));
                    Response<BaseResponse> invitaionResponse = invitationCall.execute();
                    rxResult.setCode(invitaionResponse != null && invitaionResponse.body() != null ? invitaionResponse.body().getCode() : -1);
                    if (invitaionResponse != null && invitaionResponse.body() != null) {
                        result = BaseResponse.SUCCESS.equals(invitaionResponse.body().getStatus());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (mInvitaionView != null) {
                    mInvitaionView.onVerify(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mInvitaionView != null) {
                    mInvitaionView.onVerify(false);
                }
            }
        }.execute(requestUserId));
    }
}
