package com.baigu.dms.presenter.impl;

import android.util.Log;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BrandCommentPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class BrandCommentPresenterImpl extends BasePresenterImpl implements BrandCommentPresenter {

    private CommentView mCommentView;

    public BrandCommentPresenterImpl(BaseActivity activity, CommentView commentView) {
        super(activity);
        this.mCommentView = commentView;
    }

    @Override
    public void addComment(final String brandId, String comment) {
        addDisposable(new BaseAsyncTask<String, Void, String>(mActivity, true) {

            @Override
            protected RxOptional<String> doInBackground(String... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    User user = getUser();
                    if (user != null) {
                        String userId = UserCache.getInstance().getUser().getIds();
                        Call<BaseResponse<String>> addCommentCall = ServiceManager.createGsonService(BrandStoryService.class).addComment(userId, params[0], params[1]);
                        Response<BaseResponse<String>> addCommentResponse = addCommentCall.execute();
                        rxResult.setCode(addCommentResponse != null && addCommentResponse.body() != null ? addCommentResponse.body().getCode() : -1);
                        if (addCommentResponse != null && addCommentResponse.body() != null && BaseResponse.SUCCESS.equals(addCommentResponse.body().getStatus())) {
                            result = addCommentResponse.body().getData();
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
                if (mCommentView != null) {
                    mCommentView.onAddComment(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mCommentView != null) {
                    mCommentView.onAddComment("");
                }
            }
        }.execute(brandId, comment));
    }

    private User getUser() {
        User user = null;
        try {
            Call<BaseResponse<User>> userCall = ServiceManager.createGsonService(UserService.class).getMyInfo(UserCache.getInstance().getUser().getIds());
            Response<BaseResponse<User>> userResponse = userCall.execute();
            if (userResponse != null && userResponse.body() != null && BaseResponse.SUCCESS.equals(userResponse.body().getStatus())) {
                user = userResponse.body().getData();
                if (user != null) {
                    //保存最新用户信息到SP
                    User spUser = UserCache.getInstance().getUser();
                    spUser.setNick(user.getNick());
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
                    Log.i("test",user.getInvitecode()+"--code");
                    spUser.setInvitecode(user.getInvitecode());
                    SPUtils.putObject(SPUtils.KEY_USER, spUser);
                }

            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return user;
    }
}
