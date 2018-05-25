package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RSAEncryptor;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.RegisterPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class RegisterPresenterImpl extends BasePresenterImpl implements RegisterPresenter {
    private RegisterView mRegisterView;

    public RegisterPresenterImpl(BaseActivity activity, RegisterView registerView) {
        super(activity);
        mRegisterView = registerView;
    }

    @Override
    public void getUserByInviteCode(String inviteCode) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = null;
                try {
                    Call<BaseResponse<String>> userCall = ServiceManager.createGsonService(UserService.class).getByUserInviteCode(params[0]);
                    Response<BaseResponse<String>> userCallResponse = userCall.execute();
                    if (userCallResponse != null && userCallResponse.body() != null && userCallResponse.body().getCode()>0) {
                        result = userCallResponse.body().getData();
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
                if (mRegisterView != null) {
                    mRegisterView.onGetUserByInviteCode(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mRegisterView != null) {
                    mRegisterView.onGetUserByInviteCode(null);
                }
            }
        }.execute(inviteCode));
    }


    @Override
    public void register(String phone,
                         String realname,
                         String pwd,
                         String code,
                         String inviteCode,
//                         String banktype,
//                         String banknumber,
//                         String alipayaccount,
                         String wxaccount) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.failed_register_failed);
                try {
                    Call<BaseResponse<String>> keyCall = ServiceManager.createGsonService(UserService.class).getPublicKey(params[0], User.RSAKeyType.REGISTER);
                    Response<BaseResponse<String>> keyResponse = keyCall.execute();
                    if (keyResponse != null && keyResponse.body() != null && keyResponse.body().getCode()>0 && !TextUtils.isEmpty(keyResponse.body().getData())) {
                        RSAEncryptor encryptor = new RSAEncryptor();
                        encryptor.loadPublicKey(keyResponse.body().getData());
                        String pwdEncrypt = encryptor.encryptWithBase64(params[2]);
                        Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).register(params[0],
                                params[1],
                                pwdEncrypt,
                                params[3],
                                params[4],
                                params[5]
//                                params[6],
//                                params[7],
//                                params[8]
                                );
                        Response<BaseResponse> sendSmsCodeResponse = call.execute();
                        Log.i("test",sendSmsCodeResponse.body().toString());
                        if (sendSmsCodeResponse != null && sendSmsCodeResponse.body() != null) {

                            if (sendSmsCodeResponse.body().getCode()>0) {
                                result = "success";
                            } else {
                                result = sendSmsCodeResponse.body().getMessage();
                            }
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
                    ViewUtils.showToastSuccess(R.string.success_registed);
                } else {
                    ViewUtils.showToastError(result);
                }
                if (mRegisterView != null) {
                    mRegisterView.onRegister(success);
                }
            }

            @Override
            protected void doOnError() {
                if (mRegisterView != null) {
                    mRegisterView.onRegister(false);
                }
            }
        }.execute(phone, realname, pwd, code, inviteCode,
//                banktype, banknumber, alipayaccount,
                wxaccount));
    }


}
