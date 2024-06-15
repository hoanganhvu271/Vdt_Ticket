package com.hav.vt_ticket.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.hav.vt_ticket.LoginActivity;
import com.hav.vt_ticket.R;
import com.hav.vt_ticket.RegisterActivity;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.UserRoom;

public class ProfileFragment extends Fragment {

    private LinearLayout  notLoginLayout;
    private CardView profileLayout;
    private TextView tvUsername, tvName, tvDob, tvGender, tvCccd, tvEmail;
    private Button registerButton, loginButton;
    private ImageView logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(
                R.layout.fragment_profile, container, false);

        profileLayout = view.findViewById(R.id.layout_profile);
        notLoginLayout = view.findViewById(R.id.not_login_layout);
        logoutButton = view.findViewById(R.id.btn_logout);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.equals("")) {
            profileLayout.setVisibility(View.GONE);
            notLoginLayout.setVisibility(View.VISIBLE);
        } else {
            setupData(view);
            profileLayout.setVisibility(View.VISIBLE);
            notLoginLayout.setVisibility(View.GONE);
        }

        registerButton = view.findViewById(R.id.btn_register);
        loginButton = view.findViewById(R.id.btn_login);

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", "");
            editor.apply();
            profileLayout.setVisibility(View.GONE);
            notLoginLayout.setVisibility(View.VISIBLE);
        });

        return view;
    }

    private void setupData(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvGender = view.findViewById(R.id.tv_sex);
        tvCccd = view.findViewById(R.id.tv_cccd);
        tvEmail = view.findViewById(R.id.tv_email);


        String username = getActivity().getSharedPreferences("MySharedPref", getActivity().MODE_PRIVATE).getString("username", "");
        UserRoom user = AppDatabase.getInstance(getActivity()).userDAO().getUserByUsername(username);

        tvUsername.setText(user.getUsername());
        tvName.setText(user.getName());
        tvDob.setText(user.getDob());
        tvGender.setText(user.getSex());
        tvCccd.setText(user.getCccd());
        tvEmail.setText(user.getEmail());
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", getActivity().MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {

        } else {
            setupData(getView());
            profileLayout.setVisibility(View.VISIBLE);
            notLoginLayout.setVisibility(View.GONE);
        }
    }
}
