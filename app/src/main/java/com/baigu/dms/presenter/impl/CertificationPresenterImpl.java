package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.activity.CertificationStep1Activity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.file.FileUpload;
import com.baigu.dms.domain.model.Praise;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.CertificationPresenter;
import com.micky.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class CertificationPresenterImpl extends BasePresenterImpl implements CertificationPresenter {

    private CertificationView mCertificationView;

    public CertificationPresenterImpl(BaseActivity activity, CertificationView userView) {
        super(activity);
        this.mCertificationView = userView;
    }

    @Override
    public void saveIDCard(String idCard, String idcardFrontPath, String idcardBackPath,String payPwd) {
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
                    //上传图片
//                    FileUpload fileUpload = new FileUpload();
//                    fileUpload.setRequestName("file");
//                    String url = mActivity.getString(R.string.end_point) + mActivity.getString(R.string.url_update_idcard);
//                    String frontUrl = fileUpload.upload(url, params[1], null, null);
//                    String backdUrl = "";
//                    if (!TextUtils.isEmpty(frontUrl)) {
//                        backdUrl = fileUpload.upload(url, params[2], null, null);
//                    }
//                    if (!TextUtils.isEmpty(frontUrl) && !TextUtils.isEmpty(backdUrl)) {
                        String userId = UserCache.getInstance().getUser().getIds();
//                        String idcardImgStr = frontUrl + "," + backdUrl;
                        Call<BaseResponse> certificationCall = ServiceManager.createGsonService(WalletService.class).certification(userId, params[0], params[1], params[2],params[3]);
                        Response<BaseResponse> certificationResponse = certificationCall.execute();
                        rxResult.setCode(certificationResponse != null && certificationResponse.body() != null ? certificationResponse.body().getCode() : -1);
                        if (certificationResponse != null && certificationResponse.body() != null) {
                            result = BaseResponse.SUCCESS.equals(certificationResponse.body().getStatus());
                            User user = UserCache.getInstance().getUser();
                            user.setIdcard(params[0]);
                            user.setIdcardimg(params[1]+params[2]);
                            UserCache.getInstance().setUser(user);
                        }
//                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (mCertificationView != null) {
                    mCertificationView.onSaveIDCard(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mCertificationView != null) {
                    mCertificationView.onSaveIDCard(false);
                }
            }
        }.execute(idCard, idcardFrontPath, idcardBackPath,payPwd));
    }

    @Override
    public void upLoadImage(String imageFile, final int type) {
        addDisposable(new BaseAsyncTask<String, Void, String>() {
            @Override
            protected RxOptional<String> doInBackground(String... strings) {
                FileUpload fileUpload = new FileUpload();
                String url = mActivity.getString(R.string.end_point) + "/platform/upimage/idCardImg";
                fileUpload.setRequestName("imageFile");
                String frontUrl = fileUpload.upload(url, strings[0], null, null);
                RxOptional<String> rxResult = new RxOptional();
                rxResult.setResult(frontUrl);
                return rxResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(mCertificationView!=null){
                    if(type== CertificationStep1Activity.REQUEST_CODE_IDCARD_FRONT){
                        mCertificationView.onLoadImage(s);
                    }else{
                        mCertificationView.onLoadImageBack(s);
                    }

                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if(mCertificationView!=null){
                    mCertificationView.onLoadImage("");
                }
            }
        }.execute(imageFile));
    }
}
