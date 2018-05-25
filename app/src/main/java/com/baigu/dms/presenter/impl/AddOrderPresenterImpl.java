package com.baigu.dms.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.baigu.dms.R;
import com.baigu.dms.activity.BaseActivity;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.db.RepositoryFactory;
import com.baigu.dms.domain.db.repository.CityRepository;
import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.Sku;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.AddOrderPresenter;
import com.google.gson.JsonObject;
import com.micky.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 23:21
 */
public class AddOrderPresenterImpl extends BasePresenterImpl implements AddOrderPresenter {
    private OrderAddView mOrderAddView;

    public AddOrderPresenterImpl(BaseActivity activity, OrderAddView orderAddView) {
        super(activity);
        this.mOrderAddView = orderAddView;
    }

    @Override
    public void getDefaultAddress(boolean showDialog) {
        addDisposable(new BaseAsyncTask<Void, Void, Address>(mActivity, showDialog) {

            @Override
            protected RxOptional<Address> doInBackground(Void... params) {
                RxOptional<Address> result = new RxOptional<>();
                try {
                    Call<BaseResponse<Address>> expressComputeCall = ServiceManager.createGsonService(UserService.class).getDefaultAddress(UserCache.getInstance().getUser().getIds());
                    Response<BaseResponse<Address>> expressComputeResponse = expressComputeCall.execute();
                    result.setCode(expressComputeResponse != null && expressComputeResponse.body() != null ? expressComputeResponse.body().getCode() : -1);
                    Address address = null;
                    if (expressComputeResponse != null && expressComputeResponse.body() != null && BaseResponse.SUCCESS.equals(expressComputeResponse.body().getStatus())) {
                        address = expressComputeResponse.body().getData();
                        if (address != null) {
                            List<City> cityList = getCityList(address.getAreaId());
                            if (cityList == null || cityList.size() < 0) {
                                return null;
                            }
                            StringBuilder sbCity = new StringBuilder();
                            for (City city : cityList) {
                                sbCity.append(city.getName());
                            }
                            address.setFullRegionName(sbCity.toString());
                        }
                    }
                    result.setResult(address);
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Address address) {
                super.onPostExecute(address);
                if (mOrderAddView != null) {
                    mOrderAddView.onGetDefaultAddress(address);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderAddView != null) {
                    mOrderAddView.onGetDefaultAddress(null);
                }
            }

        }.execute());
    }

    @Override
    public void expressCompute(final String cityId, String expressId, List<Goods> goodsList) {
        final JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            for (Goods goods : goodsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("goodsId",goods.getIds());
                jsonObject.put("num", String.valueOf(goods.getSkus().get(0).getNumber()));
                jsonObject.put("skuId", String.valueOf(goods.getSkus().get(0).getSkuId()));
                jsonArray.put(jsonObject);
            }
            json.put("goodslist",jsonArray);
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
            jsonArray = null;
        }
        addDisposable(new BaseAsyncTask<String, Void, Double>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.computing_price);
            }

