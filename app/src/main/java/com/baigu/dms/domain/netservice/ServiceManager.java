package com.baigu.dms.domain.netservice;


import android.text.TextUtils;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.common.utils.Constants;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.common.utils.SSLUtils;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.User;
import com.baigu.dms.domain.netservice.common.ToStringConverterFactory;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.micky.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Description 网络服务管理类
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/24 19:24
 */
public class ServiceManager {
    private static OkHttpClient sOkHttpClient;

    private static HashMap<String, Object> mStringServiceMap = new HashMap<String, Object>();
    private static HashMap<String, Object> mGsonServiceMap = new HashMap<String, Object>();


    public static <T> T createGsonService(Class<T> t) {
        T service = (T) mGsonServiceMap.get(t.getName());

        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(getEndPoint(t))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            service = retrofit.create(t);
            mGsonServiceMap.put(t.getName(), service);
        }
        return service;
    }


    /**
     * 创建Response为String格式的Retrofit Service
     *
     * @param t   Service类型
     * @param <T>
     * @return
     */
    public static <T> T createStringService(Class<T> t) {
        T service = (T) mStringServiceMap.get(t.getName());

        if (service == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(getEndPoint(t))
                    .addConverterFactory(new ToStringConverterFactory())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            service = retrofit.create(t);
            mStringServiceMap.put(t.getName(), service);
        }
        return service;
    }

    private static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            clientBuilder.sslSocketFactory(SSLUtils.getSSLSocketFactory(), new SSLUtils.TrustAllCertsManager());
            clientBuilder.hostnameVerifier(SSLUtils.getHostnameVerifier());

            //添加Token拦截器
            clientBuilder.addInterceptor(new TokenInterceptor());

            //判断是否开启Http日志
            if (Constants.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        if (!TextUtils.isEmpty(message)) {
                            Logger.t("HttpMessage").i(message);
                        }
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(loggingInterceptor);
            }

            //缓存处理
            final File baseDir = BaseApplication.getContext().getCacheDir();
            if (baseDir != null) {
                final File cacheDir = new File(baseDir, "HttpResponseCache");
                clientBuilder.cache(new Cache(cacheDir, Constants.HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }

//            clientBuilder.addInterceptor(new TokenInterceptor());
            clientBuilder.connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            sOkHttpClient = clientBuilder.build();
        }
        return sOkHttpClient;
    }

    /**
     * 获取EndPoint URL
     *
     * @param t   Service类型
     * @param <T>
     * @return
     */
    public static <T> String getEndPoint(Class<T> t) {
        String endPoint = "";
        if (t.getName().equals(UserService.class.getName())
                || t.getName().equals(HomeService.class.getName())
                || t.getName().equals(BrandStoryService.class.getName())
                || t.getName().equals(ShopService.class.getName())
                || t.getName().equals(NoticeService.class.getName())
                || t.getName().equals(WalletService.class.getName())) {
            endPoint = BaseApplication.getContext().getString(R.string.end_point);
        }
        if ("".equals(endPoint)) {
            throw new IllegalArgumentException("Error: Can't get end point url. Please configure at the method " + ServiceManager.class.getSimpleName() + ".getEndPoint(T t)");
        }
        return endPoint;
    }

    /**
     * Token相关拦截器
     */
    static class TokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            User user = UserCache.getInstance().getUser();
//            String token = TokenManager.getInstance().getToken();
            String token = SPUtils.getObject("token","");
            Request request = chain.request().newBuilder()
                    .addHeader("phone", user == null ? "" : TextUtils.isEmpty(user.getCellphone()) ? "" : user.getCellphone())
                    .addHeader("token", token)
                    .addHeader("device", "android").build();
            return chain.proceed(request);
        }
    }
}
