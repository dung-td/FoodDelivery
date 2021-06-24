package com.example.fooddelivery.activity.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.ProductOnSectionAdapter;
import com.example.fooddelivery.model.Product;

import java.util.ArrayList;
import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity {

    TextView textViewTitle, textViewNoData;
    ImageButton buttonBack;
    RecyclerView recyclerViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_search_result);

        initView();
        loadLanguage();
        initRecyclerViewProduct();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        textViewTitle = findViewById(R.id.tv_title);
        textViewNoData = findViewById(R.id.tv_no_data);
        recyclerViewProduct = findViewById(R.id.recycler_view_product);

        String input = getIntent().getExtras().get("searchInput").toString();
        textViewTitle.setText(Html.fromHtml(textViewTitle.getText() + " " +
                "<strong>" + input + "</strong>"));

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SearchingActivity.searchingItemAdapter.notifyDataSetChanged();
        if (SearchingActivity.searchData.size() == 1)
            SearchingActivity.textViewNoSearches.setVisibility(View.INVISIBLE);
        super.onBackPressed();
    }

    private void initRecyclerViewProduct() {
        if (!SearchingActivity.queryResult.isEmpty()) {
            textViewNoData.setVisibility(View.INVISIBLE);
            final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerViewProduct.setLayoutManager(layoutManager);
            ProductOnSectionAdapter productAdapter = new ProductOnSectionAdapter(this, SearchingActivity.queryResult);
            recyclerViewProduct.setAdapter(productAdapter);
        }
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
