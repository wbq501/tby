package com.baigu.dms.presenter.impl;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.DBCore;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.BankTypeRepository;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.db.repository.ExpressRepository;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.ShareInfo;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.HomeService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.common.model.HomeResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.BasicDataResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.HomePresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/21 22:24
 */
public class HomePreseterImpl extends BasePresenterImpl implements HomePresenter {
    private HomeView mHomeView;

    public HomePreseterImpl(BaseActivity activity, HomeView homeView) {
        super(activity);
        mHomeView = homeView;
    }

    @Override
    public void loadHomeData(boolean showProgress) {
        addDisposable(new BaseAsyncTask<Void, Void, HomeResult>(mActivity, showProgress) {

            @Override
            protected RxOptional<HomeResult> doInBackground(Void... params) {
                RxOptional<HomeResult> rxResult = new RxOptional<>();
                HomeResult homeData = null;
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse<HomeResult>> homeDataCall = ServiceManager.createGsonService(HomeService.class).getHomeData(userId);
                    Response<BaseResponse<HomeResult>> homeDataResponse = homeDataCall.execute();
                    rxResult.setCode(homeDataResponse != null && homeDataResponse.body() != null ? homeDataResponse.body().getCode() : -1);
                    if (homeDataResponse != null && homeDataResponse.body() != null && BaseResponse.SUCCESS.equals(homeDataResponse.body().getStatus())) {
                        homeData = homeDataResponse.body().getData();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }

                try {
                    Call<BaseResponse<BasicDataResult>> basicDataCall = ServiceManager.createGsonService(HomeService.class).getBasicData(0);
                    Response<BaseResponse<BasicDataResult>> basicDataResultResponse = basicDataCall.execute();
                    if (basicDataResultResponse != null
                            && basicDataResultResponse.body() != null
                            && BaseResponse.SUCCESS.equals(basicDataResultResponse.body().getStatus())) {
                        BasicDataResult basicDataResult = basicDataResultResponse.body().getData();
                        if (basicDataResult != null) {
                            //银行
                            if (basicDataResult.bankList != null && basicDataResult.bankList.size() > 0) {
                                BankTypeRepository repository = RepositoryFactory.getInstance().getBankRepository();
                                if (repository != null) {
                                    repository.deleteAll();
                                    repository.saveOrUpdate(basicDataResult.bankList);
                                }
                            }

                            //快递
                            if (basicDataResult.expressList != null && basicDataResult.expressList.size() > 0) {
                                ExpressRepository repository = RepositoryFactory.getInstance().getExpressRepository();
                                if (repository != null) {
                                    repository.deleteAll();
                                    repository.saveOrUpdate(basicDataResult.expressList);
                                }
                            }
                            //地区
                            if (basicDataResult.areaList != null && basicDataResult.areaList.size() > 0) {
                                CityRepository repository = RepositoryFactory.getInstance().getCityRepository();
                                if (repository != null) {
                                    repository.deleteAll();
                                    repository.saveOrUpdate(basicDataResult.areaList);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
//                loadShareInfo();
                rxResult.setResult(homeData);
                return rxResult;
            }

            @Override
            protected void onPostExecute(HomeResult homeData) {
                super.onPostExecute(homeData);
                if (mHomeView != null) {
                    mHomeView.onLoadHomeData(homeData);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mHomeView != null) {
                    mHomeView.onLoadHomeData(null);
                }
            }
        }.execute());
    }

    private void loadShareInfo() {
        Constants.sExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //分享信息
                    Call<BaseResponse<ShareInfo>> shareCall = ServiceManager.createGsonService(HomeService.class).getShareInfo();
                    Response<BaseResponse<ShareInfo>> shareResponse = shareCall.execute();
                    if (shareResponse != null && shareResponse.body() != null && BaseResponse.SUCCESS.equals(shareResponse.body().getStatus())) {
                        UserCache.getInstance().setShareInfo(shareResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
            }
        });
    }

    @Override
    public void loadBrandStory(int pageNum) {
        addDisposable(new BaseAsyncTask<Integer, Void, BaseResponse<List<BrandStory>>>() {

            @Override
            protected RxOptional<BaseResponse<List<BrandStory>>> doInBackground(Integer... params) {
                RxOptional<BaseResponse<List<BrandStory>>> rxResult = new RxOptional<>();
                BaseResponse<List<BrandStory>> result = new BaseResponse<>();
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse<List<BrandStory>>> brandStoryCall = ServiceManager.createGsonService(BrandStoryService.class).getBrandStory(userId, String.valueOf(params[0]));
                    Response<BaseResponse<List<BrandStory>>> brandStoryResultResponse = brandStoryCall.execute();
                    rxResult.setCode(brandStoryResultResponse != null && brandStoryResultResponse.body() != null ? brandStoryResultResponse.body().getCode() : -1);
                    if (brandStoryResultResponse != null && brandStoryResultResponse.body() != null && BaseResponse.SUCCESS.equals(brandStoryResultResponse.body().getStatus())) {
//                        result = brandStoryResultResponse.body().getData();
//                        if (result == null) {
//                            result = new PageResult<>();
//                            result.firstPage = false;
//                            result.lastPage = true;
//                        }
                        result.setData(brandStoryResultResponse.body().getData());
                        result.setCode(brandStoryResultResponse.body().getCode());
                        result.setMessage(brandStoryResultResponse.body().getMessage());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(BaseResponse<List<BrandStory>> brandStoryPageResult) {
                super.onPostExecute(brandStoryPageResult);
                if (mHomeView != null) {
                    mHomeView.onLoadBrandStory(brandStoryPageResult);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mHomeView != null) {
                    mHomeView.onLoadBrandStory(null);
                }
            }
        }.execute(pageNum));
    }
}
