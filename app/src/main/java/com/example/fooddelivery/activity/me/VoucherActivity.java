package com.example.fooddelivery.activity.me;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.LanguageSetting;
import com.example.fooddelivery.adapter.voucherStatusAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class VoucherActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton bt_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        setLocal();
        Init();
        initTabLayoutAndViewPager();
    }

    private void Init() {
        bt_back = findViewById(R.id.vc_bt_back);
        viewPager = findViewById(R.id.info_viewpager);
        tabLayout = findViewById(R.id.tab_button);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void initTabLayoutAndViewPager() {
        voucherStatusAdapter adapter = new voucherStatusAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void setLocal()
    {
        String langCode;
        TextView tvVoucher = findViewById(R.id.tv_voucher);
        LanguageSetting languageSetting = new LanguageSetting();

        Log.e("Lang", languageSetting.getChosenLanguege());
        if (tvVoucher.getText().equals("Voucher"))
            langCode = "en";
        else   {
            langCode ="vi";
        }

        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

}