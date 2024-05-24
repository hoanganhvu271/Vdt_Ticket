package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.LocationAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Location;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.widget.ProgressBar;

public class LocationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private int tvId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        recyclerView = findViewById(R.id.rv_location);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        tvId = intent.getIntExtra("tvId", 0);

        progressBar = findViewById(R.id.progress_bar);
        getLocationList();

    }

    private void getLocationList() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        ApiService.apiService.getLocation().enqueue(new Callback<ApiResponse<Location>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Location>> call, @NonNull Response<ApiResponse<Location>> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.isSuccessful() && response.body().getStatus() == 200) {

                    List<Location> locationList = response.body().getData();
                    LocationAdapter locationAdapter = new LocationAdapter(locationList, LocationActivity.this, new LocationAdapter.ItemClickListener(){
                        @Override
                        public void onItemClick(int position) {
                            String name = locationList.get(position).getName();
                            Intent intent = new Intent();
                            intent.putExtra("location", name);
                            intent.putExtra("tvId", tvId);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    });
                    recyclerView.setAdapter(locationAdapter);

                    searchView = findViewById(R.id.sv_location);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            locationAdapter.getFilter().filter(query);
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            locationAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Location>> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
//                Log.d("vu", t.getMessage());
                Toast.makeText(LocationActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
