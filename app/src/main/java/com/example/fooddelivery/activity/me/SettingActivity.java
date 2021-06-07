package com.example.fooddelivery.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.LanguageSetting;
import com.example.fooddelivery.fragment.MeFragment;

public class SettingActivity extends AppCompatActivity {
    ImageButton bt_back, bt_language, bt_notification, bt_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Init();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meFragment = new Intent(SettingActivity.this, MeFragment.class);
                startActivity(meFragment);
            }
        });

        bt_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent languageSetting = new Intent(SettingActivity.this, LanguageSetting.class);
                startActivity(languageSetting);
            }
        });
    }

    private void Init() {
        bt_back = findViewById(R.id.st_ib_back);
        bt_language = findViewById(R.id.st_ib_language);
        bt_notification = findViewById(R.id.st_ib_notification);
        bt_account = findViewById(R.id.st_ib_account);
    }
}
