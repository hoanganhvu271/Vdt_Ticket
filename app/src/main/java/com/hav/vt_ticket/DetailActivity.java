package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.hav.vt_ticket.Api.ObjectResponse;
import com.hav.vt_ticket.Model.Car;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.Utils.FormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Ticket ticketDetail;
    private Car carDetail;

    private TextView priceTextView;
    private ProgressBar progressBar;

    private Button payButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tabLayout = findViewById(R.id.tbl_detail_ticket);
        viewPager = findViewById(R.id.vp_detail_ticket);
        priceTextView = findViewById(R.id.tv_detail_ticket_price);
        progressBar = findViewById(R.id.tk_progress_bar);

        Intent intent = getIntent();
        int ticketId = intent.getIntExtra("ticket", 0);
        Log.d("Vu", "onCreate: " + ticketId);
        setUpData(ticketId);

        payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(v -> {
           clickPayButton();
        });


    }

    private void clickPayButton() {
        String token = getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("token", "");
        if(token.isEmpty()){
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            intent.putExtra("to", PaymentActivity.class.getSimpleName());
            startActivity(intent);
        } else {
            Intent intent = new Intent(DetailActivity.this, PaymentActivity.class);
            intent.putExtra("ticket", ticketDetail);
            intent.putExtra("car", carDetail);
            startActivity(intent);
        }


    }

    private void setUpData(int ticketId) {
        progressBar.setVisibility(android.view.View.VISIBLE);
        ApiService.apiService.getTicketById(ticketId).enqueue(new Callback<ObjectResponse<Ticket>>() {
            @Override
            public void onResponse(Call<ObjectResponse<Ticket>> call, Response<ObjectResponse<Ticket>> response) {

                if (response.body().getData() != null) {
                    Ticket ticket = response.body().getData();

                    if (ticket != null) {
                        ticketDetail = ticket;
                        priceTextView.setText(FormatUtils.getMoneyType(ticket.getPrice()));
                        ApiService.apiService.getCarById(ticketDetail.getCarId()).enqueue(new Callback<ApiResponse<Car>>() {
                            @Override
                            public void onResponse(Call<ApiResponse<Car>> call, Response<ApiResponse<Car>> response) {
                                progressBar.setVisibility(android.view.View.GONE);
                                if (response.body() != null) {
                                    Car car = response.body().getData().get(0);
                                    if (car != null) {
                                        carDetail = car;
                                    }
                                }
                                if (ticketDetail != null && carDetail != null) {
                                    TicketVPAdapter adapter = new TicketVPAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, ticketDetail, carDetail);
                                    viewPager.setAdapter(adapter);
                                    tabLayout.setupWithViewPager(viewPager);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse<Car>> call, Throwable t) {
                                progressBar.setVisibility(android.view.View.GONE);
                                Toast.makeText(DetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ObjectResponse<Ticket>> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
