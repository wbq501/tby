package com.baigu.dms.presenter.impl;

import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.presenter.AddressPresenter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.micky.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class AddressPresenterImpl extends BasePresenterImpl implements AddressPresenter {
    private AddressView mAddressView;

    public AddressPresenterImpl(BaseActivity activity, AddressView addressView) {
        super(activity);
        mAddressView = addressView;
    }

    @Override
    public void loadAddress(int pageNum, final boolean showDialog) {
        String userId = UserCache.getInstance().getUser().getIds();
        addDisposable(new BaseAsyncTask<String, Void, PageResult<Address>>(mActivity, showDialog) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (showDialog) {
                    setLoadingText(R.string.loading);
                }
            }

            @Override
            protected RxOptional<PageResult<Address>> doInBackground(String... params) {
                RxOptional<PageResult<Address>> result = new RxOptional<>();
                PageResult<Address> resultPage = null;
                try {
                    Call<BaseResponse<PageResult<Address>>> call = ServiceManager.createGsonService(UserService.class).getAddressList(params[0], Integer.parseInt(params[1]));
                    Response<BaseResponse<PageResult<Address>>> pageResultResponse = call.execute();
                    result.setCode(pageResultResponse != null && pageResultResponse.body() != null ? pageResultResponse.body().getCode() : -1);
                    if (pageResultResponse != null && pageResultResponse.body() != null && BaseResponse.SUCCESS.equals(pageResultResponse.body().getStatus())) {
                        resultPage = pageResultResponse.body().getData();
                        //拼完整地址
                        if (resultPage != null && resultPage.list != null && resultPage.list.size() > 0) {
                            for (Address address : resultPage.list) {
                                List<City> cityList = getCityList(address.getAreaId());
                                if (cityList != null && cityList.size() > 0) {
                                    StringBuilder sbCity = new StringBuilder();
                                    for (City city : cityList) {
                                        sbCity.append(city.getName());
                                    }
                                    address.setFullRegionName(sbCity.toString());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                result.setResult(resultPage);
                return result;
            }

            @Override
            protected void onPostExecute(PageResult<Address> result) {
                super.onPostExecute(result);
                if (mAddressView != null) {
                    mAddressView.onLoadAddress(result);
                }
            }

            @Override
            protected void doOnError() {
                if (mAddressView != null) {
                    mAddressView.onLoadAddress(null);
                }
            }
        }.execute(userId, String.valueOf(pageNum)));
    }

    @Override
    public void saveOrUpdateAddress(final Address address) {
        addDisposable(new BaseAsyncTask<Address, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<String> doInBackground(Address... params) {
                RxOptional<String> rxResult = new RxOptional<>();
                String result = "";
                try {
                    JsonObject jsonObject =new JsonObject();
                    jsonObject.addProperty("ids",address.getId());
                    jsonObject.addProperty("userId",address.getUserid());
                    jsonObject.addProperty("regionId",address.getRegionid() == null ? address.getAreaId() : address.getRegionid());
                    jsonObject.addProperty("shipTo",address.getShipTo() == null ? address.getName() : address.getShipTo());
                    jsonObject.addProperty("address",address.getAddress());
                    jsonObject.addProperty("phone",address.getPhone());
                    jsonObject.addProperty("default",address.isDefault());
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).saveOrUpdateAddress(body);
                    Response<BaseResponse> pageResultResponse = call.execute();
                    rxResult.setCode(pageResultResponse != null && pageResultResponse.body() != null ? pageResultResponse.body().getCode() : -1);
                    if (pageResultResponse != null && pageResultResponse.body() != null && BaseResponse.SUCCESS.equals(pageResultResponse.body().getStatus())) {
                        result = TextUtils.isEmpty(address.getId()) ? "saved" : address.getId();
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
                if (!"".equals(result) && !"saved".equals(result)) {
                    RxBus.getDefault().post(EventType.TYPE_ADDRESS_UPDATE, result);
                }
                if (mAddressView != null) {
                    mAddressView.onSaveOrUpdateAddress(address, !"".equals(result));
                }
            }

            @Override
            protected void doOnError() {
                if (mAddressView != null) {
                    mAddressView.onSaveOrUpdateAddress(address, false);
                }
            }
        }.execute());
    }

    @Override
    public void deleteAddress(final Address address) {

        addDisposable(new BaseAsyncTask<Void, Void, Boolean>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<Boolean> doInBackground(Void... params) {
                RxOptional<Boolean> result = new RxOptional<>();
                result.setResult(false);
                try {
                    String userId = UserCache.getInstance().getUser().getIds();
                    Call<BaseResponse> call = ServiceManager.createGsonService(UserService.class).deleteAddress(userId, address.getId());
                    Response<BaseResponse> pageResultResponse = call.execute();
                    result.setResult(pageResultResponse != null && pageResultResponse.body() != null && BaseResponse.SUCCESS.equals(pageResultResponse.body().getStatus()));
                    result.setCode(pageResultResponse != null && pageResultResponse.body() != null ? pageResultResponse.body().getCode() : -1);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result) {
                    RxBus.getDefault().post(EventType.TYPE_ADDRESS_UPDATE);
                }
                if (mAddressView != null) {
                    mAddressView.onDeleteAddress(address, result);
                }
            }

            @Override
            protected void doOnError() {
                if (mAddressView != null) {
                    mAddressView.onDeleteAddress(address, false);
                }
            }
        }.execute());
    }

    @Override
    public Observable<RxOptional<List<City>>> loadCityList(final String cityId) {
        return Observable.create(new ObservableOnSubscribe<RxOptional<List<City>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<RxOptional<List<City>>> observableEmitter) throws Exception {
                List<City> cityList = getCityList(cityId);
                observableEmitter.onNext(new RxOptional<>(cityList));
            }
        });
    }

    private List<City> getCityList(String cityId) throws Exception {
        CityRepository cityRepository = RepositoryFactory.getInstance().getCityRepository();
        List<City> cityList = new ArrayList<>();
        City city = cityRepository.query(cityId);
        while (city != null) {
            cityList.add(city);
            city = cityRepository.query(city.getParentId());
        }
        Collections.reverse(cityList);
        return cityList;
    }
}
