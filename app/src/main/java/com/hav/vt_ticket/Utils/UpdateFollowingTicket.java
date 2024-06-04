package com.hav.vt_ticket.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.TicketRoom;

import java.util.List;
import android.util.Log;

import org.json.JSONArray;

public class UpdateFollowingTicket {
    public static void updateFollowingTicket(Context context) {
        List<TicketRoom> ticketRoomList = AppDatabase.getInstance(context).ticketDAO().getAllTickets();
        SharedPreferences sharedPreferences = context.getSharedPreferences("fcm_token", Context.MODE_PRIVATE);
        String fcm_token = sharedPreferences.getString("fcm_token", "");

        JSONArray jsonArray = new JSONArray();
        JSONArray priceList = new JSONArray();

        for (TicketRoom ticketRoom : ticketRoomList) {
            if (ticketRoom.beFollowed) {
                jsonArray.put(ticketRoom.id);
                priceList.put(ticketRoom.price);
            }
        }

        Log.d("Vu", jsonArray.toString());
        ApiService.apiService.updateFollowingTicket(fcm_token, jsonArray.toString(), priceList.toString()).enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                Log.d("Vu", "Updated following ticket");
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Log.d("Vu", "Failed to update following ticket");
            }
        });

    }
}
