package com.example.fooddelivery.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.main.MainActivity;
import com.example.fooddelivery.activity.me.SettingActivity;

import java.util.Locale;

public class LanguageSetting extends AppCompatActivity {
    Button bt_save;
    ImageButton bt_back;
    String[] languages;
    ArrayAdapter<String> languageAdapter;
    AutoCompleteTextView temp;

    String chosenLanguege ="Tiếng Việt";

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

        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 chosenLanguege = parent.getItemAtPosition(position).toString();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chosenLanguege.equals("English"))
                {
                    setLocal(LanguageSetting.this, "en");
                }

                if (chosenLanguege.equals("Tiếng Việt"))
                {
                    setLocal(LanguageSetting.this, "vi");
                }

                Intent main = new Intent(LanguageSetting.this, MainActivity.class);
                startActivity(main);
            }
        });
    }


    public void setLocal(Activity activity, String langCode)
    {
        Locale locale = new Locale(langCode);
        createShareReferences(langCode);
        Locale.setDefault(locale);

        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void createShareReferences(String langCode)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("lang_code", langCode);  // Saving string

        editor.commit();

        Log.e("Save ", langCode);
    }

    private void Init() {
        bt_save = findViewById(R.id.st_lg_bt_save);
        bt_back = findViewById(R.id.st_lg_ib_back);
        temp = findViewById(R.id.auto_language);
        languages = getResources().getStringArray(R.array.languages);
        languageAdapter = new ArrayAdapter<>(LanguageSetting.this, R.layout.dropdown_item, languages);
        temp.setAdapter(languageAdapter);

    }

    public String getChosenLanguege() {
        return chosenLanguege;
    }
}
