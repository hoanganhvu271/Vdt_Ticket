package com.hav.vt_ticket.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.hav.vt_ticket.FollowedTicketActivity;
import com.hav.vt_ticket.LoginActivity;
import com.hav.vt_ticket.PurchasedTicketActivity;
import com.hav.vt_ticket.R;
import com.hav.vt_ticket.ViewedTicketActivity;

public class TicketFragment extends Fragment {

    MaterialCardView followedCardView, viewedCarView, boughtCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_ticket, container, false);
        followedCardView = view.findViewById(R.id.cv_followed_ticket);
        followedCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FollowedTicketActivity.class);
                startActivity(intent);
            }
        });

        viewedCarView = view.findViewById(R.id.cv_viewed_ticket);
        viewedCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewedTicketActivity.class);
                startActivity(intent);
            }
        });

        boughtCardView = view.findViewById(R.id.cv_bought_ticket);
        boughtCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = getActivity().getSharedPreferences("MySharedPref", getActivity().MODE_PRIVATE).getString("token", "");
                Log.d("Vu", token);
                if(token.isEmpty()){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("to", PurchasedTicketActivity.class.getSimpleName());
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getActivity(), PurchasedTicketActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
