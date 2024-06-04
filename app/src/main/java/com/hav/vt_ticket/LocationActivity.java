package com.hav.vt_ticket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hav.vt_ticket.Adapter.LocationAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ProgressBar;
import android.util.Log;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class LocationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ProgressBar progressBar;
    private int tvId;
//    private View view;

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

        LinearLayout linearLayout = findViewById(R.id.ll_real_location);
        linearLayout.setOnClickListener(v -> {
            //Return to real user city location
            GetUserCity();

        });
    }

    private void GetUserCity() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            android.location.Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
//                Log.d("Vu", latitude + " " + longitude);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String cityName = addresses.get(0).getAdminArea();
                    OpenPopUp( cityName, latitude, longitude);

//                    Toast.makeText(this, "Your city is: " + cityName, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Không tìm thấy thành phố của bạn :<<", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void OpenPopUp(String cityName, double latitude, double longitude) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_location, null);



        //Visualize map
        MapView mapView = popupView.findViewById(R.id.city_map_view);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        mapView.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(startPoint);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().add(startMarker);



        //Popup window
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        int width = ViewGroup.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        TextView tvCity = popupView.findViewById(R.id.tv_city_name);
        tvCity.setText( "Thành phố của bạn là " + cityName + "?");
        popupWindow.showAtLocation(findViewById(R.id.activity_location), Gravity.CENTER, 0, 0);

        final View dimLayout = findViewById(R.id.dim_layout);
        dimLayout.setVisibility(View.VISIBLE);

        popupWindow.setOnDismissListener(() -> dimLayout.setVisibility(View.GONE));

        Button btnYes = popupView.findViewById(R.id.btn_city_yes);
        Button btnNo = popupView.findViewById(R.id.btn_city_no);


        //On click event

        btnYes.setOnClickListener(v -> {
            String name = cityName;
            Intent intent = new Intent();
            intent.putExtra("location", name);
            intent.putExtra("tvId", tvId);
            setResult(RESULT_OK, intent);
            popupWindow.dismiss();
            finish();
        });

        btnNo.setOnClickListener(v -> {
            popupWindow.dismiss();
            dimLayout.setVisibility(View.GONE);
        });
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
