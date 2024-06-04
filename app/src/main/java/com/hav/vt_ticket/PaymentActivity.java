package com.hav.vt_ticket;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hav.vt_ticket.Model.Ticket;
import com.hav.vt_ticket.Payment.CreateOrder;

import org.json.JSONObject;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;
import com.hav.vt_ticket.Utils.FormatUtils;

public class PaymentActivity extends AppCompatActivity {
    private LinearLayout btnPay;
    private Ticket ticket;
    private TextView tvPrice, tvStartPoint, tvEndPoint, tvTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zalo_pay);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        ticket = (Ticket) getIntent().getSerializableExtra("ticket");


        //create new order
        btnPay = findViewById(R.id.btn_zalo_pay);
        btnPay.setOnClickListener(v -> {
            createNewOrder();
        });

        //toolbar setting
        toolBarSetting();

        //set ticket info
        setTicketInfo();
    }

    private void setTicketInfo() {

//        Log.d("Vu", ticket.getPrice() + " " + ticket.getStartPoint() + " " + ticket.getEndPoint() + " " + ticket.getStartTime());

        tvPrice = findViewById(R.id.tv_price);
        tvStartPoint = findViewById(R.id.tv_st_point);
        tvEndPoint = findViewById(R.id.tv_end_point);
        tvTime = findViewById(R.id.tv_time);

        tvPrice.setText(FormatUtils.getMoneyType(ticket.getPrice()));
        tvStartPoint.setText(ticket.getStartPoint());
        tvEndPoint.setText(ticket.getEndPoint());
        tvTime.setText(ticket.getStartTime());
    }

    private void createNewOrder() {
        CreateOrder orderApi = new CreateOrder();

        try {

            JSONObject data = orderApi.createOrder(String.valueOf(ticket.getPrice()));

            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(PaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
//                        public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID)
                        Intent intent = new Intent(PaymentActivity.this, BillingActivity.class);

                        Log.d("PaymentActivity", "onPaymentSucceeded: " + s + " " + s1 + " " + s2);
                        intent.putExtra("ticket", ticket);
                        intent.putExtra("z_token", s1);
                        intent.putExtra("code", code);
                        startActivity(intent);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {
                        Toast.makeText(PaymentActivity.this, "Payment canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                        Toast.makeText(PaymentActivity.this, "Payment error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void toolBarSetting() {
        Toolbar toolbar = findViewById(R.id.ticket_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Thanh toán");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
