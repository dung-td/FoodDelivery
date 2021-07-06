package com.example.fooddelivery.activity.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.fooddelivery.R;

public class PaymentMethodActivity extends AppCompatActivity {

    ImageButton bt_back;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        bt_back = findViewById(R.id.bt_method_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentMethodActivity.super.onBackPressed();
                finish();
            }
        });
    }
}