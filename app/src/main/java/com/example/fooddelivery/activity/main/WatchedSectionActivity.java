package com.example.fooddelivery.activity.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.adapter.ProductOnSectionAdapter;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.Locale;

public class WatchedSectionActivity extends AppCompatActivity {

    TextView textViewNoData;
    ImageButton buttonBack, buttonRemoveData;
    RecyclerView recyclerViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched_section);

        initView();
        loadLanguage();
        initRecyclerViewProduct();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        buttonRemoveData = findViewById(R.id.btn_remove_data);
        recyclerViewProduct = findViewById(R.id.recycler_view_product);
        textViewNoData = findViewById(R.id.tv_no_data);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchedSectionActivity.super.onBackPressed();
            }
        });
        buttonRemoveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(WatchedSectionActivity.this)
                        .setTitle(getString(R.string.title_remove_data))
                        .setMessage(getString(R.string.msg_remove_watched_data))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                WelcomeActivity.firebase.removeWatchedProductData(new OnGetDataListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        recyclerViewProduct.removeAllViewsInLayout();
                                        recyclerViewProduct.invalidate();
                                        textViewNoData.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        })
                        .setNegativeButton(R.string.msg_cancel, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void initRecyclerViewProduct() {
        ArrayList<Product> watchedProduct = new ArrayList<>();
        for (Product p : WelcomeActivity.firebase.productList) {
            if (WelcomeActivity.firebase.watchedList.contains(p.getId())) {
                   watchedProduct.add(p);
            }
        }
        if (!watchedProduct.isEmpty()) {
            textViewNoData.setVisibility(View.GONE);
        }

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewProduct.setLayoutManager(layoutManager);
        ProductOnSectionAdapter productAdapter = new ProductOnSectionAdapter(this, watchedProduct);
        recyclerViewProduct.setAdapter(productAdapter);
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");

        Log.e("language", language);

        Locale locale = new Locale(language);
        locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
