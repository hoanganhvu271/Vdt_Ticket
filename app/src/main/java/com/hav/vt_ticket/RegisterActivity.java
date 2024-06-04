package com.hav.vt_ticket;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Api.ObjectResponse;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etName, etSex, etCCCD, etDOB;
    private Button btnRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_ten);
        etSex = findViewById(R.id.et_gioi_tinh);
        etCCCD = findViewById(R.id.et_cccd);
        etDOB = findViewById(R.id.et_ngay_sinh);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            register();
        });

        etDOB.setOnClickListener(view -> showDatePickerDialog());


    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    etDOB.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        String name = etName.getText().toString();
        String sex = etSex.getText().toString();
        String cccd = etCCCD.getText().toString();
        String dob = etDOB.getText().toString();

        // Call API to register

        ApiService.apiService.register(username, password, name, dob, sex, cccd, email).enqueue(new retrofit2.Callback<ObjectResponse<String>>() {
            @Override
            public void onResponse(retrofit2.Call<ObjectResponse<String>> call, retrofit2.Response<ObjectResponse<String>> response) {
                if (response.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ObjectResponse<String>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
