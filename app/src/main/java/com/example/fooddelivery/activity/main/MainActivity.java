package com.example.fooddelivery.activity.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.login.LoginActivity;
import com.example.fooddelivery.activity.login.WelcomeActivity;
import com.example.fooddelivery.fragment.HomeFragment;
import com.example.fooddelivery.fragment.MeFragment;
import com.example.fooddelivery.fragment.NotificationFragment;
import com.example.fooddelivery.fragment.OrderFragment;
import com.example.fooddelivery.model.CallBackData;
import com.example.fooddelivery.model.ModifyFirebase;
import com.example.fooddelivery.model.MyNotification;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Product;
import com.example.fooddelivery.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.example.fooddelivery.model.DirectionFinder;
import com.example.fooddelivery.model.DirectionFinderListener;
import com.example.fooddelivery.model.Merchant;
import com.example.fooddelivery.model.OnGetDataListener;
import com.example.fooddelivery.model.Route;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 1;
    FloatingActionButton bt_location;
    public static BottomNavigationView bottomNav;
    ProgressDialog progressDialog;

    boolean doubleBackToExitPressedOnce = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLanguage();
        initView();
        getData();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getText(R.string.press_back_twice_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void initView() {
        bt_location = findViewById(R.id.locationButton);
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setBackground(null);
        bottomNav.getMenu().getItem(2).setEnabled(false);
        initBottomNavigation();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for (Merchant merchant : WelcomeActivity.firebase.merchantList) {
                    merchant.getRoutes().clear();
                }

                WelcomeActivity.firebase.getUser().setAddress(addresses.get(0));
            }
        }
    }

    private void getData() {
        WelcomeActivity.firebase.getVoucherList();
        WelcomeActivity.firebase.getAvailableVoucherList();
        WelcomeActivity.firebase.getComment();
        WelcomeActivity.firebase.getUserInfo();
        WelcomeActivity.firebase.getListOrdersOfUser();
        WelcomeActivity.firebase.getNotificationList(new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess() {
                for (MyNotification notification : LoginActivity.firebase.notifications)
                {
                    if (notification.getStatus().equals("false"))
                    {
                        sendNotification(notification.getTitle(), notification.getDesc());
                        LoginActivity.firebase.updateNotificationStatus(notification.getId());
                    }
                }
            }
        });
    }

    private void initBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        temp = new HomeFragment();
                        break;
                    case R.id.nav_order:
                        temp = new OrderFragment(MainActivity.this);
                        break;
                    case R.id.nav_me:
                        temp = new MeFragment();
                        break;
                    case R.id.nav_notification:
                        temp = new NotificationFragment();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.fragment_container, temp);
                ft.commit();
                return true;
            }
        });
    }

    void loadLanguage() {
        String langPref = "lang_code";
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
      
        Locale locale = new Locale(language);

        Log.e("MainActivity language", language);
        Locale.setDefault(locale);

        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sendNotification(String title, String message)
    {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, "channel1")
                        .setSmallIcon(R.drawable.ic_add_img)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel1",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}