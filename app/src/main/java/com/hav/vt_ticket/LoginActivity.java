package com.hav.vt_ticket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Api.LoginData;
import com.hav.vt_ticket.Api.ObjectResponse;
import com.hav.vt_ticket.Model.User;
import com.hav.vt_ticket.RoomDatabase.AppDatabase;
import com.hav.vt_ticket.RoomDatabase.UserRoom;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();

            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        LoginData loginData = new LoginData(username, password);
        ApiService.apiService.checkLogin(loginData).enqueue(new retrofit2.Callback<ObjectResponse<User>>() {
            @Override
            public void onResponse(retrofit2.Call<ObjectResponse<User>> call, retrofit2.Response<ObjectResponse<User>> response) {

                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {

                        //save  token
                        String token = response.body().getData().getToken();
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("token", token);

                        //save username
                        myEdit.putString("username", username);
                        myEdit.apply();


                        //save user
                        UserRoom userRoom = response.body().getData().getUser();
                        AppDatabase db = AppDatabase.getInstance(LoginActivity.this);
                        db.userDAO().insertAccount(userRoom);


                        //redirect to destination
                        String destination = getIntent().getStringExtra("to");
                        Log.d("Vu", "onResponse: " + destination);
                        if (destination != null) {
                            Class<?> destinationClass;
                            try {
                                destinationClass = Class.forName(destination);
                                Intent intent = new Intent(LoginActivity.this, destinationClass);
                                Log.d("Vu", "onResponse: " + destinationClass.getSimpleName());
                                startActivity(intent);
                                finish();
                            } catch (ClassNotFoundException e) {
                                Log.d("Vu", "onResponse: ");
                                e.printStackTrace();
                            }
                        }

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ObjectResponse<User>> call, Throwable t) {
                //login failed
            }
        });
    }
}
