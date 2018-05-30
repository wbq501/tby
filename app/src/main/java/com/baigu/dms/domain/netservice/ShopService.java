package com.baigu.dms.domain.netservice;

import com.alipay.tscenter.biz.rpc.vkeydfp.result.BaseResult;
import com.baigu.dms.domain.model.Order;
import com.baigu.dms.domain.model.Goods;
import com.baigu.dms.domain.model.GoodsCategory;
import com.baigu.dms.domain.model.ShopAdverPictrue;
import com.baigu.dms.domain.netservice.common.model.GoodsResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.OrderDetailResult;
import com.baigu.dms.domain.netservice.common.model.PageResult;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 20:24
 */
public interface ShopService {

    @GET("shop/api/scGoods/getGoodsList")
    Call<BaseResponse<GoodsResult>> getGoodsList();

    @GET("shop/api/scGoods/getGoodsListByCategory")
    Call<BaseResponse<PageResult<Goods>>> getGoodsList(@Query("pageNum") String pageNum, @Query("categroyId") String categroyId);

    @GET("c/api/advertis/pageAdvertis")
    Call<BaseResponse<List<ShopAdverPictrue>>> getPictrue();

    @POST("shop/api/scGoods/getGoodsListByCategory")
    @FormUrlEncoded
    Call<BaseResponse<List<Goods>>> getGoodsListByCategory(@Field("categoryId") String categroyId);

    @GET("shop/api/scGoods/getCategory")
    Call<BaseResponse<List<GoodsCategory>>> getGoodsCategory();

    @POST("shop/api/scGoods/searchGoods")
    @FormUrlEncoded
    Call<BaseResponse<List<Goods>>> searchGoods(@Field("searchName") String searchName);

    @GET("shop/api/scGoods/getGoodsDetail")
    Call<BaseResponse<Goods>> getGoodsDetail(@Query("ids") String id);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("shop/api/scOrder/logisticsReckon")
//    Call<BaseResponse<String>> expressCompute(@Field("provinceId") String provinceId, @Field("cityId") String cityId, @Field("areaId") String areaId, @Field("logisticsId") String logisticsId, @Field("goodsList") String goodsList);
    Call<BaseResponse<List<Double>>> expressCompute(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("shop/api/scOrder/checkGoodsStock")
    Call<BaseResponse<Boolean>> checkGoodsStock(@Body RequestBody body);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("shop/api/scOrder/addOrder")
    Call<BaseResponse<OrderDetailResult>> addOrder(@Body RequestBody orderJson);

    @GET("c/api/my/myOrder")
    Call<BaseResponse<PageResult<Order>>> getOrderList(@Query("userId") String userId, @Query("orderStatus") String status, @Query("pageNum") String pageNum);

    @GET("c/api/my/myOrder")
    Call<BaseResponse<PageResult<Order>>> getOrderListAll(@Query("userId") String userId, @Query("pageNum") String pageNum);

    @GET("c/api/my/getOrderById")
    Call<BaseResponse<OrderDetailResult>> getOrderById(@Query("orderId") String orderId, @Query("orderDate") String orderDate);

    @POST("shop/api/scOrder/cancelOrder")
    @FormUrlEncoded
    Call<BaseResponse> cancelOrder(@Field("orderId") String id, @Field("orderDate") String orderDate);

    @POST("pay/alipay/create")
    @FormUrlEncoded
    Call<BaseResponse<String>> getPayOrder(@Field("orderNo") String id, @Field("orderDate") String orderDate,@Field("payMode") String payMode);


    @POST("pay/alipay/create")
    @FormUrlEncoded
    Call<BaseResponse<String>> getPayOrderMerge(@Field("orderNo") String id, @Field("orderDate") String orderDate,@Field("walletAmount") String walletAmount,@Field("payMode") String payMode,@Field("payPwd") String payPwd);

    @POST("pay/wallet/pay")
    @FormUrlEncoded
    Call<BaseResponse<String>> getPayOrderByWallet(@Field("orderNo") String id, @Field("orderDate") String orderDate,@Field("pwd") String pwd);

    @POST("shop/api/scOrder/updateOrder")
    @FormUrlEncoded
    Call<BaseResponse> updateOrderStatus(@Field("source") String source);

    @POST("shop/api/scOrder/returnOrder")
    @FormUrlEncoded
    Call<BaseResponse> refundOrder(@Field("orderId") String orderId, @Field("orderDate") String orderDate,@Field("refundReason") String refundReason);

    @GET("c/api/my/seeLogistics")
    Call<BaseResponse<String>> queryLogistics(@Query("orderId") String orderId, @Query("orderDate") String orderDate,@Query("userId")String userId);
}