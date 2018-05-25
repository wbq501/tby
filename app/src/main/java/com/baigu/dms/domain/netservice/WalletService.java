package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.Bank;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.CertificationResult;
import com.baigu.dms.domain.model.Money;
import com.baigu.dms.domain.netservice.response.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description 用户访问接口
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 19:36
 */
public interface WalletService {

    @GET("c/api/my/myMoney")
    Call<BaseResponse<Money>> getMyMoney(@Query("userId") String userId);

    @POST("c/api/my/getRealCertInfo")
    @FormUrlEncoded
    Call<BaseResponse<CertificationResult>> getCertificationResult(@Field("userId") String userId);

    @POST("c/api/my/real")
    @FormUrlEncoded
    Call<BaseResponse> certification(@Field("userId") String userId, @Field("idCard") String idCard, @Field("idCardFront") String cardFront,@Field("idCardBack") String cardBack,@Field("payPwd") String payPwd);


    @POST("/platform/upimage/idCardImg")
    @FormUrlEncoded
    Call<BaseResponse<String>> upLoadImage(@Field("imageFile") String imageFile);


    @POST("c/api/capplywithdraw/applywithdraw")
    @FormUrlEncoded
    Call<BaseResponse> applyWithdraw(@Field("userId") String userId, @Field("pwd") String pwd, @Field("money") String money, @Field("bankid") String bankid);

    @POST("c/api/my/myBank")
    Call<BaseResponse<List<Bank>>> getBankList(@Query("userId") String userId);

    //    @POST("c/api/my/addMyBlank")
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("c/api/my/addMyBank")
    Call<BaseResponse> addBank(@Body RequestBody  blankJson);

    @POST("c/api/my/deleteMyBlank")
    @FormUrlEncoded
    Call<BaseResponse> deleteBank(@Field("userId") String userId, @Field("blankId") String blankId);

    @POST("/c/api/capplywithdraw/applywithdraw")
    @FormUrlEncoded
    Call<BaseResponse> applywithdraw(@Field("pwd") String pwd, @Field("money") String money,@Field("bankid") String bankid);
}
