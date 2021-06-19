package com.example.fooddelivery.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ProductOnSectionAdapter;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;

public class FoodSectionActivity extends AppCompatActivity {
    TextView textViewNoData;
    ImageButton buttonBack;
    RecyclerView recyclerViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_section);

        initView();
        initRecyclerViewProduct();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        recyclerViewProduct = findViewById(R.id.recycler_view_product);
        textViewNoData = findViewById(R.id.tv_no_data);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodSectionActivity.super.onBackPressed();
            }
        });
    }

    private void initRecyclerViewProduct() {
        ArrayList<Product> foodList = new ArrayList<>();
        for (Product p : LoginActivity.firebase.productList) {
            if (p.getType().equals("Food")) {
                foodList.add(p);
            }
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(layoutManager);
        ProductOnSectionAdapter productAdapter = new ProductOnSectionAdapter(this, foodList);
        recyclerViewProduct.setAdapter(productAdapter);
    }
}
