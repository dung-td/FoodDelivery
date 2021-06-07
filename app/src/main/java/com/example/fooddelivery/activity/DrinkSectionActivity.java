package com.example.fooddelivery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.fooddelivery.adapter.MyOnSectionAdapter;
import com.example.fooddelivery.R;
import com.example.fooddelivery.model.MyViewPager;
import com.google.android.material.tabs.TabLayout;

public class DrinkSectionActivity extends AppCompatActivity {

    TabLayout tabLayout;
    MyViewPager viewPager;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_section);

        initView();
        initTabLayoutAndViewPager();
    }

    private void initView() {
        buttonBack = findViewById(R.id.btn_back);
        viewPager = findViewById(R.id.info_viewpager);
        tabLayout = findViewById(R.id.tab_button);

        tabLayout.addTab(tabLayout.newTab().setText("GẦN ĐÂY"));
        tabLayout.addTab(tabLayout.newTab().setText("ĐÁNH GIÁ CAO"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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
}