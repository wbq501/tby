package com.baigu.dms.presenter.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.AESUtils;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.IMHelper;
import com.baigu.dms.common.utils.RSAEncryptor;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.ShopCart;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.DBCore;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.LoginPresenter;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class LoginPresenterImpl extends BasePresenterImpl implements LoginPresenter {
    private LoginView mLoginView;

    public LoginPresenterImpl(Activity activity, LoginView loginView) {
        super(activity);
        mLoginView = loginView;
    }

    public LoginPresenterImpl(Activity activity) {
        super(activity);
    }

    @Override
    public void autoLogin() {
        addDisposable(new BaseAsyncTask<String, Void, String>() {

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.login_failed);
                try {
                    ShopCart.clearCart();
                    User user = UserCache.getInstance().getUser();
                    String loginResult = loginInner(user.getCellphone(), user.getLoginToken(), true);
                    if (!TextUtils.isEmpty(loginResult)) {
                        result = loginResult;
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
                if (mLoginView != null) {
                    mLoginView.onLogin(success);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mLoginView != null) {
                    mLoginView.onLogin(false);
                }
            }
        }.execute());
    }

    @Override
    public void login(String phone, String pwd) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.logining);
            }

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = mActivity.getString(R.string.login_failed);
                try {
                    ShopCart.clearCart();
                    String loginResult = loginInner(params[0], params[1], false);
                    if (!TextUtils.isEmpty(loginResult)) {
                        result = loginResult;
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
                if (mLoginView != null) {
                    mLoginView.onLogin(success);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mLoginView != null) {
                    mLoginView.onLogin(false);
                }
            }
        }.execute(phone, pwd));
    }

    private String loginInner(String phone, String pwd, boolean autoLogin) throws Exception {
        String result = "";
        Call<BaseResponse<String>> keyCall = ServiceManager.createGsonService(UserService.class).getPublicKey(phone, User.RSAKeyType.LOGIN);
        Response<BaseResponse<String>> keyResponse = keyCall.execute();

        if (keyResponse != null && keyResponse.body() != null && keyResponse.body().getCode()>0 && !TextUtils.isEmpty(keyResponse.body().getData())) {
            RSAEncryptor encryptor = new RSAEncryptor();
            encryptor.loadPublicKey(keyResponse.body().getData());
            String pwdEncrypt = encryptor.encryptWithBase64(pwd);
            Call<BaseResponse<User>> loginCall = ServiceManager.createGsonService(UserService.class).login(phone, pwdEncrypt);
            Response<BaseResponse<User>> loginResponse = loginCall.execute();

            if (loginResponse != null && loginResponse.body() != null) {
                if (loginResponse.body().getCode()>0) {
                    if ( loginResponse.body().getData().getToken() != null) { //登录成功
                        User user = loginResponse.body().getData();
                        if (!TextUtils.isEmpty(user.getLoginToken())) {
                            SPUtils.putObject(SPUtils.KEY_LOGIN_TIME, String.valueOf(System.currentTimeMillis()));
                        }
                        SPUtils.putObject("token",user.getToken());
                        TokenManager.getInstance().setToken(user.getToken());
                        user.setToken("");
                        if (autoLogin) {
                            user.setLoginToken(UserCache.getInstance().getUser().getLoginToken());
                        }
                        UserCache.getInstance().setUser(user);
                        DBCore.openDatabase();
                        loginHx(user);
                        UserCache.getInstance().setAccount(user.getCellphone());
                        result = "success";
                    }
                } else {
                    result = result=loginResponse.body().getMessage();
                }
            }
        } else {
            result = mActivity.getString(R.string.server_exception);
        }
        return result;
    }

    private void loginHx(final User user) {
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    RSAEncryptor encryptor = new RSAEncryptor();
                    encryptor.loadPrivateKey(user.getPrivateKey());
                    String passwd = encryptor.decryptWithBase64(user.getImpwd());

                    ChatClient.getInstance().login(user.getImuser(), passwd, new Callback() {
                        @Override
                        public void onSuccess() {
                            IMHelper.getInstance().addMessageListener();
                        }

                        @Override
                        public void onError(int i, String s) {
                            Logger.e(s);
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
            }
        });
    }

}
