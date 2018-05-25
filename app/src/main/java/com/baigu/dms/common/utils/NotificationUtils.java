package com.baigu.dms.common.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.baigu.dms.R;
import com.baigu.dms.activity.ChatActivity;
import com.baigu.dms.broadcast.IMNotificationReceiver;
import com.micky.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/10/27 17:04
 */
public class NotificationUtils {

    private MediaPlayer mMediaPlayer;

    private static class NotificationUtilsHolder {
        private static final NotificationUtils INSTANCE = new NotificationUtils();
    }

    public static NotificationUtils getInstance() {
        return NotificationUtilsHolder.INSTANCE;
    }


    public static final Map<Integer,Integer> NOTIFY_IDS = new HashMap<>();
    public static final int NOTIFY_ID = 55555555;

    public void sendNotification(Context context, String content, boolean increaseCount)  {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) return;

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default_channel");
            builder.setContentText(content);
            builder.setWhen(0);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            try {
                Intent intent = new Intent(context, IMNotificationReceiver.class);
                intent.setAction("com.baigu.dms.IM_NOTIFICATION_RECEIVER");
                builder.setDeleteIntent(PendingIntent.getBroadcast(context, NOTIFY_ID, intent, 0));
            } catch (Exception e) {
                Logger.e(e, e.getMessage());
            }

            // 点击通知重新启动应用
            int count = 0;
            if (!NOTIFY_IDS.containsKey(NOTIFY_ID)) {
                count++;
                NOTIFY_IDS.put(NOTIFY_ID, count);
            } else {
                count = NOTIFY_IDS.get(NOTIFY_ID);
                if (increaseCount) {
                    count++;
                }
                NOTIFY_IDS.put(NOTIFY_ID, count);
            }
            Intent openyt = new Intent(context, ChatActivity.class);
            openyt.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            openyt.setAction(Intent.ACTION_MAIN);
            openyt.addCategory(Intent.CATEGORY_LAUNCHER);
            openyt.putExtra("clearNotify", true);
            int num = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getActivity(context, num, openyt, 0);
            builder.setContentIntent(pendingIntent);

            if (count != 1) {
                builder.setContentTitle(context.getResources().getString(R.string.app_name) + "(" + count + "条)");
            } else {
                builder.setContentTitle(context.getResources().getString(R.string.app_name));
            }

            Notification notification = builder.build();
            notification.ledARGB = 0xff00ff00;
            notification.ledOnMS = 3; //亮的时间
            notification.ledOffMS = 6; //灭的时间
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            if (SPUtils.getObject(SPUtils.KEY_VIBRATE, true)) {
                notification.vibrate = new long[]{0, 300, 300, 300};
            }
            if (SPUtils.getObject(SPUtils.KEY_SOUND, true)) {
                notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification);
            }

            // 发起通知
            notificationManager.notify(NOTIFY_ID, notification);
        } catch (Exception e) {
            Logger.e(e, e.getMessage());
        }
    }

    public void clearAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
            NOTIFY_IDS.clear();
        }
    }
}
