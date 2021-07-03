package com.example.fooddelivery.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.adapter.VoucherAdapter2;
import com.example.fooddelivery.adapter.VoucherAdapter3;
import com.example.fooddelivery.model.Voucher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AvailableVoucherActivity extends AppCompatActivity {

    String activity;
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
        activity = getIntent().getStringExtra("Type");

        bt_back = findViewById(R.id.vc_bt_back);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvailableVoucherActivity.super.onBackPressed();
            }
        });

        availableVoucher = findViewById(R.id.voucherAvailableRecyclerView);

        if (activity.equals("Merchant")) {
            initAvailableVoucherList();
            VoucherAdapter3 voucherAdapter3 = new VoucherAdapter3(AvailableVoucherActivity.this, vouchers);
            LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(AvailableVoucherActivity.this, RecyclerView.VERTICAL, false);
            availableVoucher.setLayoutManager(linearLayoutManager3);
            availableVoucher.setAdapter(voucherAdapter3);
        } else if (activity.equals("Order")) {
            initUserAvailableVoucherList();
            VoucherAdapter2 voucherAdapter2 = new VoucherAdapter2(AvailableVoucherActivity.this, vouchers);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(AvailableVoucherActivity.this, RecyclerView.VERTICAL, false);
            availableVoucher.setLayoutManager(linearLayoutManager2);
            availableVoucher.setAdapter(voucherAdapter2);
        }
    }

    @Override
    public void onBackPressed() {
        if (activity.equals("Order")) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
        }
        finish();
    }

    private void initUserAvailableVoucherList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        vouchers = new ArrayList<>();
        for (Voucher v : WelcomeActivity.firebase.voucherList) {
            try {
                if ((format.parse(v.getDate())).after(new Date()))
                    if (v.getStatus().equals("Hiện có"))
                        vouchers.add(v);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void initAvailableVoucherList() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        vouchers = new ArrayList<>();
        for (Voucher v : WelcomeActivity.firebase.availableVoucherList) {
            try {
                if ((format.parse(v.getDate())).after(new Date()))
                    vouchers.add(v);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}