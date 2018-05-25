package com.baigu.dms.domain.netservice.common.token;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/7 22:28
 */
public class TokenManager {

    private String mToken = "";


    private static class TokenManagerHolder {
        private static TokenManager sInstance = new TokenManager();
    }

    private TokenManager() {}

    public static final TokenManager getInstance() {
        return TokenManagerHolder.sInstance;
    }
    
    public void setToken(String token) {
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }
}
