package com.baigu.dms.presenter.impl;

import android.app.Activity;
import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RSAEncryptor;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.ChangePayPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

public class ChangePayPresenterImpl extends BasePresenterImpl implements ChangePayPresenter{

    private ChangePayView changePayView;

    public ChangePayPresenterImpl(Activity activity,ChangePayView changePayView) {
        super(activity);
        this.changePayView = changePayView;
    }

    @Override
    public void resetpay(String phone, String code, String pwd, String idCard) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity,true){

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    Call<BaseResponse<String>> keyCall = ServiceManager.createGsonService(UserService.class).getPublicKey(params[0], User.RSAKeyType.UPD_PAY_PWD);
                    Response<BaseResponse<String>> keyResponse = keyCall.execute();
                    if (keyResponse != null && keyResponse.body() != null && BaseResponse.SUCCESS.equals(keyResponse.body().getStatus()) && !TextUtils.isEmpty(keyResponse.body().getData())) {
                        RSAEncryptor encryptor = new RSAEncryptor();
                        encryptor.loadPublicKey(keyResponse.body().getData());
                        String pwdEncrypt = encryptor.encryptWithBase64(params[2]);
                        Call<BaseResponse> updateCall = ServiceManager.createGsonService(UserService.class).resetPayPwd(params[0], pwdEncrypt, params[1],params[3]);
                        Response<BaseResponse> updateResponse = updateCall.execute();
                        if (updateResponse != null && updateResponse.body() != null) {
                            if (BaseResponse.SUCCESS.equals(updateResponse.body().getStatus())) {
                                result = mActivity.getString(R.string.change_success);
                            } else if (-10004 == updateResponse.body().getCode()) {
                                result = mActivity.getString(R.string.invalid_sms_code);
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                if (TextUtils.isEmpty(result)) {
                    result = mActivity.getString(R.string.failed_update_pay_passwd);
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (changePayView != null){
                    changePayView.changePayState(s);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (changePayView != null){
                    changePayView.changePayState(null);
                }
            }
        }.execute(phone,code,pwd,idCard));
    }
}
