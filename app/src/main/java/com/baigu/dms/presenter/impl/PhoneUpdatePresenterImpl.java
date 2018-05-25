package com.baigu.dms.presenter.impl;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.PhoneUpdatePresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class PhoneUpdatePresenterImpl extends BasePresenterImpl implements PhoneUpdatePresenter {

    private PhoneUpdateView mPhoneUpdateView;

    public PhoneUpdatePresenterImpl(BaseActivity activity, PhoneUpdateView phoneUpdateView) {
        super(activity);
        this.mPhoneUpdateView = phoneUpdateView;
    }

    @Override
    public void checkOldPhone(String phone, String code) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.failed_check);
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).chkOldPhone(userId, params[0], params[1]);
                    Response<BaseResponse> checkPhoneResponse = call.execute();
                    rxResult.setCode(checkPhoneResponse != null && checkPhoneResponse.body() != null ? checkPhoneResponse.body().getCode() : -1);
                    if (checkPhoneResponse != null && checkPhoneResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(checkPhoneResponse.body().getStatus())) {
                            result = "success";
                        } else if (10006 == checkPhoneResponse.body().getCode()) {
                            result = mActivity.getString(R.string.phone_not_match);
                        } else if (10005 == checkPhoneResponse.body().getCode()) {
                            result = mActivity.getString(R.string.invalid_sms_code);
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                boolean success = "success".equals(result);
                if (!success) {
                    ViewUtils.showToastError(result);
                }
                if (mPhoneUpdateView != null) {
                    mPhoneUpdateView.onCheckPhone(success);
                }
            }

            @Override
            protected void doOnError() {
                ViewUtils.showToastError(mActivity.getString(R.string.failed_send_sms_code));
                if (mPhoneUpdateView != null) {
                    mPhoneUpdateView.onCheckPhone(false);
                }
            }
        }.execute(phone, code));
    }

    @Override
    public void updatePhone(String phone, String code) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.failed_update_phone);
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).updatePhone(userId, params[0], params[1]);
                    Response<BaseResponse> updatePhoneResponse = call.execute();
                    rxResult.setCode(updatePhoneResponse != null && updatePhoneResponse.body() != null ? updatePhoneResponse.body().getCode() : -1);
                    if (updatePhoneResponse != null && updatePhoneResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(updatePhoneResponse.body().getStatus())) {
                            result = "success";
                        } else if (10005 == updatePhoneResponse.body().getCode()) {
                            result = mActivity.getString(R.string.invalid_sms_code);
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                boolean success = "success".equals(result);
                if (!success) {
                    ViewUtils.showToastError(result);
                }
                if (mPhoneUpdateView != null) {
                    mPhoneUpdateView.onUpdatePhone(success);
                }
            }

            @Override
            protected void doOnError() {
                ViewUtils.showToastError(mActivity.getString(R.string.failed_send_sms_code));
                if (mPhoneUpdateView != null) {
                    mPhoneUpdateView.onUpdatePhone(false);
                }
            }
        }.execute(phone, code));
    }
}
