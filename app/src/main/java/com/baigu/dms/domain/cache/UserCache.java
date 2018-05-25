package com.baigu.dms.domain.cache;

import com.baigu.dms.common.utils.AESUtils;
import com.baigu.dms.common.utils.SPUtils;
import com.baigu.dms.domain.model.ShareInfo;
import com.baigu.dms.domain.model.User;
import com.google.gson.GsonBuilder;

/**
 * @Description
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/6/8 0:10
 */
public class UserCache {

    private boolean mChatting = false;
    private boolean mForceUpdate = false;
    private ShareInfo mShareInfo;
    private User mUser;


    private static class UserCacheHolder {
        private static UserCache sInstance = new UserCache();
    }

    private UserCache() {
    }

    public static final UserCache getInstance() {
        return UserCache.UserCacheHolder.sInstance;
    }

    public void destroy() {
        mChatting = false;
        mForceUpdate = false;
        mUser = null;
        mShareInfo = null;
    }

    public User getUser() {
        if (mUser == null) {
            String userStr = SPUtils.getObject(SPUtils.KEY_USER, "");
            String key = SPUtils.getObject(SPUtils.KEY_LOGIN_TIME, "dms_***");
            userStr = AESUtils.decrypt(key, userStr);
            mUser = new GsonBuilder().create().fromJson(userStr, User.class);
        }
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
        String userStr = user == null ? "" : new GsonBuilder().create().toJson(user);
        String key = SPUtils.getObject(SPUtils.KEY_LOGIN_TIME, "dms_***");
        String userEncrypt = AESUtils.encrypt(key, userStr);
        SPUtils.putObject(SPUtils.KEY_USER, userEncrypt);
    }

    public void setSound(boolean b) {
        SPUtils.putObject(SPUtils.KEY_SOUND + getUser().getCellphone(), b);
    }

    public boolean getSound() {
        return SPUtils.getObject(SPUtils.KEY_SOUND + getUser().getCellphone(), false);
    }

    public void setVibrate(boolean b) {
        SPUtils.putObject(SPUtils.KEY_VIBRATE + getUser().getCellphone(), b);
    }

    public boolean getVibrate() {
        return SPUtils.getObject(SPUtils.KEY_VIBRATE + getUser().getCellphone(), false);
    }

    public void setGesture(String passwd) {
        String userEncrypt = AESUtils.encrypt(getUser().getCellphone(), passwd);
        SPUtils.putObject(SPUtils.KEY_GESTURE + getUser().getCellphone(), userEncrypt);
    }

    public String getGesture() {
        String gesture = SPUtils.getObject(SPUtils.KEY_GESTURE + getUser().getCellphone(), "");
        return AESUtils.decrypt(getUser().getCellphone(), gesture);
    }

    public String getAccount() {
        return SPUtils.getObject(SPUtils.KEY_ACCOUNT, "");
    }

    public void setAccount(String account) {
        SPUtils.putObject(SPUtils.KEY_ACCOUNT, account);
    }

    public void setForceUpdate(boolean b) {
        mForceUpdate = b;
    }

    public boolean isForceUpdate() {
        return mForceUpdate;
    }

    public boolean isChatting() {
        return mChatting;
    }

    public void setChatting(boolean chatting) {
        this.mChatting = chatting;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        mShareInfo = shareInfo;
    }

    public ShareInfo getShareInfo() {
        return mShareInfo;
    }
}
