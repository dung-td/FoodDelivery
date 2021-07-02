package com.example.fooddelivery.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.fooddelivery.adapter.MyOnSectionAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.MyViewPager;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class DrinkSectionActivity extends AppCompatActivity {

    TabLayout tabLayout;
    MyViewPager viewPager;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_section);

        initView();
        loadLanguage();
        initTabLayoutAndViewPager();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        viewPager = findViewById(R.id.info_viewpager);
        tabLayout = findViewById(R.id.tab_button);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.all));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.high_rating));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrinkSectionActivity.super.onBackPressed();
            }
        });
    }

    private void initTabLayoutAndViewPager() {
        MyOnSectionAdapter adapter = new MyOnSectionAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
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