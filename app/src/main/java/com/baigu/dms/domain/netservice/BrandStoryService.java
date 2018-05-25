package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.model.Comment;
import com.baigu.dms.domain.model.Praise;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/8/20 20:24
 */
public interface BrandStoryService {

    @POST("c/api/brandStory/brandZan")
    @FormUrlEncoded
    Observable<BaseResponse> addPraise(@Field("userId") String userId, @Field("brandId") String brandId);

    @POST("c/api/brandStory/brandComment")
    @FormUrlEncoded
    Call<BaseResponse<String>> addComment(@Field("userId") String userId, @Field("brandId") String brandId, @Field("content") String content);

    @GET("c/api/brandStory/getBrandStory")
    Call<BaseResponse<List<BrandStory>>> getBrandStory(@Query("userId") String userId, @Query("pageNum") String pageNum);

    @POST("c/api/advertis/brandStory/getBrandStoryAll")
    @FormUrlEncoded
    Call<BaseResponse<PageResult<BrandStory>>> getBrandStoryAll(@Field("pageNum") String pageNum);

    @GET("c/api/brandStory/getBrandStoryById")
    Call<BaseResponse<BrandStory>> getBrandStoryById( @Query("id") String id);

    @GET("c/api/brandStory/getCommentList")
    Call<BaseResponse<PageResult<Comment>>> getCommentList(@Query("id") String id, @Query("pageNum") String pageNum);

    @GET("c/api/brandStory/getDzDetail")
    Call<BaseResponse<PageResult<Praise>>> getPraseList(@Query("id") String id, @Query("pageNum") String pageNum);

}
