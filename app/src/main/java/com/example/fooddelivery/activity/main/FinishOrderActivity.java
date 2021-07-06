package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.model.OnGetDataListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FinishOrderActivity extends AppCompatActivity {

    ImageButton buttonBack;
    Button buttonConfirmOrder, buttonFinishOrder;
    ProgressBar progressBar;
    String discount, freight_cost, time, total_amount, voucherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        initView();
        confirmOrder();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        buttonConfirmOrder = findViewById(R.id.btn_confirm_order);
        buttonFinishOrder = findViewById(R.id.btn_finish_order);
        progressBar = findViewById(R.id.progress_finish_order);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonFinishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (buttonFinishOrder.getVisibility() == View.INVISIBLE) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    private void getOrderData() {
        discount = CartActivity.textViewVoucherDiscount.getText().toString();
        freight_cost = CartActivity.textViewShippingCost.getText().toString();
        total_amount = CartActivity.textViewTotalCost.getText().toString();
        total_amount = total_amount.substring(0, total_amount.indexOf(" "));
        voucherId = CartActivity.voucherId;

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        time = format.format(Calendar.getInstance().getTime()) + " "+ CartActivity.deliveryTime.toString();
    }

    private void confirmOrder() {
        buttonConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonConfirmOrder.setVisibility(View.INVISIBLE);
                getOrderData();
                WelcomeActivity.firebase.addUserNewOrder(discount, freight_cost, time, total_amount, voucherId, new OnGetDataListener() {
                    @Override
                    public void onStart() {
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                        buttonFinishOrder.setVisibility(View.VISIBLE);
                        HomeFragment.updateCartBadge();
                    }
                });
            }
        });
    }
}