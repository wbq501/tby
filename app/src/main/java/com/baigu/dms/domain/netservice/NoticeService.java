package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.Notice;
import com.baigu.dms.domain.netservice.common.model.PageResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;

import java.util.List;

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
 * @Date 2017/9/21 18:17
 */
public interface NoticeService {

    @POST("c/api/cbullitin/getBullitinTitle")
    @FormUrlEncoded
    Call<BaseResponse<PageResult<Notice>>> getNoticeList(@Field("pageNum") String pageNum);

    @POST("c/api/cbullitin/getBullitinContent")
    Call<BaseResponse<Notice>> getNotice(@Query("id") String id);
}
