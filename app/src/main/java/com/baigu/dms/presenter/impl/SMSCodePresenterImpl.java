package com.baigu.dms.presenter.impl;

import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.SMSCodePresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class SMSCodePresenterImpl extends BasePresenterImpl implements SMSCodePresenter {

    private SMSCodeView mMsgCodeView;

    public SMSCodePresenterImpl(BaseActivity activity, SMSCodeView msgCodeView) {
        super(activity);
        mMsgCodeView = msgCodeView;
    }

    @Override
    public void sendSMSCode(String type, final String phone) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.code_sending);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.failed_send_sms_code);
                try {
                    Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).sendSMSCode(params[0], params[1]);
                    Response<BaseResponse> sendSmsCodeResponse = call.execute();
                    if (sendSmsCodeResponse != null && sendSmsCodeResponse.body() != null) {
                        if (sendSmsCodeResponse.body().getCode()>0) {
                            result = "success";
                        }else{
                            result=sendSmsCodeResponse.body().getMessage();
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
                if (success) {
                    ViewUtils.showToastSuccess(R.string.success_send_sms);
                } else {
                    ViewUtils.showToastError(result);
                }
                if (mMsgCodeView != null) {
                    mMsgCodeView.onSendSMSCode(success, phone);
                }
            }

            @Override
            protected void doOnError() {
                ViewUtils.showToastError(mActivity.getString(R.string.failed_send_sms_code));
                if (mMsgCodeView != null) {
                    mMsgCodeView.onSendSMSCode(false, "");
                }
            }
        }.execute(type, phone));
    }
}
