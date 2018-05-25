package com.baigu.dms.presenter.impl;

import android.app.Activity;

import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.domain.model.Praise;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.BrandStoryDetailPresenter;
import com.micky.logger.Logger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/9/10 11:26
 */
public class BrandStoryDetailPresenterImpl extends BasePresenterImpl implements BrandStoryDetailPresenter {

    private BrandStoryDetailView mBrandStoryDetailView;

    public BrandStoryDetailPresenterImpl(Activity activity, BrandStoryDetailView brandStoryDetailView) {
        super(activity);
        this.mBrandStoryDetailView = brandStoryDetailView;
    }

    @Override
    public void loadBrandStoryById(String brandId) {
        addDisposable(new BaseAsyncTask<String, Void, BrandStory>(mActivity, true) {

            @Override
            protected RxOptional<BrandStory> doInBackground(String... params) {
                RxOptional<BrandStory> rxResult = new RxOptional<>();
                BrandStory result = null;
                try {
//                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse<BrandStory>> brandStoryCall = ServiceManager.createGsonService(BrandStoryService.class).getBrandStoryById(params[0]);
                    Response<BaseResponse<BrandStory>> brandStoryResponse = brandStoryCall.execute();
                    rxResult.setCode(brandStoryResponse != null && brandStoryResponse.body() != null ? brandStoryResponse.body().getCode() : -1);
                    if (brandStoryResponse != null && brandStoryResponse.body() != null && BaseResponse.SUCCESS.equals(brandStoryResponse.body().getStatus())) {
                        result = brandStoryResponse.body().getData();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(BrandStory result) {
                super.onPostExecute(result);
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadBrandStory(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadBrandStory(null);
                }
            }
        }.execute(brandId));
    }

    @Override
    public void loadCommentList(String brandId, int pageNum, boolean showDialog) {
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Comment>>(mActivity, showDialog) {

            @Override
            protected RxOptional<PageResult<Comment>> doInBackground(String... params) {
                RxOptional<PageResult<Comment>> rxResult = new RxOptional<>();
                PageResult<Comment> result = null;
                try {
                    Call<BaseResponse<PageResult<Comment>>> commentCall = ServiceManager.createGsonService(BrandStoryService.class).getCommentList(params[0], params[1]);
                    Response<BaseResponse<PageResult<Comment>>> commentResponse = commentCall.execute();
                    rxResult.setCode(commentResponse != null && commentResponse.body() != null ? commentResponse.body().getCode() : -1);
                    if (commentResponse != null && commentResponse.body() != null && BaseResponse.SUCCESS.equals(commentResponse.body().getStatus())) {
                        result = commentResponse.body().getData();
                        if (result == null) {
                            result = new PageResult<>();
                            result.firstPage = false;
                            result.lastPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<Comment> result) {
                super.onPostExecute(result);
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadCommentList(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadCommentList(null);
                }
            }
        }.execute(brandId, String.valueOf(pageNum)));
    }

    @Override
    public void loadPraiseList(String brandId, int pageNum, boolean showDialog) {
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Praise>>(mActivity, false) {

            @Override
            protected RxOptional<PageResult<Praise>> doInBackground(String... params) {
                RxOptional<PageResult<Praise>> rxResult = new RxOptional<>();
                PageResult<Praise> result = null;
                try {
                    Call<BaseResponse<PageResult<Praise>>> commentCall = ServiceManager.createGsonService(BrandStoryService.class).getPraseList(params[0], params[1]);
                    Response<BaseResponse<PageResult<Praise>>> commentResponse = commentCall.execute();
                    rxResult.setCode(commentResponse != null && commentResponse.body() != null ? commentResponse.body().getCode() : -1);
                    if (commentResponse != null && commentResponse.body() != null && BaseResponse.SUCCESS.equals(commentResponse.body().getStatus())) {
                        result = commentResponse.body().getData();
                        if (result == null) {
                            result = new PageResult<>();
                            result.firstPage = false;
                            result.lastPage = true;
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(result);
                return rxResult;
            }

            @Override
            protected void onPostExecute(PageResult<Praise> result) {
                super.onPostExecute(result);
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadPraiseList(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mBrandStoryDetailView != null) {
                    mBrandStoryDetailView.onLoadPraiseList(null);
                }
            }
        }.execute(brandId, String.valueOf(pageNum)));
    }
}
