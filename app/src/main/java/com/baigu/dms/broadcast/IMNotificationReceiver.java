package com.baigu.dms.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baigu.dms.common.utils.NotificationUtils;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/27 17:08
 */
public class IMNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NotificationUtils.getInstance().clearAllNotification(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
