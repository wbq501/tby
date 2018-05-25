package com.baigu.dms.presenter.impl;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Notice;
import com.baigu.dms.domain.netservice.NoticeService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.NoticePresenter;
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
public class NoticePresenterImpl extends BasePresenterImpl implements NoticePresenter {

    private NoticeView mNoticeView;

    public NoticePresenterImpl(BaseActivity activity, NoticeView noticeView) {
        super(activity);
        this.mNoticeView = noticeView;
    }

    @Override
    public void getNoticeList(String pageNum) {
        addDisposable(new BaseAsyncTask<String, Void, List<Notice>>(mActivity, true) {

            @Override
            protected RxOptional<List<Notice>> doInBackground(String... params) {
                RxOptional<List<Notice>> rxResult = new RxOptional<>();
                List<Notice> noticeList = null;
                try {
                    Call<BaseResponse<PageResult<Notice>>> noticeCall = ServiceManager.createGsonService(NoticeService.class).getNoticeList(params[0]);
                    Response<BaseResponse<PageResult<Notice>>> noticeResponse = noticeCall.execute();
                    rxResult.setCode(noticeResponse != null && noticeResponse.body() != null ? noticeResponse.body().getCode() : -1);
                    if (noticeResponse != null && noticeResponse.body() != null && BaseResponse.SUCCESS.equals(noticeResponse.body().getStatus())) {
                        noticeList = noticeResponse.body().getData().list;
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(noticeList);
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<Notice> result) {
                super.onPostExecute(result);
                if (mNoticeView != null) {
                    mNoticeView.onGetNoticeList(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mNoticeView != null) {
                    mNoticeView.onGetNoticeList(null);
                }
            }
        }.execute(pageNum));
    }

    @Override
    public void getNotice(String ids) {
        addDisposable(new BaseAsyncTask<String, Void, Notice>(mActivity, true) {

            @Override
            protected RxOptional<Notice> doInBackground(String... params) {
                RxOptional<Notice> rxResult = new RxOptional<>();
                Notice notice = null;
                try {
                    Call<BaseResponse<Notice>> noticeCall = ServiceManager.createGsonService(NoticeService.class).getNotice(params[0]);
                    Response<BaseResponse<Notice>> noticeResponse = noticeCall.execute();
                    rxResult.setCode(noticeResponse != null && noticeResponse.body() != null ? noticeResponse.body().getCode() : -1);
                    if (noticeResponse != null && noticeResponse.body() != null && BaseResponse.SUCCESS.equals(noticeResponse.body().getStatus())) {
                        notice = noticeResponse.body().getData();
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(notice);
                return rxResult;
            }

            @Override
            protected void onPostExecute(Notice result) {
                super.onPostExecute(result);
                if (mNoticeView != null) {
                    mNoticeView.onGetNotice(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mNoticeView != null) {
                    mNoticeView.onGetNotice(null);
                }
            }
        }.execute(ids));
    }

}
