package com.hav.vt_ticket.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.hav.vt_ticket.R;

public class NotificationUtils {

    private static final String CHANNEL_ID = "MyServiceChannel";

    public static Notification createNotification(Context context, String title, String content, PendingIntent pendingIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }

        return new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo)
                .build();
    }

    public static void showNotification(Context context, Notification notification) {
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.notify(1, notification);
    }
}