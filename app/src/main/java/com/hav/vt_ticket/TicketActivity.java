package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.LocationAdapter;
import com.hav.vt_ticket.Adapter.TicketAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Location;
import com.hav.vt_ticket.Model.Ticket;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String stPoint;
    private String endPoint;
    private String date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket);
        recyclerView = findViewById(R.id.rv_ticket);

        Intent intent = getIntent();
        stPoint = intent.getStringExtra("start");
        endPoint = intent.getStringExtra("end");
        date = intent.getStringExtra("date");

//        Log.d("Vu", "onCreate: " + stPoint + " " + endPoint + " " + date);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = findViewById(R.id.tk_progress_bar);

        loadTicketDataFromApi();

    }

    private void loadTicketDataFromApi() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        ApiService.apiService.findTicket(stPoint, endPoint, date).enqueue(new Callback<ApiResponse<Ticket>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Ticket>> call, @NonNull Response<ApiResponse<Ticket>> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.isSuccessful() && response.body().getStatus() == 200) {

                    List<Ticket> ticketList = response.body().getData();

//                    Log.d("Vu", "onResponse: " + ticketList.size());
                    TicketAdapter ticketAdapter = new TicketAdapter(ticketList, TicketActivity.this, new TicketAdapter.ItemClickListener(){
                        @Override
                        public void onItemClick(int position) {
//                            Toast.makeText(TicketActivity.this, "Item clicked", Toast.LENGTH_SHORT).show();
                        }

                    });
                    recyclerView.setAdapter(ticketAdapter);
                    ticketAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Ticket>> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Toast.makeText(TicketActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
