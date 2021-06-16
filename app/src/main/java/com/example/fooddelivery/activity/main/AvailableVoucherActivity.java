package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.VoucherAdapter;
import com.example.fooddelivery.adapter.VoucherAdapter2;

public class AvailableVoucherActivity extends AppCompatActivity {
    ImageView bt_back;
    RecyclerView availableVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_voucher);

        Init();
    }

    private void Init() {
        bt_back = findViewById(R.id.voucher_ib_back);
        availableVoucher = findViewById(R.id.voucherAvailableRecyclerView);
        VoucherAdapter2 voucherAdapter = new VoucherAdapter2(AvailableVoucherActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AvailableVoucherActivity.this, RecyclerView.VERTICAL, false);
        availableVoucher.setLayoutManager(linearLayoutManager);
        availableVoucher.setAdapter(voucherAdapter);
    }
}