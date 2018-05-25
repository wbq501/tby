package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.ExpressInfo;
import com.baigu.dms.domain.model.ExpressType;
import com.baigu.dms.domain.model.LogisticsInfo;

import java.util.ArrayList;


import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ExpressService {
    @FormUrlEncoded
    @POST("query")
    Call<LogisticsInfo<ExpressInfo>> getExpress(@Field("type") String type, @Field("postid") String postid);

    @FormUrlEncoded
    @POST("autonumber/auto")
    Call<ArrayList<ExpressType>> getExpressAuto(@Field("num") String postid);

    @FormUrlEncoded
    @POST("query")
    Observable<LogisticsInfo> getExpress2(@Field("type") String type, @Field("postid") String postid);
}
