package com.hav.vt_ticket.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hav.vt_ticket.Model.Location;
import com.hav.vt_ticket.R;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable {
    private List<Location> locationList;
    private Context context;
    private List<Location> locationOldList;
    private ItemClickListener listener;
    public LocationAdapter(List<Location> locationList,Context context, ItemClickListener listener) {
        this.locationList = locationList;
        this.locationOldList = locationList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position) {
        Location location = locationList.get(position);
        if(location != null){
            holder.name.setText(location.getName());
            holder.description.setText("All stops for this location");
        }
    }

    @Override
    public int getItemCount() {
        if(locationList != null){
            return locationList.size();
        }
        return 0;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView description;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_location_name);
            description = itemView.findViewById(R.id.tv_location_description);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString();
                if(searchKey.isEmpty()){
                    locationList = locationOldList;
                }
                else{
                    List<Location> filteredList = new ArrayList<>();
                    for(Location location: locationOldList){
                        if(location.getName().toLowerCase().contains(searchKey.toLowerCase())){
                            filteredList.add(location);
                        }
                    }
                    locationList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = locationList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                locationList = (List<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
