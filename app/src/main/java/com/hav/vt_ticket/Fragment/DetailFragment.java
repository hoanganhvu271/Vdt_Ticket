package com.hav.vt_ticket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hav.vt_ticket.Model.Car;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.R;

public class DetailFragment extends Fragment {

    private Car car;
    private LinearLayout serviceLayout;
    private TextView carName, carType, carCapacity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        car = (Car) getArguments().getSerializable("car");

        serviceLayout = view.findViewById(R.id.ll_detail_car_service);
        if (car != null) {
            for (int i = 0; i < car.getService().size(); i++) {
                View serviceView = inflater.inflate(R.layout.item_service, null);
                TextView tvService = serviceView.findViewById(R.id.tv_service_name);
                tvService.setText(car.getService().get(i));
                serviceLayout.addView(serviceView);
            }
        }

        carName = view.findViewById(R.id.tv_detail_car_name);
        carType = view.findViewById(R.id.tv_detail_car_type);
        carCapacity = view.findViewById(R.id.tv_detail_car_capacity);

        carName.setText(car.getName());
        carType.setText(car.getType());
        carCapacity.setText(car.getCapacity() + " chỗ");

        return view;
    }
}
