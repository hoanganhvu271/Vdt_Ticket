package com.hav.vt_ticket.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.hav.vt_ticket.Utils.NotificationUtils;

public class TicketCheckService extends Service {

    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "TicketPriceChannel";
    private static final long INTERVAL = 1 * 60 * 1000;
    private Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = NotificationUtils.createNotification(this, "Viettel Ticket", "Luôn giúp bạn cập nhật giá vé nhanh nhất <3");
        startForeground(NOTIFICATION_ID, notification);
        schedulePriceCheck();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }


    private void schedulePriceCheck() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Thực hiện kiểm tra giá vé ở đây
                // Nếu giá vé giảm, gửi thông báo cho người dùng
                Notification noti = NotificationUtils.createNotification(TicketCheckService.this, "Viettel Ticket", "Giá vé đã giảm, hãy đặt vé ngay!");
                NotificationUtils.showNotification(TicketCheckService.this, noti);
                handler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }
}
