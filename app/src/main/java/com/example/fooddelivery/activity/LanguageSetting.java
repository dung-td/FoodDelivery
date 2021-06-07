package com.example.fooddelivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.me.SettingActivity;

public class LanguageSetting extends AppCompatActivity {
    Button bt_save;
    ImageButton bt_back;
    String[] languages;
    ArrayAdapter<String> languageAdapter;
    AutoCompleteTextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);

        Init();

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingActivity = new Intent(LanguageSetting.this, SettingActivity.class);
                startActivity(settingActivity);
            }
        });
    }

    private void Init() {
        bt_save = findViewById(R.id.st_lg_bt_save);
        bt_back = findViewById(R.id.st_lg_ib_back);
        temp = findViewById(R.id.auto_language);
        languages = getResources().getStringArray(R.array.languages);
        languageAdapter = new ArrayAdapter<>(LanguageSetting.this, R.layout.dropdown_item, languages);
        temp.setAdapter(languageAdapter);
    }
}
