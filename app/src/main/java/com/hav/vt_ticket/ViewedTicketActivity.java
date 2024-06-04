package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.TicketAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Api.ObjectResponse;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.TicketRoom;
import com.hav.vt_ticket.Utils.UpdateFollowingTicket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ViewedTicketActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private     LinearLayout emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewed_ticket);

        RecyclerView recyclerView = findViewById(R.id.rv_ticket_viewed);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<TicketRoom> viewedTicketList = AppDatabase.getInstance(this).ticketDAO().getAllTickets();
        List<Ticket> ticketList = new ArrayList<>();

        progressBar = findViewById(R.id.tk_progress_bar);
        progressBar.setVisibility(android.view.View.VISIBLE);

        emptyView = findViewById(R.id.layout_empty);

        if (viewedTicketList.isEmpty()) {
            progressBar.setVisibility(android.view.View.GONE);
            emptyView.setVisibility(android.view.View.VISIBLE);
        }

        final int[] apiCallCount = {viewedTicketList.size()};
        for (TicketRoom ticketRoom : viewedTicketList) {

            ApiService.apiService.getTicketById(ticketRoom.getId()).enqueue(new retrofit2.Callback<ObjectResponse<Ticket>>() {
                @Override
                public void onResponse(retrofit2.Call<ObjectResponse<Ticket>> call, retrofit2.Response<ObjectResponse<Ticket>> response) {
                    if (response.body() != null) {
                        Ticket ticket = response.body().getData();
//                        Log.d("Vu", response.body().getData().get(0).getStartPoint());
                        ticketList.add(ticket);
                    }
                    apiCallCount[0]--;
                    if (apiCallCount[0] == 0) {
                        progressBar.setVisibility(android.view.View.GONE);
                        Collections.reverse(ticketList);
                        if(ticketList.isEmpty()){
                            emptyView.setVisibility(android.view.View.VISIBLE);
                        }
                        TicketAdapter ticketAdapter = new TicketAdapter(ticketList, ViewedTicketActivity.this, new TicketAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(ViewedTicketActivity.this, DetailActivity.class);
                                intent.putExtra("ticket", ticketList.get(position).getId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(ticketAdapter);
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ObjectResponse<Ticket>> call, Throwable t) {
                    apiCallCount[0]--;
                    if (apiCallCount[0] == 0) {
                        progressBar.setVisibility(android.view.View.GONE);
                    }
                }
            });
        }

        toolBarSetting();

    }


    private void toolBarSetting() {
        Toolbar toolbar = findViewById(R.id.ticket_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.d("Vu", "onDestroy: " + this.getClass().getSimpleName());
        UpdateFollowingTicket.updateFollowingTicket(this);
    }
}
