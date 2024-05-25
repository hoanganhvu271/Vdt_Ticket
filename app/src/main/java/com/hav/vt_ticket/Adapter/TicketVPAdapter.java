package com.hav.vt_ticket.Adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hav.vt_ticket.Fragment.DetailFragment;
import com.hav.vt_ticket.Fragment.GuideFragment;
import com.hav.vt_ticket.Fragment.HomeFragment;
import com.hav.vt_ticket.Fragment.NotiFragment;
import com.hav.vt_ticket.Fragment.ProfileFragment;
import com.hav.vt_ticket.Fragment.RouteFragment;
import com.hav.vt_ticket.Fragment.TicketFragment;
import com.hav.vt_ticket.Model.Car;
import com.hav.vt_ticket.Model.Ticket;

public class TicketVPAdapter extends FragmentStatePagerAdapter {

    Ticket ticket;
    Car car;
    public TicketVPAdapter(@NonNull FragmentManager fm, int behavior, Ticket ticket, Car car) {
        super(fm, behavior);
        this.ticket = ticket;
        this.car = car;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                DetailFragment detailFragment = new DetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("car", car);
                detailFragment.setArguments(bundle);
                return detailFragment;
            case 1:
                RouteFragment routeFragment = new RouteFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("ticket", ticket);
                routeFragment.setArguments(bundle1);
                return routeFragment;
            case 2:
                return new GuideFragment();
            default:
                return new DetailFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Thông tin";
                break;
            case 1:
                title = "Lộ trình";
                break;
            case 2:
                title = "Hướng dẫn";
                break;
            default:
                title = "Thông tin";
                break;
        }
        return title;
    }
}
