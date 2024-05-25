package com.hav.vt_ticket.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.R;
import com.hav.vt_ticket.RoomDatabase.NotificationRoom;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.NotiViewHolder>{

    private List<NotificationRoom> notificationList;
    private Context context;

    private OnItemClickListener listener;

    public NotiAdapter(List<NotificationRoom> notificationRooms, Context context, OnItemClickListener listener) {
        this.notificationList = notificationRooms;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_noti, parent, false);
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotiViewHolder holder, int position) {
        Log.d("Vu", "notificationList is " + (notificationList == null ? "null" : "not null"));
        Log.d("Vu",  notificationList.size() + "");
        NotificationRoom notification = notificationList.get(position);
        holder.tvTitle.setText(notification.getTitle());
        holder.tvContent.setText(notification.getContent());
    }

    @Override
    public int getItemCount() {
        if(notificationList == null) {
            return 0;
        }
        return notificationList.size();
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTitle;
        private TextView tvContent;

        public NotiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_noti_title);
            tvContent = itemView.findViewById(R.id.tv_noti_content);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(position);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
