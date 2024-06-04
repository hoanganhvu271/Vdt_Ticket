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
import com.hav.vt_ticket.Utils.FormatUtils;

public class RouteFragment extends Fragment {

    private TextView tvRouteStart, tvRouteTimeStart, tvRouteEnd, tvRouteTimeEnd, tvRouteTimeTotal, tvRouteDayStart , tvRouteDayEnd;
    private Ticket ticket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_route, container, false);
        tvRouteStart = view.findViewById(R.id.tv_route_start);
        tvRouteTimeStart = view.findViewById(R.id.tv_route_time_start);
        tvRouteDayStart = view.findViewById(R.id.tv_route_day_start);
        tvRouteEnd = view.findViewById(R.id.tv_route_end);
        tvRouteTimeEnd = view.findViewById(R.id.tv_route_time_end);
        tvRouteDayEnd = view.findViewById(R.id.tv_route_day_end);
        tvRouteTimeTotal = view.findViewById(R.id.tv_route_time_total);

        ticket = (Ticket) getArguments().getSerializable("ticket");

        String timeStart = ticket.getStartTime().split(" ")[1];
        String dateStart = ticket.getStartTime().split(" ")[0];


        tvRouteStart.setText(ticket.getStartPoint());
        tvRouteTimeStart.setText(timeStart);
        tvRouteDayStart.setText(dateStart);
        tvRouteEnd.setText(ticket.getEndPoint());

        tvRouteTimeTotal.setText("+" + ticket.getTotalTime() + " giờ");

        try {
            String endTime = FormatUtils.getEndDay(ticket.getStartTime(), ticket.getTotalTime());
            String timeEnd = endTime.split(" ")[1];
            String dayEnd = endTime.split(" ")[0];
            tvRouteTimeEnd.setText(timeEnd);
            tvRouteDayEnd.setText(dayEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
