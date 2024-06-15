package com.hav.vt_ticket;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.hav.vt_ticket.Adapter.TicketAdapter;
import com.hav.vt_ticket.Api.ApiResponse;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.TicketRoom;
import com.hav.vt_ticket.Utils.FormatUtils;
import com.hav.vt_ticket.Utils.UpdateFollowingTicket;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private String stPoint;
    private String tvendPoint;
    private String date;

    private TextView tvCalendar;
    private Button searchBtn;

    private ImageView switchButton, filterButton;

    private View startPoint, endPoint;

    private LinearLayout btnChange, btnSort , emptyView;

    private Dialog dialog;

    private TicketAdapter ticketAdapter;

    private List<Ticket> ticketList, oldTicketList;

    private String selectedTime = "Tất cả";
    private String selectedCar = "Tất cả";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ticket);
        recyclerView = findViewById(R.id.rv_ticket);
        emptyView = findViewById(R.id.layout_empty);

        Intent intent = getIntent();
        stPoint = intent.getStringExtra("start");
        tvendPoint = intent.getStringExtra("end");
        date = intent.getStringExtra("date");

//        Log.d("Vu", "onCreate: " + stPoint + " " + endPoint + " " + date);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ticketList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketList, TicketActivity.this, new TicketAdapter.ItemClickListener(){
            @Override
            public void onItemClick(int position) {

                saveViewedHistory(ticketList.get(position));

                Intent intent = new Intent(TicketActivity.this, DetailActivity.class);
                intent.putExtra("ticket", ticketList.get(position).getId());
//                            Log.d("Vu", "onItemClick: " + ticketList.get(position).getId());
                startActivity(intent);
            }

        });
        recyclerView.setAdapter(ticketAdapter);

        progressBar = findViewById(R.id.tk_progress_bar);
        loadTicketDataFromApi();

        btnChange = findViewById(R.id.btn_change);
        btnChange.setOnClickListener(v -> {
            showDialog();
        });

        btnSort = findViewById(R.id.btn_sort);
        btnSort.setOnClickListener(v -> {
            showSortDialog();
        });

        filterButton = findViewById(R.id.filter);
        filterButton.setOnClickListener(v -> {
            showFilterDialog();
        });

        toolBarSetting();

    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TicketActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter, null);
        builder.setView(view);

        Spinner spinnerTime = view.findViewById(R.id.spinner_time);
        Spinner spinnerCar = view.findViewById(R.id.spinner_car);

        List<String> timeList = Arrays.asList(new String[]{"Tất cả", "0h - 6h", "6h - 12h", "12h - 18h", "18h - 24h"});
        List<String> carList = Arrays.asList(new String[]{"Tất cả", "Hoang Long"});
        ArrayAdapter<String> adapterTime = new ArrayAdapter<>(TicketActivity.this, android.R.layout.simple_spinner_item, timeList);
        ArrayAdapter<String> adapterCar = new ArrayAdapter<>(TicketActivity.this, android.R.layout.simple_spinner_item, carList);
        adapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(adapterTime);
        spinnerCar.setAdapter(adapterCar);


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);

        spinnerTime.setSelection(((ArrayAdapter<String>)spinnerTime.getAdapter()).getPosition(selectedTime));
        spinnerCar.setSelection(((ArrayAdapter<String>)spinnerCar.getAdapter()).getPosition(selectedCar));

        Button buttonConfirm = view.findViewById(R.id.button_confirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy các tiêu chí lọc từ Spinner
                selectedTime = spinnerTime.getSelectedItem().toString();
                selectedCar = spinnerCar.getSelectedItem().toString();

                // Lọc dữ liệu theo các tiêu chí đã chọn
                List<Ticket> filteredList = new ArrayList<>();




                for (Ticket ticket : oldTicketList) {
                    if (selectedTime.equals("Tất cả") && selectedCar.equals("Tất cả")) {
                        filteredList = oldTicketList;
                        break;
                    } else if (selectedTime.equals("Tất cả") && !selectedCar.equals("Tất cả")) {
                        if (ticket.getCarName().equals(selectedCar)) {
                            filteredList.add(ticket);
                        }
                    } else if (!selectedTime.equals("Tất cả") && selectedCar.equals("Tất cả")) {
                        int time = Integer.parseInt(FormatUtils.getHour(ticket.getStartTime()).split(":")[0]);
                        if (selectedTime.equals("0h - 6h") && time >= 0 && time < 6) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("6h - 12h") && time >= 6 && time < 12) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("12h - 18h") && time >= 12 && time < 18) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("18h - 24h") && time >= 18 && time < 24) {
                            filteredList.add(ticket);
                        }
                    } else {
                        int time = ticket.getTotalTime();
                        if (selectedTime.equals("0h - 6h") && time >= 0 && time < 6 && ticket.getCarName().equals(selectedCar)) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("6h - 12h") && time >= 6 && time < 12 && ticket.getCarName().equals(selectedCar)) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("12h - 18h") && time >= 12 && time < 18 && ticket.getCarName().equals(selectedCar)) {
                            filteredList.add(ticket);
                        } else if (selectedTime.equals("18h - 24h") && time >= 18 && time < 24 && ticket.getCarName().equals(selectedCar)) {
                            filteredList.add(ticket);
                        }
                    }
                }
                // Cập nhật dữ liệu lọc vào RecyclerView
                updateData(filteredList);
                // Đóng Dialog
                dialog.dismiss();

            }
        });



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

    private void showSortDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sort_ticket);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        setUpSortItemCLick();


    }

    private void setUpSortItemCLick() {
        LinearLayout sortPrice = dialog.findViewById(R.id.sort_by_price);
        sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                List<Ticket> sortedList = new ArrayList<>(ticketList);
                sortedList.sort((o1, o2) -> {
                    if (o1.getPrice() > o2.getPrice()) {
                        return 1;
                    } else if (o1.getPrice() < o2.getPrice()) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                updateData(sortedList);
            }
        });

        LinearLayout sortTime = dialog.findViewById(R.id.sort_by_time);
        sortTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                List<Ticket> sortedList = new ArrayList<>(ticketList);
                sortedList.sort((o1, o2) -> {
                    if (o1.getTotalTime() > o2.getTotalTime()) {
                        return 1;
                    } else if (o1.getTotalTime() < o2.getTotalTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                updateData(sortedList);
            }
        });

        LinearLayout sortStartTime = dialog.findViewById(R.id.sort_by_start);
        sortStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                List<Ticket> sortedList = new ArrayList<>(ticketList);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                sortedList.sort((o1, o2) -> {
                    try {
                        Date date1 = format.parse(o1.getStartTime());
                        Date date2 = format.parse(o2.getStartTime());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0;
                    }
                });
                updateData(sortedList);
            }
        });
    }

    private void updateData(List<Ticket> sortedList) {
        ticketList.clear();
        ticketList.addAll(sortedList);
        ticketAdapter.notifyDataSetChanged();
    }

    private void showDialog() {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        tvCalendar = dialog.findViewById(R.id.tv_calendar);
        setUpCalendarPicked(tvCalendar, dialog);

        searchBtn = dialog.findViewById(R.id.seach_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stPoint = ((TextView) dialog.findViewById(R.id.tv_start_point)).getText().toString();
                tvendPoint = ((TextView) dialog.findViewById(R.id.tv_end_point)).getText().toString();
                date = ((TextView) dialog.findViewById(R.id.tv_calendar)).getText().toString();
                loadTicketDataFromApi();
                dialog.dismiss();
            }
        });

        switchButton = dialog.findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView startPoint = (TextView) dialog.findViewById(R.id.tv_start_point);
                TextView endPoint = (TextView) dialog.findViewById(R.id.tv_end_point);

                String tmp = startPoint.getText().toString();
                startPoint.setText(endPoint.getText().toString());
                endPoint.setText(tmp);
            }
        });

        startPoint = dialog.findViewById(R.id.start_point);
        startPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TicketActivity.this, LocationActivity.class);
                intent.putExtra("tvId", R.id.tv_start_point);
                locationActivityResultLauncher.launch(intent);
            }
        });

        endPoint = dialog.findViewById(R.id.end_point);
        endPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TicketActivity.this, LocationActivity.class);
                intent.putExtra("tvId", R.id.tv_end_point);
                locationActivityResultLauncher.launch(intent);
            }
        });

    }



    private void loadTicketDataFromApi() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        ApiService.apiService.findTicket(stPoint, tvendPoint, date).enqueue(new Callback<ApiResponse<Ticket>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Ticket>> call, @NonNull Response<ApiResponse<Ticket>> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                if (response.isSuccessful() && response.body().getStatus() == 200) {
                    List<Ticket> newTickets = response.body().getData();
                    ticketList.clear();
                    ticketList.addAll(newTickets);
                    oldTicketList = new ArrayList<>(newTickets);
                    ticketAdapter.notifyDataSetChanged();

                    if(newTickets.size() == 0){
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    else{
                        emptyView.setVisibility(View.GONE);
                    }

                }
                else{
                    emptyView.setVisibility(View.VISIBLE);
                    ticketList.clear();
                    ticketAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Ticket>> call, Throwable t) {
                emptyView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Toast.makeText(TicketActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveViewedHistory(Ticket ticket){
        AppDatabase db = AppDatabase.getInstance(this);
        TicketRoom existedTicket = db.ticketDAO().getTicketById(ticket.getId());
        if (existedTicket != null){
            return;
        }
        else{
            TicketRoom ticketRoom = new TicketRoom(ticket.getId(), ticket.getCarId(), ticket.getStartPoint(), ticket.getEndPoint(), ticket.getStartTime(),ticket.getTotalTime(), ticket.getPrice(), ticket.getAmount(), ticket.getCarName(), false);
            db.ticketDAO().insertTicket(ticketRoom);
        }
    }

    private final ActivityResultLauncher<Intent> locationActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
//                Log.d("Vu", "OK");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String location = data.getStringExtra("location");
                        int id = data.getIntExtra("tvId", 0);
//                        Log.d("Vu", id + "");
                        TextView startPoint = dialog.findViewById(id);
                        startPoint.setText(location);
                    }
                }
            }
    );

    private void setUpCalendarPicked(TextView tvCalendar, Dialog dialog) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedDate = day + "/" + (month + 1) + "/" + year;
        tvCalendar.setText(selectedDate);

        MaterialCardView cardView = dialog.findViewById(R.id.calendar_card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        TicketActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                                tvCalendar.setText(selectedDate);
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
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
