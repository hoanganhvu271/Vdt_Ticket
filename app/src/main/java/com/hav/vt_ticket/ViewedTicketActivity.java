package com.hav.vt_ticket;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.TicketAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.TicketRoom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ViewedTicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewed_ticket);

        RecyclerView recyclerView = findViewById(R.id.rv_ticket_viewed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<TicketRoom> viewedTicketList = AppDatabase.getInstance(this).ticketDAO().getAllTickets();

        List<Ticket> ticketList = new ArrayList<>();

        final int[] apiCallCount = {viewedTicketList.size()};
        for(TicketRoom ticketRoom : viewedTicketList) {
            ApiService.apiService.getTicketById(ticketRoom.getId()).enqueue(new retrofit2.Callback<ApiResponse<Ticket>>() {
                @Override
                public void onResponse(retrofit2.Call<ApiResponse<Ticket>> call, retrofit2.Response<ApiResponse<Ticket>> response) {
                    if (response.body() != null) {
                        Ticket ticket = response.body().getData().get(0);
                        ticketList.add(ticket);
                    }
                    apiCallCount[0]--;
                    if (apiCallCount[0] == 0) {
                        TicketAdapter ticketAdapter = new TicketAdapter(ticketList, ViewedTicketActivity.this, new TicketAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        recyclerView.setAdapter(ticketAdapter);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ApiResponse<Ticket>> call, Throwable t) {
                    apiCallCount[0]--;
                }
            });
        }

    }
}
