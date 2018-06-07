package com.baigu.dms.presenter.impl;

import android.app.Activity;
import android.text.TextUtils;

import com.baigu.dms.R;
import com.baigu.dms.common.utils.BaseAsyncTask;
import com.baigu.dms.common.utils.RxOptional;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BuyNum;
import com.baigu.dms.domain.model.City;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.ShopService;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.presenter.BuyNumPresenter;
import com.micky.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class BuyNumPresenterimpl extends BasePresenterImpl implements BuyNumPresenter{

    private BuyNumView buyNumView;

    public BuyNumPresenterimpl(Activity activity,BuyNumView buyNumView) {
        super(activity);
        this.buyNumView = buyNumView;
    }

    @Override
    public void buyNum(final List<Goods> goodsList) {
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
            paramJsonObject.put("userId", UserCache.getInstance().getUser().getIds());
        } catch (Exception e) {
            goodsJsonArr = null;
            Logger.e(e, e.getMessage());
        }
        addDisposable(new BaseAsyncTask<JSONObject,Void,BuyNum>(mActivity,true){

            @Override
            protected RxOptional<BuyNum> doInBackground(JSONObject... params) {
                RxOptional<BuyNum> rxResult = new RxOptional<>();
                BuyNum buyNum = new BuyNum();
                try {
                    JSONObject paramJson = params[0];
                    String regionId = "";
                    if (paramJson.has("regionId")) {
                        regionId = paramJson.getString("regionId");
                        paramJson.remove("regionId");
                    }
                    RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),paramJsonObject.toString());
                    Call<BaseResponse<String>> addOrderCall = ServiceManager.createGsonService(ShopService.class).preCheckOrder(body);
                    Response<BaseResponse<String>> addOrderCallResponse = addOrderCall.execute();
                    rxResult.setCode(addOrderCallResponse != null && addOrderCallResponse.body() != null ? addOrderCallResponse.body().getCode() : -1);
                    if (addOrderCallResponse != null && addOrderCallResponse.body() != null) {
                        buyNum.setCode(addOrderCallResponse.body().getCode());
                        buyNum.setResult(addOrderCallResponse.body().getData() == null ? addOrderCallResponse.body().getMessage() : addOrderCallResponse.body().getData());
                        rxResult.setResult(buyNum);
                    }
                } catch (Exception e) {
                    Logger.e(e, e.getMessage());
                }
                return rxResult;
            }

            @Override
            protected void onPostExecute(BuyNum s) {
                super.onPostExecute(s);
                if (buyNumView != null){
                    buyNumView.isBuy(s);
                }
            }

            @Override
            protected void doOnError() {
                super.doOnError();
                if (buyNumView != null){
                    buyNumView.isBuy(null);
                }
            }
        }.execute(paramJsonObject));
    }
}
