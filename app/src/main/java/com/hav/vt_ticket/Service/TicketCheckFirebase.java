package com.hav.vt_ticket.Service;

import android.app.Notification;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Api.ObjectResponse;
import com.hav.vt_ticket.DetailActivity;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.NotificationRoom;
import com.hav.vt_ticket.Utils.NotificationUtils;
import com.hav.vt_ticket.MainActivity;
import android.content.Intent;
import android.app.PendingIntent;

import java.util.List;

public class TicketCheckFirebase extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
//        Log.d("Vu", "hehe");

        if (!message.getData().isEmpty()) {
//            Log.d("Vu", "Message data payload: " + message.getData());
            String title = message.getData().get("title");
            String body = message.getData().get("body");
            String ticketId = message.getData().get("ticket");

            Intent intent = new Intent(TicketCheckFirebase.this, DetailActivity.class);
            intent.putExtra("ticket", Integer.parseInt(ticketId));
            PendingIntent pendingIntent = PendingIntent.getActivity(TicketCheckFirebase.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification noti = NotificationUtils.createNotification(TicketCheckFirebase.this, title, body, pendingIntent);
            NotificationUtils.showNotification(TicketCheckFirebase.this, noti);
//            getTicketInfo(title, body, ticketId);


            //Add to database:
            NotificationRoom notificationRoom = new NotificationRoom(title, body, "DetailActivity", Integer.parseInt(ticketId));
            AppDatabase.getInstance(TicketCheckFirebase.this).notificationDAO().insertNoti(notificationRoom);
        }
    }





    @Override
    public void onNewToken(@NonNull String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("fcm_token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fcm_token", token);
        editor.apply();
        Log.d("Vu", token);
        super.onNewToken(token);
    }
}
