package com.baigu.dms.domain.netservice;

import com.baigu.dms.domain.model.Address;
import com.baigu.dms.domain.model.Agreement;
import com.baigu.dms.domain.model.BankType;
import com.baigu.dms.domain.model.BrandQuestion;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.model.VersionInfo;
import com.baigu.dms.domain.netservice.common.model.MyDataResult;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.baigu.dms.domain.netservice.common.model.PageResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
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
public interface UserService {

    @POST("platform/api/ptappsecret/getToken")
    @FormUrlEncoded
    Observable<BaseResponse<String>> getToken(@Field("appid") String appid, @Field("secret") String secret);

    @POST("platform/api/ptappsecret/getToken")
    @FormUrlEncoded
    Call<BaseResponse<String>> getTokenSync(@Field("appid") String appid, @Field("secret") String secret);

    @POST("c/api/cmembers/login")
    @FormUrlEncoded
    Call<BaseResponse<User>> login(@Field("phone") String phone, @Field("pwd") String pwd);

    @POST("c/api/cmembers/updatePwd")
    @FormUrlEncoded
    Call<BaseResponse> updatePasswd(@Field("uid") String userId, @Field("oldPwd") String oldPwd, @Field("newPwd") String newPwd);

    @POST("c/api/cmembers/updatePayPwd")
    @FormUrlEncoded
    Call<BaseResponse> updatePayPasswd(@Field("phone") String phone, @Field("pwd") String pwd, @Field("code") String code,@Field("originPwd") String originPwd);

    @POST("c/api/cmembers/resetPayPwd")
    @FormUrlEncoded
    Call<BaseResponse> resetPayPwd(@Field("phone") String phone, @Field("pwd") String pwd, @Field("code") String code,@Field("idCard") String idCard);


    @POST("c/api/cmembers/getCode")
    @FormUrlEncoded
    Call<BaseResponse> sendSMSCode(@Field("type") String type, @Field("phone") String phone);

    @GET("c/api/cmembers/getByInviteCode")
    Call<BaseResponse<String>> getByUserInviteCode(@Query("inviteCode") String invitecode);

    @POST("c/api/cmembers/register")
    @FormUrlEncoded
    Call<BaseResponse> register(@Field("phone") String phone,
                                @Field("realName") String realName,
                                @Field("pwd") String pwd,
                                @Field("code") String code,
                                @Field("inviteCode") String inviteCode,
//                                @Field("blanktype") String banktype,
//                                @Field("blanknumber") String banknumber,
//                                @Field("alipayaccount") String alipayaccount,
                                @Field("wxaccount") String wxaccount);

    @POST("c/api/cmembers/forgotPwd")
    @FormUrlEncoded
    Call<BaseResponse> refindPasswd(@Field("phone") String phone, @Field("pwd") String pwd, @Field("code") String code);

    @GET("c/api/caddress/getAddressByid")
    Call<BaseResponse<PageResult<Address>>> getAddressList(@Query("userId") String userid, @Query("pageNum") int pageNum);

    @GET("c/api/caddress/getDefAddr")
    Call<BaseResponse<Address>> getDefaultAddress(@Query("userId") String userid);

    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("c/api/caddress/addAndUpdateAddress")
    Call<BaseResponse> saveOrUpdateAddress(@Body RequestBody json);

    @POST("c/api/caddress/deleteAddress")
    @FormUrlEncoded
    Call<BaseResponse> deleteAddress(@Field("userId") String userId, @Field("id") String id);

    @GET("c/api/my/myInfo")
    Call<BaseResponse<User>> getMyInfo(@Query("userId") String userId);

    @POST("c/api/my/updateHeadImg")
    @FormUrlEncoded
    Call<BaseResponse> updateHeadImg( @Field("imgUrl") String imgurl);

    @POST("c/api/basic/qiniuToken")
    Call<BaseResponse<String>> loadQiNiuToken();


    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("c/api/my/updateMyInfo")
    Call<BaseResponse> updateMyInfo(@Body RequestBody userInfoJson);

    @GET("c/api/my/myInvitation")
    Call<BaseResponse<PageResult<User>>> getInvitationVerified(@Query("userId") String userId, @Query("pageNum") String pageNum,@Query("inviteStatus") int inviteStatus);

    @GET("c/api/my/myInvitation")
    Call<BaseResponse<PageResult<User>>> getInvitationUnVerify(@Query("userId") String userId,  @Query("pageNum") String pageNum,@Query("inviteStatus") int inviteStatus);

    @POST("c/api/my/memberExamine")
    @FormUrlEncoded
    Call<BaseResponse> invitationVerify(@Field("userId") String userId, @Field("coverUserId") String id);

    @POST("c/api/cmembers/chkOldPhone")
    @FormUrlEncoded
    Call<BaseResponse> chkOldPhone(@Field("userid") String userId, @Field("phone") String phone, @Field("code") String code);

    @POST("c/api/cmembers/updatePhone")
    @FormUrlEncoded
    Call<BaseResponse> updatePhone(@Field("userid") String userId, @Field("phone") String phone, @Field("code") String code);

    @GET("c/api/my/my")
    Call<BaseResponse<MyDataResult>> getMyData(@Query("userId") String userId);

    @GET("c/api/cbrandQa/getCbrandQa")
    Call<BaseResponse<PageResult<BrandQuestion>>> getBrandQuestion(@Query("pageNum") int pageNum);

    @GET("c/api/cbrandQa/getCbrandQaDetailById")
    Call<BaseResponse<BrandQuestion>> getBrandQuestionDetail(@Query("id") String id);

    @GET("c/api/my/aboutUs")
    Call<BaseResponse<String>> getAboutUs();

    //软件使用许可协议
    @GET("c/api/basic/agreement")
    Call<BaseResponse<Agreement>> getUrl();

    @GET("platform/api/ptappsecret/checkAppUpdate")
    Call<BaseResponse<VersionInfo>> checkAppUpdate(@Query("mobileType") String type);

    @GET("platform/api/ptappsecret/getPublicKey")
    Call<BaseResponse<String>> getPublicKey(@Query("phone") String phone, @Query("type") String type);

    @GET("platform/api/ptappsecret/getTcUrl")
    Observable<BaseResponse<String>> getReward();

    @GET("c/api/cmembers/getBankName")
    Call<BaseResponse<List<BankType>>> getBankTypeList();
}
