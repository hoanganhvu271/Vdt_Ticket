package com.hav.vt_ticket.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Model.Location;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.R;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.TicketRoom;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder>  {
    private List<Ticket> ticketList;
    private Context context;
    private List<Ticket> ticketOldList;
    private ItemClickListener listener;
    public TicketAdapter(List<Ticket> ticketList,Context context, ItemClickListener listener) {

        this.ticketList = ticketList;
        this.ticketOldList = ticketList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TicketAdapter.TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.TicketViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        Log.d("Vu", ticketList.size() + "");
        if (ticket != null) {
            if (holder.ticketId != null) {
                holder.ticketId.setText(String.valueOf(ticket.getId()));
            }
            if (holder.carName != null) {
                holder.carName.setText(ticket.getCarName());
            }
            if (holder.stPoint != null) {
                holder.stPoint.setText(ticket.getStartPoint());
            }
            if (holder.endPoint != null) {
                holder.endPoint.setText(ticket.getEndPoint());
            }
            if (holder.stTime != null) {
                holder.stTime.setText(ticket.getStartTime());
            }
            if (holder.totalTime != null) {
                holder.totalTime.setText(String.valueOf(ticket.getTotalTime()));
            }
            if (holder.price != null) {
                holder.price.setText(String.valueOf(ticket.getPrice()));
            }
        }

        TicketRoom existingTicket = AppDatabase.getInstance(TicketAdapter.this.context).ticketDAO().getTicketById(ticket.getId());
        if (existingTicket != null && existingTicket.beFollowed) {
            holder.followButton.setImageResource(R.drawable.heart_on);
        } else {
            holder.followButton.setImageResource(R.drawable.heart);
        }

        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TicketRoom existingTicket = AppDatabase.getInstance(TicketAdapter.this.context).ticketDAO().getTicketById(ticket.getId());
                if (existingTicket == null) {
                    TicketRoom ticketRoom = new TicketRoom(ticket.getId(), ticket.getCarId(), ticket.getStartPoint(), ticket.getEndPoint(), ticket.getStartTime(), ticket.getTotalTime(), ticket.getPrice(), ticket.getAmount(), ticket.getCarName(), true);
                    AppDatabase.getInstance(TicketAdapter.this.context).ticketDAO().insertTicket(ticketRoom);
                    holder.followButton.setImageResource(R.drawable.heart_on);
                } else {
                    if(existingTicket.beFollowed){
                        TicketRoom ticketRoom = new TicketRoom(ticket.getId(), ticket.getCarId(), ticket.getStartPoint(), ticket.getEndPoint(), ticket.getStartTime(), ticket.getTotalTime(), ticket.getPrice(), ticket.getAmount(), ticket.getCarName(), false);
                        holder.followButton.setImageResource(R.drawable.heart);
                        AppDatabase.getInstance(TicketAdapter.this.context).ticketDAO().updateTicket(ticketRoom);
                    }
                    else{
                        TicketRoom ticketRoom = new TicketRoom(ticket.getId(), ticket.getCarId(), ticket.getStartPoint(), ticket.getEndPoint(), ticket.getStartTime(), ticket.getTotalTime(), ticket.getPrice(), ticket.getAmount(), ticket.getCarName(), true);
                        holder.followButton.setImageResource(R.drawable.heart_on);
                        AppDatabase.getInstance(TicketAdapter.this.context).ticketDAO().updateTicket(ticketRoom);
                    }

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        if(ticketList != null){
            return ticketList.size();
        }
        return 0;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView ticketId;
        private TextView carName;
        private TextView stPoint;

        private TextView endPoint;

        private TextView stTime;

        private TextView totalTime;

        private TextView price;

        private ImageView followButton;


        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketId = itemView.findViewById(R.id.tv_ticket_id);
            carName = itemView.findViewById(R.id.tv_ticket_car);
            stPoint = itemView.findViewById(R.id.tv_ticket_start_point);
            endPoint = itemView.findViewById(R.id.tv_ticket_end_point);
            stTime = itemView.findViewById(R.id.tv_ticket_start_time);
            totalTime = itemView.findViewById(R.id.tv_ticket_hour);
            price = itemView.findViewById(R.id.tv_ticket_price);

            followButton = itemView.findViewById(R.id.follow_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getBindingAdapterPosition();
//            Log.d("Vu", position + "");
            if (position != RecyclerView.NO_POSITION && listener != null) {
                listener.onItemClick(position);
            }
        }
    }


    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
