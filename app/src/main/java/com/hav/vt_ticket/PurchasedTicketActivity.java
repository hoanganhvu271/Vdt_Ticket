package com.hav.vt_ticket;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.TicketAdapter;
import com.hav.vt_ticket.Api.ApiClient;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Ticket;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchasedTicketActivity extends AppCompatActivity {

    private List<Ticket> ticketList;
    private ProgressBar progressBar;
    private LinearLayout emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchased_ticket);

        RecyclerView recyclerView = findViewById(R.id.rv_ticket_purchased);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        String token = getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("token", "");
        ApiService apiService = ApiClient.createClientApi(token);


        //effect
        progressBar = findViewById(R.id.tk_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        emptyView = findViewById(R.id.layout_empty);


        // Call API to get purchased ticket
         apiService.getPurchasedTicket().enqueue(new Callback<ApiResponse<Ticket>>() {
            @Override
            public void onResponse(Call<ApiResponse<Ticket>> call, Response<ApiResponse<Ticket>> response) {
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    ticketList = response.body().getData();
                    Collections.reverse(ticketList);
                    TicketAdapter ticketAdapter = new TicketAdapter(ticketList, PurchasedTicketActivity.this, new TicketAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });

                    recyclerView.setAdapter(ticketAdapter);

                    progressBar.setVisibility(View.GONE);
                    if(ticketList.isEmpty()){
                        emptyView.setVisibility(View.VISIBLE);
                    }
                }
            }

             @Override
             public void onFailure(Call<ApiResponse<Ticket>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
             }


         });

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
}
