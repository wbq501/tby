package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.presenter.CitySelectorPresenter;
import com.micky.logger.Logger;

import java.util.List;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class CitySelectorPresenterImpl extends BasePresenterImpl implements CitySelectorPresenter {
    private CityView mCityView;

    public CitySelectorPresenterImpl(BaseActivity activity, CityView addressView) {
        super(activity);
        mCityView = addressView;
    }

    @Override
    public List<City> loadCity(final String parentId) {
        try {
            CityRepository repository = RepositoryFactory.getInstance().getCityRepository();
            Log.i("test", "all---"+repository.queryAll().toString());
            List<City> cityList = TextUtils.isEmpty(parentId) ? repository.queryByAreaType(1) : repository.queryByParentId(parentId);

            return cityList;
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
        return null;
    }
//    @Override
//    public void loadCity(final String parentId) {
//        addDisposable(new BaseAsyncTask<String, Void, List<City>>() {
//            @Override
//            protected List<City> doInBackground(String... params) {
//                try {
//                    CityRepository repository = RepositoryFactory.getInstance().getCityRepository();
//                    List<City> cityList = TextUtils.isEmpty(parentId) ? repository.queryByAreaType(1) : repository.queryByParentId(parentId);
//                    return cityList;
//                } catch (Exception e) {
//                    Logger.e(e, e.getMessage());
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(List<City> result) {
//                super.onPostExecute(result);
//                if (mCityView != null) {
//                    mCityView.onLoadCity(result);
//                }
//            }
//
//            @Override
//            protected void doOnError() {
//                if (mCityView != null) {
//                    mCityView.onLoadCity(null);
//                }
//            }
//        }.execute(parentId));
//    }
}
