package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RSAEncryptor;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.UpdateUserPresenter;
import com.micky.logger.Logger;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class UpdateUserPresenterImpl extends BasePresenterImpl implements UpdateUserPresenter {

    private UpdateUserView mUserUpdateView;

    public UpdateUserPresenterImpl(BaseActivity activity, UpdateUserView userUpdateView) {
        super(activity);
        this.mUserUpdateView = userUpdateView;
    }

    @Override
    public void updateNick(final String nick) {
        addDisposable(new BaseAsyncTask<String, Void, Boolean>(mActivity, true) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<Boolean> doInBackground(String... params) {
                RxOptional<Boolean> rxResult  = new RxOptional<>();
                boolean result = false;
                try {
                    User user = getUser(rxResult);
                    if (user != null) {
                        result = updateUser(user.getIds(), params[0], user.getEmail(), user.getWx_account());
                        if (result) {
                            user = UserCache.getInstance().getUser();
                            user.setNick(params[0]);
                            UserCache.getInstance().setUser(user);
                        }
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
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(false);
                }
            }
        }.execute(nick));
    }

    @Override
    public void updateEmail(final String email) {
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
                    User user = getUser(rxResult);
                    if (user != null) {
                        result = updateUser(user.getIds(), user.getNick(), params[0], user.getWx_account() == null ? user.getWxaccount() : user.getWx_account());
                        if (result) {
                            user = UserCache.getInstance().getUser();
                            user.setEmail(params[0]);
                            UserCache.getInstance().setUser(user);
                        }
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
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(false);
                }
            }
        }.execute(email));
    }


    @Override
    public void updateWeixin(final String weixin) {
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
                    User user = getUser(rxResult);
                    if (user != null) {
                        result = updateUser(user.getIds(), user.getNick(), user.getEmail(), params[0]);
                        if (result) {
                            user = UserCache.getInstance().getUser();
                            user.setWx_account(params[0]);
                            UserCache.getInstance().setUser(user);
                        }
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
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(false);
                }
            }
        }.execute(weixin));
    }

    @Override
    public void updatePasswd(String oldPasswd, String newPasswd) {
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
                    Call<BaseResponse<String>> keyCall = ServiceManager.createGsonService(UserService.class).getPublicKey(user.getCellphone(), User.RSAKeyType.UPD_PWD);
                    Response<BaseResponse<String>> keyResponse = keyCall.execute();

                    if (keyResponse != null && keyResponse.body() != null && keyResponse.body().getCode()>0 && !TextUtils.isEmpty(keyResponse.body().getData())) {
                        RSAEncryptor encryptor = new RSAEncryptor();
                        encryptor.loadPublicKey(keyResponse.body().getData());
                        String oldPwdEncrypt = encryptor.encryptWithBase64(params[0]);
                        String newPwdEncrypt = encryptor.encryptWithBase64(params[1]);
                        Call<BaseResponse> updateCall = ServiceManager.createGsonService(UserService.class).updatePasswd(user.getIds(), oldPwdEncrypt, newPwdEncrypt);
                        Response<BaseResponse> updateResponse = updateCall.execute();

                        if (updateResponse != null && updateResponse.body() != null) {
                            result = updateResponse.body().getCode()>0;
                        }
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
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(false);
                }
            }
        }.execute(oldPasswd, newPasswd));
    }


    @Override
    public void updatePayPasswd(String oldpsd, String phone, String passwd, String code) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

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
                        String pwdEncrypt = encryptor.encryptWithBase64(params[1]);
                        Call<BaseResponse> updateCall = ServiceManager.createGsonService(UserService.class).updatePayPasswd(params[0], pwdEncrypt, params[2],params[3]);
                        Response<BaseResponse> updateResponse = updateCall.execute();
                        if (updateResponse != null && updateResponse.body() != null) {
                            if (BaseResponse.SUCCESS.equals(updateResponse.body().getStatus())) {
                                result = "success";
                            } else if (10008 == updateResponse.body().getCode()) {
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
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                boolean success = "success".equals(result);
                if (!success) {
                    ViewUtils.showToastError(result);
                }
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(success);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserUpdateView != null) {
                    mUserUpdateView.onUpdateUser(false);
                }
            }
        }.execute(phone, passwd, code,oldpsd));
    }

    private boolean updateUser(String ids, String nick, String email, String wx_account) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ids", ids);
            jsonObject.put("nickname", nick);
            jsonObject.put("email", email);
            jsonObject.put("wechat", wx_account);
            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),jsonObject.toString());
            Call<BaseResponse> updateCall = ServiceManager.createGsonService(UserService.class).updateMyInfo(body);
            Response<BaseResponse> updateResponse = updateCall.execute();
            if (updateResponse != null && updateResponse.body() != null && BaseResponse.SUCCESS.equals(updateResponse.body().getStatus())) {
                result = true;
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return result;
    }

    private User getUser(RxOptional rxOptional) {
        User user = null;
        try {
            Call<BaseResponse<User>> userCall = ServiceManager.createGsonService(UserService.class).getMyInfo(UserCache.getInstance().getUser().getIds());
            Response<BaseResponse<User>> userResponse = userCall.execute();
            rxOptional.setCode(userResponse != null && userResponse.body() != null ? userResponse.body().getCode() : -1);
            if (userResponse != null && userResponse.body() != null && BaseResponse.SUCCESS.equals(userResponse.body().getStatus())) {
                user = userResponse.body().getData();
                if (user != null) {
                    //保存最新用户信息到SP
                    User spUser = UserCache.getInstance().getUser();
                    spUser.setNick(user.getNick());
                    spUser.setUsername(user.getUsername());
                    spUser.setCellphone(user.getCellphone());
                    spUser.setBlance(user.getBlance());
                    spUser.setEmail(user.getEmail());
                    spUser.setMyqrcode(user.getMyqrcode());
                    spUser.setPhoto(user.getPhoto());
                    spUser.setLastlogindate(user.getLastlogindate());
                    spUser.setIdcard(user.getIdcard());
                    spUser.setIdcardimg(user.getIdcardimg());
                    spUser.setIdcardstatus(user.getIdcardstatus());
                    spUser.setWx_account(user.getWx_account() == null ? user.getWxaccount() : user.getWx_account());
                    spUser.setWxaccount(user.getWx_account() == null ? user.getWxaccount() : user.getWx_account());
                    SPUtils.putObject(SPUtils.KEY_USER, spUser);
                }

            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return user;
    }
}
