package com.example.fooddelivery.activity.me;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.R;
import com.example.fooddelivery.adapter.voucherStatusAdapter;
import com.google.android.material.tabs.TabLayout;

public class VoucherActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        Init();
        initTabLayoutAndViewPager();
    }

    private void Init() {
        buttonBack = findViewById(R.id.btn_back);
        viewPager = findViewById(R.id.info_viewpager);
        tabLayout = findViewById(R.id.tab_button);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void initTabLayoutAndViewPager() {
        voucherStatusAdapter adapter = new voucherStatusAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

}