package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.adapter.VoucherAdapter;
import com.example.fooddelivery.adapter.VoucherAdapter2;
import com.example.fooddelivery.model.Voucher;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class AvailableVoucherActivity extends AppCompatActivity {

    ImageButton bt_back;
    RecyclerView availableVoucher;
    ArrayList<Voucher> vouchers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_voucher);

        Init();
    }

    private void Init() {
        bt_back = findViewById(R.id.vc_bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailableVoucherActivity.super.onBackPressed();
            }
        });

        availableVoucher = findViewById(R.id.voucherAvailableRecyclerView);

        initUserAvailableVoucherList();
        VoucherAdapter2 voucherAdapter = new VoucherAdapter2(AvailableVoucherActivity.this, vouchers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AvailableVoucherActivity.this, RecyclerView.VERTICAL, false);
        availableVoucher.setLayoutManager(linearLayoutManager);
        availableVoucher.setAdapter(voucherAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void initUserAvailableVoucherList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        vouchers = new ArrayList<>();
        for (Voucher v : WelcomeActivity.firebase.voucherList) {
            try {
                if ((format.parse(v.getDate())).after(new Date())) {
                    if (v.getStatus().equals("Hiện có")) {
                        vouchers.add(v);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}