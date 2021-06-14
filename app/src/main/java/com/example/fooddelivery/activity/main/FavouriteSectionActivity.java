package com.example.fooddelivery.activity.main;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.adapter.ChosenItemAdapter;
import com.example.fooddelivery.adapter.ProductAdapter;
import com.example.fooddelivery.adapter.ProductOnSectionAdapter;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;

public class FavouriteSectionActivity extends AppCompatActivity {

    TextView textViewNoData;
    ImageButton buttonBack;
    RecyclerView recyclerViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_section);

        initView();
        initRecyclerViewProduct();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        textViewNoData = findViewById(R.id.tv_no_data);
        recyclerViewProduct = findViewById(R.id.recycler_view_product);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavouriteSectionActivity.super.onBackPressed();
            }
        });
    }

    private void initRecyclerViewProduct() {
        ArrayList<Product> favouriteList = new ArrayList<>();
        for (Product p: LoginActivity.firebase.productList) {
            if (LoginActivity.firebase.favouriteProductList.contains(p.getId())) {
                favouriteList.add(p);
            }
        }
        if (!favouriteList.isEmpty())
            textViewNoData.setVisibility(View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(layoutManager);
        ProductOnSectionAdapter productAdapter = new ProductOnSectionAdapter(this, favouriteList);
        recyclerViewProduct.setAdapter(productAdapter);
    }
}
