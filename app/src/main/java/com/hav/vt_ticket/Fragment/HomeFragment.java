package com.hav.vt_ticket.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hav.vt_ticket.LocationActivity;
import com.hav.vt_ticket.MainActivity;
import com.hav.vt_ticket.R;

import com.google.android.material.card.MaterialCardView;
import com.hav.vt_ticket.TicketActivity;

import android.app.DatePickerDialog;

import java.util.Calendar;

import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    private MainActivity mainActivity;
    private TextView tvCalendar;
    private Button searchBtn;
    private ImageView switchButton;

    private View startPoint;
    private View endPoint;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_home, container, false);

        mainActivity = (MainActivity) getActivity();
        tvCalendar = view.findViewById(R.id.tv_calendar);

        setUpCalendarPicked(tvCalendar, view);

        searchBtn = view.findViewById(R.id.seach_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String start = ((TextView) view.findViewById(R.id.tv_start_point)).getText().toString();
                String end = ((TextView) view.findViewById(R.id.tv_end_point)).getText().toString();
                String date = ((TextView) view.findViewById(R.id.tv_calendar)).getText().toString();



                Intent intent = new Intent(mainActivity, TicketActivity.class);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                intent.putExtra("date", date);

                startActivity(intent);
            }
        });

        switchButton = view.findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView startPoint = (TextView) view.findViewById(R.id.tv_start_point);
                TextView endPoint = (TextView) view.findViewById(R.id.tv_end_point);

                String tmp = startPoint.getText().toString();
                startPoint.setText(endPoint.getText().toString());
                endPoint.setText(tmp);
            }
        });

        startPoint = view.findViewById(R.id.start_point);
        startPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, LocationActivity.class);
                intent.putExtra("tvId", R.id.tv_start_point);
                locationActivityResultLauncher.launch(intent);
            }
        });

        endPoint = view.findViewById(R.id.end_point);
        endPoint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, LocationActivity.class);
                intent.putExtra("tvId", R.id.tv_end_point);
                locationActivityResultLauncher.launch(intent);
            }
        });

        return view;
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
                        TextView startPoint = getView().findViewById(id);
                        startPoint.setText(location);
                    }
                }
            }
    );

    private void setUpCalendarPicked(TextView tvCalendar, View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedDate = day + "/" + (month + 1) + "/" + year;
        tvCalendar.setText(selectedDate);

        MaterialCardView cardView = view.findViewById(R.id.calendar_card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        mainActivity,
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
}