            @Override
            protected RxOptional<Double> doInBackground(String... params) {
                RxOptional<Double> result = new RxOptional<>();
                result.setResult(0.0);
                try {
                    List<City> cityList = getCityList(params[0]);
                    if (cityList == null || cityList.size() <= 0) {
                        return result;
                    }
                    String provinceId = cityList.get(0).getId();
                    json.put("provinceId",provinceId);
                    String cityId = "";
                    if (cityList.size() > 1) {
                        cityId = cityList.get(1).getId();
                    }
                    json.put("cityId",cityId);
                    String areaId = "";
                    if (cityList.size() > 2) {
                        areaId = cityList.get(2).getId();
                    }
                    json.put("areaId",areaId);
                    json.put("logisticsId",params[1]);
                    RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json.toString());
                    Call<BaseResponse<String>> expressComputeCall = ServiceManager.createGsonService(ShopService.class).expressCompute(body);
                    Response<BaseResponse<String>> expressComputeResponse = expressComputeCall.execute();
                    result.setCode(expressComputeResponse != null && expressComputeResponse.body() != null ? expressComputeResponse.body().getCode() : -1);
                    if (expressComputeResponse != null && expressComputeResponse.body() != null && BaseResponse.SUCCESS.equals(expressComputeResponse.body().getStatus())) {
                        result.setResult(expressComputeResponse.body().getData() == null ? 0 : Double.parseDouble(expressComputeResponse.body().getData()));
                    }

                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Double price) {
                super.onPostExecute(price);
                if (mOrderAddView != null) {
                    mOrderAddView.onExpressCompute(price);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderAddView != null) {
                    mOrderAddView.onExpressCompute(0);
                }
            }

        }.execute(cityId, expressId, jsonArray.toString()));
    }

    @Override
    public void addOrder(final List<Goods> goodsList, Address address, boolean saveAddress, String expressId, String remark) {
        final JSONObject paramJsonObject = new JSONObject();
        JSONArray goodsJsonArr = new JSONArray();
        try {

            for (Goods goods : goodsList) {
                JSONObject goodsJson = new JSONObject();
                goodsJson.put("goodsId", goods.getIds());
                goodsJson.put("num", String.valueOf(goods.getSkus().get(0).getNumber()));
                goodsJson.put("skuId", String.valueOf(goods.getSkus().get(0).getSkuId()));
                goodsJsonArr.put(goodsJson);
            }
            paramJsonObject.put("goodslist", goodsJsonArr);
            paramJsonObject.put("logisticsId", expressId);
            paramJsonObject.put("userId", UserCache.getInstance().getUser().getIds());
            paramJsonObject.put("whetherSaveAddress", saveAddress ? "true" : "false");
            if (!TextUtils.isEmpty(address.getId())) {
                paramJsonObject.put("addressId", address.getId());
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("shipTo", address.getName());
                jsonObject.put("phone", address.getPhone());
                jsonObject.put("address", address.getAddress());
                jsonObject.put("regionId", address.getAreaId());
                jsonObject.put("userId", address.getUserid());
                jsonObject.put("default",address.isDefault());
                paramJsonObject.put("address", jsonObject);
            }
            paramJsonObject.put("remark", remark);
            paramJsonObject.put("regionId", address.getRegionid());
        } catch (Exception e) {
            goodsJsonArr = null;
            Logger.e(e, e.getMessage());
        }
        addDisposable(new BaseAsyncTask<JSONObject, Void, OrderDetailResult>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.submitting);
            }

            @Override
            protected RxOptional<OrderDetailResult> doInBackground(JSONObject... params) {
                RxOptional<OrderDetailResult> rxResult = new RxOptional<>();
                try {
                    JSONObject paramJson = params[0];
                    String regionId = "";
                    if (paramJson.has("regionId")) {
                        regionId = paramJson.getString("regionId");
                        paramJson.remove("regionId");
                    }
                    List<City> cityList = getCityList(regionId);

                    if (cityList == null || cityList.size() <= 0) {
                        return null;
                    }
                    City province = cityList.get(0);
                    if (province != null) {
                        paramJson.put("provinceId", province.getId());
                        paramJson.put("province", province.getName());
                    }
                    if (cityList.size() > 1) {
                        City city = cityList.get(1);
                        if (city != null) {
                            paramJson.put("cityId", city.getId());
                            paramJson.put("city", city.getName());
                        }
                    }
                    if (cityList.size() > 2) {
                        City area = cityList.get(2);
                        if (area != null) {
                            paramJson.put("areaId", area.getId());
                            paramJson.put("area", area.getName());
                        }
                    }
                    RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramJsonObject.toString());
                    Call<BaseResponse<OrderDetailResult>> addOrderCall = ServiceManager.createGsonService(ShopService.class).addOrder(body);
                    Response<BaseResponse<OrderDetailResult>> addOrderCallResponse = addOrderCall.execute();
                    rxResult.setCode(addOrderCallResponse != null && addOrderCallResponse.body() != null ? addOrderCallResponse.body().getCode() : -1);
                    if (addOrderCallResponse != null && addOrderCallResponse.body() != null && BaseResponse.SUCCESS.equals(addOrderCallResponse.body().getStatus())) {
                        rxResult.setResult(addOrderCallResponse.body().getData());
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(OrderDetailResult result) {
                super.onPostExecute(result);
                if (mOrderAddView != null) {
                    mOrderAddView.onAddOrder(result);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (mOrderAddView != null) {
                    mOrderAddView.onAddOrder(null);
                }
            }

        }.execute(paramJsonObject));
    }

    @Override
    public void checkGoodsStock(final List<Goods> goodsList) {

        addDisposable(new BaseAsyncTask<List<Goods>, Void, String>(mActivity, true) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setLoadingText(R.string.checking_stock);
            }

            @Override
            protected RxOptional<String> doInBackground(List<Goods>... params) {
                RxOptional<String> result = new RxOptional<>();
                result.setResult(mActivity.getString(R.string.failed_check_goods_stock));

                try {
                    String paramJson = "";
                    try {
                        JSONArray goodsJsonArr = new JSONArray();
                        for (Goods goods : goodsList) {
                            for (Sku sku:goods.getSkus()) {
                                if(sku.getNumber()>0){
                                    JSONObject goodsJson = new JSONObject();
                                    goodsJson.put("goodsId", goods.getIds());
                                    goodsJson.put("skuId", sku.getSkuId());
                                    goodsJson.put("num", String.valueOf(sku.getNumber()));
                                    goodsJsonArr.put(goodsJson);
                                }
                            }
                        }
                        paramJson = goodsJsonArr.toString();
                    } catch (Exception e) {
                        Logger.e(e, e.getMessage());
                    }
                    RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramJson);
                    Call<BaseResponse<Boolean>> checkGoodsStoreCall = ServiceManager.createGsonService(ShopService.class).checkGoodsStock(body);
                    Response<BaseResponse<Boolean>> checkGoodsStoreResponse = checkGoodsStoreCall.execute();
                    result.setCode(checkGoodsStoreResponse != null && checkGoodsStoreResponse.body() != null ? checkGoodsStoreResponse.body().getCode() : -1);
                    if (checkGoodsStoreResponse != null && checkGoodsStoreResponse.body() != null) {
                        if (BaseResponse.SUCCESS.equals(checkGoodsStoreResponse.body().getStatus())) {
                            if (checkGoodsStoreResponse.body().getData()) {
                                result.setResult("success");
                            } else {
                                result.setResult(checkGoodsStoreResponse.body().getDescription());
                            }
                        } else {
                            result.setResult(checkGoodsStoreResponse.body().getDescription());
                        }
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                boolean success = "success".equals(result);
                if (!success) {
                    ViewUtils.showToastError(result);
                }
                if (mOrderAddView != null) {
                    mOrderAddView.onCheckGoodsStock(success);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                ViewUtils.showToastError(mActivity.getString(R.string.failed_check_goods_stock));
                if (mOrderAddView != null) {
                    mOrderAddView.onCheckGoodsStock(false);
                }
            }

        }.execute(goodsList));
    }

    public List<City> getCityList(String cityId) {
       try {
           CityRepository cityRepository = RepositoryFactory.getInstance().getCityRepository();
           List<City> cityList = new ArrayList<>();
           City city = cityRepository.query(cityId);
           while (city != null) {
               cityList.add(city);
               city = cityRepository.query(city.getParentId());
           }
           Collections.reverse(cityList);
           return cityList;
       } catch (Exception e) {
           Logger.e(e, e.getMessage());
       }
       return null;
    }
}
