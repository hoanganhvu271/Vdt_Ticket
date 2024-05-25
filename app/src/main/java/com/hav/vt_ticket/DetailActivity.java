package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hav.vt_ticket.Adapter.TicketVPAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Car;
import com.hav.vt_ticket.Model.Ticket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Ticket ticketDetail;
    private Car carDetail;

    private TextView priceTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tabLayout = findViewById(R.id.tbl_detail_ticket);
        viewPager = findViewById(R.id.vp_detail_ticket);
        priceTextView = findViewById(R.id.tv_detail_ticket_price);

        Intent intent = getIntent();
        int ticketId = intent.getIntExtra("ticket", 0);
//        Log.d("Vu", "onCreate: " + ticketId);
        setUpData(ticketId);


    }

    private void setUpData(int ticketId) {
        ApiService.apiService.getTicketById(ticketId).enqueue(new Callback<ApiResponse<Ticket>>() {
            @Override
            public void onResponse(Call<ApiResponse<Ticket>> call, Response<ApiResponse<Ticket>> response) {
                if (response.body() != null) {
                    Log.d("Vu", "onResponse: " + response.body().getData());
                }
                    if (response.body().getData() != null && !response.body().getData().isEmpty()) {
                        Ticket ticket = response.body().getData().get(0);

                    if (ticket != null) {
                        ticketDetail = ticket;
                        priceTextView.setText(ticket.getPrice() + " VND");
                        ApiService.apiService.getCarById(ticketDetail.getCarId()).enqueue(new Callback<ApiResponse<Car>>(){
                            @Override
                            public void onResponse(Call<ApiResponse<Car>> call, Response<ApiResponse<Car>> response) {
                                if (response.body() != null) {
                                    Car car = response.body().getData().get(0);
                                    if (car != null) {
                                        carDetail = car;
                                    }
                                }
                                if(ticketDetail != null && carDetail != null){
                                    TicketVPAdapter adapter = new TicketVPAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, ticketDetail, carDetail);
                                    viewPager.setAdapter(adapter);
                                    tabLayout.setupWithViewPager(viewPager);
                                }
                            }
                            @Override
                            public void onFailure(Call<ApiResponse<Car>> call, Throwable t) {
                                Toast.makeText(DetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Ticket>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
