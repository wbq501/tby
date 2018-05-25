package com.baigu.dms.presenter.impl;

import android.app.Activity;
import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.model.ExpressInfo;
import com.baigu.dms.domain.model.ExpressType;
import com.baigu.dms.domain.model.LogisticsInfo;
import com.baigu.dms.domain.netservice.ExpressService;
import com.baigu.dms.presenter.ExpressGetPresenter;
import com.micky.logger.Logger;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpressGetPresenterImpl extends BasePresenterImpl implements ExpressGetPresenter{

    private ExpressGetView expressGetView;

    public ExpressGetPresenterImpl(Activity activity,ExpressGetView expressGetView) {
        super(activity);
        this.expressGetView = expressGetView;
    }

    @Override
    public void getExpress(String expressnum) {
        addDisposable(new BaseAsyncTask<String, Void, LogisticsInfo<ExpressInfo>>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.loading);
            }

            @Override
            protected RxOptional<LogisticsInfo<ExpressInfo>> doInBackground(String... params) {
                RxOptional<LogisticsInfo<ExpressInfo>> rxResult = new RxOptional<>();
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BaseApplication.getContext().getString(R.string.express_url2))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    Call<ArrayList<ExpressType>> expressAuto = retrofit.create(ExpressService.class).getExpressAuto(params[0]);
                    Response<ArrayList<ExpressType>> execute1 = expressAuto.execute();
                    ArrayList<ExpressType> body = execute1.body();
                    Call<LogisticsInfo<ExpressInfo>> express = retrofit.create(ExpressService.class).getExpress(body.get(0).getComCode(),params[0]);
                    Response<LogisticsInfo<ExpressInfo>> execute = express.execute();
                    rxResult.setResult(execute.body());
                    rxResult.setCode(1);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(LogisticsInfo<ExpressInfo> result) {
                super.onPostExecute(result);
                if (expressGetView != null) {
                    expressGetView.loadExpress(result);
                }
            }

            @Override
            protected void doOnError() {
                if (expressGetView != null) {
                    expressGetView.loadExpress(null);
                }
            }
        }.execute(expressnum));
    }
}
