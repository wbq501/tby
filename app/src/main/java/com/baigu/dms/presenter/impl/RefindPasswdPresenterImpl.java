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
import com.baigu.dms.presenter.RefindPasswdPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class RefindPasswdPresenterImpl extends BasePresenterImpl implements RefindPasswdPresenter {
    private RefindPasswdView mRefindPasswdView;

    public RefindPasswdPresenterImpl(BaseActivity activity, RefindPasswdView refindPasswdView) {
        super(activity);
        mRefindPasswdView = refindPasswdView;
    }

    @Override
    public void refindPasswd(String phone, String pwd, String code) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.failed_refind_passwd);
                try {
                    Call<BaseResponse<String>> keyCall = ServiceManager.createGsonService(UserService.class).getPublicKey(params[0], User.RSAKeyType.REFIND_PWD);
                    Response<BaseResponse<String>> keyResponse = keyCall.execute();
                    if (keyResponse != null && keyResponse.body() != null && BaseResponse.SUCCESS.equals(keyResponse.body().getStatus()) && !TextUtils.isEmpty(keyResponse.body().getData())) {
                        RSAEncryptor encryptor = new RSAEncryptor();
                        encryptor.loadPublicKey(keyResponse.body().getData());
                        String pwdEncrypt = encryptor.encryptWithBase64(params[1]);
                        Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).refindPasswd(params[0], pwdEncrypt, params[2]);
                        Response<BaseResponse> refindPwdResponse = call.execute();

                        if (refindPwdResponse != null && refindPwdResponse.body() != null) {
                            if (refindPwdResponse.body().getCode()>0) {
                                result = "success";
                            }  else{
                                result = refindPwdResponse.body().getMessage();
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
                    ViewUtils.showToastSuccess(R.string.success_refind_passwd);
                } else {
                    ViewUtils.showToastError(result);
                }
                if (mRefindPasswdView != null) {
                    mRefindPasswdView.onRefindPasswd(success);
                }
            }

            @Override
            protected void doOnError() {
                if (mRefindPasswdView != null) {
                    mRefindPasswdView.onRefindPasswd(false);
                }
            }
        }.execute(phone, pwd, code));
    }
}
