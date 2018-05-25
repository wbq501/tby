package com.baigu.dms.presenter.impl;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.model.Express;
import com.baigu.dms.presenter.ExpressSelectorPresenter;
import com.micky.logger.Logger;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class ExpressSelectorPresenterImpl extends BasePresenterImpl implements ExpressSelectorPresenter {
    private ExpressView mExpressView;

    public ExpressSelectorPresenterImpl(BaseActivity activity, ExpressView expressView) {
        super(activity);
        mExpressView = expressView;
    }

    @Override
    public void loadExpress() {
        addDisposable(new BaseAsyncTask<String, Void, List<Express>>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.loading);
            }

            @Override
            protected RxOptional<List<Express>> doInBackground(String... params) {
                RxOptional<List<Express>> rxResult = new RxOptional<>();
                List<Express> expressList = null;
                try {
                    expressList = RepositoryFactory.getInstance().getExpressRepository().queryAllOrdered();
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                rxResult.setResult(expressList);
                return rxResult;
            }

            @Override
            protected void onPostExecute(List<Express> result) {
                super.onPostExecute(result);
                if (mExpressView != null) {
                    mExpressView.onLoadExpress(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mExpressView != null) {
                    mExpressView.onLoadExpress(null);
                }
            }
        }.execute());
    }
}
