package com.hav.vt_ticket.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Adapter.NotiAdapter;
import com.hav.vt_ticket.R;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.NotificationRoom;

import java.util.Collections;
import java.util.List;
import android.util.Log;
public class NotiFragment extends Fragment {


    private List<NotificationRoom> notificationList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_noti, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_noti);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        notificationList = AppDatabase.getInstance(getContext()).notificationDAO().getAllNoti();
        Collections.reverse(notificationList);

//        Log.d("Vu", notificationList.size() + "");
        NotiAdapter adapter = new NotiAdapter( notificationList, this.getActivity(), new NotiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Jump to Ticket Info;
            }

        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
