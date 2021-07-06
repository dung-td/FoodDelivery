package com.example.fooddelivery.activity.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fooddelivery.R;

import java.util.Locale;

public class ChangeOrderTimeActivity extends AppCompatActivity {

    TimePicker timePicker;
    Button buttonConfirm;
    TextView textViewInvalidTime;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_time);

        initView();
        loadLanguage();
        checkValidTime();
    }

    private void initView() {
        timePicker = findViewById(R.id.time_picker);
        buttonBack = findViewById(R.id.btn_back);
        buttonConfirm = findViewById(R.id.btn_confirm);
        textViewInvalidTime = findViewById(R.id.tv_warning);

        timePicker.setIs24HourView(true);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                String timePicked = checkDigit(timePicker.getCurrentHour()) + ":" + checkDigit(timePicker.getMinute());
                returnIntent.putExtra("result", timePicked);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void checkValidTime() {
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                textViewInvalidTime.setVisibility(View.INVISIBLE);
                buttonConfirm.setEnabled(true);
                if (hourOfDay < Calendar.getInstance().getTime().getHours()) {
                    textViewInvalidTime.setVisibility(View.VISIBLE);
                    buttonConfirm.setEnabled(false);
                }
                else {
                    if (hourOfDay == Calendar.getInstance().getTime().getHours()) {
                        if (minute < Calendar.getInstance().getTime().getMinutes()) {
                            textViewInvalidTime.setVisibility(View.VISIBLE);
                            buttonConfirm.setEnabled(false);
                        }
                    }
                }
            }
        });
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