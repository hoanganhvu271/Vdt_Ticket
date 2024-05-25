package com.hav.vt_ticket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RouteFragment extends Fragment {

    private TextView tvRouteStart, tvRouteTimeStart, tvRouteEnd, tvRouteTimeEnd, tvRouteTimeTotal;
    private Ticket ticket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_route, container, false);
        tvRouteStart = view.findViewById(R.id.tv_route_start);
        tvRouteTimeStart = view.findViewById(R.id.tv_route_time_start);
        tvRouteEnd = view.findViewById(R.id.tv_route_end);
        tvRouteTimeEnd = view.findViewById(R.id.tv_route_time_end);
        tvRouteTimeTotal = view.findViewById(R.id.tv_route_time_total);

        ticket = (Ticket) getArguments().getSerializable("ticket");

        tvRouteStart.setText(ticket.getStartPoint());
        tvRouteTimeStart.setText(ticket.getStartTime());
        tvRouteEnd.setText(ticket.getEndPoint());

        tvRouteTimeTotal.setText(String.valueOf(ticket.getTotalTime()));

        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            Date startTime = sdf.parse(ticket.getStartTime());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startTime);
            calendar.add(Calendar.HOUR, ticket.getTotalTime());

            String endTime = sdf.format(calendar.getTime());

            tvRouteTimeEnd.setText(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
