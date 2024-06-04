package com.hav.vt_ticket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hav.vt_ticket.Api.ApiClient;
import com.hav.vt_ticket.Api.ApiService;
import com.hav.vt_ticket.Api.ObjectResponse;
import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.Utils.FormatUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BillingActivity extends AppCompatActivity {

    private Ticket ticket;
    private String z_token;
    private ImageView qrCodeImageView;
    private String formatDateTime;
    private String billCode;
    private Button btnPay, btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // Get the ticket object from the intent
        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("ticket");
        billCode = intent.getStringExtra("code");
        z_token = intent.getStringExtra("z_token");
        qrCodeImageView = findViewById(R.id.qr_image);


        // Date and Time Now and convert toString()
        // Get current date and time
        LocalDateTime now = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formatDateTime = now.format(formatter);
        } else {
            formatDateTime = "2021-09-01 12:00:00";
        }

        //setupData()
        setupData();
        // Send billing to server and receive qr code:
        senBilling();

        // Back to Home
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(BillingActivity.this, MainActivity.class);
            startActivity(intent1);
        });

        //Save QR code to gallery
        btnPay = findViewById(R.id.btn_save_qr);
        btnPay.setOnClickListener(v -> {
            qrCodeImageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = qrCodeImageView.getDrawingCache();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "QR Code", "QR Code");
            Toast.makeText(BillingActivity.this, "Lưu ảnh thành công", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupData() {
        // Set the ticket information to the view
        TextView tvOrderId = findViewById(R.id.tv_order_id);
        TextView tvPrice = findViewById(R.id.tv_price);
        TextView tvDate = findViewById(R.id.tv_date);

        tvOrderId.setText(billCode);
        tvDate.setText(formatDateTime);
        tvPrice.setText(FormatUtils.getMoneyType(ticket.getPrice()));
    }

    private void senBilling() {

        String token = getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("token", "");

        ApiService apiService = ApiClient.createClientApi(token);
        apiService.sendBilling(ticket.getId(), formatDateTime, z_token).enqueue(new Callback<ObjectResponse<String>>() {
            @Override
            public void onResponse(Call<ObjectResponse<String>> call, Response<ObjectResponse<String>> response) {
                if (response.body() != null && response.body().getStatus() == 200){
                    // Get the qr code from the response

                    String qrCode = response.body().getData();
                    // Show the qr code
                    byte[] decodedString = Base64.decode(qrCode.split(",")[1], Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    qrCodeImageView.setImageBitmap(decodedByte);
                }
            }

            @Override
            public void onFailure(Call<ObjectResponse<String>> call, Throwable t) {
                Toast.makeText(BillingActivity.this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
