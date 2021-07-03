package com.example.fooddelivery.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.main.MainActivity;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.OnGetDataListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    public static String language;
    ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static ModifyFirebase firebase;
    public static String userID;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            getUserData();
        } else {
            navigateToLogin();
        }
    }

    private void getUserData() {
        userID = mAuth.getUid();
        firebase.setUserId(userID);
        firebase.getData(new OnGetDataListener() {
            @Override
            public void onStart() {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess() {
                if (progressBar != null && progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.INVISIBLE);
                    navigateToMain();
                }
            }
        });
    }

    private void navigateToLogin() {
        Intent login = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    private void navigateToMain() {
        Intent main = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(main);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLanguage(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Init();
    }

    private void Init() {
        progressBar = findViewById(R.id.pb_loading);
        firebase = new ModifyFirebase();
    }

    public static void loadLanguage(Context applicationContext) {
        String langPref = "lang_code";
        SharedPreferences prefs = applicationContext.getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        language = prefs.getString(langPref, "");

        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = applicationContext.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}