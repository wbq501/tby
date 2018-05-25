package com.baigu.dms.presenter.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.FileUtils;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.file.FileUpload;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.UserPresenter;
import com.micky.logger.Logger;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class UserPresenterImpl extends BasePresenterImpl implements UserPresenter {

    private UserPresenter.UserView mUserView;

    public UserPresenterImpl(Activity activity, UserView userView) {
        super(activity);
        this.mUserView = userView;
    }

    @Override
    public void getMyInfo(boolean showDialog) {
        addDisposable(new BaseAsyncTask<String, Void, User>(mActivity, showDialog) {

            @Override
            protected RxOptional<User> doInBackground(String... params) {
                RxOptional<User> rxResult = new RxOptional<>();
                User user = null;
                try {
                    Call<BaseResponse<User>> userCall = ServiceManager.createGsonService(UserService.class).getMyInfo(UserCache.getInstance().getUser().getIds());
                    Response<BaseResponse<User>> userResponse = userCall.execute();
                    rxResult.setCode(userResponse != null && userResponse.body() != null ? userResponse.body().getCode() : -1);
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
                            spUser.setWx_account(user.getWx_account());
                            spUser.setWxaccount(user.getWxaccount());
                            spUser.setAlipayaccount(user.getAlipayaccount());
                            spUser.setBlance(user.getBlance());
                            spUser.setBlanknumber(user.getBlanknumber());
                            spUser.setBlankname(user.getBlankname());
                            spUser.setInvitecode(user.getInvitecode());
                            SPUtils.putObject(SPUtils.KEY_USER, spUser);
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(user);
                return rxResult;
            }

            @Override
            protected void onPostExecute(User result) {
                super.onPostExecute(result);
                if (mUserView != null) {
                    mUserView.onGetMyInfo(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mUserView != null) {
                    mUserView.onGetMyInfo(null);
                }
            }
        }.execute());
    }

    @Override
    public void loadToken() {
        addDisposable(new BaseAsyncTask<String, Void, BaseResponse<String>>() {
            @Override
            protected RxOptional<BaseResponse<String>> doInBackground(String... strings) {
                RxOptional<BaseResponse<String>> rxOptional = new RxOptional<>();
                BaseResponse<String> result = new BaseResponse();
                try {
                    Call<BaseResponse<String>> baseResponseCall = ServiceManager.createGsonService(UserService.class).loadQiNiuToken();
                    Response<BaseResponse<String>> response = baseResponseCall.execute();
                    if (response != null && response.body() != null) {
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
            protected void onPostExecute(BaseResponse<String> s) {
                super.onPostExecute(s);
                if (mUserView != null) {
                    if (s.getCode() == 1) {
                        mUserView.onLoadToken(s.getData());
                    } else {
                        ViewUtils.showToastError(mActivity.getString(R.string.connect_server_failed));
                    }
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mUserView != null) {
                    mUserView.onLoadToken("");
                }
            }
        }.execute());
    }

    @Override
    public void saveHead(String path, String token) {
        Configuration config = new Configuration.Builder()
                .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                .build();
        UploadManager uploadManager = new UploadManager(config);
        String key = "head" + new Date().getTime();
        uploadManager.put(path, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    addDisposable(new BaseAsyncTask<String, Void, Boolean>(mActivity, true) {

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            setLoadingText(R.string.submitting);
                        }

                        @Override
                        protected RxOptional<Boolean> doInBackground(final String... params) {
                            RxOptional<Boolean> rxResult = new RxOptional<>();
                            boolean result = false;

                            if(!TextUtils.isEmpty(params[0])){
                                try {
                                    Call<BaseResponse> updateHeadCall = ServiceManager.createGsonService(UserService.class).updateHeadImg(params[0]);
                                    Response<BaseResponse> updateHeadResponse = updateHeadCall.execute();
                                    rxResult.setCode(updateHeadResponse != null && updateHeadResponse.body() != null ? updateHeadResponse.body().getCode() : -1);
                                    if (updateHeadResponse != null && updateHeadResponse.body() != null && BaseResponse.SUCCESS.equals(updateHeadResponse.body().getStatus())) {
                                        result = true;
                                        User user = UserCache.getInstance().getUser();
                                        user.setPhoto(mActivity.getString(R.string.url_upload_head)+params[0]);
                                        UserCache.getInstance().setUser(user);
                                    }
                                } catch (Exception e) {
                                    Logger.e(e, e.getMessage());
                                } finally {
                                    deleteOutCropDir();
                                }
                            }
                            rxResult.setResult(result);
                            return rxResult;
                        }

                        @Override
                        protected void onPostExecute(Boolean result) {
                            super.onPostExecute(result);
                            if (mUserView != null) {
                                mUserView.onSaveHead(result);
                            }
                        }

                        @Override
                        protected void doOnError() {
                            deleteOutCropDir();
                            if (mUserView != null) {
                                mUserView.onSaveHead(false);
                            }
                        }
                    }.execute(key));
                } else {
                    //如果失败，这里  url[0] = "";可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    if (mUserView != null) {
                        mUserView.onSaveHead(false);
                    }
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
            }
        }, null);

    }

    private void deleteOutCropDir() {
        try {
            FileUtils.deleteOutputCropDir();
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

}
