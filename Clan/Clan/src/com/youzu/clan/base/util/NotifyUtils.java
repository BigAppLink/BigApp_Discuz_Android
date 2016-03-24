package com.youzu.clan.base.util;

import com.kit.utils.NotificationUtils;

/**
 * Created by Zhao on 15/7/21.
 */
public class NotifyUtils extends NotificationUtils {
    public static int NOTIFY = 1234567 + 1;
//    public static void mkNotity(Context context, NotificationManager nm, Intent intent, PendingIntent pendingIntent, String title, String content) {
//        Notification n = new Notification(R.drawable.ic_launcher, "新消息", System.currentTimeMillis());
//        n.flags = Notification.FLAG_AUTO_CANCEL;
////        Intent i = new Intent(arg0.getContext(), NotificationShow.class);
////        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        Intent i = intent;
//        if (i == null) {
//            i = new Intent();
//        }
//
//        PendingIntent contentIntent = pendingIntent;
//        //PendingIntent
//        if (contentIntent == null) {
//            contentIntent = PendingIntent.getActivity(
//                    context,
//                    R.string.app_name,
//                    i,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        n.setLatestEventInfo(
//                context,
//                title,
//                content,
//                contentIntent);
//        nm.notify(NOTIFY, n);
//    }
}
