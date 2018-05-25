package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.ShareInfo;
import com.baigu.dms.domain.netservice.common.model.HomeResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.BasicDataResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @Description 首页接口
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 19:36
 */
public interface HomeService {

    @GET("c/api/brandStory/sy")
    Call<BaseResponse<HomeResult>> getHomeData(@Query("userId") String userId);

    @GET("c/api/basic/getBasic")
    Call<BaseResponse<BasicDataResult>> getBasicData(@Query("version") int version);

    @GET("platform/api/ptappsecret/appShare")
    Call<BaseResponse<ShareInfo>> getShareInfo();
}
