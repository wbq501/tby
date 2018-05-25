package com.baigu.dms.presenter.impl;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.WalletService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.WalletPresenter;
import com.micky.logger.Logger;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class WalletPresenterImpl extends BasePresenterImpl implements WalletPresenter {

    private WalletView mWalletView;

    public WalletPresenterImpl(BaseActivity activity, WalletView userView) {
        super(activity);
        this.mWalletView = userView;
    }

    @Override
    public void getMyMoney() {
        addDisposable(new BaseAsyncTask<String, Void, Money>(mActivity, false) {

            @Override
            protected RxOptional<Money> doInBackground(String... params) {
                RxOptional<Money> rxResult = new RxOptional<>();
                Money result = null;
                try {
                    User user = getUser();
                    if (user != null) {
                        Call<BaseResponse<Money>> walletCall = ServiceManager.createGsonService(WalletService.class).getMyMoney(UserCache.getInstance().getUser().getIds());
                        Response<BaseResponse<Money>> walletResponse = walletCall.execute();
                        rxResult.setCode(walletResponse != null && walletResponse.body() != null ? walletResponse.body().getCode() : -1);
                        if (walletResponse != null && walletResponse.body() != null && BaseResponse.SUCCESS.equals(walletResponse.body().getStatus())) {
                            result = walletResponse.body().getData();
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Money result) {
                super.onPostExecute(result);
                if (mWalletView != null) {
                    mWalletView.onGetMyMoney(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mWalletView != null) {
                    mWalletView.onGetMyMoney(null);
                }
            }
        }.execute());
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
                    SPUtils.putObject(SPUtils.KEY_USER, spUser);
                }
            }
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return user;
    }
}
