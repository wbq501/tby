package com.baigu.dms.common.utils;

import android.text.TextUtils;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.domain.cache.UserCache;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/4 11:43
 */
public class ShareUtils {

    public static String getShareTitle() {
        return UserCache.getInstance().getShareInfo() == null || TextUtils.isEmpty(UserCache.getInstance().getShareInfo().getTitle()) ?
                BaseApplication.getContext().getString(R.string.share_title) : UserCache.getInstance().getShareInfo().getTitle();
    }

    public static String getShareContent() {
        String str =  UserCache.getInstance().getShareInfo() == null || TextUtils.isEmpty(UserCache.getInstance().getShareInfo().getContent()) ?
                BaseApplication.getContext().getString(R.string.share_content) : UserCache.getInstance().getShareInfo().getContent();
        str = BaseApplication.getContext().getString(R.string.share_invide_code_tip, UserCache.getInstance().getUser().getInvitecode()) + str;
        return str;
    }

    public static String getShareUrl() {
        return UserCache.getInstance().getShareInfo() == null || TextUtils.isEmpty(UserCache.getInstance().getShareInfo().getUrl()) ?
                BaseApplication.getContext().getString(R.string.share_url) : UserCache.getInstance().getShareInfo().getUrl();
    }
}
